import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class ForestPanel extends JPanel {
	
	// This was needed for whatever reason
	private static final long serialVersionUID = 1L;
	
	// Tile translation
	private final int normalTile = 0;
	private final int denseTile = 1;
	private final int concreteTile = 2;
	private final int fireTile = 3;
	private final int victimTile = 4;
	private final int firefighterTile = 5;
	private final int firefighterWithVictimTile = 6;
	
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
	
	// array for storing firefighters and their information
	private ArrayList<Firefighter> firefighters;
	
	public void initRandomForest(int xSize, int ySize) {

		Random r = new Random();
		int[][] temp = new int[xSize][ySize];
		for (int i = 0; i < ySize; i++) {
			for (int j = 0; j < xSize; j++) {
				temp[i][j] = normalTile;
			}
		}
		
		for (int i = 0; i < 5; i++) {
			
			int clusterType = r.nextInt(3);
			int x = r.nextInt(temp.length);
			int y = r.nextInt(temp[x].length);
			int xS = r.nextInt(10);
			int yS = r.nextInt(10);
			int tileType = r.nextInt(4);
			
			while(tileType == fireTile || tileType == normalTile) tileType = r.nextInt(3);
			
			temp = this.addCluster(temp, clusterType, tileType, x, y, xS, yS);
			
		}
			
		int fires = r.nextInt(3)+1;
		for (int i = 0; i < fires; i++) {
			this.addCluster(temp, r.nextInt(2), fireTile, r.nextInt(temp.length), r.nextInt(temp[0].length), r.nextInt(3), r.nextInt(3));
		}
		
		int sign = r.nextBoolean() ? 1 : -1;
		windX = r.nextInt(100) * sign;
		windY = r.nextInt(100) * sign;
		
		forest = temp;
		initialState=forest;
	}
	
	/*public void initForest(int a, int b) {
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
		initialState=forest;
	}*/
	
	public ForestPanel () {			
		/*this.initForest(40,40);
		this.initFirefighters(3);
		this.setPreferredSize(new Dimension(this.forest.length*15,this.forest.length*15));*/
	}
	

	public ForestPanel(int posX, int posY, int windX, int windY, int[][]forest) {
		this.windX = windX;
		this.windY = windY;
		// TODO change
		this.forest = forest;
		this.initialState = forest;

		this.initFirefighters(5);
		this.setPreferredSize(new Dimension(this.forest.length*15,this.forest.length*15));
	}
	
	
	public ForestPanel(String dummy) {
		// TODO change
		this.initRandomForest(40,40);
		this.initFirefighters(5);
		this.setPreferredSize(new Dimension(this.forest.length*15,this.forest.length*15));
	}
	
	
	public void initFirefighters(int num) {
		this.firefighters = new ArrayList<Firefighter>();
		while (num > 0) {
			// TODO
			this.addFirefighter(5, 5);
			num--;
		}
	}
	
	public void addFirefighter(int x, int y) {
		this.firefighters.add(new Firefighter(x,y,forest.length, forest[0].length));
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
				
				for (Firefighter f : this.firefighters) {
					if (f.x == i && f.y == j) g.setColor(Color.BLUE);
				}
				
				g.drawRect(offset+boxSpacing*i,offset+boxSpacing*j,boxWidth,boxWidth);
				g.fillRect(offset+boxSpacing*i,offset+boxSpacing*j,boxWidth,boxWidth);
				repaint();
			}
		}
	}
	
}