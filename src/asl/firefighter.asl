// Agent firefighter in project forestFires

/* Initial beliefs and rules */

alive(true).		// i'm alive
safe(true).			// no fire around me
pos(0, 0).			// i'm at position (0,0)
pos(clear).			// there's no fire here

/* Initial goals */

!start.				// start
!stayAlive.			// duh

/* Plans */

+!start : true <-
	.print("hello world.");
	?pos(X, Y);
	.print("pos(", X, ", ", Y, ")").
	
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