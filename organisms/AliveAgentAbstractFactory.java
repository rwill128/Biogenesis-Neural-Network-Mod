package organisms;

import java.io.Serializable;

import world.World;

public interface AliveAgentAbstractFactory extends Serializable {	
	public AliveAgent createAliveAgent(World world);
	public AliveAgent createAliveAgent(World world, GeneticCode gc);
	public AliveAgent[] initialPopulation(World world);
}
