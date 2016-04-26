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
		String[] lines = new String[1];

		while (scn.hasNextLine()) {
			String strung = scn.nextLine();

			if (strung.contains("D1  miss rate:")) {
				lines = strung.replaceAll("\\s+", " ").split(" ");
				break;
			}
		}

		return Double
				.parseDouble(lines[4].substring(0, lines[4].length() - 1));
	}

}
