// TODO limpar moving!

/**
 * This file implements the firefigher agent
 */

/* --- Possible beliefs / perceptions --- */

// pos(X, Y)  			the current position of the agent
// goal(X, Y)			where the agent wants to go
// facing(Dir) 			direction the agent is facing. Can only extinguish fires in that direction
// extinguished 		when all the fires have been extinguished
// ready				when txt file of environment has been fully loaded
// moving(X, Y)			currently moving towards (X, Y)


/* --- Initial goals --- */

!start.


/* --- Possible goals --- */

// !start				start the agent
// !scan				scan the environment, looking for nearest fire and updating goal(GX, GY)
// !pos					go from pos(X, Y) to goal(X, Y), one cell at a time
// !extinguishFire		extinguish fire in nearby, in the direction the agent is currently facing


/* --- Plans --- */

/**
 * Start the agent.
 * Add it to the environment (forest).
 * Start scanning the environment.
 */
 
+!start : ready <-
	!scan.

+!start : not ready <-
	init;
	!start.



@findNearestFire[atomic]
+!findNearestFire : true <-
	findNearestFire.

@move[atomic]
+!move : true <-
	move.

@extinguishFire[atomic]	
+!extinguishFire : true <-
	extinguishFire.

/**
 * Constantly scan the environment
 * looking for nearest fire and
 * updating goal(X, Y)
 */
 
+!scan : not extinguished <-
	!findNearestFire;
	!scan.

+!scan : extinguished <-
	.print("Scanning stopped, there are no more fires!").


/**
 * When nearest fire has been found
 * add goal to move towards it
 */
 
+goal(X, Y) : not moving(X, Y) <-
	!pos(X, Y).
	
+goal(X, Y) : moving(X, Y) <- .print("Already moving to requested spot!").


/**
 * Move towards the goal position,
 * one cell at a time
 */
 
+!pos(X, Y) : not pos(X, Y) <-
	-+moving(X, Y);
 	!move;
 	!pos(X, Y).
	
+!pos(X, Y) : pos(X, Y) <-
	-moving(X, Y);
 	!extinguish.
 	

/**
 * Extinguish fire in adjacent cell
 * (based on the direction the agent is facing)
 */
 	
+!extinguish : not extinguished <-			
	!extinguishFire.
	
+!extinguish : extinguished <- .print("All the fires have been extinguished!").
	