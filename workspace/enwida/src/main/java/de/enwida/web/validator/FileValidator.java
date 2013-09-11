/**
 * 
 */
package de.enwida.web.validator;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import de.enwida.rl.dtos.DOUserLines;
import de.enwida.web.db.model.UserLinesMetaData;
import de.enwida.web.utils.Constants;

/**
 * @author Jitin
 *
 */
@Component
public class FileValidator implements Validator {

	private Logger logger = Logger.getLogger(getClass());
	public static final String ERROR_LINE_NUMBER = "error_line_number";
	public static final String ERROR_COLUMN_NUMBER = "error_column_number";

	private PropertyDescriptor[] beanProps;

	public PropertyDescriptor[] getBeanProps() {
		return beanProps;
	}

	public void setBeanProps(PropertyDescriptor[] beanProps) {
		this.beanProps = beanProps;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(File.class);
	}

	@Override
	public void validate(Object target, Errors errors) {
		File file = (File) target;

		Map<String, Object> dataMap = readAndValidateFile(file);

		if (dataMap != null && !dataMap.isEmpty()) {

			if (dataMap.containsKey(ERROR_LINE_NUMBER)
					&& dataMap.containsKey(ERROR_COLUMN_NUMBER)) {
				errors.reject("file.upload.parse.error",
						"Parse error at : (" + dataMap.get(ERROR_LINE_NUMBER)
								+ "," + dataMap.get(ERROR_COLUMN_NUMBER)
								+ ")");

			} else if (dataMap.containsKey(ERROR_LINE_NUMBER)) {
				errors.reject(
						"file.upload.parse.error",
						"Parse error at line number : "
								+ dataMap.get(ERROR_LINE_NUMBER));
			} else if (dataMap.containsKey(Constants.GENERAL_ERROR_MESSAGE)) {
				errors.reject("file.upload.parse.error",
						(String) dataMap.get(Constants.GENERAL_ERROR_MESSAGE));
			} else {
				// returning successfully parsed data in global errorobject
				errors.reject("file.upload.parse.success",
						new Object[] { dataMap },
						"Parsed successfully");
			}
		}

	}

	private Map<String, Object> readAndValidateFile(File file) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<DOUserLines> dataLines = new ArrayList<DOUserLines>();
		UserLinesMetaData metadata =  new UserLinesMetaData();
		boolean processheaders = true;
		boolean processbody = false;

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			int linenumber = 1;
			while ((line = br.readLine()) != null) {
				// logger.debug("Reading line : " + linenumber);
				// process the line.
				if (!processbody
						&& !line.contains(Constants.COMMENT_START_SYMBOL)) {
					// either data or header
					String[] headers = line.split(Constants.DATA_SEPARATOR);
					if (headers != null && headers.length == 2) {
						metadata.setHeader1(headers[0].trim());
						metadata.setHeader2(headers[1].trim());
						processbody = true;
						processheaders = false;
					} else {
						logger.debug("Invalid Record Found at : " + linenumber);
						dataMap.put(ERROR_LINE_NUMBER, linenumber);
						break;
					}
					continue;
				}
				// if (line.contains("}")) {
				// logger.debug("File end delimiter encountered");
				// break;
				// }
				// process all headers and continue until
				// process headers is not reset
				if (processheaders) {
					logger.debug("processing header : " + linenumber);
					// if any metadata present update it
					metadata = processHeader(line, linenumber, dataMap,
							metadata);

				} else if (processbody) {
					logger.debug("processing body : " + linenumber);
					DOUserLines userline = processBody(line, linenumber,
							dataMap);
					if (userline != null) {
						// this case for blank data lines but format is correct
						// continue processing data
						if (userline.getTime() == 0
								&& userline.getValue() == -1) {
							logger.debug("No record found at line number : "
									+ linenumber);
						} else {
							// userline.setUserLineId(metadata.getMetaDataId());
							// check for unique lines
							if (!dataLines.contains(userline)) {
								dataLines.add(userline);
							}
						}
					} else {
						logger.debug("Invalid Record Found at : " + linenumber);
						break;
					}
				}
				linenumber++;
			}

			br.close();
		} catch (Exception e) {
			logger.error("File incorrect data");
		}

		if (dataLines.size() > 0) {
			dataMap.put(Constants.UPLOAD_LINES_KEY, dataLines);
			dataMap.put(Constants.UPLOAD_LINES_METADATA_KEY, metadata);
		} else if (processheaders) {
			// update datamap
			dataMap.put(Constants.GENERAL_ERROR_MESSAGE,
					"Data starter not found");
		}

		return dataMap;
	}

	private UserLinesMetaData processHeader(String line, int linenumber,
			Map<String, Object> dataMap, UserLinesMetaData metadata) {
		String newline = new String(line).toLowerCase();
		try {
			if (getBeanProps() == null) {
				setBeanProps(Introspector.getBeanInfo(metadata.getClass(),
						Object.class).getPropertyDescriptors());
			}
			for (PropertyDescriptor pd : getBeanProps()) {
				String name = pd.getName();
				if (newline.contains(name)) {
					// int startpos = line.indexOf(":") + 1;
					// int endpos = line.indexOf("#", startpos);
					String[] result = line
							.split(Constants.COMMENT_VALUE_SEPARATOR);
					if (result != null && result.length >= 2) {
						String value = result[1].trim();
						Method setter = pd.getWriteMethod();
						setter.invoke(metadata, value);
					}
				}
			}
		} catch (Exception e) {
			dataMap.put(ERROR_COLUMN_NUMBER,
					(line.indexOf(Constants.COMMENT_VALUE_SEPARATOR) + 1));
			dataMap.put(ERROR_LINE_NUMBER, linenumber);
			return null;
		}
		return metadata;
	}

	private DOUserLines processBody(String line, int linenumber,
			Map<String, Object> dataMap) {
		DOUserLines userline = null;
		Calendar timestamp = null;
		double value = -1;
		if (line != null && !line.trim().isEmpty()) {
			String[] content = line.split(Constants.DATA_SEPARATOR);
			if (content != null && content.length == 2) {
				try {
					timestamp = Calendar.getInstance();
					Date date = new SimpleDateFormat(
							Constants.UPLOAD_DATE_FORMAT, Locale.ENGLISH)
							.parse(content[0].trim());
					timestamp.setTime(date);

				} catch (Exception e) {
					dataMap.put(ERROR_COLUMN_NUMBER,
							(line.indexOf(content[0]) + 1));
					dataMap.put(ERROR_LINE_NUMBER, linenumber);
					return null;
				}
				try {
					value = Double.parseDouble(content[1].trim());
				} catch (Exception e) {
					dataMap.put(ERROR_COLUMN_NUMBER,
							(line.indexOf(content[1]) + 1));
					dataMap.put(ERROR_LINE_NUMBER, linenumber);
					return null;
				}

				userline = new DOUserLines(timestamp.getTimeInMillis(), value,
						0);
			} else if (content != null && content.length == 1) {
				dataMap.put(ERROR_LINE_NUMBER, linenumber);
				dataMap.put(ERROR_COLUMN_NUMBER,
						(line.indexOf(Constants.DATA_SEPARATOR) + 1));

			} else if (line.trim().equalsIgnoreCase(
					Constants.DATA_SEPARATOR + Constants.DATA_SEPARATOR)) {
				// handling black record
				userline = new DOUserLines(0, -1, 0);
			} else {
				// incorrect data encountered at line number
				dataMap.put(ERROR_LINE_NUMBER, linenumber);
				return null;
			}
		} else {
			// Hnadling blank lines
			userline = new DOUserLines(0, -1, 0);
		}
		return userline;
	}
}
