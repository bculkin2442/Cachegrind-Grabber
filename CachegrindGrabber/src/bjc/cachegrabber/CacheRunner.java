package bjc.cachegrabber;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Runs cachegrind for a given set of parameters
 * 
 * @author ben
 *
 */
public class CacheRunner {
	/**
	 * Run cachegrind for the provided parameters
	 * 
	 * @param ct
	 *            The set of parameters to use
	 * @param progr
	 *            The program to run
	 * @return A scanner attached to the output
	 */
	public static Scanner runCachegrind(CacheTriple ct, String progr) {
		String initCom = "valgrind --tool=cachegrind --I1=4096,64,64 --D1="
				+ ct.getCacheSize() + "," + ct.getAssociativity() + ","
				+ ct.getBlockSize() + " --LL=16384,64,64 " + progr;

		System.err.println("Initial command: " + initCom);
		try {

			ProcessBuilder pb = new ProcessBuilder(initCom.split(" "));

			return new Scanner(pb.start().getErrorStream());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Run cachegrind with multiple differing parameters on the same
	 * program
	 * 
	 * @param cacheSize
	 *            The list of cache sizes to use
	 * @param associativity
	 *            The list of associativities to use
	 * @param blockSize
	 *            The list of block sizes to use
	 * @param progName
	 *            The program to run
	 * @return A map of parameters to data miss percentages
	 */
	public static Map<CacheTriple, Double> runMultiCachegrind(
			Integer[] cacheSize, Integer[] associativity,
			Integer[] blockSize, String progName) {

		Map<CacheTriple, Double> results = new HashMap<>();

		for (int cSize : cacheSize) {
			for (int aCount : associativity) {
				for (int bSize : blockSize) {

					CacheTriple ct = new CacheTriple(bSize, aCount, cSize);

					Scanner resSC = runCachegrind(ct, progName);

					double missPercentage =
							CacheGrabber.getDataMissPercentage(resSC);

					results.put(ct, missPercentage);
				}
			}
		}

		return results;
	}
}
