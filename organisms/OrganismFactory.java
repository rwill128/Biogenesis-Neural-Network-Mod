package organisms;

import biogenesis.Utils;
import world.World;

public class OrganismFactory implements AliveAgentAbstractFactory {
	private static final long serialVersionUID = 1L;
	public AliveAgent createAliveAgent(World world) {
		return new Organism(world);
	}
	public AliveAgent createAliveAgent(World world, GeneticCode gc) {
		return new Organism(world, gc);
	}
	public AliveAgent[] initialPopulation(World world) {
		AliveAgent[] agents = new AliveAgent[Utils.getINITIAL_ORGANISMS()];
		for (int i=0; i<agents.length; i++)
			agents[i] = createAliveAgent(world);
		return agents;
	}
}
