package genes;

import eyes.EyeType;
import java.io.Serializable;

/**
 *
 */
public class EyeGene extends Gene implements Cloneable, Serializable
{
    protected EyeType eyeType;
    
    public EyeGene() { }
    
    public EyeGene(EyeType eyeType) 
    {
            this.eyeType = eyeType;
    }

    public EyeType getEyeType()
    {
        return eyeType;
    }
    
    public void randomize() 
    {
        eyeType = EyeType.SEGMENT_EYE;
    }
}
