/**
 * This file implements the firefigher agent
 */

/* --- Possible beliefs / perceptions --- */

// extinguished 		when all the fires have been extinguished
// extinguishing		the agent is currently extinguishing a nearby fire
// facing(Dir) 			direction the agent is facing. Can only extinguish fires in that direction
// goal(X, Y)			where the agent wants to go
// moving(X, Y)			the agent is currently on its way to (X, Y)
// pos(X, Y)  			the current position of the agent
// ready				when environment has been fully loaded


/* --- Possible goals --- */

// !extinguish			extinguish fire nearby, in the direction the agent is currently facing
// !pos					go from pos(X, Y) to goal(X, Y), one cell at a time
// !scan				scan the environment, looking for nearest fire and updating goal(GX, GY)
// !start				start the agent
// ![action]			see below for an explanation on actions


/* --- Initial goals --- */

!start.


/* --- Actions --- */

// This is used in order to simulate synchronization.
// If actions are called directly from within a plan,
// they overlap each other, causing consistency problems.
// Thus, each action should be wrapped within its own (atomic) goal.
// Exceptions: init

@findNearestFire[atomic]
+!findNearestFire : true <-
	findNearestFire.
	

@move[atomic]
+!move : true <-
	move.
	

@extinguishFire[atomic]	
+!extinguishFire : true <-
	extinguishFire.
	
	
/* --- Plans --- */

/**
 * Start the agent.
 * Add it to the environment (forest).
 * Start scanning.
 */
 
+!start : ready <-
	!scan.

+!start : not ready <-
	init;
	!start.


/**
 * Constantly scan the environment,
 * looking for nearest fire
 * and updating goal(X, Y).
 */
 
+!scan : not extinguished <-
	!findNearestFire;
	!scan.

+!scan : extinguished <-
	.print("Scanning stopped, there are no more fires!").


/**
 * When goal has been added,
 * start moving towards it.
 */
 
+goal(X, Y) : not moving(X, Y) & not extinguished <-
	-+moving(X, Y);
	!pos(X, Y).
	
	
/**
 * Move towards the goal position,
 * one cell at a time.
 */
 
+!pos(X, Y) : not pos(X, Y)  & not extinguished <-
 	!move;
 	!pos(X, Y).
	
+!pos(X, Y) : pos(X, Y) & not extinguished & not extinguishing <-
	+extinguishing;
 	!extinguish;
 	-moving(X, Y);
 	-extinguishing.
 	
 // prevents exception	
 +!pos(X, Y) : true <- true.


/**
 * Extinguish fire in adjacent cell.
 * (based on the direction the agent is facing)
 */
 	
+!extinguish : not extinguished <-			
	!extinguishFire.
	
+!extinguish : extinguished <- .print("All the fires have been extinguished!").
	