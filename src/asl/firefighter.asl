/**
 * This file implements the firefigher agent
 */

/* --- Possible beliefs / perceptions --- */

// pos(X, Y)  			the current position of the agent
// facing(Dir) 			direction the agent is facing. Can only extinguish fires in that direction
// extinguished(true) 	when all the fires have been extinguished

/* --- Initial goals --- */

!start.				// i want to start thinking

// going to (NX,NY) to clear fire (FX,FY)
+going(NX,NY,FX,FY)[source(A)] : true <- .print("Got going(",A,",",NX,",",NY,",",FX,",",FY,")").

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
+!extinguishFire : not extinguished(true)<-
	?pos(X, Y);	
	checkForFire(X, Y);
	
	?fireAt(FX,FY);
	?pos(NX,NY);
	!informPlan(NX,NY,FX,FY);
	
	!extinguishFireAux(X,Y,NX,NY,FX,FY).

// stop	
+!extinguishFire : extinguished(true) <- true.

// someone has the same intention
// don't extinguish
+!extinguishFireAux(_,_,NX,NY,FX,FY) : 
	going(NX,NY,FX,FY)[source(A)] & .my_name(Me) & not (A == Me) <-
	!extinguishFire.

// else extinguish
+!extinguishFireAux(X,Y,NX,NY,FX,FY) : not (going(NX,NY,FX,FY)[source(A)] & .my_name(Me) & not (A == Me)) <- 
	?facing(NDir);
	goToNearestFire(X,Y,NDir,NX,NY);
	extinguishFire(NDir, NX, NY);
	!extinguishFire.

+!informPlan(NX,NY,FX,FY) : true <-
	.broadcast(tell, going(NX,NY,FX,FY)).