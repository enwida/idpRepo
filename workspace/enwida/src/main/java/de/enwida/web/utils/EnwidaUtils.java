package de.enwida.web.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.SecureRandom;

import de.enwida.transport.Aspect;
import de.enwida.transport.DataResolution;

public class EnwidaUtils {

	public static DataResolution getDataResolution(String legacyDataResolution) {
		
		DataResolution dR = null;		
		switch (legacyDataResolution.trim()) {
		case "15min":
			dR = DataResolution.QUATER_HOURLY;
			break;
		case "1h":
			dR = DataResolution.HOURLY;
			break;
		case "1d":
			dR = DataResolution.DAILY;
			break;
		case "1w":
			dR = DataResolution.WEEKLY;
			break;
		case "1m":
			dR = DataResolution.MONTHLY;
			break;
		case "1a":
			dR = DataResolution.YEARLY;
			break;		
		}
		
		return dR;
	}
	
	public static String getTableNameByAspect(Aspect aspect) {
		
		String tableName = null;
		switch (aspect) {
		case CR_ACTIVATION_FREQUENCY:
		case CR_POWERPRICE_MIN:
		case CR_POWERPRICE_MAX:
		case CR_POWERPRICE_MID:
		case CR_DEGREE_OF_ACTIVATION:
		case CR_VOL_ACCEPTED:
		case CR_VOL_ACTIVATION:
		case CR_VOL_ACTIVATION2:	
		case CR_VOL_OFFERED:
		case CR_WORKPRICE_ACC_MIN:
		case CR_WORKPRICE_ACC_MAX:
		case CR_WORKPRICE_ACC_MID:
		case CR_WORKPRICE_MARG_MAX:
		case CR_WORKPRICE_MARG_MID:
			tableName = "analysis";
			break;
			
		case CR_POWERPRICE_ACCEPTED:
		case CR_POWERPRICE_REJECTED:
		case CR_WORKPRICE_ACCEPTED:
		case CR_WORKPRICE_REJECTED:
			tableName = "auction_results";
			break;
		
		case CR_REVENUES:			
			break;
		default:
			break;
		}
		
		return tableName;
	}
	
	public static String getStringFromInputStream(InputStream is) {
		 
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
 
		String line;
		try {
 
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
 
		return sb.toString();
 
	}
	
	private SecureRandom random = new SecureRandom();

    public String getActivationId()
    {
      return new BigInteger(130, random).toString(32);
    }

	/**
	 * Writes given content to a file.
	 * 
	 * @param file
	 *            the file to write to
	 * @param text
	 *            the text to write.
	 */
	public static void writeAllText(File file, String text) throws Exception {
		BufferedWriter out = new BufferedWriter(new FileWriter(file));
		out.write(text);
		out.close();
	}

	/**
	 * Creates a new directory depending on the input path given
	 * 
	 * @param path
	 *            directory path that needs to be created
	 * @return success status of directory creation
	 */
	public static boolean createDirectory(String path) {
		boolean status = false;

		if (path != null && !path.trim().isEmpty()) {
			File f = new File(path);
			f.setWritable(true);

			if (!f.exists()) {
				status = f.mkdirs();
			}
		}
		return status;
	}

	/**
	 * @param completeName
	 * @return
	 */
	public static String extractFileFormat(String completeName) {
		String[] fnameParts = completeName.split("\\\\");
		// String fname = fnameParts[fnameParts.length - 1];
		// this.displayFileName = fname;

		fnameParts = completeName.split("\\.");
		if (fnameParts.length < 2) {
			// file without format.
			return null;
		}

		return fnameParts[fnameParts.length - 1];
	}

	/**
	 * @param fileTORemove
	 * @return
	 * @throws Exception
	 */
	public static boolean removeTemporaryFile(File fileTORemove)
			throws Exception {
		fileTORemove.delete();
		return true;
	}
}
