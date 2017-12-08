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
 
+goal(X, Y) : not moving(X, Y) & commitedGoal(_,_,FCX,FCY) & not extinguished <-
	?fireAt(FX,FY);
	!informOthersOfPlan(X,Y,FX,FY);
	-+moving(X, Y);
	!pos(X, Y,FCX,FCY).
	
+goal(X, Y) : not moving(X, Y) & not commitedGoal(_,_,_,_) & not extinguished <-
	?fireAt(FX,FY);
	!informOthersOfPlan(X,Y,FX,FY);
	-+moving(X, Y);
	!pos(X, Y,FX,FY).

/**
 * The colaboration algorithm:
 * 	1. Get goal (cell near fire) and set
 *  2. Inform others of this choice
 * 	3. If someone has made this choice already, back off
 * 	4. Else go to goal
 */

// I made the choice first
+going(X,Y,FX,FY)[source(A)] : .my_name(Me) & not (A == Me) & commitedGoal(X,Y,FX,FY)<- 
	.print("Got Going(",X,",",Y,")[source(",A,")] - I MADE THIS CHOICE FIRST").
// I made a different choice
+going(X,Y,FX,FY)[source(A)] : .my_name(Me) & not (A == Me) & not commitedGoal(X,Y,FX,FY)<- 
	.print("Got Going(",X,",",Y,")[source(",A,")] - I MADE A DIFFERENT CHOICE SO I KEEP IT").
// I did not make the choice first
+commitedGoal(X,Y,FX,FY) : .my_name(Me) & going(X,Y,FX,FY)[source(A)] & not (A==Me) <-
	-commitedGoal(X,Y,FX,FY).

+arrived(X,Y,FX,FY)[source(A)] : .my_name(Me) & not (A==Me) & going(X,Y,FX,FY)[source(A)] <-
	.print("-------------------- AGENT ", A, " GOT TO GOAL ", X, ",", Y, " for fire at ", FX, ",", FY,")");
	-going(X,Y,FX,FY)[source(A)].
+arrived(X,Y,FX,FY)[source(A)] : .my_name(Me) & not(A==Me) & not going(X,Y,FX,FY)[source(A)] <- .print("------- IGNORED").

/**
 * Inform other agents of this agent's intention in going to a given cell.
 */

+!informOthersOfPlan(X,Y,FX,FY) : not commitedGoal(_,_,_,_) <-
	+commitedGoal(X,Y,FX,FY);
	.broadcast(tell,going(X,Y,FX,FY)).
+!informOthersOfPlan(X,Y,FX,FY) : commitedGoal(_,_,_,_) <- true.
	
/**
 * Inform other agents that this agent reached its goal
 */

+!informOthersOfSuccess(X,Y,FX,FY) : commitedGoal(X,Y,FX,FY) <-
	.broadcast(tell, arrived(X,Y,FX,FY));
	-commitedGoal(X,Y,FX,FY).
+!informOthersOfSuccess(X,Y,FX,FY) : true <- true.

/**
 * Move towards the goal position,
 * one cell at a time.
 */
 
+!pos(X, Y,FX,FY) : not pos(X, Y)  & not extinguished & not extinguishing<-
 	!move;
 	!pos(X, Y,FX,FY).
	
+!pos(X, Y,FX,FY) : pos(X, Y) & not extinguished & not extinguishing <-
	+extinguishing;
 	!extinguish;
 	-moving(X, Y);
 	!informOthersOfSuccess(X,Y,FX,FY);
 	-extinguishing.
 	
 // prevents exception	
 +!pos(X, Y,FX,FY) : true <- true.


/**
 * Extinguish fire in adjacent cell.
 * (based on the direction the agent is facing)
 */
 	
+!extinguish : not extinguished <-			
	!extinguishFire.
	
+!extinguish : extinguished <- .print("All the fires have been extinguished!").
	