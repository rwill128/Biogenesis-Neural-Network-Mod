package segments;

import agents.AliveAgent;
import eyes.SegmentEye;
import intentionalmover.BCyanSegment;
import organisms.Pigment;

public class SegmentFactory {
	private static SegmentFactory segmentFactory = new SegmentFactory();
	
	protected SegmentFactory() {
		;
	}
	
	public static SegmentFactory getInstance() {
		return segmentFactory;
	}
	
	public Segment createSegment(AliveAgent agent, Pigment pigment) {
		Segment segment = null;
		
		if (pigment == Pigment.BLUE)
			segment = new BlueSegment(agent);
		if (pigment == Pigment.CYAN)
			segment = new CyanSegment(agent);
		if (pigment == Pigment.GRAY)
			segment = new GraySegment(agent);
		if (pigment == Pigment.GREEN)
			segment = new GreenSegment(agent);
		if (pigment == Pigment.RED)
			segment = new RedSegment(agent);
		if (pigment == Pigment.WHITE)
			segment = new WhiteSegment(agent);
		if (pigment == Pigment.YELLOW)
			segment = new YellowSegment(agent);
                if (pigment == Pigment.BCYAN)
			segment = new BCyanSegment(agent);
                if (pigment == Pigment.EYE)
			segment = new SegmentEye(agent);
                return segment;
	}

}
