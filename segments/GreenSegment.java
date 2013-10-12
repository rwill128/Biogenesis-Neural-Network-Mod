package segments;

import java.awt.Color;

import biogenesis.Utils;

import agents.AliveAgent;
import world.Atmosphere;

public class GreenSegment extends Segment {
	private static final long serialVersionUID = 1L;

	public GreenSegment(AliveAgent thisAgent) {
		super(thisAgent, Color.GREEN);
	}
	
	@Override
	public void frameEffects() {
		AliveAgent thisAgent = getThisAgent();
		
		if (isAlive() && thisAgent.useEnergy(Utils.getGREEN_ENERGY_CONSUMPTION())) {
			Atmosphere atmosphere = thisAgent.getWorld().getAtmosphere();
			double energy = thisAgent.getEnergy();
			
			thisAgent.setEnergy(energy+atmosphere.photosynthesis(getMass()));
		}
	}
}
