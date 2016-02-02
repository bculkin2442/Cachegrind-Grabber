package bjc.cachegrabber;

public class CacheTriple {
	public final int blockSize;
	public final int associativity;
	public final int cacheSize;
	
	public CacheTriple(int blockSize, int associativity, int cacheSize) {
		this.blockSize = blockSize;
		this.associativity = associativity;
		this.cacheSize = cacheSize;
	}
}
