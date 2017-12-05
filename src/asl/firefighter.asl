/**
 * This file implements the firefigher agent
 */

/* --- Possible beliefs / perceptions --- */

// pos(X, Y)  			the current position of the agent
// facing(Dir) 			direction the agent is facing. Can only extinguish fires in that direction
// extinguished(true) 	when all the fires have been extinguished

/* --- Initial goals --- */

!start.				// i want to start thinking


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
+!extinguishFire : not extinguished(true) <-
	?pos(X, Y);
	goToNearestFire(X, Y);
	?pos(NX, NY);
	?facing(NDir);	
	extinguishFire(NDir, NX, NY);
	//?pos(PX, PY);
	//savePeople(NDir, PX, PY);
	!extinguishFire.

// stop	
+!extinguishFire : extinguished(true) <- true.