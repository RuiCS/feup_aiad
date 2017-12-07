import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class ForestPanel extends JPanel {
	
	// This was needed for whatever reason
	private static final long serialVersionUID = 1L;
	
	// Tile translation
	private final int victimTile = 4;
	private final int firefighterTile = 5;
	private final int firefighterWithVictimTile = 6;
	//TODO clean up
	public static final int NORMALTILE = 0;
	public static final int DENSETILE = 1;
	public static final int CONCRETETILE = 2;
	public static final int FIRETILE = 3;
	public static final int PEOPLETILE = 4;
	public static final int FIREFIGHTER = 5;
	public static int WIDTH;
	public static int HEIGHT;
	
	// important GUI constants
	private int offset = 20;
	private int boxWidth = 10;
	private int boxSpacing = 12;
	
	// Where the game is set
	private int[][] forest;
	// Game's last state for clearing
	private int[][] initialState;
	
	// Wind variables
	private int windX;
	private int windY;
	
	public void initRandomForest(int xSize, int ySize) {

		Random r = new Random();
		int[][] temp = new int[xSize][ySize];
		for (int i = 0; i < ySize; i++) {
			for (int j = 0; j < xSize; j++) {
				temp[i][j] = this.NORMALTILE;
			}
		}
		
		for (int i = 0; i < 5; i++) {
			
			int clusterType = r.nextInt(3);
			int x = r.nextInt(temp.length);
			int y = r.nextInt(temp[x].length);
			int xS = r.nextInt(10);
			int yS = r.nextInt(10);
			int tileType = r.nextInt(4);
			
			while(tileType == this.FIRETILE || tileType == this.NORMALTILE) tileType = r.nextInt(3);
			
			temp = this.addCluster(temp, clusterType, tileType, x, y, xS, yS);
			
		}
			
		int fires = r.nextInt(3)+1;
		for (int i = 0; i < fires; i++) {
			this.addCluster(temp, r.nextInt(2), this.FIRETILE, r.nextInt(temp.length), r.nextInt(temp[0].length), r.nextInt(3), r.nextInt(3));
		}
		
		int sign = r.nextBoolean() ? 1 : -1;
		windX = r.nextInt(100) * sign;
		windY = r.nextInt(100) * sign;
		
		forest = temp;
		initialState=forest;
	}
	

	public void defaultConfig() {
		this.WIDTH = forest.length;
		this.HEIGHT = forest[0].length;
		this.setPreferredSize(new Dimension(offset*2 + this.forest.length*(boxSpacing), offset*2 + this.forest.length*(boxSpacing)));
		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Simulator Window"));
	}
	
	public ForestPanel(int posX, int posY, int windX, int windY, int[][]forest) {
		this.windX = windX;
		this.windY = windY;
		// TODO change
		this.forest = forest;
		this.initialState = forest;
		this.defaultConfig();
	}
	
	
	public ForestPanel(String dummy) {
		// TODO change
		this.initRandomForest(40,40);
		this.defaultConfig();
	}
	
	public int[][] addCluster(int[][] f, int clusterType, int tileType, int x, int y, int xSize, int ySize) {
		
		int[][] temp = f;
		
		switch (clusterType) {
		
			// Circle
			case 0: {
				for (int i = 0; i < temp.length; i++) {
					for(int j = 0; j < temp[i].length; j++) {
						if ( ((j - x)*(j - x) + (i - y)*(i - y)) <= xSize*xSize) {
							temp[i][j] = tileType;
						}
					}
				}
				break;
			}
			
			// Square
			case 1: {
				for (int i = 0; i < temp.length; i++) {
					for(int j = 0; j < temp[i].length; j++) {
						if ( Math.abs(j-x) <= xSize/2 ) {
							if ( Math.abs(i-y) <= ySize/2) {
								if(j < 0 || i < 0 || i > (temp.length-1) || j > (temp[i].length-1)) {
									continue;
								}else {
									temp[i][j] = tileType;
								}
							}
						}
					}
				}
				break;
			}
			// Elipse
			case 2: {
				int t = ySize;
				ySize = xSize;
				xSize = t;
				for (int i = -ySize; i<= ySize; i++) {
					for (int j = -xSize; j<= xSize; j++) {
						if (j*j*ySize*ySize + i*i*xSize*xSize <= ySize*ySize*xSize*xSize) {
							if ( x+j < 0 || y+i < 0 || x+j >= temp.length || y+i >= temp[0].length ) {
								continue;
							}
							else temp[x+j][y+i] = tileType;
						}
					}
				}
			}
			default: break;
		}
		return temp;
	}
	
	public void spreadFire() {
		this.forest = FireSpreading.spreadFire(forest, windX, windY);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < forest.length; i++) {
			for(int j = 0; j < forest[i].length; j++) {
				switch (forest[j][i]){
					case(NORMALTILE): { 
						g.setColor(Color.GREEN);
						break; 
					}
					case(DENSETILE): {
						g.setColor(Color.getHSBColor((float)0.33,(float)1.0,(float)0.5));
						break; 
					}
					case(CONCRETETILE): {
						g.setColor(Color.GRAY);
						break;
					}
					case(FIRETILE): { 
						g.setColor(Color.RED);
						break; 
					}
					case(FIREFIGHTER): { 
						g.setColor(Color.ORANGE);
						break; 
					}
					case(PEOPLETILE): { 
						g.setColor(Color.BLUE);
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
	
	  /**
     * Getter
     * 
     * @return Matrix representing the current environment
     */
	public int[][] getForest() {
		return forest;
	}
	
	public int[][] getInitialForest() {
		return initialState;
	}
	

	public int getWindX() {
		return windX;
	}
	
	public int getWindY() {
		return windY;
	}
	
}