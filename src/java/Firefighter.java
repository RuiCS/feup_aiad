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
	        removePerceptsByUnif(forest, agName, "fireAt(X, Y)");
	        addPercept(forest, agName, "fireAt(" + firex + "," + firey + ")");
	        
	        removePerceptsByUnif(forest, agName, "goal(X, Y)");
	        addPercept(forest, agName, "goal(" + newx + ", " + newy + ")");
	        
	        removePerceptsByUnif(forest, agName, "facing(X)");
	        addPercept(forest, agName, "facing(" + dir + ")");
	        
	        // verbose
	        //System.out.println("[" + agName + "] My position is ("+x+", "+y+").");
	        //System.out.println("[" + agName + "] Nearest fire located at (" + firex + ", " + firey + ").");
	        //System.out.println("[" + agName + "] Planning to move to (" + newx + ", " + newy + "). Facing '" + dir + "'.");
	       
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
			int[] pos = getPosition(forest, agName);
			int x = pos[0];
			int y = pos[1];
	        
	        // get forest matrix
	        int[][] mforest = forest.getForest();
	        
	        // distance to nearest person
	        int peopled = ForestPanel.WIDTH + ForestPanel.HEIGHT;
	        
	        // nearest person position and fire around them position
	        int peoplex = ForestPanel.WIDTH;
	        int peopley = ForestPanel.HEIGHT;
	        
	        // safe spot around possible fire
			int[] fireFightingInfo;
			int safeCellx = ForestPanel.WIDTH;
			int safeCelly = ForestPanel.HEIGHT;	
			int victimFirex = ForestPanel.WIDTH;
			int victimFirey = ForestPanel.HEIGHT;
			String safeDir = "";
	        
			
	        // find nearest person
	        for (int i = 0; i < ForestPanel.HEIGHT; i++) {
	        	
	        	for (int j = 0; j < ForestPanel.WIDTH; j++) {
	        		
	        		// if it's a person, check if it's a victim (fire is close) 
	        		if (mforest[i][j] == ForestPanel.PEOPLETILE) {
	        			System.out.println("there's a person at ("+ j + " "+ i+")");
	        			fireFightingInfo = lookAroundPerson(forest, j, i, pos);

	        			// if it's a victim, check distance to agent (it goes to the closer one)
	        			if(fireFightingInfo[0] != -1) {
	        				
		        			int dx = Math.abs(i - y);
		        			int dy = Math.abs(j - x);
		        			
		        			// found closer victim
		        			if (dx + dy < peopled) {
		        				
		        				//set fire position and safe direction
		        				victimFirex = fireFightingInfo[0];
		        				victimFirey = fireFightingInfo[1];
		        				switch (fireFightingInfo[2]) {
		        					case 0: {
		        						safeDir = "up";
		        						break;}

		        					case 1: {
		        						safeDir = "down";
		        						break;}

		        					case 2: {
		        						safeDir = "left";
		        						break;}

		        					case 3: {
		        						safeDir = "right";
		        						break;}
		        				}

		        				//get safe cell
		        				int[] safeCell = findSafeCell(fireFightingInfo);
		        				safeCellx = safeCell[0];
		        				safeCelly = safeCell[1];
		        				
		        				//set new closest victim
			        			peoplex = j;
			        			peopley = i;
			        			peopled = dx + dy;

		              			System.out.println("Chose victim at ("+ j + " "+ i+")");
		              			System.out.println("Because there's fire at ("+ victimFirex + " "+ victimFirey+")");
	        			}
	        			}

	        		}
	        	}
	        }
	        
	        // if there are no victims
	        if (peoplex == ForestPanel.WIDTH && peopley == ForestPanel.HEIGHT) {
	        			        
		        // verbose
		        System.out.println("[" + agName + "] No victims with safe spots around");
		        
		        return;
	        }
	        
	        if(safeDir == "") {
	        	// verbose
		        System.out.println("[" + agName + "] There is no safe spot around the victim fire located at (" + victimFirex + ", " + victimFirey + ")!");
		        return;
	        }
	        
	        //TODO maybe verificacoes? se vars nao estao == a inicializacao
	        
	        // verbose
	        System.out.println("[" + agName + "] Nearest victim located at (" + peoplex + ", " + peopley + ").");
	        System.out.println("[" + agName + "] Nearest victim FIRE located at (" + victimFirex + ", " + victimFirey + ").");
	        System.out.println("[" + agName + "] Moving to (" + safeCellx + ", " + safeCelly + "). Facing '" + safeDir + "'.");
	       	     			
	        // update agent beliefs
	        removePerceptsByUnif(forest, agName, "fireAt(X, Y)");
	        addPercept(forest, agName, "fireAt(" + victimFirex + "," + victimFirey + ")");
	        
	        removePerceptsByUnif(forest, agName, "goal(X, Y)");
	        addPercept(forest, agName, "goal(" + safeCellx + ", " + safeCelly + ")");
	        
	        removePerceptsByUnif(forest, agName, "facing(X)");
	        addPercept(forest, agName, "facing(" + safeDir + ")");
	       
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Find safe cell knowing where the fire is and the safe cell direction
	 * @param forest
	 * @param victimFirex
	 * @param victimFirey
	 * @param dir
	 * @return
	 */
	public static int[] findSafeCell(int[] fireInfo) {
		
		int[] res = {-1,-1}; //safe cell position
		int victimFirex = fireInfo[0];
		int victimFirey = fireInfo[1];
		int dir = fireInfo[2];
		
		/*
		 * 0 - up 
		 * 1 - down
		 * 2 - left
		 * 3 - right
		 */
		// up - fire is down from here
	    if (dir == 0) {	
	    	res[1] = victimFirey-1; 
	    	res[0] = victimFirex;
	    }
	    // down
	    else if (dir == 1) {	
	    	res[1] = victimFirey+1;
	    	res[0] = victimFirex;
	    }
	    // left
	    else if (dir == 2) {	
	    	res[1] = victimFirey;
	    	res[0] = victimFirex-1;
	    }
	    // right
	    else if (dir == 3) {	
	    	res[1] = victimFirey;
	    	res[0] = victimFirex+1;
	    }
	    System.out.println("Found safe fight cell (" + res[0] + ", " + res[1] + ")");
		return res;
		
	}
	
	/**
	 * Check there's a safe spot near the victim fire
	 * 
	 * up - 0
	 * down - 1
	 * left - 2
	 * right - 3
	 * 
	 * @param forest
	 * @param x
	 * @param y
	 * @return
	 */
	public static int[] thereIsASafeSpotNear(Forest forest, int victimFirex, int victimFirey, int[]currPos) {
		
		int[] res = new int[3];
		int x = currPos[0];
		int y = currPos[1];
		int[][] mforest = forest.getForest();
	    
	    // new agent pos and direction
	    int dir = -1;// newx = -1, newy = -1;
	    
	    // distance to cells around nearest fire
	    int peopled = ForestPanel.WIDTH;
	    int upd = peopled, downd = peopled, leftd = peopled, rightd = peopled;
	    
	    // find possible safe spots near the nearest victim fire
	    // up
	    if (victimFirey-1 > 0 
	    		&& mforest[victimFirey-1][victimFirex] != ForestPanel.FIRETILE 
	    		&& (mforest[victimFirey-1][victimFirex] != ForestPanel.FIREFIGHTER
	    				|| (victimFirey-1 == y  && victimFirex == x))
	    		&& mforest[victimFirey-1][victimFirex] != ForestPanel.PEOPLETILE
	    		) {	
	    	System.out.println("Safe spot : up");
	    	upd = Math.abs(victimFirey-1 - y) + Math.abs(victimFirex - x);
	    }
	    // down
	    if (victimFirey+1 < ForestPanel.HEIGHT 
	    		&& mforest[victimFirey+1][victimFirex] != ForestPanel.FIRETILE
	    		&& (mforest[victimFirey+1][victimFirex] != ForestPanel.FIREFIGHTER
	    				|| (victimFirey+1 == y  && victimFirex == x))
	    		&& mforest[victimFirey+1][victimFirex] != ForestPanel.PEOPLETILE
	    		) {	
	    	System.out.println("Safe spot : down");
	    	downd = Math.abs(victimFirey+1 - y) + Math.abs(victimFirex - x);
	    }
	    // left
	    if (victimFirex-1 > 0 
	    		&& mforest[victimFirey][victimFirex-1] != ForestPanel.FIRETILE 
	    		&& (mforest[victimFirey][victimFirex-1] != ForestPanel.FIREFIGHTER
	    				|| (victimFirey == y  && victimFirex-1 == x))
	    		&& mforest[victimFirey][victimFirex-1] != ForestPanel.PEOPLETILE
	    		) {	
	    	System.out.println("Safe spot : left");
	    	leftd = Math.abs(victimFirey - y) + Math.abs(victimFirex-1 - x);
	    }
	    // right
	    if (victimFirex+1 < ForestPanel.WIDTH 
	    		&& mforest[victimFirey][victimFirex+1] != ForestPanel.FIRETILE 
	    		&& (mforest[victimFirey][victimFirex+1] != ForestPanel.FIREFIGHTER
	    				|| (victimFirey == y  && victimFirex+1 == x))
	    		&& mforest[victimFirey][victimFirex+1] != ForestPanel.PEOPLETILE
	    		) {	
	    	System.out.println("Safe spot : right");
	    	rightd = Math.abs(victimFirey - y) + Math.abs(victimFirex+1 - x);
	    }
	    
	    // find nearest safe spot of all possible safe spots
        int mind = Collections.min(Arrays.asList(upd, downd, leftd, rightd));	        
       	    
	    // up - 0
	    if (upd == mind && upd != peopled) {
	    	//dir = "down";
	    	//newx = victimFirex;
        	//newy = victimFirey-1;
	    	dir = 0;
	    }
	    // down - 1
	    else if (downd == mind && downd != peopled) {
	    	//dir = "up";
	    	//newx = victimFirex;
        	//newy = victimFirey+1;
	    	dir = 1;
	    }
	    // left - 2
	    else if (leftd == mind && leftd != peopled) {
	    	//dir = "right";
	    	//newx = victimFirex-1;
        	//newy = victimFirey;
	    	dir = 2;
	    }
	    // right - 3
	    else if (rightd == mind && rightd != peopled) {
	    	//dir = "left";
	    	//newx = victimFirex+1;
        	//newy = victimFirey;
	    	dir = 3;
	    }
	    else {
	    	// verbose
	        System.out.println("[ temporary ] There is no safe spot around the victim fire located at (" + victimFirex + ", " + victimFirey + ")!");
	    }
	    
	    //res[0] = newx;
	    //res[1] = newy;
	    //res[2] = dir;
	    
	    res[0] = dir;
	    
	    return res;
    
    }
	
	/**
	 * Determine if person is victim (if there's a fire close by)
	 * 
	 * @param forest Current environment
	 * @param int x person x position
	 * @param int y person y position
	 */
	public static int[] lookAroundPerson(Forest forest, int x, int y, int[] currPos) {
		System.out.println("*Looking around (" + x + " " + y + ")");
		int[] res = {0,0};
		int[][] mforest = forest.getForest();
		
		int[] fireInfo = {-1,-1,-1};
		
		// person is in danger if fire is closer than 1 cell
		System.out.println("mforest[y+1][x] "+ mforest[y+1][x]);
        if (y+1 < ForestPanel.HEIGHT && mforest[y+1][x] == ForestPanel.FIRETILE){  //forest[y][x]
        	res[1] = y + 1; res[0] = x;
        	int[] dir = thereIsASafeSpotNear(forest, res[0], res[1], currPos);
        	fireInfo[0] = res[0];
        	fireInfo[1] = res[1];
        	fireInfo[2] = dir[0];
        	if(fireInfo[0] != -1) {
        		return fireInfo;
        	}
        } 
        System.out.println("mforest[y][x+1] "+ mforest[y][x+1]);
        if (x+1 < ForestPanel.WIDTH && mforest[y][x+1] == ForestPanel.FIRETILE){
        	res[1] = y; res[0] = x + 1;
        	int[] dir = thereIsASafeSpotNear(forest, res[0], res[1], currPos);
        	fireInfo[0] = res[0];
        	fireInfo[1] = res[1];
        	fireInfo[2] = dir[0];
        	if(fireInfo[0] != -1) {
        		return fireInfo;
       	 	}
        }
        System.out.println("mforest[y-1][x] "+ mforest[y-1][x]);
        if(y-1 > 0 && mforest[y-1][x] == ForestPanel.FIRETILE){
        	res[1] = y - 1; res[0] = x;
        	int[] dir = thereIsASafeSpotNear(forest, res[0], res[1], currPos);
        	fireInfo[0] = res[0];
        	fireInfo[1] = res[1];
        	fireInfo[2] = dir[0];
         	if(fireInfo[0] != -1) {
        		return fireInfo;
        	}
        }
        System.out.println("mforest[y][x-1]  "+ mforest[y][x-1] );
        if(x-1 > 0 && mforest[y][x-1] == ForestPanel.FIRETILE){
        	res[1] = y; res[0] = x - 1;
        	int[] dir = thereIsASafeSpotNear(forest, res[0], res[1], currPos);
        	fireInfo[0] = res[0];
        	fireInfo[1] = res[1];
        	fireInfo[2] = dir[0];
        	if(fireInfo[0] != -1) {
        		return fireInfo;
       	 	}
        } 
      //diagonal
        /*tb nao e preciso
        else if (x+1 < ForestPanel.WIDTH && y+1 < ForestPanel.HEIGHT && mforest[y+1][x+1] == ForestPanel.FIRETILE) 
        	{res[0] = y + 1; res[1] = x + 1;} 
        else if (x-1 > 0 && y-1 > 0 && mforest[y-1][x-1] == ForestPanel.FIRETILE)
        	{res[0] = y - 1; res[1] = x - 1;} 
        else if (x+1 < ForestPanel.WIDTH  && y-1 > 0 && mforest[y-1][x+1] == ForestPanel.FIRETILE) 
        	{res[0] = y - 1; res[1] = x + 1;} 
        else if (x-1 > 0   && y+1 < ForestPanel.HEIGHT && mforest[y+1][x-1] == ForestPanel.FIRETILE) 
        	{res[0] = y + 1; res[1] = x - 1;}
		else if (y+2 < ForestPanel.HEIGHT && mforest[y+2][x] == ForestPanel.FIRETILE) 
			{res[0] = y + 2; res[1] = x;}
		else if (x+2 < ForestPanel.WIDTH && mforest[y][x+2] == ForestPanel.FIRETILE) 
			{res[0] = y; res[1] = x + 2;} 
		else if (y-2 > 0 && mforest[y-2][x] == ForestPanel.FIRETILE) 
			{res[0]= y - 2; res[1] = x;} 
		else if (x-2 > 0 && mforest[y][x-2] == ForestPanel.FIRETILE) 
			{res[0]= y; res[1] = x - 2;}
        		*//*
        		//no need for 2 cell diagonal
        		||
        		(x+2 < ForestPanel.WIDTH && y+2 < ForestPanel.HEIGHT && mforest[y+2][x+2] == ForestPanel.FIRETILE) ||
        		(x-2 > 0 && y-2 > 0 && mforest[y-2][x-2] == ForestPanel.FIRETILE) ||
        		(x+2 < ForestPanel.WIDTH  && y-2 > 0 && mforest[y-2][x+2] == ForestPanel.FIRETILE) ||
        		(x-2 > 0 && y+2 < ForestPanel.HEIGHT && mforest[y+2][x-2] == ForestPanel.FIRETILE) */
        System.out.println("*Returning (" + res[0] + "," + res[1] +"," +fireInfo[0] + ")");
        return fireInfo;
        
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
	        		System.out.println("[" + agName + "] Already in position, no need to move! pos(" + pos[0] + "," + pos[1] + ")");
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
