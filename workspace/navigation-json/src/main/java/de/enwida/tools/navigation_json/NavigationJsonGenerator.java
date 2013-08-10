package de.enwida.tools.navigation_json;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.enwida.transport.Aspect;
import de.enwida.transport.DataResolution;
import de.enwida.web.db.model.CalendarRange;
import de.enwida.web.db.model.NavigationDefaults;
import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.model.ProductTree;
import de.enwida.web.utils.ObjectMapperFactory;
import de.enwida.web.utils.ProductLeaf;
import de.enwida.web.utils.ProductNode;

public class NavigationJsonGenerator {
	
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static ObjectMapper objectMapper;
	
	static {
		final ObjectMapperFactory objectMapperFactory = new ObjectMapperFactory(dateFormat);
		objectMapper = objectMapperFactory.create();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
	}
	
    public static void main(String[] args) throws Exception {
    	ChartNavigationData navigationData;
    	
    	if (args.length == 0) {
    		navigationData = getExampleNavigation();
    	} else {
    		final String templatePath = args[0];
    		final byte[] bytes = Files.readAllBytes(Paths.get(templatePath));
    		final String templateJson = Charset.forName("UTF-8").decode(ByteBuffer.wrap(bytes)).toString();

	    	final Template template = objectMapper.readValue(templateJson, Template.class);
    		navigationData = getNavigationFromTemplate(template);
    	}
	    
    	System.out.println(objectMapper.writeValueAsString(navigationData));
    }
    
    private static ChartNavigationData getNavigationFromTemplate(Template template) {
    	final ChartNavigationData result = new ChartNavigationData();

    	// Set flags
    	result.setIsDateScale(template.isDateScale != null ? template.isDateScale : true);
    	result.setHasLineSelection(template.hasLineSelection != null ? template.hasLineSelection : true);
    	result.setHasProductSelection(template.hasProductSelection != null ? template.hasProductSelection : true);
    	result.setHasTimeSelection(template.hasTimeSelection != null ? template.hasTimeSelection : true);
    	
    	// Set defaults
    	if (template.timeRanges == null) {
    		template.timeRanges = Arrays.asList(new String[] { "Day", "Week", "Month", "Year" });
    	}
    	if (template.aspects == null) {
    		template.aspects = Arrays.asList(Aspect.values());
    	}
    	if (template.tsos == null) {
    		template.tsos = Collections.singletonList(99);
    	}
    	if (template.products == null) {
    		template.products = Arrays.asList(new String[] { "2**", "3**" });
    	}
    	if (template.resolutions == null) {
    		template.resolutions = Arrays.asList(DataResolution.values());
    	}
    	if (template.dateFrom == null) {
    		template.dateFrom = Calendar.getInstance();
    		template.dateFrom.setTimeInMillis(0);
    	}
    	if (template.dateTo == null) {
    		template.dateTo = Calendar.getInstance();
    		template.dateTo.setTimeInMillis(Long.MAX_VALUE);
    	}
    	
    	// Set time ranges
    	result.getTimeRanges().addAll(template.timeRanges);
    	
    	// Set aspects
    	result.getAspects().addAll(template.aspects);
    	
    	// Set defaults
    	final NavigationDefaults defaults = new NavigationDefaults();
    	defaults.setTsoId(template.tsos.get(0));
    	defaults.setResolution(template.resolutions.get(0));
    	final Calendar defaultFrom = Calendar.getInstance();
    	final Calendar defaultTo = Calendar.getInstance();
    	defaultFrom.set(2010, 0, 1);
    	defaultTo.set(2011, 0, 1);
    	defaults.setTimeRange(new CalendarRange(defaultFrom, defaultTo));
    	defaults.setProduct(Integer.parseInt(template.products.get(0).replaceAll("\\*", "1")));
    	defaults.setDisabledLines(new HashSet<Integer>(0));
    	result.setDefaults(defaults);
    	
    	// Create product trees
    	for (final int tso : template.tsos) {
    		result.addProductTree(new ProductTree(tso));
    	}
    	
    	// Fill product trees
    	for (final String product : template.products) {
    		for (final ProductTree tree : result.getProductTrees()) {
    			ProductNode node = tree.getRoot();
    			if (product.equals("200")) {
    				final ProductNode scr = new ProductNode(2, "SCR");
    				node.addChild(scr);
    				appendZeroParts(scr, template);
    			} else if (product.equals("300")) {
    				final ProductNode tcr = new ProductNode(3, "TCR");
    				node.addChild(tcr);
    				appendZeroParts(tcr, template);
    			} else if (product.equals("2**")) {
    				final ProductNode scr = new ProductNode(2, "SCR");
    				node.addChild(scr);
    				
    				final ProductNode pos = new ProductNode(1, "pos");
    				final ProductNode neg = new ProductNode(2, "neg");

    				appendSCRLeaves(pos, template);
    				appendSCRLeaves(neg, template);
    				scr.addChild(pos);
    				scr.addChild(neg);
    			} else if (product.equals("3**")) {
    				final ProductNode tcr = new ProductNode(3, "TCR");
    				node.addChild(tcr);
    
    				final ProductNode pos = new ProductNode(1, "pos");
    				final ProductNode neg = new ProductNode(2, "neg");

    				appendTCRLeaves(pos, template);
    				appendTCRLeaves(neg, template);
    				tcr.addChild(pos);
    				tcr.addChild(neg);
    			}
    		}
    	}
    	
    	return result;
    }
    
