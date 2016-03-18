package bjc.cachegrabber;

public class CacheTriple {
	private final int blockSize;
	private final int associativity;
	private final int cacheSize;
	
	public CacheTriple(int blockSize, int associativity, int cacheSize) {
		this.blockSize = blockSize;
		this.associativity = associativity;
		this.cacheSize = cacheSize;
	}

	/**
	 * @return the blockSize
	 */
	public int getBlockSize() {
		return blockSize;
	}

	/**
	 * @return the associativity
	 */
	public int getAssociativity() {
		return associativity;
	}

	/**
	 * @return the cacheSize
	 */
	public int getCacheSize() {
		return cacheSize;
	}
}
