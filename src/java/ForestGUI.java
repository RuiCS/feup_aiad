import javax.swing.JFrame;

public class ForestGUI extends JFrame {
    	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ForestPanel panel;
	public ForestGUI() {
		panel = new ForestPanel();
		this.setContentPane(panel);
        pack();
        setVisible(true);
	}
	
}