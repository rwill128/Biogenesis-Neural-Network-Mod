package genes;

import java.awt.Color;
import organisms.Pigment;
import stbiogenesis.STUtils;

/**
 *
 */
public class SegmentEyeGene extends EyeGene
{
        private Pigment pigment;

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
        @Override
	public void randomizeLength() 
        {
		length = 2.0 + STUtils.random.nextDouble() * 16.0;
	}

	/**
	 * Chooses a random segment inclination degree.
	 */
        @Override
	public void randomizeTheta() 
        {
		theta = STUtils.random.nextDouble() * 2.0 * Math.PI;
	}
    
        
        /**
	 * Returns the length of this segment.
	 * 
	 * @return  The length of this segment.
	 */
        @Override
	public double getLength() {
		return length;
	}

	/**
	 * Returns the inclination degree of this segment.
	 * 
	 * @return The inclination of this segment.
	 */
        @Override
	public double getTheta() {
		return theta;
	}
        
        @Override
        public Pigment getPigment() {
		return pigment;
	}
	
        @Override
	public Color getColor() {
		return pigment.getColor();
	}
}
