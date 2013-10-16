package genes;

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
        brainType = BrainType.STANDARD_NN_BRAIN;
    }
    
    public BrainType getBrainType()
    {
        return brainType;
    }
    
}
