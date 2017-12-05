import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LabelPanel extends JPanel {

	ForestPanel panel;
	
	String borderString = "Simulator Labels";
	JLabel fireLabel;
	JLabel normalLabel;
	JLabel denseLabel;
	JLabel concreteLabel;
	JLabel firefighterLabel;
	JLabel firefighterVitimLabel;
	JLabel victimLabel;
	JLabel windXLabel;
	JLabel windYLabel;
	JLabel windLabel;
	JLabel normalColor;
	JLabel denseColor;
	JLabel fireColor;
	JLabel firefighterColor;
	JLabel victimColor;
	JLabel concreteColor;
	
	JPanel windPanel;
	JPanel tilesPanel;
	JPanel firefighterPanel;
	
	public LabelPanel(ForestPanel panel) {
		this.panel = panel;
		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), borderString));
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(350, 400));
		
		windPanel = new JPanel(new GridBagLayout());
		tilesPanel = new JPanel(new GridBagLayout());
		firefighterPanel = new JPanel(new GridBagLayout());
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(10,10,10,10);
		constraints.gridx = 0;
		constraints.gridy = 0;
		
		tilesPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Tiles"));
		normalLabel = new JLabel("normal vegetation");
		denseLabel = new JLabel("dense vegetation");
		concreteLabel = new JLabel("building");
		fireLabel = new JLabel("fire");
		firefighterLabel = new JLabel("firefighter");
		victimLabel = new JLabel("civillian");
		
		normalColor = new JLabel("     ");
		normalColor.setOpaque(true);
		normalColor.setBackground(Color.GREEN);
		denseColor = new JLabel("     ");
		denseColor.setOpaque(true);
		denseColor.setBackground(Color.getHSBColor((float)0.33,(float)1.0,(float)0.5));
		fireColor = new JLabel("     ");
		fireColor.setOpaque(true);
		fireColor.setBackground(Color.RED);
		concreteColor = new JLabel("     ");
		concreteColor.setOpaque(true);
		concreteColor.setBackground(Color.GRAY);
		firefighterColor = new JLabel("     ");
		firefighterColor.setOpaque(true);
		firefighterColor.setBackground(Color.ORANGE);
		victimColor = new JLabel("     ");
		victimColor.setOpaque(true);
		victimColor.setBackground(Color.BLUE);
	
		tilesPanel.add(normalColor, constraints);
		constraints.gridx++;
		tilesPanel.add(normalLabel, constraints);
		constraints.gridy++;
		constraints.gridx=0;
		tilesPanel.add(denseColor, constraints);
		constraints.gridx++;
		tilesPanel.add(denseLabel, constraints);
		constraints.gridy++;
		constraints.gridx=0;
		tilesPanel.add(fireColor, constraints);
		constraints.gridx++;
		tilesPanel.add(fireLabel, constraints);
		constraints.gridy++;
		constraints.gridx=0;
		tilesPanel.add(concreteColor, constraints);
		constraints.gridx++;
		tilesPanel.add(concreteLabel, constraints);
		constraints.gridy++;
		constraints.gridx=0;
		tilesPanel.add(firefighterColor, constraints);
		constraints.gridx++;
		tilesPanel.add(firefighterLabel, constraints);
		constraints.gridy++;
		constraints.gridx=0;
		tilesPanel.add(victimColor, constraints);
		constraints.gridx++;
		tilesPanel.add(victimLabel, constraints);
		
		// TODO VICTIMS
		
		constraints.gridy = 0;
		constraints.gridx = 0;
		this.add(tilesPanel, constraints);
		
		windPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Wind"));
		windLabel = new JLabel("Wind Intensity:");
		windPanel.add(windLabel);

		int windX = this.panel.getWindX();
		int windY = this.panel.getWindY();
		
		if (windX > 0) // W-E
			windXLabel = new JLabel("W -> E: " + Math.abs(windX) + " km/h");
		else if (windX < 0) {
			windXLabel = new JLabel("E -> W: " + Math.abs(windX) + " km/h");
		}
		
		if (windY > 0) // N-S
			windYLabel = new JLabel("N -> S: " + Math.abs(windY) + " km/h");
		else if (windY < 0)
			windYLabel = new JLabel("S -> N: " + Math.abs(windY) + " km/h");
		
		if (windX == 0 && windY == 0)
			windXLabel = new JLabel("No wind on simulation");
		
		constraints.gridy++;
		windPanel.add(windXLabel, constraints);
		constraints.gridy++;
		if (windYLabel != null) windPanel.add(windYLabel, constraints);
		
		constraints.gridy = 1;
		this.add(windPanel, constraints);
		
	}
	
}
