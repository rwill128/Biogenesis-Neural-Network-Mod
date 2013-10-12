package segments;

import java.awt.Color;

import biogenesis.Utils;

import agents.AliveAgent;

public class RedSegment extends Segment {
	private static final long serialVersionUID = 1L;

	public RedSegment(AliveAgent thisAgent) {
		super(thisAgent, Color.RED);
	}
	
	@Override
	public void touchEffectsOneWay(Segment otherSegment) {
		double takenEnergy;
		double CO2freed;
		AliveAgent thisAgent = getThisAgent();
		
		if (isAlive() && !otherSegment.shield(this) && thisAgent.useEnergy(Utils.getRED_ENERGY_CONSUMPTION())) {
			AliveAgent otherAgent = otherSegment.getThisAgent();
			takenEnergy = Utils.between(getMass()*Utils.getORGANIC_OBTAINED_ENERGY(), 0, otherAgent.getEnergy());
			otherAgent.setEnergy(otherAgent.getEnergy()-takenEnergy);
			thisAgent.setEnergy(thisAgent.getEnergy()+takenEnergy);
			CO2freed = takenEnergy * Utils.getORGANIC_SUBS_PRODUCED();
			thisAgent.useEnergy(CO2freed);
			thisAgent.setColor(Color.RED);
			if (otherAgent.isAlive() && otherAgent.getEnergy() < Utils.tol)
				otherAgent.die(thisAgent);
		}
	}
}
