/**
 * This file implements the firefigher agent
 */

/* --- Initial beliefs and rules --- */

alive(true).		// i'm alive
safe(true).			// there is no fire near me
pos(0, 0).			// i'm at position (0,0)


/* --- Initial goals --- */

!start.				// i want to start thinking
!stayAlive.			// i want to stay alive as long as possible


/* --- Plans --- */

+!start : true <-
	init;
	?pos(X, Y);
	.print("Initializing Firefighter Agent at pos (", X, ", ", Y, ")").
	
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