import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ForestGUI extends JFrame {
    	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ForestPanel panel;
	public OptionsPanel options;
	public JPanel casing;
	public LabelPanel labels;	
	
	// 0 - none; 1 - random; 2 - file
	public int state;
	
	class OptionsPanel extends JPanel{

		private static final long serialVersionUID = 1L;
		public String fileName;
		
		public JButton randomBtn;
		public JButton loadfile;
		public JTextField fileSrc;
		
		public OptionsPanel() {
			
			JPanel manualOptions = new JPanel();
			JPanel randomOptions = new JPanel();
			JPanel fileOptions = new JPanel();
			
			manualOptions.setLayout(new GridBagLayout());
			randomOptions.setLayout(new GridBagLayout());
			fileOptions.setLayout(new GridBagLayout());
			
			this.setLayout(new GridBagLayout());
			this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Firefighting Simulator"));
			this.setPreferredSize(new Dimension(700,400));
			
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.anchor = GridBagConstraints.WEST;
			constraints.insets = new Insets(10,10,10,10);
			
			JLabel random = new JLabel("Start a random board");
			randomBtn = new JButton("Start");
			
			constraints.gridx = 0;
			constraints.gridy = 0;
			
			randomOptions.add(random,constraints);
			constraints.gridx = 1;
			randomOptions.add(randomBtn, constraints);
			randomOptions.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Random Setup"));
			
			constraints.gridx = 0;
			constraints.gridy = 1;
			this.add(randomOptions, constraints);
			
			constraints.gridy = 2;
			JLabel file = new JLabel("Enter a filename");
			fileSrc = new JTextField(10);
			fileOptions.add(file,constraints);
			constraints.gridx = 1;
			fileOptions.add(fileSrc,constraints);
			loadfile = new JButton("Load");
			constraints.gridy = 3;
			fileOptions.add(loadfile,constraints);
			fileOptions.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Load From File"));
			
			constraints.gridx = 0;
			this.add(fileOptions, constraints);
						
		}
		
	}
	
	public ForestGUI() {
		
		casing = new JPanel();
		options = new OptionsPanel();
		options.randomBtn.addActionListener(new ActionListener() { 
		  public void actionPerformed(ActionEvent e) { 
			state = 1;
		    panel = new ForestPanel("");
		    casing.add(panel);
		    labels = new LabelPanel(panel);
		    casing.add(labels);
			setContentPane(casing);
			pack();
		  } 
		} );
		
		options.loadfile.addActionListener(new ActionListener() { 
		  public void actionPerformed(ActionEvent e) { 
			 
			if (options.fileSrc.getText() == null) {return;}
			else {
				
				int dimensionX, dimensionY;
				int [][] forest;
				int posX, posY;
				int windX, windY;
				
				try {
					System.out.println("LABEL: " + options.fileSrc.getText());
					BufferedReader br = new BufferedReader(new FileReader(options.fileSrc.getText()));
					
					String line;
					
					// Board Dimensions
					line = br.readLine();
					dimensionX = Integer.parseInt(line.split(" ")[0]);
					dimensionY = Integer.parseInt(line.split(" ")[1]);
					
					// Firefighter's position
					line = br.readLine();
					posX = Integer.parseInt(line.split(" ")[0]);
					posY = Integer.parseInt(line.split(" ")[1]);
					
					// Wind
					line = br.readLine();
					windX = Integer.parseInt(line.split(" ")[0]);
					windY = Integer.parseInt(line.split(" ")[1]);
					
					// Board
					int[][] temp = new int[dimensionX][dimensionY];
					for (int i = 0; i < dimensionY; i++) {
						line = br.readLine();
						for (int j = 0; j < dimensionX; j++) {
							temp[i][j] = Integer.parseInt(line.split(" ")[j]);
						}
					}
					
					forest = temp;				
					
				} catch (Exception a) {
					a.printStackTrace(); 
					return;
				}
				state = 1;
			    panel = new ForestPanel(posX, posY, windX, windY, forest);
			    casing.add(panel);
			    labels = new LabelPanel(panel);
			    casing.add(labels);
				setContentPane(casing);
				pack();
			}
		  } 
		} );
		
		this.setResizable(false);
		this.setContentPane(options);
        pack();
        setVisible(true);
	}
	
	/**
	 * Getter
	 * 
	 * @return Forest panel
	 */
	public ForestPanel getForestPanel() {
		return panel;
	}
	
}