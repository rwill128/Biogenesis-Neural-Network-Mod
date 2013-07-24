package segments;

import java.awt.Color;

import biogenesis.Utils;

import organisms.AliveAgent;

public class GraySegment extends Segment {
	private static final long serialVersionUID = 1L;

	public GraySegment(AliveAgent thisAgent) {
		super(thisAgent, Color.GRAY);
	}
	
	@Override
	public void touchEffectsOneWay(Segment otherSegment) {
		AliveAgent thisAgent = getThisAgent();
		if (isAlive() && otherSegment.isAlive() && !otherSegment.shield(this) &&
				thisAgent.useEnergy(Utils.getGRAY_ENERGY_CONSUMPTION())) {
			otherSegment.getThisAgent().die(thisAgent);
			thisAgent.setColor(Color.GRAY);
		}
	}
}
