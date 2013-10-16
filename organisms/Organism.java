/* Copyright (C) 2006-2010  Joan Queralt Molina
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


import genes.Gene;
import java.awt.Graphics;
import java.awt.geom.*;

import segments.Segment;
import segments.YellowSegment;
import world.World;
import auxiliar.Vector2D;
import biogenesis.Utils;
import geneticcodes.GeneticCode;

/**
 * This class implements an organism.
 * The body of the organism is drawn inside the Rectangle from which it inherits.
 */
public class Organism extends SegmentBasedOrganism {
	/**
	 * The version of this class
	 */
	private static final long serialVersionUID = Utils.FILE_VERSION;
	protected static transient Vector2D v = new Vector2D();
	/**
	 * Growth ratio of the organism. Used to calculate segments when the organism is not
	 * fully grown.
	 */
	protected int growthRatio = 16;
	/**
	 * Indicates if the organism has grown at the last frame. If it has grown it is
	 * necessary to recalculate its segments. Can be 1, -1 or 0.
	 */
	protected int hasGrown;
	
	protected int symmetry;
	
	protected boolean mirror;
	
	protected int nChildren = 1;
	
	public Organism(SegmentBasedOrganism parent) {
		super(parent);
		initialize();
	}
	/**
	 * Basic constructor. Doesn't initialize it: use {@link randomCreate}
	 * or {@link inherit} to do this.
	 * 
	 * @param world  A reference to the world where this organism is in.
	 */
	public Organism(World world) {
		this(world, new GeneticCode());
	}
	
	/**
	 * Construct an organism with a given genetic code. Doesn't initialize it:
	 * use {@link pasteOrganism} to do it. Use {@link World.addOrganism} to add
	 * it to the world.
	 * 
	 * @param world  A reference to the world where this organism is in.
	 * @param geneticCode  A reference to the genetic code of this organism.
	 */
	public Organism(World world, GeneticCode geneticCode) {
		super(world, geneticCode, null);
		initialize();
	}
	
	public void initialize() {
		symmetry = getGeneticCode().getSymmetry();
		mirror = getGeneticCode().getMirror();
		updateBody();
		for (Segment s : getSegments())
			if (s instanceof YellowSegment)
				nChildren++;
		nChildren = Utils.between(nChildren, 1, 8);
	}
	
	
	
	@Override
	public void draw(Graphics g, int width, int height) {
                getGeneticCode().draw(g, width, height);
	}
	/**
	 * If its the time for this organism to grow, calculates its new segments and speed.
	 * An alive organism can grow once every 8 frames until it gets its maximum size.
	 */
	public void grow() {
		double energy = getEnergy();
		if (growthRatio > 1 && (getAge() & 0x07) == 0x07 && isAlive() && energy >= getMass()/10) {
			growthRatio--;
			double m = getMass();
			double I = getInertia();
			updateBody();
			// Cynetic energy is constant. If mass changes, speed must also change.
			m = Math.sqrt(m/getMass());
			setDx(getDx() * m);
			setDy(getDy() * m);
			setDtheta(getDtheta() * Math.sqrt(I/getInertia()));
			hasGrown = 1;
		} else {
			if (growthRatio < 15 && energy < getMass()/12) {
				growthRatio++;
				double m = getMass();
				double I = getInertia();
				updateBody();
				// Cynetic energy is constant. If mass changes, speed must also change.
				m = Math.sqrt(m/getMass());
				setDx(getDx() * m);
				setDy(getDy() * m);
				setDtheta(getDtheta() * Math.sqrt(I/getInertia()));
				hasGrown = -1;
			} else
				hasGrown = 0;
		}
	}
	
