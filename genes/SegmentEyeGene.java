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
        
        /**
	 * Chooses a random segment length.
	 */
	public void randomizeLength() {
		length = 2.0 + Utils.random.nextDouble() * 64.0;
	}

	/**
	 * Chooses a random segment inclination degree.
	 */
	public void randomizeTheta() {
		theta = Utils.random.nextDouble() * 2.0 * Math.PI;
	}
}
