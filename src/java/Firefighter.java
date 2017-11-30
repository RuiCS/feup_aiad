import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Structure;

/**
 * This class implements the firefighter agent's actions
 */
public class Firefighter {
	
	/** Agent position */
	private int x;
	/** Agent position */
	
	private int y;
	/** Agent name */
	private String name;
	
	
	/**
	 * Constructor
	 * (updates agent's beliefs)
	 * 
	 * @param forest Current environment
	 * @param name Agent name
	 */
	public Firefighter(Forest forest, String name) {
		
		this.name = name;
		
		// initial position
		x = 0;
		y = 0;
		
		// update agent beliefs
		forest.addPercept(name, Literal.parseLiteral("pos(" + x + ", " + y + ")"));
	}
	
	
	/**
	 * Executes the given action by calling respective firefighter's method
	 * and updates its beliefs accordingly
	 * 
	 * @param forest Current environment
	 * @param action Action called by the agent
	 * 
	 * @return True if action was implemented, false otherwise
	 */
	public boolean executeAction(Forest forest, Structure action) {
		
		// get action name
    	String functor = action.getFunctor();
    	
    	// figure out what action was called
    	switch (functor) {
    			
			case "checkSurroundings":
				checkSurroundings(forest, action);
				break;
				
			case "run":
				run(forest, action);
				break;
				
			default:
				forest.logger.info("executing: " + action + ", but not implemented!");
	    		return false;
		}
    	
    	return true;
	}
	

	/**
	 * Checks if there is fire near the agent
	 * and updates its beliefs accordingly
	 * 
	 * @param forest Current environment
	 * @param action Action called by the agent
	 */
	public void checkSurroundings(Forest forest, Structure action) {
		
		try {
			// get pos
			int x = (int)((NumberTerm)action.getTerm(0)).solve();
	        int y = (int)((NumberTerm)action.getTerm(1)).solve();
			
	        // TODO
			// if there's fire to the right, he aint safe
			if (forest.getForest()[x + 1][y] == 3) {
				forest.addPercept(name, Literal.parseLiteral("safe(false)"));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Moves the agent away from the fire
	 * and updates its beliefs accordingly
	 * 
	 * @param forest Current environment
	 * @param action Action called by the agent
	 */
	public void run(Forest forest, Structure action) {
		
		try {
			// get pos
			int x = (int)((NumberTerm)action.getTerm(0)).solve();
	        int y = (int)((NumberTerm)action.getTerm(1)).solve();
	        
	        // place agent on the environment
			forest.getForest()[y][x] = 4;
			
	        // update agent beliefs
	        forest.addPercept(name, Literal.parseLiteral("pos(" + x + ", " + (y + 1) + ")"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	public int getX() {
		return x;
	}


	public void setX(int x) {
		this.x = x;
	}


	public int getY() {
		return y;
	}


	public void setY(int y) {
		this.y = y;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
}