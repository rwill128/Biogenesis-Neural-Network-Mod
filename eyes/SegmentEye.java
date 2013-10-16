package eyes;

import segments.*;

import agents.AliveAgent;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import organisms.Pigment;
import stbiogenesis.STUtils;

public class SegmentEye extends Segment implements Eye {
	private static final long serialVersionUID = 1L;

       // SevenDoubleInput eyeFeedback = new SevenDoubleInput();
        
        MLData eyeInput;

	public SegmentEye(AliveAgent thisAgent) {
		super(thisAgent, Pigment.SUPERMAGENTA.getColor());
                eyeInput = new BasicMLData(7);
	}
        
        public MLData getEyeFeedback()
        {
            return eyeInput;
        }
	
	@Override
	public void frameEffects() {
		eyeInput.clear();
	}
        
        @Override
        public void touchEffects(Segment otherSegment) {
		touchEffectsOneWay(otherSegment);
	}
        
        @Override
	public void touchEffectsOneWay(Segment otherSegment) {
		AliveAgent thisAgent = getThisAgent();
                
                eyeInput.clear();
                
		
		if (isAlive() && thisAgent.useEnergy(STUtils.getEYE_ENERGY_CONSUMPTION())) {
			AliveAgent otherAgent = otherSegment.getThisAgent();
                        
                        for (int i = 0; i < Pigment.values().length; i++)
                        if (otherSegment.getColor() == Pigment.values()[i].getColor()) {
                            //eyeFeedback.setInputAtIndex(i, 1d);
                            eyeInput.add(i, 1d);
                        } else {
                            //eyeFeedback.setInputAtIndex(i, 0d);
                            eyeInput.add(i, 0d);
                        }
		}
	}
}
	
