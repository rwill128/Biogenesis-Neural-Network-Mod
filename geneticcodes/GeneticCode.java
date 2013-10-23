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
package geneticcodes;


import genes.Gene;
import agents.AliveAgent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.*;
import java.util.List;

import segments.Segment;
import segments.SegmentFactory;
import biogenesis.Utils;
import auxiliar.BioXMLParser;
import auxiliar.Vector2D;
import auxiliar.Writeable;

/**
 * This class implements a full organism's genetic code. A genetic code is
 * composed by a number of genes, a symmetry, optional mirroring, optional children
 * dispersing, maximum life age, and energy needed to reproduce it.
 * 
 * Genes are represented with segments that form the organism's body and are
 * drawn one behind the other. This basic set is repeated symmetry times in one
 * of two possible ways:
 * 
 * If mirroring is not applied, each repetition is rotated so they are distributed
 * in a uniform way around the central (starting) point.
 * 
 * When mirrored, segments are calculated as follows:
 * First, third, fifth and septh repetitions are applied a rotation of
 * 0, 180, 90, 270 degrees respectively.
 * 
 * Second repetition is like the first one with opposite x coordinate.
 * Fourth repetition is like the third one with opposite x coordinate.
 * Sixth repetition is like the fifth one with opposite y coordinate.
 * Eigth repetition is like the septh one with opposite y coordinate.
 */
public class GeneticCode implements Cloneable, Serializable, Writeable {
	/**
	 * The version of this class
	 */
	private static final long serialVersionUID = Utils.FILE_VERSION;
	/**
	 * Maximum number of segments that an organism can have.
	 * The genes number multiplied by the symmetry is the number
	 * of segments.
	 */
	protected static final int MAX_SEGMENTS = 64;
	/**
	 * Minimum number of segments that an organism can have.
	 * The genes number multiplied by the symmetry is the number
	 * of segments.
	 */
	protected static final int MIN_SEGMENTS = 4;
	/**
	 * Array with the genes. Every gene is represented by symmetry
	 * segments when drawing the organism.
	 */
	protected Gene[] genes;
	/**
	 * The symmetry used when drawing the organism. Possible values are
	 * 2, 4 or 8. 
	 */
	protected int symmetry;
	/**
	 * Mirroring indicates if symmetric segments are drawn in the same
	 * way than the original, only changing their angle, or if they are
	 * drawn like in a mirror. 0 = not mirrored, 1 = mirrored
	 */
	protected boolean mirror;
	/**
	 * Indicates if children will be placed moving towards the same
	 * direction than the father or to a different direction to move
	 * away.
	 */
	protected boolean disperseChildren;
	/**
	 * Minimum required energy to reproduce this genetic code.
	 * More genes, means more energy is needed. 
	 */
	protected int reproduceEnergy;
	/**
	 * The maximum time that the organism can be alive.
	 * At the moment, this is the same for all organisms.
	 */
	protected int maxAge;
	/**
	 * Returns the symmetry applied to organisms with this genetic code
	 * 
	 * @return  a value of 2, 4 or 8.
	 */
	public int getSymmetry() { return symmetry; }
        
//        //Used by extending classes to set base organism random symmetry.
//        public void setRandomSymmetry() {
//            randomSymmetry();
//        }
//        
        public void setSymmetry(int symmetry) {
            this.symmetry = symmetry;
        }
	/**
	 * Returns if mirroring is applied to organisms with this genetic code
	 * 
	 * @return  0 if no mirroring is applied, 1 if mirroring is applied.
	 */
	public boolean getMirror() { return mirror; }
//        
//        public void setRandomMirror() {
//		randomMirror();
//	}
//        
//        public void setMirror(boolean mirror) {
//            this.mirror = mirror;
//        }
	/**
	 * Returns if organisms with this genetic code will disperse their children or not.
	 * 
	 * @return  true if the organism disperses its children, false otherwise.
	 */
	public boolean getDisperseChildren() {
		return disperseChildren;
	}
	/**
	 * Returns the energy needed to replicate this genetic code.
	 * This energy is equal to 40 plus 3 for each segment.
	 * 
	 * @return  the energy needed to replicate this genetic code.
	 */
	public int getReproduceEnergy() {
		return reproduceEnergy;
	}
	/**
	 * Returns the maximum age that the organism can be.
	 * This is fixed at 30.  
	 * 
	 * @return  The maximum age that the organism can be.
	 */
	public int getMaxAge() {
		return maxAge;
	}
	/**
	 * Return a reference to a gene.
	 * 
	 * @param i  The index of the gene in the genetic code.
	 * @return  A reference to the gene
	 */
	public Gene getGene(int i) {
		return genes[i];
	}
        
