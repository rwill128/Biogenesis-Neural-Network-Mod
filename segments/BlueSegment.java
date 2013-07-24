package segments;

import java.awt.Color;

import biogenesis.Utils;

import organisms.AliveAgent;

public class BlueSegment extends Segment {
	private static final long serialVersionUID = 1L;

	public BlueSegment(AliveAgent thisAgent) {
		super(thisAgent, Color.BLUE);
	}
	
	@Override
	public boolean shield(Segment otherSegment) {
		boolean shielded = false;
		AliveAgent thisAgent = getThisAgent();
		
		if (isAlive() && thisAgent.useEnergy(Utils.getBLUE_ENERGY_CONSUMPTION())) {
			thisAgent.setColor(Color.BLUE);
			shielded = true;
		}
		return shielded;
	}
}	
