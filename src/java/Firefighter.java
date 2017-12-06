import java.util.Arrays;
import java.util.Collections;
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
			
			case "savePeople":
				savePeople(forest, agName, action);
				break;
				
			case "move":
				move(forest, agName, action);
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
		
		if (forest.getForest() == null) {
			System.out.println("[" + agName + "] - Not ready yet");
		}
		else {
			forest.addPercept(agName, Literal.parseLiteral("ready"));
			System.out.println("[" + agName + "] - Ready for action ");
		}
		
		if (forest.getForest() != null) {
			// TODO proper initial position
			
			// initial position and direction
			//int x = ThreadLocalRandom.current().nextInt(0, ForestPanel.WIDTH);
			//int y = ThreadLocalRandom.current().nextInt(0, ForestPanel.HEIGHT);
			int x = 0;
			int y = 0;
			String dir = "down";
			
			// place agent on the environment
			forest.getForest()[y][x] = ForestPanel.FIREFIGHTER;
			
			
			// update agent beliefs
			forest.addPercept(agName, Literal.parseLiteral("pos(" + x + ", " + y + ")"));
			try { Thread.sleep(100);} catch (Exception e) {}
			forest.addPercept(agName, Literal.parseLiteral("facing(" + dir + ")"));
			
			// verbose
			System.out.println("[" + agName + "] Initializing at (" + x + ", " + y + "). Facing '" + dir + "'.");
		}
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
	        int newx = x;
	        int newy = y;
	        String dir = "down";
	        
	        // distance to cells around nearest fire
	        int upd = fired;
	        int downd = fired;
	        int leftd = fired;
	        int rightd = fired;	
	        
	        // find possible safe spots near the nearest fire
	        // up
	        if (firey-1 > 0 
	        		&& mforest[firey-1][firex] != ForestPanel.FIRETILE 
	        		&& (mforest[firey-1][firex] != ForestPanel.FIREFIGHTER
	        				|| (firey-1 == y  && firex == x))
	        		&& mforest[firey-1][firex] != ForestPanel.PEOPLETILE
	        		) {	
	        	upd = Math.abs(firey-1 - y) + Math.abs(firex - x);
	        }
	        // down
	        if (firey+1 < ForestPanel.HEIGHT 
	        		&& mforest[firey+1][firex] != ForestPanel.FIRETILE
	        		&& (mforest[firey+1][firex] != ForestPanel.FIREFIGHTER
	        				|| (firey+1 == y  && firex == x))
	        		&& mforest[firey+1][firex] != ForestPanel.PEOPLETILE
	        		) {	
	        	downd = Math.abs(firey+1 - y) + Math.abs(firex - x);
	        }
	        // left
	        if (firex-1 > 0 
	        		&& mforest[firey][firex-1] != ForestPanel.FIRETILE 
	        		&& (mforest[firey][firex-1] != ForestPanel.FIREFIGHTER
	        				|| (firey == y  && firex-1 == x))
	        		&& mforest[firey][firex-1] != ForestPanel.PEOPLETILE
	        		) {	
	        	leftd = Math.abs(firey - y) + Math.abs(firex-1 - x);
	        }
	        // right
	        if (firex+1 < ForestPanel.WIDTH 
	        		&& mforest[firey][firex+1] != ForestPanel.FIRETILE 
	        		&& (mforest[firey][firex+1] != ForestPanel.FIREFIGHTER
	        				|| (firey == y  && firex+1 == x))
	        		&& mforest[firey][firex+1] != ForestPanel.PEOPLETILE
	        		) {	
	        	rightd = Math.abs(firey - y) + Math.abs(firex+1 - x);
	        }
	        
	        // find nearest safe spot of all possible safe spots
	        int mind = Collections.min(Arrays.asList(upd, downd, leftd, rightd));	        
	       
	        // up
	        if (mind == upd && upd != fired) {
	        	newx = firex;
	        	newy = firey-1;
	        	dir = "down";
	        }
	        // down
	        else if (mind == downd && downd != fired) {
	        	newx = firex;
	        	newy = firey+1;
	        	dir = "up";
	        }
	        // left
	        else if (mind == leftd && leftd != fired) {
	        	newx = firex-1;
	        	newy = firey;
	        	dir = "right";
	        }
	        // right
	        else if (mind == rightd && rightd != fired) {
	        	newx = firex+1;
	        	newy = firey;
	        	dir = "left";
	        }	        
	        else {
	        	// verbose
		        System.out.println("[" + agName + "] There is no safe spot around the fire located at (" + firex + ", " + firey + ")!");
	        }
	        
	        // verbose
	        System.out.println("[" + agName + "] Nearest fire located at (" + firex + ", " + firey + ").");
	        System.out.println("[" + agName + "] Moving to (" + newx + ", " + newy + "). Facing '" + dir + "'.");
	     			
	     	// update agent beliefs
	        forest.removePerceptsByUnif(agName, Literal.parseLiteral("move(X, Y)"));
	        forest.addPercept(agName, Literal.parseLiteral("move(" + newx + ", " + newy + ")"));
	        
	        Thread.sleep(100);
	        
	        forest.removePerceptsByUnif(agName, Literal.parseLiteral("facing(X)"));
	        forest.addPercept(agName, Literal.parseLiteral("facing(" + dir + ")"));
	        
	        System.out.println(forest.consultPercepts(agName).size());
	       
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
			System.out.println(1);
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
	        
	        System.out.println(2);
	        
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
	        
	        forest.removePerceptsByUnif(agName, Literal.parseLiteral("ready(extinguish)"));
	        forest.removePerceptsByUnif(agName, Literal.parseLiteral("move(X, Y)"));
	        
	        // verbose
	        System.out.println("[" + agName + "] Extinguished fire at (" + firex + ", " + firey + ").");
	        
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Find people in danger and move them away from fire
	 * 
	 * @param forest Current environment
	 * @param agName Name of the agent
	 * @param action Action requested by the agent
	 */
	public static void savePeople(Forest forest, String agName, Structure action) {
		
		try {
			
			if (forest.containsPercept(agName, Literal.parseLiteral("extinguished(true)"))) {
	        	
		        // verbose
		        System.out.println("[" + agName + "] All the fires have been extinguished so there's no-one to save");
		        
	        	return;
	        }
			
			// get current pos
			int x = (int)((NumberTerm)action.getTerm(1)).solve();
	        int y = (int)((NumberTerm)action.getTerm(2)).solve();
	        
	        
	        // get forest matrix
	        int[][] mforest = forest.getForest();
	        
	        // distance to nearest person
	        int peopled = ForestPanel.WIDTH + ForestPanel.HEIGHT;
	        
	        // nearest person position
	        int peoplex = ForestPanel.WIDTH;
	        int peopley = ForestPanel.HEIGHT;
	        
	        // find nearest person
	        for (int i = 0; i < ForestPanel.HEIGHT; i++) {
	        	
	        	for (int j = 0; j < ForestPanel.WIDTH; j++) {
	        		
	        		// if it's a person, check if it's a victim (fire is close) 
	        		if (mforest[i][j] == ForestPanel.PEOPLETILE) {
	        			
	        			boolean isVictim = lookAroundPerson(forest, j, i);
	        			
	        			// if it's a victim, check distance to agent (it goes to the closer one)
	        			if(isVictim) {
		        			int dx = Math.abs(i - y);
		        			int dy = Math.abs(j - x);
		        			
		        			// found closer victim
		        			if (dx + dy < peopled) {
			        			
			        			peoplex = j;
			        			peopley = i;
			        			peopled = dx + dy;
	        			}
	        			}

	        		}
	        	}
	        }
	        
	        // if there are no victims
	        if (peoplex == ForestPanel.WIDTH && peopley == ForestPanel.HEIGHT) {
	        			        
		        // verbose
		        System.out.println("[" + agName + "] No victims right now");
		        
		        return;
	        }
	        
	        // new agent pos and direction
	        int newx = x;
	        int newy = y;
	        String dir = "down";
	        
	        // distance to cells around nearest fire
	        int upd = peopled;
	        int downd = peopled;
	        int leftd = peopled;
	        int rightd = peopled;	
	        
	        // find possible safe spots near the nearest victim
	        // up
	        if (peopley-1 > 0 
	        		&& mforest[peopley-1][peoplex] != ForestPanel.FIRETILE 
	        		&& (mforest[peopley-1][peoplex] != ForestPanel.FIREFIGHTER
	        				|| (peopley-1 == y  && peoplex == x))
	        		&& mforest[peopley-1][peoplex] != ForestPanel.PEOPLETILE
	        		) {	
	        	upd = Math.abs(peopley-1 - y) + Math.abs(peoplex - x);
	        }
	        // down
	        if (peopley+1 < ForestPanel.HEIGHT 
	        		&& mforest[peopley+1][peoplex] != ForestPanel.FIRETILE
	        		&& (mforest[peopley+1][peoplex] != ForestPanel.FIREFIGHTER
	        				|| (peopley+1 == y  && peoplex == x))
	        		&& mforest[peopley+1][peoplex] != ForestPanel.PEOPLETILE
	        		) {	
	        	downd = Math.abs(peopley+1 - y) + Math.abs(peoplex - x);
	        }
	        // left
	        if (peoplex-1 > 0 
	        		&& mforest[peopley][peoplex-1] != ForestPanel.FIRETILE 
	        		&& (mforest[peopley][peoplex-1] != ForestPanel.FIREFIGHTER
	        				|| (peopley == y  && peoplex-1 == x))
	        		&& mforest[peopley][peoplex-1] != ForestPanel.PEOPLETILE
	        		) {	
	        	leftd = Math.abs(peopley - y) + Math.abs(peoplex-1 - x);
	        }
	        // right
	        if (peoplex+1 < ForestPanel.WIDTH 
	        		&& mforest[peopley][peoplex+1] != ForestPanel.FIRETILE 
	        		&& (mforest[peopley][peoplex+1] != ForestPanel.FIREFIGHTER
	        				|| (peopley == y  && peoplex+1 == x))
	        		&& mforest[peopley][peoplex+1] != ForestPanel.PEOPLETILE
	        		) {	
	        	rightd = Math.abs(peopley - y) + Math.abs(peoplex+1 - x);
	        }
	        
	        // find nearest safe spot of all possible safe spots
	        int mind = Collections.min(Arrays.asList(upd, downd, leftd, rightd));	        
	       
	        // up
	        if (mind == upd && upd != peopled) {
	        	newx = peoplex;
	        	newy = peopley-1;
	        	dir = "down";
	        }
	        // down
	        else if (mind == downd && downd != peopled) {
	        	newx = peoplex;
	        	newy = peopley+1;
	        	dir = "up";
	        }
	        // left
	        else if (mind == leftd && leftd != peopled) {
	        	newx = peoplex-1;
	        	newy = peopley;
	        	dir = "right";
	        }
	        // right
	        else if (mind == rightd && rightd != peopled) {
	        	newx = peoplex+1;
	        	newy = peopley;
	        	dir = "left";
	        }
	        else {
	        	// verbose
		        System.out.println("[" + agName + "] There is no safe spot around the victim located at (" + peoplex + ", " + peopley + ")!");
	        }
	        
	        // place agent on the environment
	        mforest[y][x] = ForestPanel.NORMALTILE;
	        mforest[newy][newx] = ForestPanel.FIREFIGHTER;
	        
	        // verbose
	        System.out.println("[" + agName + "] Nearest victim located at (" + peoplex + ", " + peopley + ").");
	        System.out.println("[" + agName + "] Moving to (" + newx + ", " + newy + "). Facing '" + dir + "'.");
	        System.out.println("[" + agName + "] Cleared (" + x + ", " + y + ")["+mforest[y][x]+"] and went to (" + newx + " " + newy +")["+mforest[newy][newx]+"]");
	       	     			
	     	// update agent beliefs
	        forest.removePerceptsByUnif(agName, Literal.parseLiteral("pos(X, Y)"));
	        forest.addPercept(agName, Literal.parseLiteral("pos(" + newx + ", " + newy + ")"));
	        
	        Thread.sleep(100);
	        	        
	        forest.removePerceptsByUnif(agName, Literal.parseLiteral("facing(X)"));
	        forest.addPercept(agName, Literal.parseLiteral("facing(" + dir + ")"));
	        
	        
	       
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Determine if person is victim (if there's a fire close by)
	 * 
	 * @param forest Current environment
	 * @param int x person x position
	 * @param int y person y position
	 */
	public static boolean lookAroundPerson(Forest forest, int x, int y) {
		
		int[][] mforest = forest.getForest();
		
		// person is in danger if fire is closer than 2 cells
        if ((y+1 < ForestPanel.HEIGHT && mforest[y+1][x] == ForestPanel.FIRETILE) ||
        		(x+1 < ForestPanel.WIDTH && mforest[y][x+1] == ForestPanel.FIRETILE) ||
        		(y-1 > 0 && mforest[y-1][x] == ForestPanel.FIRETILE) ||
        		(x-1 > 0 && mforest[y][x-1] == ForestPanel.FIRETILE) ||
        		
        		(y+2 < ForestPanel.HEIGHT && mforest[y+1][x] == ForestPanel.FIRETILE) ||
        		(x+2 < ForestPanel.WIDTH && mforest[y][x+1] == ForestPanel.FIRETILE) ||
        		(y-2 > 0 && mforest[y-1][x] == ForestPanel.FIRETILE) ||
        		(x-2 > 0 && mforest[y][x-1] == ForestPanel.FIRETILE) ||
        		
        		(y+3 < ForestPanel.HEIGHT && mforest[y+1][x] == ForestPanel.FIRETILE) ||
        		(x+3 < ForestPanel.WIDTH && mforest[y][x+1] == ForestPanel.FIRETILE) ||
        		(y-3 > 0 && mforest[y-1][x] == ForestPanel.FIRETILE) ||
        		(x-3 > 0 && mforest[y][x-1] == ForestPanel.FIRETILE))
        
        {	
        	return true;
        }
        else {
        	return false;
        }
	}
	
	
	/**
	 * Moves the agent one cell towards the given position
	 * 
	 * @param forest Current environment
	 * @param agName Name of the agent
	 * @param action Action requested by the agent
	 */
	public static void move(Forest forest, String agName, Structure action) {
		
		try {	        
			// get current pos and goal pos
			int x = (int)((NumberTerm)action.getTerm(0)).solve();
	        int y = (int)((NumberTerm)action.getTerm(1)).solve();
			int gx = (int)((NumberTerm)action.getTerm(2)).solve();
	        int gy = (int)((NumberTerm)action.getTerm(3)).solve();
	        
	        // get forest matrix
	        int[][] mforest = forest.getForest();
	        
	        int offset = 0;
	        
	        // new pos
	        int nx = x;
	        int ny = y;
	        
	        // delta
	        int dx = gx - x;
	        int dy = gy - y;
	        
	        // goal pos is not in same row and not in same col as pos
	        if (dx != 0 && dy != 0) {
	        	
	        	// randomly choose whether to move up/down or left/right
	        	int rand = ThreadLocalRandom.current().nextInt(0, 2);
	        	
	        	// move vertically
	        	if (rand == 0) {
	        		offset = (dy > 0)? 1 : -1;
	        		ny = ny + offset;
	        	}
	        	// move horizontaly
	        	else {
	        		offset = (dx > 0)? 1 : -1;
	        		nx = nx + offset;
	        	}
	        }
	        // goal pos in same col as pos
	        else if (dx == 0 && dy != 0) {
	        	
	        	offset = (dy > 0)? 1 : -1;
        		ny = ny + offset;	        	
	        }
	        // goal pos in same row as pos
	        else if (dx != 0 && dy == 0) {
	        	
	        	offset = (dx > 0)? 1 : -1;
        		nx = nx + offset;	        	
	        }
	        
	        // place agent on the environment
	        mforest[y][x] = ForestPanel.NORMALTILE; // TODO
	        mforest[ny][nx] = ForestPanel.FIREFIGHTER;
	        
	        // verbose
	        System.out.println("[" + agName + "] Moved from pos("+x+", "+y+") to pos("+nx+", "+ny+").");
	     			
	     	// update agent beliefs
	        forest.removePerceptsByUnif(agName, Literal.parseLiteral("pos(X, Y)"));
	        forest.addPercept(agName, Literal.parseLiteral("pos(" + nx + ", " + ny + ")"));
	        
	        // agent is already at goal position
	        if (nx == gx && ny == gy) {	        	
	        	System.out.println("[" + agName + "] I'm where I want to be!");
	        	forest.removePerceptsByUnif(agName, Literal.parseLiteral("move(X, Y)"));
	        	forest.addPercept(agName, Literal.parseLiteral("ready(extinguish)"));
	        	return;
	        	
	        }
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}	
}
