package genes;

import eyes.EyeType;
import java.io.Serializable;
import organisms.Pigment;

/**
 *
 */
public class EyeGene extends NeuralGene implements Cloneable, Serializable
{
    protected EyeType eyeType;
    
    public EyeGene() {
    
    }
    
    public EyeGene(EyeType eyeType) 
    {
            this.eyeType = eyeType;
    }
    
    public EyeGene(double length, double theta) 
    {
            this.length = length;
            this.theta = theta;
            this.pigment = Pigment.EYE;
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
