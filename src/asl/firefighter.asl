/**
 * This file implements the firefigher agent
 */

/* --- Possible beliefs / perceptions --- */

// pos(X, Y)  			the current position of the agent
// move(X, Y)			where the agent wants to go
// facing(Dir) 			direction the agent is facing. Can only extinguish fires in that direction
// extinguished(true) 	when all the fires have been extinguished

/* --- Initial goals --- */

!start.					// i want to start thinking


/* --- Plans --- */

/**
 * Start the agent.
 * Add it to the environment (forest).
 * Agent wants to extinguish the fire.
 */
+!start : ready <-
	!extinguishFire.

+!start : not ready <-
	init;
	!start.

/**
 * Go to the nearest fire.
 * Extinguish that fire.
 * Repeat.
 */
 // main
+!extinguishFire : not extinguished(true) & not ready(extinguish)<-
	?pos(X, Y);
	goToNearestFire(X, Y);
	!extinguishFire.

-!extinguishFire : not extinguished(true) <- !extinguishFire.
// stop	
-!extinguishFire : extinguished(true) <- true.


/**
 * Move towards the given position,
 * one cell at a time
 */
 +move(GX, GY) : not ready(extinguish) <-
 	?pos(X, Y);
 	move(X, Y, GX, GY).

/**
 * When agent arrived at destination
 */
-move(X, Y) : ready(extinguish) <-
	?pos(NX, NY);
	?facing(NDir);	
	extinguishFire(NDir, NX, NY).
	
	
