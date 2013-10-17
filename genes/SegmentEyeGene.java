package genes;

import biogenesis.Utils;
import java.awt.Color;
import organisms.Pigment;


/**
 *
 */
public class SegmentEyeGene extends EyeGene
{
        public SegmentEyeGene() {
            this.pigment = Pigment.EYE;
        }

        public SegmentEyeGene(int length , int theta)
        {
            this.length = length;
            this.theta = theta;
            this.pigment = Pigment.EYE;
        }
        
        @Override
        public void randomize() 
        {
            randomizeLength();
            randomizeTheta();
        }
}
