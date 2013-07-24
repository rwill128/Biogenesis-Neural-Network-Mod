package world;

import java.io.Serializable;

import biogenesis.Utils;

public class Atmosphere implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The amount of O2 in the atmosphere of this world. 
	 */
	private double o2;
	
	/**
	 * The amount of CO2 in the atmosphere of this world.
	 */
	private double co2;
	
	public Atmosphere() {
		o2 = Utils.getINITIAL_O2();
		co2 = Utils.getINITIAL_CO2();
	}
	
	/**
	 * Returns the amount of O2 that exist in the atmosphere.
	 * 
	 * @return  The amount of O2.
	 */
	public double getO2() {
		return o2;
	}
	
	/**
	 * Returns the amount of CO2 that exist in the atmosphere.
	 * 
	 * @return  The amount of CO2.
	 */
	public double getCO2() {
		return co2;
	}
	
	/**
	 * Add O2 to the atmosphere.
	 * 
	 * @param q  The amount of O2 to add.
	 */
	public void addO2(double q) {
		o2 += q;
	}
	
	/**
	 * Add CO2 to the atmosphere.
	 * 
	 * @param q  The amount of CO2 to add.
	 */
	public void addCO2(double q) {
		co2 += q;
	}
	
	/**
	 * Substracts O2 from the atmosphere.
	 * 
	 * @param q  The amount of O2 to substract.
	 */
	public void decreaseO2(double q) {
		o2 -= Math.min(q, o2);
	}
	
	/**
	 * Substract CO2 from the atmosphere.
	 * 
	 * @param q  The amount of CO2 to substract.
	 */
	public void decreaseCO2(double q) {
		co2 -= Math.min(q, co2);
	}

	/**
	 * Consume O2 from the atmosphere to realize the respiration process
	 * needed to consume accumulated chemical energy. Frees the same
	 * amount of CO2 to the atmosphere than O2 consumed.
	 * 
	 * @param q  The amount of O2 required.
	 * @return  The amount of O2 obtained. This is always <code>q</code>
	 * unless there weren't enough O2 in the atmosphere.
	 */
	public double respiration(double q) {
		double d = Math.min(q, o2);
		o2 -= d;
		co2 += d;
		return d;
	}
	/**
	 * Consume CO2 from the atmosphere to realize the photosynthesis process
	 * needed to obtain chemical energy from the Sun. Frees the same amount
	 * of O2 to the atmosphere than CO2 consumed.
	 * 
	 * The CO2 obtained is calculated as follows: the total length of the
	 * organism's green segments is divided by a fixed parameter that indicates
	 * green segment effectiveness. Then, the result is multiplied by the total
	 * CO2 in the atmosphere and divided by another parameter that indicates
	 * the concentration of CO2 needed to absorve it. The result is the total
	 * amount of CO2 that the organism can get. This value can't be greater than
	 * the total amount of CO2 in the atmosphere, nor the effectiveness of the
	 * initial length.
	 * 
	 * @param q  The total length of the organism's green segments.
	 * @return  The amount of CO2 obtained. 
	 */
	public double photosynthesis(double q) {
		q /= Utils.getGREEN_OBTAINED_ENERGY_DIVISOR();
		q = Utils.min(q, q * co2 / Utils.getDRAIN_SUBS_DIVISOR(), co2);
		co2 -= q;
		o2 += q;
		return q;
	}

}
