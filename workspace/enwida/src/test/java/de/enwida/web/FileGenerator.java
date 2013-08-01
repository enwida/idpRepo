package de.enwida.web;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.enwida.web.db.model.UserLinesMetaData;

public class FileGenerator {



	private static final String dateformat = "yyyy-MM-dd HH:mm:ss.SSSZ";
	private static final String valueformat = "KWh";
	public static SimpleDateFormat formatter = new SimpleDateFormat(dateformat);
	public static DecimalFormat df = new DecimalFormat("####.########");

	@Before
	public void setUp() {
	}

	@Test
	@Ignore
	public void setValueReflection() {
		UserLinesMetaData metaData = new UserLinesMetaData();
		try {

		BeanInfo info = Introspector.getBeanInfo(metaData.getClass(),
					Object.class);
		PropertyDescriptor[] props = info.getPropertyDescriptors();
		for (PropertyDescriptor pd : props) {
			String name = pd.getName();
			if (name.equals("name")) {
				Method setter = pd.getWriteMethod();
				// Method getter = pd.getReadMethod();
				// Class<?> type = pd.getPropertyType();

				setter.invoke(metaData, "somename");
			}
		}
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println(metaData.getName());
	}

	@Test
	public void generateFile() {
		// TODO Auto-generated method stub
		for (int i = 1; i <= 5; i++) {
			String filePath = "C://Users//Jitin//Desktop//sample-" + i + ".csv";
			int numberOfDataLines = 400;
			try {
				generateCsvFile(filePath, numberOfDataLines);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("File generated : sample-" + i + ".csv ");
		}
	}

	private void generateCsvFile(String sFileName, int lines) throws Exception {
		try {
			FileWriter writer = new FileWriter(sFileName);

			generateInfo(writer);
			// first header
			writer.append("Time");
			writer.append(';');
			writer.append("Data");
			writer.append(";\n");
			// second header
			writer.append(dateformat);
			writer.append(';');
			writer.append(valueformat);
			writer.append(";\n");

			for (int i = 0; i < lines; i++) {
				String formattedDate = formatter.format(getRandomDate());
				writer.append(formattedDate);
				writer.append(';');
				writer.append(df.format(getRandomNumber()));
				writer.append(";\n");
			}
			// generate whatever data you want

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Date getRandomDate() throws ParseException {
		long start2012 = new SimpleDateFormat("yyyy").parse("2012").getTime();
		final long millisInYear2012 = 1000 * 60 * 60 * 24 * 365 + 1000;
		long millis = Math.round(millisInYear2012 * Math.random());
		return new Date(start2012 + millis);
	}

	private Double getRandomNumber() {
		Random r = new Random();
		return (Double) (r.nextDouble() * 30000 + r.nextDouble() * 10);
	}

	public void generateInfo(FileWriter writer) throws IOException {
		writer.append("######################################\n");
		writer.append("#####Name : something#################\n");
		writer.append("#####Comments : something##############\n");
		writer.append("#####Units : KWh#######################\n");
		writer.append("#####Country : DE#####################\n");
		writer.append("#####Resolution : something###########\n");
		writer.append("#####Interpolation style : left#######\n");
		writer.append("#####Aspect : PowerConsumption########\n");
		writer.append("######################################\n");
	}
}
