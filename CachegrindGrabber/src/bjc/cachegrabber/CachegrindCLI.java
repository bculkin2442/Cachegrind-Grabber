package bjc.cachegrabber;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

public class CachegrindCLI {
	public static void main(String[] args) {
		Scanner scn = new Scanner(System.in);

		System.out.print("Enter the name of the program to profile: ");
		String progName = scn.nextLine();

		Integer[] ia = new Integer[0];

		Integer[] cacheSizes = getCacheSizes(scn).toArray(ia);
		Integer[] blockSizes = getBlockSizes(scn).toArray(ia);
		Integer[] associativities = getAssociativities(scn).toArray(ia);

		Map<CacheTriple, Double> res = CacheRunner.runMultiCachegrind(
				cacheSizes, associativities, blockSizes, progName);

		Set<Entry<CacheTriple, Double>> st = res.entrySet();

		for (Entry<CacheTriple, Double> e : st) {
			CacheTriple ct = e.getKey();

			System.out
					.printf("Cache size: %d - Associativity: %d - Line Size: %d \t\t Data Miss %%:%f",
							ct.cacheSize, ct.associativity, ct.blockSize,
							e.getValue());
		}

		scn.close();
	}

	private static List<Integer> getAssociativities(Scanner scn) {
		System.out.print("Enter the initial associativity to test with: ");
		List<Integer> associativities = new LinkedList<Integer>();
		associativities.add(Integer.parseInt(scn.nextLine()));

		System.out
				.print("Would you like to test with an additional associativity? (true/false): ");

		while (Boolean.parseBoolean(scn.nextLine()) == true) {
			System.out
					.print("Enter the next associativity to test with (in bytes): ");
			associativities.add(Integer.parseInt(scn.nextLine()));

			System.out
					.print("Would you like to test with an additional associativity? (true/false): ");

		}
		return associativities;
	}

	private static List<Integer> getBlockSizes(Scanner scn) {
		System.out
				.print("Enter the initial block size to test with (in bytes): ");
		List<Integer> blockSizes = new LinkedList<Integer>();
		blockSizes.add(Integer.parseInt(scn.nextLine()));

		System.out
				.print("Would you like to test with an additional block size? (true/false): ");

		while (Boolean.parseBoolean(scn.nextLine()) == true) {
			System.out
					.print("Enter the next block size to test with (in bytes): ");
			blockSizes.add(Integer.parseInt(scn.nextLine()));

			System.out
					.print("Would you like to test with an additional block size? (true/false): ");

		}
		return blockSizes;
	}

	private static List<Integer> getCacheSizes(Scanner scn) {
		System.out
				.print("Enter the initial cache size to test with (in bytes): ");
		List<Integer> cacheSizes = new LinkedList<Integer>();
		cacheSizes.add(Integer.parseInt(scn.nextLine()));

		System.out
				.print("Would you like to test with an additional cache size? (true/false): ");

		while (Boolean.parseBoolean(scn.nextLine()) == true) {
			System.out
					.print("Enter the next cache size to test with (in bytes): ");
			cacheSizes.add(Integer.parseInt(scn.nextLine()));

			System.out
					.print("Would you like to test with an additional cache size? (true/false): ");

		}
		return cacheSizes;
	}
}
