import java.util.concurrent.ThreadLocalRandom;

import jason.asSyntax.Atom;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Structure;

/**
 * This class implements the firefighter agent's actions
 */
public abstract class Firefighter {		
	
	/**
	 * Executes the requested action by calling the respective firefighter's method
	 * 
	 * @param forest Current environment
	 * @param agName Name of the agent
	 * @param action Action requested by the agent
	 * 
	 * @return True if the requested action is implemented, false otherwise
	 */
	public static boolean executeAction(Forest forest, String agName, Structure action) {
		
		// get action name
    	String functor = action.getFunctor();
    	
    	// verbose
    	System.out.println("\n[" + agName + "] --- Executing action '" + functor + "' ---");
    	
    	// figure out what action was requested
    	switch (functor) {
    			
    		case "init":
    			init(forest, agName);
    			break;
				
			case "goToNearestFire":
				goToNearestFire(forest, agName, action);
				break;
				
			case "extinguishFire":
				extinguishFire(forest, agName, action);
				break;
				
			default:
				System.out.println("[" + agName +"] Action ' " + action + "', not implemented!");
	    		return false;
		}
    	
    	return true;
	}
	
	
	/**
	 * Places the newly created firefighter in the forest
	 * and updates its beliefs accordingly
	 * 
	 * @param forest Current environment
	 * @param agName Name of the agent
	 */
	public static void init(Forest forest, String agName) {
		
		// TODO proper initial position
		
		// initial position and direction
		int x = ThreadLocalRandom.current().nextInt(0, ForestPanel.WIDTH);
		int y = ThreadLocalRandom.current().nextInt(0, ForestPanel.HEIGHT);;
		String dir = "down";
		
		// place agent on the environment
		forest.getForest()[y][x] = ForestPanel.FIREFIGHTER;
		
		// update agent beliefs
		forest.addPercept(agName, Literal.parseLiteral("pos(" + x + ", " + y + ")"));
		forest.addPercept(agName, Literal.parseLiteral("facing(" + dir + ")"));
		
		// verbose
		System.out.println("[" + agName + "] Initializing at (" + x + ", " + y + "). Facing '" + dir + "'.");
	}
	
	
	/**
	 * Moves the agent to the nearest fire (using Manhattan distances)
	 * 
	 * @param forest Current environment
	 * @param agName Name of the agent
	 * @param action Action requested by the agent
	 */
	public static void goToNearestFire(Forest forest, String agName, Structure action) {
		
		try {
			// get current pos
			int x = (int)((NumberTerm)action.getTerm(0)).solve();
	        int y = (int)((NumberTerm)action.getTerm(1)).solve();
	        
	        // get forest matrix
	        int[][] mforest = forest.getForest();
	        
	        // distance to nearest fire
	        int fired = ForestPanel.WIDTH + ForestPanel.HEIGHT;
	        
	        // nearest fire position
	        int firex = ForestPanel.WIDTH;
	        int firey = ForestPanel.HEIGHT;
	        
	        // find nearest fire
	        for (int i = 0; i < ForestPanel.HEIGHT; i++) {
	        	
	        	for (int j = 0; j < ForestPanel.WIDTH; j++) {
	        		
	        		// if its fire, check its distance to agent
	        		if (mforest[i][j] == ForestPanel.FIRETILE) {
	        			
	        			int dx = Math.abs(i - y);
	        			int dy = Math.abs(j - x);
	        			
	        			// found nearer fire
	        			if (dx + dy < fired) {
		        			
		        			firex = j;
		        			firey = i;
		        			fired = dx + dy;
	        			}

	        		}
	        	}
	        }
	        
	        // if there is no fire
	        if (firex == ForestPanel.WIDTH && firey == ForestPanel.HEIGHT) {
	        	
		     	// update agent beliefs
	        	forest.removePerceptsByUnif(agName, Literal.parseLiteral("extinguished(X)"));
		        forest.addPercept(agName, Literal.parseLiteral("extinguished(true)"));
		        
		        // verbose
		        System.out.println("[" + agName + "] No fires located!");
		        
		        return;
	        }
	        
	        // new agent pos and direction
	        int newx = 0;
	        int newy = 0;
	        String dir = "";
	        
	        // find safe spot near the nearest fire
	        // down
	        if (firey+1 < ForestPanel.HEIGHT && mforest[firey+1][firex] != ForestPanel.FIRETILE) {	
	        	newy = firey + 1;
	        	newx = firex;
	        	dir = "up";
	        }
	        // right
	        else if (firex+1 < ForestPanel.WIDTH && mforest[firey][firex+1] != ForestPanel.FIRETILE) {	
	        	newy = firey;
	        	newx = firex + 1;
	        	dir = "left";
	        }
	        // up
	        else if (firey-1 > 0 && mforest[firey-1][firex] != ForestPanel.FIRETILE) {	
	        	newy = firey - 1;
	        	newx = firex;
	        	dir = "down";
	        }
	        // left
	        else if (firex-1 > 0 && mforest[firey][firex-1] != ForestPanel.FIRETILE) {	
	        	newy = firey;
	        	newx = firex - 1;
	        	dir = "right";
	        }
	        else {
	        	// verbose
		        System.out.println("[" + agName + "] There is no safe spot around the fire located at (" + firex + ", " + firey + ")!");
	        }
	        
	        // place agent on the environment
	        mforest[newy][newx] = ForestPanel.FIREFIGHTER;
	     			
	     	// update agent beliefs
	        forest.removePerceptsByUnif(agName, Literal.parseLiteral("pos(X, Y)"));
	        forest.addPercept(agName, Literal.parseLiteral("pos(" + newx + ", " + newy + ")"));
	        
	        forest.removePerceptsByUnif(agName, Literal.parseLiteral("facing(X)"));
	        forest.addPercept(agName, Literal.parseLiteral("facing(" + dir + ")"));
	        
	        
	        // verbose
	        System.out.println("[" + agName + "] Nearest fire located at (" + firex + ", " + firey + ").");
	        System.out.println("[" + agName + "] Moving to (" + newx + ", " + newy + "). Facing '" + dir + "'.");

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Extinguishes fire in the direction the agent is facing
	 * 
	 * @param forest Current environment
	 * @param agName Name of the agent
	 * @param action Action requested by the agent
	 */
	public static void extinguishFire(Forest forest, String agName, Structure action) {
		
		try {	        
			// get current pos and direction
			String dir = ((Atom)action.getTerm(0)).toString();
			int x = (int)((NumberTerm)action.getTerm(1)).solve();
	        int y = (int)((NumberTerm)action.getTerm(2)).solve();
	        
	        // get forest matrix
	        int[][] mforest = forest.getForest();
	        
	        // verbose
	        int firex = 0;
	        int firey = 0;
	        
	        // check if goToNearestFire couldn't locate a fire
	        if (forest.containsPercept(agName, Literal.parseLiteral("extinguished(true)"))) {
	        	
		        // verbose
		        System.out.println("[" + agName + "] All the fires have been extinguished!");
		        
	        	return;
	        }
	        
	        switch (dir) {
	        
				case "up":
					mforest[y-1][x] = ForestPanel.NORMALTILE;
					
					// verbose
					firex = x;
					firey = y-1;
					
					break;
				
				case "down":
					mforest[y+1][x] = ForestPanel.NORMALTILE;
					
					// verbose
					firex = x;
					firey = y+1;
					
					break;
					
				case "left":
					mforest[y][x-1] = ForestPanel.NORMALTILE;
					
					// verbose
					firex = x-1;
					firey = y;
					
					break;
				
				case "right":
					mforest[y][x+1] = ForestPanel.NORMALTILE;
					
					// verbose
					firex = x+1;
					firey = y;
					
					break;
					
				default:
					break;
			}
	        
	        // verbose
	        System.out.println("[" + agName + "] Extinguished fire at (" + firex + ", " + firey + ").");
	        
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}