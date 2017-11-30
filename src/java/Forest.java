// Environment code for project forestFires

import jason.asSyntax.*;
import jason.environment.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.*;

public class Forest extends Environment {

    public Logger logger = Logger.getLogger("forestFires."+Forest.class.getName());
    private ForestGUI gui = new ForestGUI();
    
    private ArrayList<Firefighter> firefighters = new ArrayList<>();
    
    /** Called before the MAS execution with the args informed in .mas2j */
    @Override
    public void init(String[] args) {	 
    }

    public Forest() {
    	new Thread() {
    		public void run() {
    			try {
    				while (isRunning()) {
    					gui.panel.spreadFire();
    					update();
    					Thread.sleep(1000);
    				}
    			} catch (Exception e) {}
    		}
    	}.start();
    }
    
    
    /**
     * Updates the forest (matrix) by placing the agents in their current positions
     */
    public void update() {
    	
    	for (Firefighter firefighter : firefighters) {
    		getForest()[firefighter.getY()][firefighter.getX()] = ForestPanel.FIREFIGHTER;
    	}
    }
    
    
    @Override
    public boolean executeAction(String agName, Structure action) {
    	try { Thread.sleep(500);}  catch (Exception e) {} // slow down the execution
    	
    	// new firefighter
    	if (action.getFunctor().equals("init")) {
    		
    		// create new firefighter
    		Firefighter firefighter = new Firefighter(this, agName);
    		
    		// add firefighter to array
    		firefighters.add(firefighter);
    		
    		return true;
    		
    	} else {
    		
    		// tell existing firefighter to act
    		Firefighter firefighter = getFirefighter(agName);
    		
    		if (firefighter != null) {
    			return firefighter.executeAction(this, action);
    		}
    		
    		return false;
    	}
    	
    }
    

    /** Called before the end of MAS execution */
    @Override
    public void stop() {
      super.stop();
    }
    
    
    /**
     * Gets the firefighter of given name
     * 
     * @param name Agent name
     * 
     * @return Firefighter
     */
    public Firefighter getFirefighter(String name) {
    	
    	for (Firefighter firefighter : firefighters) {
    		
    		if (firefighter.getName().equals(name)) {
    			return firefighter;
    		}
    	}
    	
    	return null;
    }
    

    /**
     * Getter
     * 
     * @return Matrix representing the current environment
     */
    public int[][] getForest() {
    	return gui.getForestPanel().getForest();
    }    
    
}
