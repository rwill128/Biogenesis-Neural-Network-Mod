package organisms;

import biogenesis.Utils;
import world.World;

public class AliveAgentFactory {
	private static AliveAgentFactory factory = new AliveAgentFactory();
	
	protected AliveAgentFactory() {
		;
	}

	public static AliveAgentFactory getInstance() {
		return factory;
	}
	
	public AliveAgent createAliveAgent(World world) {
		return new Organism(world);
	}
	
	public AliveAgent[] initialPopulation(World world) {
		AliveAgent[] agents = new AliveAgent[Utils.getINITIAL_ORGANISMS()];
		for (int i=0; i<agents.length; i++)
			agents[i] = createAliveAgent(world);
		return agents;
	}
}
