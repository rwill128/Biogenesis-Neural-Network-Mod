package segments;

import java.awt.Color;

import organisms.AliveAgent;

public class YellowSegment extends Segment {
	private static final long serialVersionUID = 1L;

	public YellowSegment(AliveAgent thisAgent) {
		super(thisAgent, Color.YELLOW);
	}
}