        /**
	 * Return the number of genes of this code
	 * 
	 * @return  The number of genes
	 */
	public int getNGenes() {
		return genes.length;
	}
	/**
	 * Gives mirror a random value (0 or 1)
	 */
	protected void randomMirror() {
		mirror = Utils.random.nextBoolean();
	}
	/**
	 * Gives symmetry a random value (2, 4 or 8)
	 */
	protected void randomSymmetry() {
		symmetry = Utils.random.nextInt(8)+1;
	}
	/**
	 * Create a random genes array making sure that there will be more or equal than
	 * MIN_SEGMENTS and less or equal than MAX_SEGMENTS segments.
	 * It needs symmetry to have a valid value. 
	 */
	protected void randomGenes() {
		int nSegments = MIN_SEGMENTS + Utils.random.nextInt(MAX_SEGMENTS-MIN_SEGMENTS+1); // 4 - 64
		if (nSegments % symmetry != 0)
		    nSegments += (symmetry - (nSegments % symmetry));
		int nGenes = nSegments / symmetry;
		genes = new Gene[nGenes];
		for (int i=0; i<nGenes; i++) {
			genes[i] = new Gene();
			genes[i].randomize();
		}
	}
	/**
	 * Decide randomly if organisms with this genetic code will try to
	 * disperse their children or not.
	 */
	protected void randomDisperseChildren() {
		disperseChildren =  Utils.random.nextBoolean();
	}
	/**
	 * Calculates the energy required to reproduce this genetic code.
	 * This energy is 40 plus 3 for each segment.
	 */
	protected void calculateReproduceEnergy() {
		reproduceEnergy = 40 + 3 * genes.length * symmetry;
	}
	/**
	 * Creates a new random genetic code.
	 */
	public GeneticCode() {
		randomMirror(); 
		randomSymmetry();
		randomGenes();
		randomDisperseChildren();
		calculateReproduceEnergy();
		maxAge = Utils.getMAX_AGE();
	}	
	/**
	 * Creates a genetic code given its content.
	 * No check about the validity of the information is done.
	 * 
	 * @param genes  A list containing the genes
	 * @param symmetry  The symmetry that an organism with this genetic code will have.
	 * @param mirror  0 if the organism won't be mirrored, 1 if it will.
	 * @param disperseChildren  true if the organism will disperse its children.
	 */
	public GeneticCode(List<Gene> genes, int symmetry, boolean mirror, boolean disperseChildren) {
		int nGenes = genes.size();
		this.genes = new Gene[nGenes];
		genes.toArray(this.genes);
		this.maxAge = Utils.getMAX_AGE();
		this.mirror = mirror;
		this.symmetry = symmetry;
		this.disperseChildren = disperseChildren;
		calculateReproduceEnergy();
	}
	/**
	 * Creates a new genetic code based on the father genetic code but
	 * applying random mutations to it.
	 * 
	 * @param parentCode  The genetic code that this code will be based on.
	 */
	public GeneticCode(GeneticCode parentCode) {
		int i,j;
		int addedGene = -1;
		int removedGene = -1;
		int nGenes;
		boolean randomLength;
		boolean randomTheta;
		boolean randomColor;
		boolean randomBack;
		
		if (Utils.randomMutation())
			randomMirror();
		else
			mirror = parentCode.getMirror();
		if (Utils.randomMutation()) {
			// change symmetry
			if (Utils.random.nextInt(10) < 2)
				randomSymmetry();
			else
				symmetry = Utils.between(symmetry+Utils.randomSign(), 1, 8);
			nGenes = parentCode.getNGenes();
			if (nGenes * symmetry > MAX_SEGMENTS) {
				symmetry = parentCode.getSymmetry();
			}
		} else {
			// keep symmetry
			symmetry = parentCode.getSymmetry();
			if (Utils.randomMutation()) {
			// change number of segments
				if (Utils.random.nextBoolean()) {
				// increase segments
					if (parentCode.getNGenes() * parentCode.getSymmetry() >= MAX_SEGMENTS)
						nGenes = parentCode.getNGenes();
					else {
						nGenes = parentCode.getNGenes() + 1;
						addedGene = Utils.random.nextInt(nGenes);
					}
				} else {
				// decrease segments
					if (parentCode.getNGenes() * parentCode.getSymmetry() <= MIN_SEGMENTS
							|| parentCode.getNGenes() <= 1)
						nGenes = parentCode.getNGenes();
					else {
						nGenes = parentCode.getNGenes() - 1;
						removedGene = Utils.random.nextInt(parentCode.getNGenes());
					}
				}
			} else {
			// keep number of segments
				nGenes = parentCode.getNGenes();
			}
		}
		// Create genes
		genes = new Gene[nGenes];
		for (i=0,j=0; i<nGenes; i++,j++) {
			if (removedGene == j) {
				i--;
				continue;
			}
			if (addedGene == i) {
				genes[i] = new Gene();
				genes[i].randomize();
				j--;
				continue;
			}
			randomLength = randomTheta = randomColor = randomBack = false;
			if (Utils.randomMutation())
				randomLength = true;
			if (Utils.randomMutation())
				randomTheta = true;
			if (Utils.randomMutation())
				randomColor = true;
			if (Utils.randomMutation())
				randomBack = true;
			if (randomLength || randomTheta || randomColor || randomBack) {
				genes[i] = new Gene();
				if (randomLength)
					genes[i].randomizeLength();
				else
					genes[i].setLength(parentCode.getGene(j).getLength());
				if (randomTheta)
					genes[i].randomizeTheta();
				else
					genes[i].setTheta(parentCode.getGene(j).getTheta());
				if (randomColor)
					genes[i].randomizeColor();
				else
					genes[i].setPigment(parentCode.getGene(j).getPigment());
			} else
				genes[i] = parentCode.getGene(j);
		}

		if (Utils.randomMutation())
			randomDisperseChildren();
		else
			disperseChildren = parentCode.getDisperseChildren();
		calculateReproduceEnergy();
		maxAge = Utils.getMAX_AGE();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		GeneticCode newCode = null;
		try {
			newCode = (GeneticCode) super.clone();
			newCode.genes = new Gene[genes.length];
			for (int i=0; i<genes.length; i++)
				newCode.genes[i] = (Gene) genes[i].clone();
		} catch (CloneNotSupportedException e) {// We should never reach this
		}
		return newCode;
	}
	
