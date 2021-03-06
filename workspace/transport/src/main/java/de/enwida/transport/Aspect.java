package de.enwida.transport;

import java.util.HashMap;
import java.util.Map;

public enum Aspect {
	
	CR_VOL_ACTIVATION,                    // 0  | rl_ab1
	CR_VOL_ACTIVATION_CP,                 // 1  | Showing the activation of reserve control as a carpet plot (summing up pos and neg values)
	CR_DEGREE_OF_ACTIVATION,              // 2  | rl_abg1
	CR_ACTIVATION_FREQUENCY,              // 3  | rl_abh1
	CR_VOL_ACCEPTED,                      // 4  | rl_vol1
	CR_VOL_OFFERED,                       // 5  | rl_vol1
	CR_POWERPRICE_ACCEPTED,               // 6  | rl_geb1
	CR_POWERPRICE_REJECTED,               // 7  | rl_geb1
	CR_WORKPRICE_ACCEPTED,                // 8  | rl_prs1
	CR_WORKPRICE_REJECTED,                // 9  | rl_prs1
	CR_POWERPRICE_MIN,                    // 10 | rl_prs1
	CR_POWERPRICE_MID,                    // 11 | rl_prs1
	CR_POWERPRICE_MAX,                    // 12 | rl_prs1
	CR_WORKPRICE_ACC_MIN,                 // 13 | rl_prs1
	CR_WORKPRICE_ACC_MID,                 // 14 | rl_prs1
	CR_WORKPRICE_ACC_MAX,                 // 15 | rl_prs1
	CR_WORKPRICE_MARG_MID,                // 16 | rl_prs1
	CR_WORKPRICE_MARG_MAX,                // 17 | rl_prs1
	CR_REVENUES,                          // 18
	UL_TH_LOAD_PROFILE,
	UL_EL_LOAD_PROFILE,
	UL_BID_CF_PC,
	UL_BID_CF_TC_POS,
	UL_BID_CF_SC_POS,
	UL_BID_WP_TC_POS,
	UL_BID_WP_SC_POS,
	UL_BID_CF_TC_NEG,
	UL_BID_CF_SC_NEG,
	UL_BID_WP_TC_NEG,
 UL_BID_WP_SC_NEG;

	private String messageKey;
	private String aspectName;

	Aspect() {
		this.aspectName = getAspectName();
		this.messageKey = "de.enwida.chart.aspect."
				+ getAspectName().toLowerCase()
				+ ".title";
	}

	public String getMessageKey() {
		return messageKey;
	}

	public String getAspectName() {
		if (this.aspectName == null) {
			this.aspectName = this.name();
		}
		return this.aspectName;
	}

	private static Map<Aspect, String> aspects = null;

	public static Map<Aspect, String> getAspectMap() {
		if (aspects == null) {
			aspects = new HashMap<Aspect, String>();
			for (final Aspect aspect : Aspect.values()) {
				aspects.put(aspect, aspect.messageKey);
			}
		}
		return aspects;
	}

	public static String getAspectMessageKey(String aspectname) {
		if (aspectname != null) {
			Aspect aspect = Aspect.valueOf(aspectname.toUpperCase());

			if (aspect != null) {
				return aspect.getMessageKey();
			}
		}
		return null;
	}
}
