/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smartorganisms;

import organisms.Gene;
import stbiogenesis.STUtils;
import organisms.GeneticCode;

/**
 *
 * @author Rick Williams
 */
public class NeuralGeneticCode extends GeneticCode
{
       /**
	* Creates a new genetic code based on the father genetic code but
	* applying random mutations to it.
	* 
	* @param parentCode  The genetic code that this code will be based on.
	*/
	public NeuralGeneticCode(GeneticCode parentCode) {
		int i,j;
		int addedGene = -1;
		int removedGene = -1;
		int nGenes;
		boolean randomLength;
		boolean randomTheta;
		boolean randomColor;
		boolean randomBack;
		
		if (STUtils.randomMutation())
			randomMirror();
		else
			setMirror(parentCode.getMirror());
		
                if (STUtils.randomMutation()) {
			// change symmetry
			if (STUtils.random.nextInt(10) < 2)
				randomSymmetry();
			else
				setSymmetry(STUtils.between(getSymmetry()+STUtils.randomSign(), 1, 8));
			nGenes = parentCode.getNGenes();
			if (nGenes * getSymmetry() > MAX_SEGMENTS) {
				setSymmetry(parentCode.getSymmetry());
			}
		} else {
			// keep symmetry
			setSymmetry(parentCode.getSymmetry());
			if (STUtils.randomMutation()) {
			// change number of segments
				if (STUtils.random.nextBoolean()) {
				// increase segments
					if (parentCode.getNGenes() * parentCode.getSymmetry() >= MAX_SEGMENTS)
						nGenes = parentCode.getNGenes();
					else {
						nGenes = parentCode.getNGenes() + 1;
						addedGene = STUtils.random.nextInt(nGenes);
					}
				} else {
				// decrease segments
					if (parentCode.getNGenes() * parentCode.getSymmetry() <= MIN_SEGMENTS)
						nGenes = parentCode.getNGenes();
					else {
						nGenes = parentCode.getNGenes() - 1;
						removedGene = STUtils.random.nextInt(parentCode.getNGenes());
					}
				}
			} else {
			// keep number of segments
				nGenes = parentCode.getNGenes();
			}
		}
		// Create genes
		setGenes(new NeuralGene[nGenes]);
		for (i=0,j=0; i<nGenes; i++,j++) {
			if (removedGene == j) {
				i--;
				continue;
			}
			if (addedGene == i) {
				setGene(i,  new Gene());
				randomizeGene(i);
				j--;
				continue;
			}
			randomLength = randomTheta = randomColor = randomBack = false;
			if (STUtils.randomMutation())
				randomLength = true;
			if (STUtils.randomMutation())
				randomTheta = true;
			if (STUtils.randomMutation())
				randomColor = true;
			if (STUtils.randomMutation())
				randomBack = true;
			if (randomLength || randomTheta || randomColor || randomBack) {
				setGene(i, new Gene());
				if (randomLength)
					randomizeGeneLength(i);
				else
					genes[i].setLength(parentCode.getGene(j).getLength());
				if (randomTheta)
					genes[i].randomizeTheta();
				else
					genes[i].setTheta(parentCode.getGene(j).getTheta());
				if (randomColor)
					genes[i].randomizeColor();
				else
					genes[i].setPigment(parentCode.getGene(j).getPigment());
			} else
				genes[i] = parentCode.getGene(j);
		}

		if (STUtils.randomMutation())
			randomDisperseChildren();
		else
			disperseChildren = parentCode.getDisperseChildren();
		calculateReproduceEnergy();
		maxAge = STUtils.getMAX_AGE();
	}
        
        /**
	 * Gives mirror a random value (0 or 1)
	 */
	private void randomMirror() {
		this.setMirror(STUtils.random.nextBoolean());
	}
	/**
	 * Gives symmetry a random value (2, 4 or 8)
	 */
	private void randomSymmetry() {
		symmetry = STUtils.random.nextInt(8)+1;
	}
}
