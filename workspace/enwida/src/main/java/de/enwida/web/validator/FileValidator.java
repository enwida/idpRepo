/**
 * 
 */
package de.enwida.web.validator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author Jitin
 *
 */
@Component
public class FileValidator implements Validator {

	public static final String ERROR_LINE_NUMBER = "error_line_number";
	public static final String ERROR_COLUMN_NUMBER = "error_column_number";

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
					|| dataMap.containsKey(ERROR_COLUMN_NUMBER)) {
				errors.reject("file.upload.error",
						"Parse error at : (" + dataMap.get(ERROR_LINE_NUMBER)
								+ "," + dataMap.get(ERROR_COLUMN_NUMBER)
								+ ")");

			}
		}

	}

	private Map<String, Object> readAndValidateFile(File file) {
		Map<String, Object> dataMap = new HashMap<String, Object>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				// process the line.
				// dataMap.put(ERROR_LINE_NUMBER, 124);
				// dataMap.put(ERROR_COLUMN_NUMBER, 259);
				dataMap.put("somedata", "sometign");
			}
			br.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return dataMap;
	}

}
