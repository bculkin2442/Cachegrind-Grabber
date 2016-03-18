package bjc.cachegrabber;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CacheRunner {
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

	public static Map<CacheTriple, Double> runMultiCachegrind(
			Integer[] cacheSize, Integer[] associativity,
			Integer[] blockSize, String progName) {

		Map<CacheTriple, Double> results = new HashMap<CacheTriple, Double>();

		for (int cSize : cacheSize) {
			for (int aCount : associativity) {
				for (int bSize : blockSize) {

					CacheTriple ct = new CacheTriple(bSize, aCount, cSize);

					Scanner resSC = runCachegrind(ct, progName);

					double missPercentage = CacheGrabber
							.getDataMissPercentage(resSC);

					results.put(ct, missPercentage);
				}
			}
		}

		return results;
	}
}
