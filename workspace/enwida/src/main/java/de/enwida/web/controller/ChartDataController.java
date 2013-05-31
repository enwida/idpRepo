package de.enwida.web.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.enwida.chart.DataManager;
import de.enwida.transport.ChartType;
import de.enwida.transport.DataRequest;
import de.enwida.transport.DataResolution;
import de.enwida.transport.DataResponse;
import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.model.GenericData;
import de.enwida.web.model.NavigationDataStructure;
import de.enwida.web.model.NavigationNode;
import de.enwida.web.service.interfaces.NavigationService;

/**
 * Handles chart data requests
 */
@Controller
@RequestMapping("/data")
public class ChartDataController {

	@Autowired
	private DataManager dataManager;
	@Autowired
	private NavigationService navigationService;

	private void convertListToNavigationStructure(List<String> inputList,
			List<NavigationNode> navigationStructure) {
		for (final String key : inputList) {
			navigationStructure.add(new NavigationNode(
					new ArrayList<NavigationNode>(), key));
		}
	}

	@RequestMapping(value = "/lines", method = RequestMethod.GET)
	@ResponseBody
	public DataResponse displayDashboard(
			@RequestParam ChartType type,
			@RequestParam int product,
			@RequestParam @DateTimeFormat(pattern = "YYYY-MM-DD") Calendar startTime,
			@RequestParam @DateTimeFormat(pattern = "YYYY-MM-DD") Calendar endTime,
			@RequestParam DataResolution resolution, Locale locale) {
		final DataRequest request = new DataRequest(type, product, startTime,
				endTime, resolution, locale);
		final DataResponse response = this.dataManager.getData(request);
		return response;
	}

	@RequestMapping(value = "/chart", method = RequestMethod.GET)
	public String exampleChart() {
		return "charts/index";
	}

	@RequestMapping(value = "/navigationData", method = RequestMethod.GET)
	@ResponseBody
	public ChartNavigationData getNavigation(
			@RequestParam ChartType type,
			@RequestParam int product,
			@RequestParam @DateTimeFormat(pattern = "YYYY-MM-DD") Calendar startTime,
			@RequestParam @DateTimeFormat(pattern = "YYYY-MM-DD") Calendar endTime,
			@RequestParam DataResolution resolution, Locale locale,
			Principal principal) {
		final DataRequest request = new DataRequest(type, product, startTime,
				endTime, resolution, locale);
		final NavigationDataStructure navigationDS = this.prepareNavigationDS(
				request, principal);
		this.navigationService.getNavigationData(navigationDS);
		final ChartNavigationData response = new ChartNavigationData();
		response.setNavigationDS(navigationDS);
		return response;
	}

	@RequestMapping(value = "/navigation", method = RequestMethod.GET)
	@ResponseBody
	public ChartNavigationData initData() {
		// FIXME: Get navigation data from a dedicated service
		final ChartNavigationData dummy = new ChartNavigationData();
		// dummy.setWidth(600);
		// dummy.setHeight(480);
		dummy.setTitle("Capacity");
		return dummy;
	}

	private NavigationDataStructure prepareNavigationDS(DataRequest request,
			Principal principal) {
		final NavigationDataStructure navigationDS = new NavigationDataStructure();

		/**
		 * bind all params thats needs to be sent to navigation layer.
		 */

		// create and fill tso values
		final List<String> tsoDataList = Arrays.asList(new String[] { "1",
				"44", "99", "431" });
		final List<NavigationNode> tsoBranch = new ArrayList<NavigationNode>();
		this.convertListToNavigationStructure(tsoDataList, tsoBranch);
		navigationDS.setList(tsoBranch);

		// create and fill RC values
		final List<String> prodAList = Arrays.asList(new String[] { "1000",
				"1001", "1002", "1003" });
		final List<NavigationNode> tsoBranchList = navigationDS.getList();
		final NavigationNode node_99 = NavigationDataStructure
				.findNavigationNodeByKey("99", tsoBranchList);
		final NavigationNode node_44 = NavigationDataStructure
				.findNavigationNodeByKey("44", tsoBranchList);
		this.convertListToNavigationStructure(prodAList.subList(2, 4),
				node_99.getList());
		this.convertListToNavigationStructure(prodAList.subList(0, 2),
				node_44.getList());

		// create and fill timeslot values
		final List<String> prodBlist = Arrays.asList(new String[] { "1", "2",
				"3", "4" });
		final NavigationNode node_1001 = NavigationDataStructure
				.findNavigationNodeByKey("1001", node_44.getList());
		this.convertListToNavigationStructure(prodBlist, node_1001.getList());

		// create and fill timeslot values
		final List<String> prodClist = Arrays.asList(new String[] { "+", "-" });
		final NavigationNode node_1 = NavigationDataStructure
				.findNavigationNodeByKey("1", node_1001.getList());
		this.convertListToNavigationStructure(prodClist, node_1.getList());

		final NavigationNode node_pos = NavigationDataStructure
				.findNavigationNodeByKey("+", node_1.getList());
		final GenericData data = new GenericData(request.getResolution(),
				"somerole", request.getStartTime().getTime(), request
				.getEndTime().getTime(), "jitin");
		node_pos.setCommonData(data);
		// rcMap_1001 = ((Map<String, Map>) paramMap.get("rc")).get("1001");
		// paramMap.put("timeSlot",rcMap_1001);
		// this.convertListToMap(rcList,tsoMap_99);

		return navigationDS;
	}
}
