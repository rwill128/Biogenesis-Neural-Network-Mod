package eyes;

import segments.*;

import agents.AliveAgent;
import biogenesis.Utils;
import java.awt.Color;
import java.awt.geom.Point2D;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import organisms.Pigment;

public class SegmentEye extends Segment {
	private static final long serialVersionUID = 1L;

       // SevenDoubleInput eyeFeedback = new SevenDoubleInput();
        
        MLData eyeInput;

	public SegmentEye(AliveAgent thisAgent) {
		super(thisAgent, Pigment.EYE.getColor());
                eyeInput = new BasicMLData(Pigment.values().length);
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
                
                // Intersection point
		Point2D intersec= this.getIntersection(otherSegment);
		
		if (isAlive() && thisAgent.useEnergy(Utils.getGREEN_ENERGY_CONSUMPTION())) {
			AliveAgent otherAgent = otherSegment.getThisAgent();
                        
                         // For now, don't see parents or dead organisms.
			if ((thisAgent.getParentId() == otherAgent.getId() || thisAgent.getId() == otherAgent.getParentId()) && otherAgent.isAlive())
                               return;
                        
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
	
