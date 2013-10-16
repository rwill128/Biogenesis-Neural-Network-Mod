package geneticcodes;

import genes.EyeGene;
import agents.AliveAgent;
import eyes.EyeType;

/**
 *
 */
public class EyeGeneFactory {
	private static EyeGeneFactory eyeGeneFactory = new EyeGeneFactory();
	
	protected EyeGeneFactory() {
		;
	}
	
	public static EyeGeneFactory getInstance() {
		return eyeGeneFactory;
	}
	
	public EyeGene createSegment(AliveAgent agent, EyeType eyeType) {
		EyeGene eyeGene = null;
		
		if (eyeType == EyeType.SEGMENT_EYE) ;
			//eyeGene = new SegmentEyeGene(agent);
		return eyeGene;
	}

}