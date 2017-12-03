import java.util.Random;

public class FireSpreading {

	private static final int normalTile = 0;
	private static final int denseTile = 1;
	private static final int concreteTile = 2;
	private static final int fireTile = 3;
	
	public static int[][] spreadFire(int[][] forest, int windX, int windY) {
		
		Random r = new Random();
		for (int i = 0; i < forest.length; i++) {
			for (int j = 0; j < forest[i].length; j++) {
				// if on a fire tile -> wtv
				// if next to a fire tile -> spread?
				// if not next to a fire tile -> wtv
				if (forest[i][j] == fireTile) continue;
				
				if (nextToFire(forest,i,j)) {
					
					int windStrengthX = 0;
					int windStrengthY = 0;
					
					if (leftOfFire(forest,i,j) && windX < 0) windStrengthX = Math.abs(windX);
					if (rightOfFire(forest,i,j) && windX > 0) windStrengthX = Math.abs(windX);
					if (upOfFire(forest,i,j) && windY < 0) windStrengthY = Math.abs(windY);
					if (downOfFire(forest,i,j) && windY > 0) windStrengthY = Math.abs(windY);
					
					int probability = 0;
					probability = 100 - (int)(0.5 * (windStrengthX+windStrengthY));
					
					if (probability <= 1) probability = 1;
					
					switch (forest[i][j]) {
						case normalTile: {
							if (r.nextInt(probability) < 7) forest[i][j] = fireTile;
							break;
						}
						case denseTile: {
							if (r.nextInt(probability) < 40) forest[i][j] = fireTile;
							break;
						}
						case concreteTile: {
							if (r.nextInt(probability) < 2) forest[i][j] = fireTile;
							break;
						}
					default: break;
					}
				}
				else {}
			}
		}
		
		return forest;
	}
	
	public static boolean nextToFire(int [][] forest, int i, int j) {
		int upperBoundX, lowerBoundX;
		int upperBoundY, lowerBoundY;
		
		if (i+1 >= forest.length) upperBoundX = i; else upperBoundX = i+1;
		if (i-1 <= 0) lowerBoundX = i; else lowerBoundX = i-1;
		if (j+1 >= forest[i].length) upperBoundY = j; else upperBoundY = j+1;
		if (j-1 <= 0) lowerBoundY = j; else lowerBoundY = j-1;
		
		if (forest[upperBoundX][j] == fireTile) return true;
		if (forest[lowerBoundX][j] == fireTile) return true;
		if (forest[i][upperBoundY] == fireTile) return true;
		if (forest[i][lowerBoundY] == fireTile) return true;
		
		return false;
	}
	
	
	public static boolean rightOfFire(int [][] forest, int i, int j) {
		int upperBoundX;
		if (i+1 >= forest.length) upperBoundX = i; else upperBoundX = i+1;
		if (forest[upperBoundX][j] == fireTile) return true;
		return false;
	}
	
	public static boolean upOfFire(int [][] forest, int i, int j) {
		int lowerBoundY;
		if (j-1 <= 0) lowerBoundY = j; else lowerBoundY = j-1;
		if (forest[i][lowerBoundY] == fireTile) return true;
		return false;
		
	}
	
	public static boolean leftOfFire(int [][] forest, int i, int j) {
		int lowerBoundX;
		if (i-1 <= 0) lowerBoundX = i; else lowerBoundX = i-1;
		if (forest[lowerBoundX][j] == fireTile) return true;
		return false;
	}
	
	public static boolean downOfFire(int [][] forest, int i, int j) {
		int upperBoundY;
		if (j+1 >= forest[i].length) upperBoundY = j; else upperBoundY = j+1;
		if (forest[i][upperBoundY] == fireTile) return true;
		return false;
	}
	
}
