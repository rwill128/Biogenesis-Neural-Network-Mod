/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smartorganisms;

import organisms.GeneticCode;
import organisms.Organism;
import organisms.SegmentBasedOrganism;
import world.World;

/**
 *
 * @author Rick Williams
 */
public class STOrganism extends Organism implements SeeingAgent, ThinkingAgent
{
    public STOrganism(SegmentBasedOrganism parent) {
		super(parent);
	}
	
    public STOrganism(World world) {
		super(world, new GeneticCode());
	}
    
    public STOrganism(World world, GeneticCode geneticCode) {
		super(world, geneticCode);
	}
}
