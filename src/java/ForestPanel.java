import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class ForestPanel extends JPanel {
	
	private final int normalTile = 0;
	private final int denseTile = 1;
	private final int fireTile = 2;
	
	private int offset = 20;
	private int boxWidth = 10;
	private int boxSpacing = 12;
	
	private int[][] forest;
	
	public void initForest(int xSize, int ySize) {
		int[][] temp = new int[xSize][ySize];
		for (int i = 0; i < xSize; i++) {
			for (int j = 0; j < ySize; j++) {
				//temp[i][j] = normalTile;
				
				// testing colors: change this
				temp[i][j] = (int)(Math.random()*3);
			}
		}
		forest = temp;
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
						g.setColor(Color.getHSBColor((float)0.33,(float)0.6,(float)1.0));
						break; 
					}
					case(denseTile): {
						g.setColor(Color.getHSBColor((float)0.33,(float)1.0,(float)0.4));
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