    private static void appendSCRLeaves(ProductNode node, Template template) {
		final List<ProductLeaf> leaves = Arrays.asList(new ProductLeaf[] {
			new ProductLeaf(1, "PT"),
			new ProductLeaf(2, "OPT")
		});

    	for (final ProductLeaf leaf : leaves) {
    		setLeafProperties(leaf, template);
    		node.addChild(leaf);
    	}
    }

    private static void appendTCRLeaves(ProductNode node, Template template) {
		final List<ProductLeaf> leaves = Arrays.asList(new ProductLeaf[] {
			new ProductLeaf(1, "0-4"),
			new ProductLeaf(2, "4-8"),
			new ProductLeaf(3, "8-12"),
			new ProductLeaf(4, "12-16"),
			new ProductLeaf(5, "16-20"),
			new ProductLeaf(6, "20-24")		});

    	for (final ProductLeaf leaf : leaves) {
    		setLeafProperties(leaf, template);
    		node.addChild(leaf);
    	}
    }
    
    private static void setLeafProperties(ProductLeaf leaf, Template template) {
    	leaf.setResolution(template.resolutions);
    	leaf.setTimeRange(new CalendarRange(template.dateFrom, template.dateTo));
    }
    
    private static void appendZeroParts(ProductNode node, Template template) {
		final ProductNode posneg = new ProductNode(0, "Pos & neg");
		node.addChild(posneg);
		
		final ProductLeaf day = new ProductLeaf(0, "Day");
		day.setResolution(template.resolutions);
		day.setTimeRange(new CalendarRange(template.dateFrom, template.dateTo));
		posneg.addChild(day);
    }
    
    private static ChartNavigationData getExampleNavigation() {
    	final Template template = new Template();
    	template.aspects = Arrays.asList(new Aspect[] { Aspect.CR_VOL_ACCEPTED, Aspect.CR_VOL_OFFERED });
    	template.tsos = Collections.singletonList(99);
    	template.products = Arrays.asList(new String[] { "2**", "3**" });
	    return getNavigationFromTemplate(new Template());
    }
    
    static class Template {
    	public List<Aspect> aspects; 
    	public List<String> timeRanges;
    	public List<Integer> tsos;
    	public List<DataResolution> resolutions;
    	public List<String> products;
    	public Calendar dateFrom;
    	public Calendar dateTo;
    	
    	public Boolean isDateScale;
    	public Boolean hasLineSelection;
    	public Boolean hasProductSelection;
    	public Boolean hasTimeSelection;
    }

}
