package bjc.cachegrabber;

import java.util.Comparator;
import java.util.Map.Entry;

/**
 * A comparator for results from cachegrind
 * 
 * @author ben
 *
 */
public class CacheResultComparator
		implements Comparator<Entry<CacheTriple, Double>> {
	private static int compareTriples(CacheTriple left,
			CacheTriple right) {
		if (left.getCacheSize() == right.getCacheSize()) {
			if (left.getBlockSize() == right.getBlockSize()) {
				if (left.getAssociativity() == right.getAssociativity()) {
					return 0;
				}

				return left.getAssociativity() - right.getAssociativity();
			}

			return left.getBlockSize() - right.getBlockSize();
		}

		return left.getCacheSize() - right.getCacheSize();
	}

	@Override
	public int compare(Entry<CacheTriple, Double> left,
			Entry<CacheTriple, Double> right) {
		int tripleComparison =
				compareTriples(right.getKey(), left.getKey());

		if (tripleComparison != 0) {
			return tripleComparison;
		}

		return (int) (left.getValue() - right.getValue());
	}
}