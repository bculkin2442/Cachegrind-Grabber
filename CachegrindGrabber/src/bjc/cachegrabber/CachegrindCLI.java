package bjc.cachegrabber;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import com.bethecoder.ascii_table.ASCIITableFactory;
import com.bethecoder.ascii_table.spec.AlignmentValues;

/**
 * Command line interface to the Cachegrind grabber
 * 
 * @author ben
 *
 */
public class CachegrindCLI {
	private static NumberFormat decimalFormatter;

	static {
		decimalFormatter = DecimalFormat.getInstance();
	}

	private static String[][]
			buildCacheResults(Set<Entry<CacheTriple, Double>> resultSet) {
		String[][] stringizedResults = new String[resultSet.size()][4];

		int i = 0;

		for (Entry<CacheTriple, Double> entry : resultSet) {
			CacheTriple triple = entry.getKey();

			BigDecimal entryValue = new BigDecimal(entry.getValue());

			stringizedResults[i][0] =
					Integer.toString(triple.getCacheSize());
			stringizedResults[i][1] =
					Integer.toString(triple.getBlockSize());
			stringizedResults[i][2] =
					Integer.toString(triple.getAssociativity());
			stringizedResults[i][3] =
					decimalFormatter.format(entryValue) + "%";

			i++;
		}

		return stringizedResults;
	}

	private static List<Integer> getIntArray(Scanner inputSource,
			String fieldName) {
		System.out.print(
				"Enter the initial " + fieldName + " to test with: ");
		List<Integer> associativities = new LinkedList<>();
		associativities.add(Integer.parseInt(inputSource.nextLine()));

		System.out.print("Would you like to test with an additional "
				+ fieldName + "? (true/false): ");

		while (Boolean.parseBoolean(inputSource.nextLine()) == true) {
			System.out.print("Enter the next " + fieldName
					+ " to test with (in bytes): ");
			associativities.add(Integer.parseInt(inputSource.nextLine()));

			System.out.print(
					"Would you like to test with an additional associativity? (true/false): ");

		}
		return associativities;
	}

	/**
	 * Main method
	 * 
	 * @param args
	 *            CLI args
	 */
	public static void main(String[] args) {
		Scanner inputSource = new Scanner(System.in);

		System.out.print("Use multi-profiling mode? (true/false): ");
		boolean multiMode = Boolean.parseBoolean(inputSource.nextLine());

		Integer[] intArr = new Integer[0];

		Integer[] cacheSizes =
				getIntArray(inputSource, "cache size").toArray(intArr);
		Integer[] blockSizes =
				getIntArray(inputSource, "block size").toArray(intArr);
		Integer[] associativities =
				getIntArray(inputSource, "associativities")
						.toArray(intArr);

		if (multiMode) {
			System.out.print(
					"Enter the name of the first program to profile: ");
			List<String> progNames = new LinkedList<>();
			progNames.add(inputSource.nextLine());

			System.out.print(
					"Would you like to profile an additional program? (true/false): ");

			while (Boolean.parseBoolean(inputSource.nextLine()) == true) {
				System.out.print(
						"Enter the name of the next program to profile: ");
				progNames.add(inputSource.nextLine());

				System.out.print(
						"Would you like to profile an additional program? (true/false): ");
			}

			for (String progName : progNames) {
				runCachegrind(cacheSizes, blockSizes, associativities,
						progName);
			}
		} else {
			System.out.print("Enter the name of the program to profile: ");
			String progName = inputSource.nextLine();

			runCachegrind(cacheSizes, blockSizes, associativities,
					progName);
		}

		inputSource.close();
	}

	private static void runCachegrind(Integer[] cacheSizes,
			Integer[] blockSizes, Integer[] associativities,
			String progName) {
		Map<CacheTriple, Double> programResults =
				CacheRunner.runMultiCachegrind(cacheSizes, associativities,
						blockSizes, progName);
		Set<Entry<CacheTriple, Double>> resultSet =
				programResults.entrySet();

		String[][] stringizedResults =
				buildCacheResults(sortCacheResults(resultSet));

		System.out.println("Results for program " + progName);

		ASCIITableFactory.getDefault().printTable(
				new String[] { "Cache size", "Line size", "Associativity",
						"Data Miss Percentage" },
				stringizedResults, AlignmentValues.ALIGN_CENTER);

		System.out.println();
	}

	private static Set<Entry<CacheTriple, Double>>
			sortCacheResults(Set<Entry<CacheTriple, Double>> resultSet) {
		List<Entry<CacheTriple, Double>> resultList =
				new ArrayList<>(resultSet);

		Collections.sort(resultList, new CacheResultComparator());

		return new HashSet<>(resultList);
	}
}