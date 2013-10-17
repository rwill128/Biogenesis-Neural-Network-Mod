package intentionalmover;

import segments.*;

import agents.AliveAgent;
import biogenesis.Utils;
import brains.Output;
import java.awt.Color;

public class BCyanSegment extends Segment implements IntentionalMover{
	private static final long serialVersionUID = 1L;
        
        double brainXAccelMod = 1;
        double brainYAccelMod = 1;
        double brainThetaAccelMod = 1;

	public BCyanSegment(AliveAgent thisAgent) {
		super(thisAgent, Color.CYAN);
	}
        
	@Override
	public void frameEffects() {
		AliveAgent thisAgent = getThisAgent();
		if (isAlive() && thisAgent.useEnergy(Utils.getCYAN_ENERGY_CONSUMPTION())) {
			double dx = thisAgent.getDx();
			double dy = thisAgent.getDy();
			double dtheta = thisAgent.getDtheta();
			double mass = thisAgent.getMass();
			double i = thisAgent.getInertia();
                                                
                        //For a thinking creature, the 12D multiplier would possibly be a modifier I use for acceleration, etc. Actually, no. I would simply have another variable that swings between -1 and 1 to scale the acceleration / momentum.
                        //I have to look at physics equations again.
			thisAgent.setDx(Utils.between(dx+brainXAccelMod*12d*(getX2()-getX1())/mass,
					-Utils.getMAX_VEL(), Utils.getMAX_VEL()));
			thisAgent.setDy(Utils.between(dy+brainYAccelMod*12d*(getY2()-getY1())/mass,
					-Utils.getMAX_VEL(), Utils.getMAX_VEL()));
			thisAgent.setDtheta(Utils.between(dtheta+brainThetaAccelMod*getMass()*Math.PI/i,
					-Utils.getMAX_ROT(), Utils.getMAX_ROT()));
		}
	}

    
    public void setBrainOutputs(double brainX, double brainY, double brainTheta)
    {
        //RegressiveTripletOutput myDirection = (RegressiveTripletOutput) myDirections;
        brainXAccelMod = brainX;
        brainYAccelMod = brainY;
        brainThetaAccelMod = brainTheta;
        
    }

    @Override
    public void setBrainOutputs(Output myDirections)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
	
}