	/**
	 * Draws a representation of this genetic code. This representation
	 * is equivalent to draw an adult organism with this genetic code and
	 * no rotation.
	 * 
	 * @param g  The place where the representation is drawn.
	 * @param width  The width of the available space. 
	 * @param height  The height of the available space.
	 */
	public void draw(Graphics g, int width, int height) {
		int[][] x0 = new int[symmetry][genes.length];
		int[][] y0 = new int[symmetry][genes.length];
		int[][] x1 = new int[symmetry][genes.length];
		int[][] y1 = new int[symmetry][genes.length];
		int maxX = 0;
		int minX = 0;
		int maxY = 0;
		int minY = 0;
		double scale = 1.0;
		Vector2D v = new Vector2D();
		Graphics2D g2 = (Graphics2D) g;

		for (int i=0; i<symmetry; i++) {
			for (int j=0; j<genes.length; j++) {
				v.setModulus(genes[j].getLength());
				if (j==0) {
					x0[i][j]=y0[i][j]=0;
					if (!mirror || i%2==0)
						v.setTheta(genes[j].getTheta()+i*2*Math.PI/symmetry);
					else {
						v.setTheta(genes[j].getTheta()+(i-1)*2*Math.PI/symmetry);
						v.invertX();
					}
				} else {
					x0[i][j] = x1[i][j-1];
					y0[i][j] = y1[i][j-1];
					if (!mirror || i%2==0)
						v.addDegree(genes[j].getTheta());
					else
						v.addDegree(-genes[j].getTheta());
				}
				
				x1[i][j] = (int) Math.round(v.getX() + x0[i][j]);
				y1[i][j] = (int) Math.round(v.getY() + y0[i][j]);
				
				maxX = Math.max(maxX, Math.max(x0[i][j], x1[i][j]));
				maxY = Math.max(maxY, Math.max(y0[i][j], y1[i][j]));
				minX = Math.min(minX, Math.min(x0[i][j], x1[i][j]));
				minY = Math.min(minY, Math.min(y0[i][j], y1[i][j]));
			}
		}
		
		g2.translate(width/2, height/2);
		if (maxX-minX > width)
			scale = (double)width/(double)(maxX-minX);
		if (maxY-minY > height)
			scale = Math.min(scale, (double)height/(double)(maxY-minY));
		g2.scale(scale, scale);
		
		for (int i=0; i<symmetry; i++) {
			for (int j=0; j<genes.length; j++) {
				x0[i][j]+=(-minX-maxX)/2;
				x1[i][j]+=(-minX-maxX)/2;
				y0[i][j]+=(-minY-maxY)/2;
				y1[i][j]+=(-minY-maxY)/2;
				g2.setColor(genes[j].getColor());
				g2.drawLine(x0[i][j],y0[i][j],x1[i][j],y1[i][j]);
			}
		}
	}
	@Override
	public boolean write(File f) 
        {
		try {
			BioXMLParser.writeGeneticCode(new PrintStream(f), this);
		} catch (FileNotFoundException ex) {
			System.err.println(ex.getLocalizedMessage());
			return false;
		}
		return true;
	}
	
	public Segment[] synthesize(AliveAgent agent)
        {
		Segment[] segments = new Segment[getNGenes() * getSymmetry()];
		SegmentFactory factory = SegmentFactory.getInstance();
		
		for (int i = 0; i < segments.length; i++)
			segments[i] = factory.createSegment(agent, getGene(i%getNGenes()).getPigment());
		return segments;
	}
       
}

