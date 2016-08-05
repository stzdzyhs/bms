
package com.db.bms.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;


public class MathUtils {

	public static String percentage(double count, double total, int scale) {
		DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
		df.setMaximumFractionDigits(scale);
		df.setRoundingMode(RoundingMode.HALF_UP);
		double accuracy_num = count / total * 100;
		return df.format(accuracy_num) + "%";
	}
}
