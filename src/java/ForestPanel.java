import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

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
	
	/*public void initForest(int xSize, int ySize) {
		int[][] temp = new int[xSize][ySize];
		for (int i = 0; i < xSize; i++) {
			for (int j = 0; j < ySize; j++) {
				temp[i][j] = normalTile;
			}
		}
		
		temp = this.addCluster(temp, 1, 5, 5, 5);
		
		forest = temp;
	}
	*/
	/*public int[][] addCluster(int[][] f, int type, int x, int y, int radius) {
		for (int i = 0; i < f.length; i++) {
			for (int j = 0; j < f[i].length; j++) {
				if ((i > x && i < (x+radius)) || (i < x && i > (x+radius))) {
					if  ((j > y && j < (y+radius)) || (j < x && j > (y+radius))) {
						f[i][j] = type;
					}
				}
			}
		}
		return f;
	}*/
	public void initForest(int a, int b) {
		int[][] temp = {
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,1,1,0},
				{0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,1,1,1},
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
		
		forest=temp;
	}
	
	public ForestPanel () {
		this.initForest(40,40);
		this.setPreferredSize(new Dimension(this.forest.length*15,this.forest.length*15));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < forest.length; i++) {
			for(int j = 0; j < forest[i].length; j++) {
				switch (forest[i][j]){
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
	
}