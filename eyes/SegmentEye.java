package eyes;

import segments.*;

import agents.AliveAgent;
import biogenesis.Utils;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import organisms.Pigment;
import static segments.Segment.deadColor;

public class SegmentEye extends Segment {
	private static final long serialVersionUID = 1L;

       // SevenDoubleInput eyeFeedback = new SevenDoubleInput();
        
        MLData eyeInput;
        double[] inputs;
        boolean seeingSomething;
        
        int frameCounter = 0;

	public SegmentEye(AliveAgent thisAgent) {
		super(thisAgent, Pigment.EYE.getColor());
                eyeInput = new BasicMLData(Pigment.values().length);
                inputs = new double[(Pigment.values().length)];
	}
        
        public double getEyeFeedback()
        {
           // return eyeInput;
            if (seeingSomething)
                return 1.0;
            else
                return 0.0;
        }
	
	@Override
	public void frameEffects() {
		eyeInput.clear();
          //      AliveAgent thisAgent = getThisAgent();
             //   thisAgent.useEnergy(.001d);
                frameCounter--;
                if (frameCounter == 0) {
                    seeingSomething = false;
                  //  inputs = new double[Pigment.values().length];
                }
                
                for (int i = 0; i < Pigment.values().length; i++)
                        {
                            inputs[i] = 1.0;
                        }
                        eyeInput.setData(inputs);
            
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
		
		if (isAlive() && thisAgent.useEnergy(Utils.getCYAN_ENERGY_CONSUMPTION()) && otherSegment.getThisAgent().getId() != thisAgent.getParentId()) {
                    
                //        thisAgent.setEnergy(thisAgent.getEnergy() + 0.001d);
                        
                        frameCounter = 5;
                        seeingSomething = true; 

                        for (int i = 0; i < Pigment.values().length; i++)
                        {
                        if (otherSegment.getColor() == Pigment.values()[i].getColor()) {
                            //eyeFeedback.setInputAtIndex(i, 1d);
                            inputs[i] = 5.0;
                        } else {
                            //eyeFeedback.setInputAtIndex(i, 0d);
                            inputs[i] = 1.0;
                        }
                        }
                        eyeInput.setData(inputs);
		}
	}
        
        @Override
        public void draw(Graphics g, boolean showColor) {
//                if (seeingSomething)
//                        g.setColor(Color.YELLOW);
//                else
//                    if (showColor) 
//			g.setColor(alive?_segColor:deadColor);
//                g.drawLine((int)Math.round(x1), (int)Math.round(y1), (int)Math.round(x2), (int)Math.round(y2));
	}
        @Override
        public void draw(Graphics g, double centerX, double centerY, boolean showColor) 
        {
		if (showColor) {
                    g.setColor(alive?_segColor:deadColor);
                }
                if (seeingSomething == true)
                    g.setColor(Color.YELLOW);
			
		g.drawLine((int)Math.round(x1 - this.centerX + centerX),
				(int)Math.round(y1 - this.centerY + centerY),
				(int)Math.round(x2 - this.centerX + centerX),
				(int)Math.round(y2 - this.centerY + centerY));
	}
}
	
