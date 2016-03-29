package bjc.cachegrabber;

import java.util.Scanner;

/**
 * Class to retrieve data miss percentage from cachegrind output
 * 
 * @author ben
 *
 */
public class CacheGrabber {
	/**
	 * Get the data miss percentage from a scanner attached to cachegrind
	 * output
	 * 
	 * @param scn
	 *            A scanner attached to cachegrind output
	 * @return The data miss percentage of the provided output
	 */
	public static double getDataMissPercentage(Scanner scn) {
		String[] strangs = new String[1];

		while (scn.hasNextLine()) {
			String strung = scn.nextLine();

			if (strung.contains("D1  miss rate:")) {
				strangs = strung.replaceAll("\\s+", " ").split(" ");
				break;
			}
		}

		return Double.parseDouble(
				strangs[4].substring(0, strangs[4].length() - 1));
	}

}
