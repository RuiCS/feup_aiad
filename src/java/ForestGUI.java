import javax.swing.JFrame;

public class ForestGUI extends JFrame {
    	
	private ForestPanel panel;
	public ForestGUI() {
		panel = new ForestPanel();
		this.setContentPane(panel);
        pack();
        setVisible(true);
	}
	
}