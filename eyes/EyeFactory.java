package eyes;

import agents.AliveAgent;
import eyes.SegmentEye;
import intentionalmover.BCyanSegment;
import organisms.Pigment;

public class EyeFactory {
	private static EyeFactory segmentFactory = new EyeFactory();
	
	protected EyeFactory() {
		;
	}
	
	public static EyeFactory getInstance() {
		return segmentFactory;
	}
	
	public Eye createEye(AliveAgent agent, EyeType eyeType) {
		Eye eye = null;
		
		if (eyeType == EyeType.SEGMENT_EYE)
			eye = new SegmentEye(agent);
                return eye;
	}

}
