package bjc.cachegrabber;

/**
 * A triple of the three parameters needed for running cachegrind
 * 
 * @author ben
 *
 */
public class CacheTriple {
	private final int	blockSize;
	private final int	associativity;
	private final int	cacheSize;

	/**
	 * Create a new cache triple
	 * 
	 * @param blockSize
	 *            The block size
	 * @param associativity
	 *            The associativity
	 * @param cacheSize
	 *            The cache size
	 */
	public CacheTriple(int blockSize, int associativity, int cacheSize) {
		this.blockSize = blockSize;
		this.associativity = associativity;
		this.cacheSize = cacheSize;
	}

	/**
	 * @return the associativity
	 */
	public int getAssociativity() {
		return associativity;
	}

	/**
	 * @return the blockSize
	 */
	public int getBlockSize() {
		return blockSize;
	}

	/**
	 * @return the cacheSize
	 */
	public int getCacheSize() {
		return cacheSize;
	}
}
