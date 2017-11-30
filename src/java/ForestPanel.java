import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JPanel;

public class ForestPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int normalTile = 0;
	private final int denseTile = 1;
	private final int concreteTile = 2;
	private final int fireTile = 3;
	
	private int offset = 20;
	private int boxWidth = 10;
	private int boxSpacing = 12;
	
	private int[][] forest;
	
	private int windX;
	private int windY;
	
	/*public void initForest(int xSize, int ySize) {
		int[][] temp = new int[xSize][ySize];
		for (int i = 0; i < xSize; i++) {
			for (int j = 0; j < ySize; j++) {
				temp[i][j] = normalTile;
			}
		}
		
		forest = temp;
	}*/
	
	public void initForest(int a, int b) {
		int[][] temp = {
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,1,1,0},
				{0,0,0,3,0,0,0,1,0,0,0,0,0,0,0,0,1,1,1,1},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0},
				{0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,3,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,3,3,3,0,0,0,0,2,2,2,0,0},
				{0,0,0,0,0,0,0,0,0,3,0,0,0,0,2,2,2,2,2,0},
				{0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,0,0},
				{0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,1,1,1,1,1,1,0,0,0,2,2,2,0,0,0,0,0,0},
				{0,0,0,0,1,1,1,1,0,2,2,2,2,2,0,0,0,0,1,1},
				{0,0,0,0,0,1,0,0,0,2,2,2,2,2,0,0,0,1,1,1},
				{0,0,0,0,0,0,0,0,0,0,0,2,2,2,0,0,0,0,1,0},
		};
		windX = -30;
		windY = -30;
		forest=temp;
	}
	
	public ForestPanel () {
		this.initForest(40,40);
		this.setPreferredSize(new Dimension(this.forest.length*15,this.forest.length*15));
	}
	
	public void spreadFire() {
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
							if (r.nextInt(probability) < 20) forest[i][j] = fireTile;
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
	}
	
	public boolean nextToFire(int [][] forest, int i, int j) {
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
	
	public boolean rightOfFire(int [][] forest, int i, int j) {
		int upperBoundX;
		if (i+1 >= forest.length) upperBoundX = i; else upperBoundX = i+1;
		if (forest[upperBoundX][j] == fireTile) return true;
		return false;
	}
	
	public boolean upOfFire(int [][] forest, int i, int j) {
		int lowerBoundY;
		if (j-1 <= 0) lowerBoundY = j; else lowerBoundY = j-1;
		if (forest[i][lowerBoundY] == fireTile) return true;
		return false;
		
	}
	
	public boolean leftOfFire(int [][] forest, int i, int j) {
		int lowerBoundX;
		if (i-1 <= 0) lowerBoundX = i; else lowerBoundX = i-1;
		if (forest[lowerBoundX][j] == fireTile) return true;
		return false;
	}
	
	public boolean downOfFire(int [][] forest, int i, int j) {
		int upperBoundY;
		if (j+1 >= forest[i].length) upperBoundY = j; else upperBoundY = j+1;
		if (forest[i][upperBoundY] == fireTile) return true;
		return false;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < forest.length; i++) {
			for(int j = 0; j < forest[i].length; j++) {
				switch (forest[j][i]){
					case(normalTile): { 
						g.setColor(Color.GREEN);
						break; 
					}
					case(denseTile): {
						g.setColor(Color.getHSBColor((float)0.33,(float)1.0,(float)0.5));
						break; 
					}
					case(concreteTile): {
						g.setColor(Color.GRAY);
						break;
					}
					case(fireTile): { 
						g.setColor(Color.RED);
						break; 
					}
					default: break;
				}
				g.drawRect(offset+boxSpacing*i,offset+boxSpacing*j,boxWidth,boxWidth);
				g.fillRect(offset+boxSpacing*i,offset+boxSpacing*j,boxWidth,boxWidth);
				repaint();
			}
		}
	}
	
	public int[][] getForest() {
		return forest;
	}
	
}