	public boolean collisionDetection() {
		boolean collision = worldBordersCollition();
		// Collision detection with other organisms.
		if (getWorld().checkHit(this) != null)
			collision = true;
		
		
		return collision;
		
	}
	public boolean worldBordersCollition() {
		boolean collision = false;
		int separateFromBorder = (hasGrown>0 ? 1 : 0);
		// Check it is inside the world
		int insideWorld = getWorld().isInsideWorld(this);
					
		if ((insideWorld & World.RIGHT_WORLD) != 0 || (insideWorld & World.LEFT_WORLD) != 0)
			setDx(-(getDx()+separateFromBorder));
		if ((insideWorld & World.UP_WORLD) != 0 || (insideWorld & World.DOWN_WORLD) != 0)
			setDy(-(getDy()+separateFromBorder));
		if (insideWorld != World.INSIDE_WORLD) {
			setDtheta(0);
			collision = true;
		}
		return collision;
	}
	
	
	public boolean move() {
		// Movement
		boolean moved = false;
		double dx=getDx(), dy=getDy(), dtheta=getDtheta();
		offset(dx, dy, dtheta);
		calculateBounds(hasGrown!=0);
		
		if (hasGrown!=0 || dx!=0 || dy!=0 || dtheta!=0) {
			moved = true;
			if (collisionDetection()) {
				// If there is a collision, undo movement.
				moved = false;
				offset(-dx, -dy, -dtheta);
				if (hasGrown!=0) {
					growthRatio+=hasGrown;
					updateBody();
				}
				calculateBounds(hasGrown!=0);
			}
		}
		return moved;
	}
	
	
	@Override
	public boolean checkReproduce() {
		boolean canReproduce = super.checkReproduce();
		// Check if it can reproduce: it needs enough energy and to be adult
		if (canReproduce && growthRatio == 1 && getEnergy() > getGeneticCode().getReproduceEnergy() + 
				Utils.getYELLOW_ENERGY_CONSUMPTION()*(nChildren-1))
			canReproduce = true;
		else
			canReproduce = false;
		return canReproduce;
	}
	/**
	 * Tries to find a spare place near its parent for this organism and place it.
	 * It also generates an identification number for the organism if it can be placed
	 * somewhere and substracts its energy from its parent's energy.
	 * 
	 * @return  true if a suitable place has been found, false if not.
	 */
	private boolean placeNear(SegmentBasedOrganism parent) {
		int nPos = Utils.random.nextInt(8);
		// Try to put it in any possible position, starting from a randomly chosen one.
		for (int nSide = 0; nSide < 8; nSide++) {
			// Calculate candidate position
			setPosition(parent.getPosX() + (parent.width / 2 + width / 2+ 1) * Utils.side[nPos][0],
					parent.getPosY() + (parent.height / 2 + height / 2 + 1) * Utils.side[nPos][1]);
			// Check this position is inside the world.
			if (getWorld().isInsideWorld(this) == World.INSIDE_WORLD) {
				// Check that it doesn't overlap with other organisms.
				if (getWorld().fastCheckHit(this) == null) {
					if (parent.getGeneticCode().getDisperseChildren()) {
						setDx(Utils.side[nPos][0]);
						setDy(Utils.side[nPos][1]);
					} else {
						setDx(parent.getDx());
						setDy(parent.getDy());
					}
					return true;
				}
			}
			nPos = (nPos + 1) % 8;
		}
		// It can't be placed.
		return false;
	}
	/**
	 * Makes this organism reproduce. It tries to create at least one
	 * child and at maximum 8 (depending on the number of yellow segments
	 * of the organism) and put them in the world.
	 */
	@Override
	public boolean reproduce() {
		boolean hasReproduced = false;
		Organism newOrg;
		double reproduceEnergy = getInfectedGeneticCode() != null ? getInfectedGeneticCode().getReproduceEnergy() : 
				getGeneticCode().getReproduceEnergy();
		
		for (int i=0; i < nChildren; i++) {
			newOrg = new Organism(this);
			newOrg.setEnergy(Math.min(reproduceEnergy / (nChildren+1), getEnergy()));
			
			if (getEnergy() >= newOrg.getEnergy()+Utils.getYELLOW_ENERGY_CONSUMPTION() &&
					newOrg.placeNear(this)) {
				// It can be created
				increaseChildren();
				setEnergy(getEnergy() - newOrg.getEnergy());
				if (i!=0)
					useEnergy(Utils.getYELLOW_ENERGY_CONSUMPTION());
				getWorld().addAgent(newOrg,this);
				setInfectedGeneticCode(null);
			}
			hasReproduced = true;
			
		}
		return hasReproduced;
	}
	@Override
	public void setPosition(double posx, double posy) {
		super.setPosition(posx, posy);
		calculateBounds(true);
	}
	
	public boolean spin() {
		if (growthRatio < 16) {
			growthRatio++;
			setTheta(getTheta() + (Math.PI / 6));
			updateBody();
			calculateBounds(true);
			return true;
		}
		return false;
	}
	
	protected void recalculateSize() {
		double newMass = 0;
		double inertia = 0;
		for (Segment s : getSegments()) {
			// calculate points distance of the origin and modulus
			s.updateMass();
			newMass += s.getMass();
			// add the effect of this segment, following the parallel axis theorem
			inertia += s.getInertia();
		}
		setInertia(inertia);
		setMass(newMass);
	}
	
	/**
	 * Translates the genetic code of this organism to its segments representation in the world.
	 * Also, calculates some useful information like segments length, inertia, etc.
	 * This method must be called when an organism is firstly displayed on the world and every
	 * time it changes its size.
	 * inherit, randomCreate and pasteOrganism are the standard ways to add an organism to a world
	 * and they already call this method.
	 */
	protected void updateBody() {
		int i,j,segment=0;
		GeneticCode geneticCode = getGeneticCode();
		Segment[] segments = getSegments();
		Gene gene;
		int sequence = segments.length / symmetry;
		
		for (i=0; i<symmetry; i++) {
			for (j=0; j<sequence; j++,segment++) {
				// Here, we take the vector that forms the segment, scale it depending on
				// the relative size of the organism and rotate it depending on the
				// symmetry and mirroring.
				gene = geneticCode.getGene(j);
				v.setModulus(gene.getLength()/Utils.scale[growthRatio-1]);
				if (j==0) {
					segments[segment].setStartingPoint(0, 0);
					if (!mirror || i%2==0)
						v.setTheta(gene.getTheta()+i*2*Math.PI/symmetry);
					else {
						v.setTheta(gene.getTheta()+(i-1)*2*Math.PI/symmetry);
						v.invertX();
					}
				} else {
					segments[segment].setStartingPoint(segments[segment-1].getEndingPoint());
					if (!mirror || i%2==0)
						v.addDegree(gene.getTheta());
					else
						v.addDegree(-gene.getTheta());
				}
				// Apply the vector to the starting point to get the ending point.
				segments[segment].setEndingPoint((int) Math.round(v.getX() +
						segments[segment].getStartingPoint().x),
						(int) Math.round(v.getY() + segments[segment].getStartingPoint().y));
			}
		}
		recalculateSize();
		
	}
	@Override
	protected double pushX(Point2D p) {
		return hasGrown*(p.getX()-getPosX())/10d;
	}
	@Override
	protected double pushY(Point2D p) {
		return hasGrown*(p.getY()-getPosY())/10d;
	}
	
	
}
