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
	@Override
	public int compare(Entry<CacheTriple, Double> left,
			Entry<CacheTriple, Double> right) {
		int tripleComparison = compareTriples(left.getKey(),
				right.getKey());

		if (tripleComparison != 0) {
			return tripleComparison;
		} else {
			return (int) (left.getValue() - right.getValue());
		}
	}

	private static int compareTriples(CacheTriple left,
			CacheTriple right) {
		if (left.getCacheSize() == right.getCacheSize()) {
			if (left.getBlockSize() == right.getBlockSize()) {
				if (left.getAssociativity() == right.getAssociativity()) {
					return 0;
				} else {
					return left.getAssociativity()
							- right.getAssociativity();
				}
			} else {
				return left.getBlockSize() - right.getBlockSize();
			}
		} else {
			return left.getCacheSize() - right.getCacheSize();
		}
	}
}