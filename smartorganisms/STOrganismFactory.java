package smartorganisms;

import agents.AliveAgent;
import agents.AliveAgentAbstractFactory;
import organisms.GeneticCode;
import organisms.Organism;
import stbiogenesis.STUtils;
import world.World;

/**
 * This class implements the AliveAgentAbstractFactory for the
 * original Biogenesis organisms.
 */
public class STOrganismFactory implements AliveAgentAbstractFactory {
	/**
	 * Version of this class
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Creates a new {@link Organism} and associates it to
	 * the world.
	 * 
	 * @param world  The World where the Organism will live.
	 * @return  the new Organism.
	 */
	public AliveAgent createAliveAgent(World world) {
		return new STOrganism(world);
	}
	/**
	 * Creates a new {@link Organism} with the given GeneticCode
	 * and associates it to the world.
	 * 
	 * @param world  The World where the Organism will live.
	 * @param gc  The genetic code of the new organism.
	 * @return  the new Organism.
	 */
	public AliveAgent createAliveAgent(World world, GeneticCode gc) {
            //TODO: create a method that takes a GeneticCode and appends the information for a NeuralGeneticCode. This might be most appropriate in the STOrganism constructors?
            return new STOrganism(world, gc);
	}
        
        
        public AliveAgent createAliveAgent(World world, NeuralGeneticCode ngc) {
            return new STOrganism(world, ngc);
	}
        
	/**
	 * Creates an array of new Organisms, of length the preferred
	 * INITIAL_ORGANISMS as specified in {@link Utils}.
	 * 
	 * @param world  The World where the Organism will live.
	 * @return  an array of new Organisms.
	 */
	public AliveAgent[] initialPopulation(World world) {
		AliveAgent[] agents = new AliveAgent[STUtils.getINITIAL_ORGANISMS()];
		for (int i=0; i<agents.length; i++)
			agents[i] = createAliveAgent(world);
		return agents;
	}
}
