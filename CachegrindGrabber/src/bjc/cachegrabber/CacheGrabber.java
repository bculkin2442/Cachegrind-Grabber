package bjc.cachegrabber;

import java.util.Scanner;

public class CacheGrabber {
	public static double getDataMissPercentage(Scanner scn) {
		String[] strangs = new String[1];

		while (scn.hasNextLine()) {
			String strung = scn.nextLine();

			if (strung.contains("D1  miss rate:")) {
				strangs = strung.replaceAll("\\s+", " ").split(" ");
				break;
			}
		}
		
		return Double.parseDouble(strangs[4].substring(0,
				strangs[4].length() - 1));
	}


}
