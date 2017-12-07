import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
				
			case "findNearestFire":
				findNearestFire(forest, agName, action);
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
		try {
			
			if (forest.getForest() == null) {
				System.out.println("[" + agName + "] - Not ready yet");
			}
			else {
				forest.addPercept(agName, Literal.parseLiteral("ready"));
				Thread.sleep(10);
				System.out.println("[" + agName + "] - Ready for action ");
			}
			
			if (forest.getForest() != null) {
				
				// TODO proper initial position
				
				// initial position and direction
				int x = ThreadLocalRandom.current().nextInt(0, ForestPanel.WIDTH);
				int y = ThreadLocalRandom.current().nextInt(0, ForestPanel.HEIGHT);
				String dir = "down";
				
				// place agent on the environment
				forest.getForest()[y][x] = ForestPanel.FIREFIGHTER;
				
				
				// update agent beliefs
				addPercept(forest, agName, "pos(" + x + ", " + y + ")");
				addPercept(forest, agName, "facing(" + dir + ")");
				
				// verbose
				System.out.println("[" + agName + "] Initializing at (" + x + ", " + y + "). Facing '" + dir + "'.");
			}
		}
		catch (Exception e) {
			
		}
		
	}
	
	
	/**
	 * Locates the nearest fire (using Manhattan distances)
	 * and tells the agent to go there
	 * 
	 * @param forest Current environment
	 * @param agName Name of the agent
	 * @param action Action requested by the agent
	 */
	public static void findNearestFire(Forest forest, String agName, Structure action) {
		
		try {
			// get current pos
			int[] pos = getPosition(forest, agName);
			int x = pos[0];
			int y = pos[1];
	        
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
		        addPercept(forest, agName, "extinguished");
		        
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
	        
	        // update agent beliefs
	        removePerceptsByUnif(forest, agName, "goal(X, Y)");
	        addPercept(forest, agName, "goal(" + newx + ", " + newy + ")");
	        
	        removePerceptsByUnif(forest, agName, "facing(X)");
	        addPercept(forest, agName, "facing(" + dir + ")");
	        
	        // verbose
	        System.out.println("[" + agName + "] My position is ("+x+", "+y+").");
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
			String dir = getDirection(forest, agName);
			int[] pos = getPosition(forest, agName);
			int x = pos[0];
			int y = pos[1];
	        
	        // get forest matrix
	        int[][] mforest = forest.getForest();
	        
	        // verbose
	        int firex = 0;
	        int firey = 0;
	        
	        switch (dir) {
	        
				case "up":
					mforest[y-1][x] = ForestPanel.NORMALTILE; // TODO
					
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
			int[] pos = getPosition(forest, agName);
			int x = pos[0];
			int y = pos[1];
			int[] gpos = getGoal(forest, agName);
			int gx = gpos[0];
			int gy = gpos[1];
	        
	        // get forest matrix
	        int[][] mforest = forest.getForest();
	        
	        int offset = 0;
	        
	        // new pos
	        int nx = x;
	        int ny = y;
	        
	        // delta
	        int dx = gx - x;
	        int dy = gy - y;
	        	
        	// randomly choose whether to move up/down or left/right
        	int rand = ThreadLocalRandom.current().nextInt(0, 2);
        	
	        boolean failVer = false;
	        boolean failHor = false;
	        boolean found = false;
	        
	        // if goal = pos
	        if (dx == 0 && dy == 0) {
	        	found = true;
	        }
	        
	        while (!(failVer && failHor) && !found) {
	        	
	        	// move vertically (rand chose vertical || goal in same col as pos)
	        	if (((dx != 0 && dy != 0) && (rand == 0 || failHor)) || (dx == 0 && dy != 0)) {
	        		
	        		offset = (dy > 0)? 1 : -1;
	        		
	        		if (mforest[ny+offset][nx] != ForestPanel.FIRETILE &&
			        		mforest[ny+offset][nx] != ForestPanel.FIREFIGHTER &&
			        		mforest[ny+offset][nx] != ForestPanel.PEOPLETILE) {
			        	
		        		ny = ny + offset;
		        		found = true;
	        		}
	        		else {
	        			failVer = true;
	        			
	        			// goal in same col as pos
	        			if (dx == 0 && dy != 0) {
	        				break;
	        			}
	        		}
	        	}
	        	// move horizontally (rand chose horizontal || goal in same row as pos)
	        	else if (((dx != 0 && dy != 0) && (rand != 0 || failVer)) || (dx != 0 && dy == 0)) {
	        		
	        		offset = (dx > 0)? 1 : -1;
	        		
	        		if (mforest[ny][nx+offset] != ForestPanel.FIRETILE &&
			        		mforest[ny][nx+offset] != ForestPanel.FIREFIGHTER &&
			        		mforest[ny][nx+offset] != ForestPanel.PEOPLETILE) {
	        			
		        		nx = nx + offset;
		        		found = true;
	        		}
	        		else {
	        			failHor = true;
	        			
	        			// goal in same row as pos
	        			if (dx != 0 && dy == 0) {
	        				break;
	        			}
	        		}
	        	}
	        }
        	
	        
	        // place agent on the environment
	        //mforest[y][x] = forest.getInitialForest()[y][x];
	        mforest[y][x] = ForestPanel.NORMALTILE; // TODO
	        mforest[ny][nx] = ForestPanel.FIREFIGHTER;
	        
	        if (found) {
	        	if (dx == 0 && dy == 0) {
	        		// verbose
	        		System.out.println("[" + agName + "] Already in position, no need to move!");	
	        	} else {
	        		// verbose
	        		System.out.println("[" + agName + "] Moved from pos("+x+", "+y+") to pos("+nx+", "+ny+").");
	        		
	    	     	// update agent beliefs
	    	        removePerceptsByUnif(forest, agName, "pos(X, Y)");
	    	        addPercept(forest, agName, "pos(" + nx + ", " + ny + ")");
	        	}	        	
	        } else {
	        	// verbose
	        	System.out.println("[" + agName + "] Can't move from pos("+x+", "+y+") at the moment.");	
	        }
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Adds given perception to given agent
	 * 
	 * @param forest Current environment
	 * @param agName Name of the agent
	 * @param percept Perception to add
	 */
	public static void addPercept(Forest forest, String agName, String percept) {
		
		forest.addPercept(agName, Literal.parseLiteral(percept));
		
		// necessary, or changes might not get detected
        try { Thread.sleep(10); } catch (Exception e) {}
	}
	
	
	/**
	 * 
	 * Removes all perceptions from given agent that unify with given perception
	 * 
	 * @param forest Current environment
	 * @param agName Name of the agent
	 * @param percept Perception to remove
	 */
	public static void removePerceptsByUnif(Forest forest, String agName, String percept) {
		
		forest.removePerceptsByUnif(agName, Literal.parseLiteral(percept));
		
		// necessary, or changes might not get detected
        try { Thread.sleep(10); } catch (Exception e) {}
	}
	
	
	/**
	 * Getter
	 * 
	 * @param forest Current environment
	 * @param agName Name of the agent
	 * 
	 * @return Current position (pos[x][y])
	 */
	public static int[] getPosition(Forest forest, String agName) {
		
		int[] pos = new int[2];
		
		List<Literal> percepts = forest.consultPercepts(agName);
		
		int n = 0;
		
		for (Literal p : percepts) {
			
			if (p.getFunctor().equals("pos")) {
				
				try {
					
					pos[0] = (int)((NumberTerm)p.getTerm(0)).solve();
					pos[1] = (int)((NumberTerm)p.getTerm(1)).solve();
					n++;
				}
				catch (Exception r) {
					
				}
			}
		}
		
		if (n > 1) {
			System.out.println("[" + agName + "] Duplicated perception found: pos(X, Y)!");
		}
		
		return pos;
	}
	
	
	/**
	 * Getter
	 * 
	 * @param forest Current environment
	 * @param agName Name of the agent
	 * 
	 * @return Direction (up, down, left or right)
	 */
	public static String getDirection(Forest forest, String agName) {
		
		String dir = "";
		
		List<Literal> percepts = forest.consultPercepts(agName);
		
		int n = 0;
		
		for (Literal p : percepts) {
			
			if (p.getFunctor().equals("facing")) {
				
				try {
					
					dir = ((Atom)p.getTerm(0)).toString();
					n++;
				}
				catch (Exception r) {
					
				}
			}
		}
		
		if (n > 1) {
			System.out.println("[" + agName + "] Duplicated perception found: facing(Dir)!");
		}
		
		return dir;
	}
	
	
	/**
	 * Getter
	 * 
	 * @param forest Current environment
	 * @param agName Name of the agent
	 * 
	 * @return Goal position (pos[x][y])
	 */
	public static int[] getGoal(Forest forest, String agName) {
		
		int[] goal = new int[2];
		
		List<Literal> percepts = forest.consultPercepts(agName);
		
		int n = 0;
		
		for (Literal p : percepts) {
			
			if (p.getFunctor().equals("goal")) {
				
				try {
					
					goal[0] = (int)((NumberTerm)p.getTerm(0)).solve();
					goal[1] = (int)((NumberTerm)p.getTerm(1)).solve();
					n++;
				}
				catch (Exception r) {
					
				}
			}
		}
		
		if (n > 1) {
			System.out.println("[" + agName + "] Duplicated perception found: goal(X, Y)!");
		}
		
		return goal;
	}
}
