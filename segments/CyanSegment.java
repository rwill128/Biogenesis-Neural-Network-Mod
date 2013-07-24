package segments;

import java.awt.Color;

import biogenesis.Utils;

import organisms.AliveAgent;

public class CyanSegment extends Segment {
	private static final long serialVersionUID = 1L;

	public CyanSegment(AliveAgent thisAgent) {
		super(thisAgent, Color.CYAN);
	}
	
	@Override
	public void frameEffects() {
		AliveAgent thisAgent = getThisAgent();
		if (isAlive() && Utils.random.nextInt(100)<8 && thisAgent.useEnergy(Utils.getCYAN_ENERGY_CONSUMPTION())) {
			double dx = thisAgent.getDx();
			double dy = thisAgent.getDy();
			double dtheta = thisAgent.getDtheta();
			double mass = thisAgent.getMass();
			double i = thisAgent.getInertia();
			
			thisAgent.setDx(Utils.between(dx+12d*(getX2()-getX1())/mass,
					-Utils.getMAX_VEL(), Utils.getMAX_VEL()));
			thisAgent.setDy(Utils.between(dy+12d*(getY2()-getY1())/mass,
					-Utils.getMAX_VEL(), Utils.getMAX_VEL()));
			thisAgent.setDtheta(Utils.between(dtheta+Utils.randomSign()*getMass()*Math.PI/i,
					-Utils.getMAX_ROT(), Utils.getMAX_ROT()));
		}
	}
	
}
