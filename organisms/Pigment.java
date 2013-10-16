package organisms;

import java.awt.Color;

import biogenesis.Utils;

public enum Pigment {
	BROWN (Utils.ColorBROWN),
	RED (Color.RED),
	GREEN (Color.GREEN),
	BLUE (Color.BLUE),
	CYAN (Color.CYAN),
	WHITE (Color.WHITE),
	GRAY (Color.GRAY),
	YELLOW (Color.YELLOW),
        BCYAN (Color.CYAN.brighter()),
        
        SUPERMAGENTA (Color.MAGENTA),
        EYE (Color.GRAY.brighter());

	
	
	private Color color;
	
	private Pigment(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
}
