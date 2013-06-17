package de.enwida.web.utils;

import de.enwida.transport.Aspect;
import de.enwida.transport.DataResolution;

public class EnwidaUtils {

	public static DataResolution getDataResolution(String legacyDataResolution) {
		
		DataResolution dR = null;		
//		switch (legacyDataResolution) {
//		case "15min":
//			dR = DataResolution.QUATER_HOURLY;
//			break;
//		case "1h":
//			dR = DataResolution.HOURLY;
//			break;
//		case "1d":
//			dR = DataResolution.DAILY;
//			break;
//		case "1w":
//			dR = DataResolution.WEEKLY;
//			break;
//		case "1m":
//			dR = DataResolution.MONTHLY;
//			break;
//		case "1a":
//			dR = DataResolution.YEARLY;
//			break;		
//		}
//		
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

}
