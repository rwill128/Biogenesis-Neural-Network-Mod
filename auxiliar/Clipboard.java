package auxiliar;

import organisms.GeneticCode;

public class Clipboard {
	private static Clipboard clipboard = null;
	
	protected Clipboard() {
		
	}
	/**
	 * This is the last genetic code obtained using a Copy option. It is used when
	 * pasting new organisms or in the genetic lab. 
	 */
	private GeneticCode clippedGeneticCode = null;
	
	/**
	 * Set a genetic code as the clipped genetic code, that will be used when
	 * pasting a new organism or in the genetic lab as the staring genetic code.
	 * 
	 * @param gc  The clipped genetic code
	 */
	public void setClippedGeneticCode(GeneticCode gc) {
		if (gc != null)
			clippedGeneticCode = gc;
	}
	
	public GeneticCode getClippedGeneticCode() {
		return clippedGeneticCode;
	}
	
	public static Clipboard getInstance() {
		if (clipboard == null)
			clipboard = new Clipboard();
		return clipboard;
	}
}
