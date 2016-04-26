package bjc.cachegrabber;

import java.io.IOException;
import java.text.MessageFormat;
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
	 * @param triple
	 *            The set of parameters to use
	 * @param programName
	 *            The program to run
	 * @return A scanner attached to the output
	 */
	public static Scanner runCachegrind(CacheTriple triple,
			String programName) {
		String initCom = MessageFormat.format(
				"valgrind --tool=cachegrind --I1=4096,64,64 --D1={0},{1},{2} --LL=16384,64,64 {3}",
				triple.getCacheSize(), triple.getAssociativity(),
				triple.getBlockSize(), programName);

		System.err.println("Running command: " + initCom);

		try {
			ProcessBuilder pb = new ProcessBuilder(initCom.split(" "));

			return new Scanner(pb.start().getErrorStream());
		} catch (IOException ioex) {
			ioex.printStackTrace();

			return null;
		}
	}

	/**
	 * Run cachegrind with multiple differing parameters on the same
	 * program
	 * 
	 * @param cacheSizes
	 *            The list of cache sizes to use
	 * @param associativities
	 *            The list of associativities to use
	 * @param blockSizes
	 *            The list of block sizes to use
	 * @param programName
	 *            The program to run
	 * @return A map of parameters to data miss percentages
	 */
	public static Map<CacheTriple, Double> runMultiCachegrind(
			Integer[] cacheSizes, Integer[] associativities,
			Integer[] blockSizes, String programName) {

		Map<CacheTriple, Double> results = new HashMap<>();

		for (int cacheSize : cacheSizes) {
			for (int associativity : associativities) {
				for (int blockSize : blockSizes) {

					CacheTriple triple = new CacheTriple(blockSize,
							associativity, cacheSize);

					Scanner resultScanner =
							runCachegrind(triple, programName);

					double missPercentage = CacheGrabber
							.getDataMissPercentage(resultScanner);

					resultScanner.close();

					results.put(triple, missPercentage);
				}
			}
		}

		return results;
	}
}
