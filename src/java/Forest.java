// Environment code for project forestFires

import jason.asSyntax.*;
import jason.environment.*;
import java.util.logging.*;

public class Forest extends Environment {

    private Logger logger = Logger.getLogger("forestFires."+Forest.class.getName());
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
    	try { Thread.sleep(500);}  catch (Exception e) {} // slow down the execution
    	if (action.getFunctor().equals("burn")) {
    		addPercept(Literal.parseLiteral("fire"));
    		return true;
    	} else {
    		logger.info("executing: "+action+", but not implemented!");
    		return false;
    	}
    }

    /** Called before the end of MAS execution */
    @Override
    public void stop() {
      super.stop();
    }
    
}
