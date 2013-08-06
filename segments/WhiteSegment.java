package segments;

import java.awt.Color;

import biogenesis.Utils;

import organisms.AliveAgent;

public class WhiteSegment extends Segment {
	private static final long serialVersionUID = 1L;

	public WhiteSegment(AliveAgent thisAgent) {
		super(thisAgent, Color.WHITE);
	}

	@Override
	public void touchEffectsOneWay(Segment otherSegment) {
		AliveAgent thisAgent = getThisAgent();
		
		if (isAlive() && otherSegment.isAlive() && !otherSegment.shield(this)) {
			AliveAgent otherAgent = otherSegment.getThisAgent();
			if (otherAgent.getGeneticCode() != thisAgent.getGeneticCode() &&
					thisAgent.useEnergy(Utils.getWHITE_ENERGY_CONSUMPTION())) {
				otherAgent.setInfectedGeneticCode(thisAgent.getGeneticCode());
				thisAgent.increaseInfected();
				thisAgent.getWorld().agentHasBeenInfected(otherAgent, thisAgent);
				thisAgent.setColor(Color.WHITE);
			}
		}
	}
}
