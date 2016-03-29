package bjc.cachegrabber;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import com.bethecoder.ascii_table.ASCIITable;

/**
 * Command line interface to the Cachegrind grabber
 * @author ben
 *
 */
public class CachegrindCLI {
	/**
	 * Main method
	 * @param args Unused CLI args
	 */
	public static void main(String[] args) {
		Scanner scn = new Scanner(System.in);

		System.out.print("Use multi-profiling mode? (true/false): ");
		boolean multiMode = Boolean.parseBoolean(scn.nextLine());

		Integer[] ia = new Integer[0];

		Integer[] cacheSizes = getCacheSizes(scn).toArray(ia);
		Integer[] blockSizes = getBlockSizes(scn).toArray(ia);
		Integer[] associativities = getAssociativities(scn).toArray(ia);

		if (multiMode) {
			System.out.print(
					"Enter the name of the first program to profile: ");
			List<String> progNames = new LinkedList<>();
			progNames.add(scn.nextLine());

			System.out.print(
					"Would you like to profile an additional program? (true/false): ");

			while (Boolean.parseBoolean(scn.nextLine()) == true) {
				System.out.print(
						"Enter the name of the next program to profile: ");
				progNames.add(scn.nextLine());

				System.out.print(
						"Would you like to profile an additional program? (true/false): ");
			}

			for (String progName : progNames) {
				Map<CacheTriple, Double> res = CacheRunner
						.runMultiCachegrind(cacheSizes, associativities,
								blockSizes, progName);
				Set<Entry<CacheTriple, Double>> st = res.entrySet();
				String[][] cacheResults = buildCacheResults(st);

				System.out.println("Results for program " + progName);

				ASCIITable.getInstance().printTable(
						new String[] { "Cache size", "Line size",
								"Associativity", "Data Miss Percentage" },
						cacheResults, ASCIITable.ALIGN_CENTER);

				System.out.println();
			}
		} else {
			System.out.print("Enter the name of the program to profile: ");
			String progName = scn.nextLine();

			Map<CacheTriple, Double> res = CacheRunner.runMultiCachegrind(
					cacheSizes, associativities, blockSizes, progName);

			Set<Entry<CacheTriple, Double>> st = res.entrySet();

			String[][] cacheResults = buildCacheResults(st);

			ASCIITable.getInstance().printTable(
					new String[] { "Cache size", "Line size",
							"Associativity", "Data Miss Percentage" },
					cacheResults, ASCIITable.ALIGN_CENTER);
		}
		/*
		 * for (Entry<CacheTriple, Double> e : st) { CacheTriple ct =
		 * e.getKey();
		 * 
		 * System.out.printf(
		 * "Cache size: %d - Associativity: %d - Line Size: %d \t\t Data Miss %%:%f"
		 * , ct.getCacheSize(), ct.getAssociativity(), ct.getBlockSize(),
		 * e.getValue()); }
		 */

		scn.close();
	}

	private static String[][] buildCacheResults(
			Set<Entry<CacheTriple, Double>> st) {
		String[][] res = new String[st.size()][4];

		int i = 0;

		for (Entry<CacheTriple, Double> entry : st) {
			CacheTriple triple = entry.getKey();

			BigDecimal bd = new BigDecimal(entry.getValue());
			NumberFormat nf = DecimalFormat.getInstance();

			res[i][0] = Integer.toString(triple.getCacheSize());
			res[i][1] = Integer.toString(triple.getBlockSize());
			res[i][2] = Integer.toString(triple.getAssociativity());
			res[i][3] = nf.format(bd) + "%";

			i++;
		}

		return res;
	}

	private static List<Integer> getAssociativities(Scanner scn) {
		System.out.print("Enter the initial associativity to test with: ");
		List<Integer> associativities = new LinkedList<>();
		associativities.add(Integer.parseInt(scn.nextLine()));

		System.out.print(
				"Would you like to test with an additional associativity? (true/false): ");

		while (Boolean.parseBoolean(scn.nextLine()) == true) {
			System.out.print(
					"Enter the next associativity to test with (in bytes): ");
			associativities.add(Integer.parseInt(scn.nextLine()));

			System.out.print(
					"Would you like to test with an additional associativity? (true/false): ");

		}
		return associativities;
	}

	private static List<Integer> getBlockSizes(Scanner scn) {
		System.out.print(
				"Enter the initial block size to test with (in bytes): ");
		List<Integer> blockSizes = new LinkedList<>();
		blockSizes.add(Integer.parseInt(scn.nextLine()));

		System.out.print(
				"Would you like to test with an additional block size? (true/false): ");

		while (Boolean.parseBoolean(scn.nextLine()) == true) {
			System.out.print(
					"Enter the next block size to test with (in bytes): ");
			blockSizes.add(Integer.parseInt(scn.nextLine()));

			System.out.print(
					"Would you like to test with an additional block size? (true/false): ");

		}
		return blockSizes;
	}

	private static List<Integer> getCacheSizes(Scanner scn) {
		System.out.print(
				"Enter the initial cache size to test with (in bytes): ");
		List<Integer> cacheSizes = new LinkedList<>();
		cacheSizes.add(Integer.parseInt(scn.nextLine()));

		System.out.print(
				"Would you like to test with an additional cache size? (true/false): ");

		while (Boolean.parseBoolean(scn.nextLine()) == true) {
			System.out.print(
					"Enter the next cache size to test with (in bytes): ");
			cacheSizes.add(Integer.parseInt(scn.nextLine()));

			System.out.print(
					"Would you like to test with an additional cache size? (true/false): ");

		}
		return cacheSizes;
	}
}
