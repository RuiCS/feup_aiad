// Environment code for project forestFires

import jason.asSyntax.*;
import jason.environment.*;
import java.util.logging.*;

public class Forest extends Environment {

    public Logger logger = Logger.getLogger("forestFires."+Forest.class.getName());
    private ForestGUI gui = new ForestGUI();
    
    
    /** Called before the MAS execution with the args informed in .mas2j */
    @Override
    public void init(String[] args) {
    }

    public Forest() {
    	new Thread() {
    		public void run() {
    			try {
    				while (isRunning()) {    			
    					if (gui.panel != null) {
        					gui.panel.spreadFire();
    					}
    					Thread.sleep(1000);
    				}
    			} catch (Exception e) {}
    		}
    	}.start();
    }
    
    
    @Override
    public boolean executeAction(String agName, Structure action) {
    	
    	// slow down the execution
    	try { Thread.sleep(100);} catch (Exception e) {} 
    	
    	return Firefighter.executeAction(this, agName, action);
    	
    }
    

    /** Called before the end of MAS execution */
    @Override
    public void stop() {
      super.stop();
    }
    

    /**
     * Getter
     * 
     * @return Matrix representing the current environment
     */
    public int[][] getForest() {
    	if (gui.getForestPanel() == null) return null; 
    	return gui.getForestPanel().getForest();
    }    
    
    /**
     * Getter
     * 
     * @return Matrix representing the initial environment
     */
    public int[][] getInitialForest() {
    	if (gui.getForestPanel() == null) return null; 
    	return gui.getForestPanel().getInitialForest();
    }    
    
}
