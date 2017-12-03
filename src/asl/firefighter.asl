/**
 * This file implements the firefigher agent
 */

/* --- Initial beliefs and rules --- */

alive(true).			// i'm alive
safe(true).				// there is no fire near me
pos(0, 0).				// i'm at position (0,0)
extinguished(false).	// true only when ALL the fire has been extinguished
facing(up).				// direction the agent is facing (up, down, left, right).
						// the agent can only extinguish fires in the direction he's currently facing


/* --- Initial goals --- */

!start.				// i want to start thinking
//!stayAlive.			// i want to stay alive as long as possible
!extinguishFire.	// i want to extinguish ALL fire


/* --- Plans --- */

+!start : true <-
	init.
	
+!stayAlive : true <-
	?pos(X, Y);
	checkSurroundings(X, Y);
	?safe(false);
	.print("I'm not safe here, I need to run!");
	run(X, Y);
	?pos(X2, Y2);
	.print("pos(", X2, ", ", Y2, ")");
	!stayAlive.
	
-!stayAlive: true <- !stayAlive.


+!extinguishFire : extinguished(false) <-
	?pos(X, Y);
	goToNearestFire(X, Y);
	?facing(Dir);
	?pos(NX, NY);
	.print("[self] My position is (", NX, ", ", NY,"). I'm facing '", Dir,"'.");
	extinguishFire(Dir, NX, NY);
	!extinguishFire.

//+! extinguishFire : extinguished(true) <- true.