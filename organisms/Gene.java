/* Copyright (C) 2006-2011  Joan Queralt Molina
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */
package organisms;


import java.io.*;
import java.awt.*;

import biogenesis.Utils;

/**
 * This class implements a single organism's gene. A gene is a colored segment.
 * This segment is part of the organism's body and will be drawn several times
 * depending on the symmetry of the organism. Genes are always segments starting
 * at (0,0). The position in the organism's body depends on their gene neighbors
 * and the organism's symmetry and mirroring.
 */
public class Gene implements Cloneable, Serializable {
	/**
	 * The version number of this class
	 */
	private static final long serialVersionUID = Utils.FILE_VERSION;
	/**
	 * Total segment length
	 */
	private double length = 0;
	/**
	 * Inclination degree
	 */
	private double theta = 0;
	/**
	 * Segment's color
	 */
	private Pigment pigment;

	/**
	 * Void constructor. Creates the gene but leave it uninitialized.
	 */
	public Gene() {
	}

	/**
	 * Creates a gene with the specified final point and color.
	 * 
	 * @param x
	 *            x coordinate of the final point
	 * @param y
	 *            t coordinate of the final point
	 * @param color
	 *            segment's color
	 */
	public Gene(double length, double theta, Pigment color) {
		this.length = length;
		this.theta = theta;
		this.pigment = color;
	}

	/**
	 * Chooses a random segment color, based on the probabilities specified in
	 * user's preferences.
	 */
	public void randomizeColor() {
		int max_prob = Utils.getRED_PROB() + Utils.getGREEN_PROB() + Utils.getBLUE_PROB()
				+ Utils.getCYAN_PROB() + Utils.getWHITE_PROB() + Utils.getGRAY_PROB()
				+ Utils.getYELLOW_PROB();
		int prob = Utils.random.nextInt(max_prob);
		int ac_prob = Utils.getRED_PROB();
		if (prob < ac_prob) {
			pigment = Pigment.RED;
			return;
		}
		ac_prob += Utils.getGREEN_PROB();
		if (prob < ac_prob) {
			pigment = Pigment.GREEN;
			return;
		}
		ac_prob += Utils.getBLUE_PROB();
		if (prob < ac_prob) {
			pigment = Pigment.BLUE;
			return;
		}
		ac_prob += Utils.getCYAN_PROB();
		if (prob < ac_prob) {
			pigment = Pigment.CYAN;
			return;
		}
		ac_prob += Utils.getWHITE_PROB();
		if (prob < ac_prob) {
			pigment = Pigment.WHITE;
			return;
		}
		ac_prob += Utils.getGRAY_PROB();
		if (prob < ac_prob) {
			pigment = Pigment.GRAY;
			return;
		}
		pigment = Pigment.YELLOW;
	}

	/**
	 * Chooses a random segment length.
	 */
	public void randomizeLength() {
		length = 2.0 + Utils.random.nextDouble() * 16.0;
	}

	/**
	 * Chooses a random segment inclination degree.
	 */
	public void randomizeTheta() {
		theta = Utils.random.nextDouble() * 2.0 * Math.PI;
	}

	/**
	 * Randomize the component of this gene: final point and color. Coordinates
	 * are given a random number between -13 and -2 or 2 and 13. Color is given
	 * a random color. The probability of each color is taken from user
	 * preferences.
	 */
	public void randomize() {
		randomizeLength();
		randomizeTheta();
		randomizeColor();
	}

	/**
	 * Return an exact copy of this gene.
	 */
	@Override
	public Object clone() {
		Gene newGen = null;
		try {
			newGen = (Gene) super.clone();
		} catch (CloneNotSupportedException e) {// We should never reach this
		}
		return newGen;
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

	/**
	 * Returns the segment's color.
	 * 
	 * @return the segment's color
	 */
	public Pigment getPigment() {
		return pigment;
	}
	
	public Color getColor() {
		return pigment.getColor();
	}

	/**
	 * Assign a color to the segment.
	 * 
	 * @param pigment
	 *            The color to assign
	 */
	public void setPigment(Pigment pigment) {
		this.pigment = pigment;
	}

	/**
	 * Assign a length to the segment.
	 * 
	 * @param length  The length to assign.
	 */
	public void setLength(double length) {
		this.length = length;
	}

	/**
	 * Assign an inclination to the segment.
	 * 
	 * @param theta  The inclination degree to assign.
	 */
	public void setTheta(double theta) {
		this.theta = theta;
	}
}
