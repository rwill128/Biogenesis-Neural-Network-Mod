package genes;

import java.awt.Color;
import organisms.Pigment;
import stbiogenesis.STUtils;

/**
 *
 */
public class SegmentEyeGene extends EyeGene
{
    
        /**
	 * Total segment length
	 */
	protected double length = 0;
	/**
	 * Inclination degree
	 */
	protected double theta = 0;
        
        private Pigment pigment;

        
        
        public SegmentEyeGene() {
        }

        public SegmentEyeGene(int length , int theta)
        {
            this.length = length;
            this.theta = theta;
            this.pigment = Pigment.SUPERMAGENTA;
        }
        
        public void randomize() 
        {
            randomizeLength();
            randomizeTheta();
        }
        /**
	 * Chooses a random segment length.
	 */
	public void randomizeLength() 
        {
		length = 2.0 + STUtils.random.nextDouble() * 16.0;
	}

	/**
	 * Chooses a random segment inclination degree.
	 */
	public void randomizeTheta() 
        {
		theta = STUtils.random.nextDouble() * 2.0 * Math.PI;
	}
    
        
        /**
	 * Returns the length of this segment.
	 * 
	 * @return  The length of this segment.
	 */
	public double getLength() {
		return length;
	}

	/**
	 * Returns the inclination degree of this segment.
	 * 
	 * @return The inclination of this segment.
	 */
	public double getTheta() {
		return theta;
	}
        
        public Pigment getPigment() {
		return pigment;
	}
	
	public Color getColor() {
		return pigment.getColor();
	}
}
