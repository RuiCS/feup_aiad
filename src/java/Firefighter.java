import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Structure;

public abstract class Firefighter {

	public static void checkSurroundings(Forest forest, Structure action) {
		
		try {
			// get pos
			int x = (int)((NumberTerm)action.getTerm(0)).solve();
	        int y = (int)((NumberTerm)action.getTerm(1)).solve();
			
	        // TODO
			// if there's fire to the right, he aint safe
			if (forest.getForest()[x + 1][y] == 3) {
				forest.addPercept(Literal.parseLiteral("safe(false)"));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void run(Forest forest, Structure action) {
		
		try {
			// get pos
			int x = (int)((NumberTerm)action.getTerm(0)).solve();
	        int y = (int)((NumberTerm)action.getTerm(1)).solve();
			
	        forest.addPercept(Literal.parseLiteral("pos(" + x + ", " + (y + 1) + ")"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}