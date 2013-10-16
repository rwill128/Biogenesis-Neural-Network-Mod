package brains;

import agents.AliveAgent;
import eyes.SegmentEye;
import intentionalmover.BCyanSegment;
import organisms.Pigment;

public class BrainFactory {
	private static BrainFactory segmentFactory = new BrainFactory();
	
	protected BrainFactory() {
		;
	}
	
	public static BrainFactory getInstance() {
		return segmentFactory;
	}
	
	public Brain createBrain(AliveAgent agent, BrainType brainType) {
		Brain brain = null;
		
		if (brainType == BrainType.STANDARD_NN_BRAIN)
			brain = new StandardNNBrain(agent);
                return brain;
	}

}
