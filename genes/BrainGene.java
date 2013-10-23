package genes;

import biogenesis.Utils;
import brains.BrainType;
import java.io.Serializable;

/**
 *
 */
public class BrainGene implements Cloneable, Serializable
{
    protected BrainType brainType;
    public void randomize() 
    {
        int max_prob = 0;
        for (int i = 0; i < BrainType.values().length; i++) {
            max_prob += Utils.getBrainTypeProb(BrainType.values()[i]);
        }
        int prob = Utils.random.nextInt(max_prob);
        int ac_prob = 0;
        for (int i = 0; i < BrainType.values().length; i++) {
            ac_prob += Utils.getBrainTypeProb(BrainType.values()[i]);
            if (prob < ac_prob) {
                brainType = BrainType.values()[i];
		return;
            }
        }
    }
    
    public BrainType getBrainType()
    {
        return brainType;
    }
}
