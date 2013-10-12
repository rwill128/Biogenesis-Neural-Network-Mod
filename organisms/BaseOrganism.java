package organisms;

import agents.Agent;
import agents.StatisticalAgent;
import agents.AliveAgent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JPopupMenu;

import world.World;

import biogenesis.Utils;

public abstract class BaseOrganism extends Rectangle implements Agent, StatisticalAgent, AliveAgent {
	/**
	 * 
	 */
	private static final long serialVersionUID = Utils.FILE_VERSION;

	/**
	 * X coordinate of this organim's center of gravity.
	 */
	private double posX;
	/**
	 * Y coordinate of this organim's center of gravity.
	 */
	private double posY;
	/**
	 * Rotation angle that this organism has at a given moment.
	 */
	private double theta = Utils.random.nextDouble() * Math.PI * 2d;
	/**
	 * Indicates if the organism is alive.
	 */
	private boolean alive = true;
	/**
	 * Number of children it has produced.
	 */
	private int nTotalChildren=0;
	/**
	 * Number of organism that has killed
	 */
	private int nTotalKills=0;
	/**
	 * Number of organism that has infected
	 */
	private int nTotalInfected=0;
	/**
	 * A reference to the genetic code of this organism
	 */
	private GeneticCode geneticCode;
	/**
	 * If this organism has been infected by a white segment, here we have the
	 * genetic code that this organism will reproduce.
	 */
	private GeneticCode infectedGeneticCode = null;
	/**
	 * Chemical energy stored by this organism
	 */
	private double energy;
	/**
	 * Indicates if it has moved at the last frame. If it has moved it is necessary
	 * to repaint it.
	 */
	private boolean moved = true;
	/**
	 * The place that this organism occupies at the last frame. If the organism
	 * moves, this rectangle must be painted too.
	 */
	private Rectangle lastFrame = new Rectangle();
	/**
	 * Identification number of this organism.
	 */
	private int id;
	/**
	 * Identification number of this organism's parent.
	 */
	private int parentId;
	/**
	 * Generation number
	 */
	private int generation;
	/**
	 * Number of frames of life of this organism
	 */
	private int age=0;
	/**
	 * Total mass of this organism. The mass is calculated as the sum of all segment lengths.
	 * Used to calculate the effect of collisions.
	 */
	private double mass = 0;

	/**
	 * Reference to the world where the organism lives.
	 */
	private World world;

	/**
	 * Color used to draw the organism when a collision occurs. We save the color that
	 * should be shown and the number of frames that it should be shown. If the number
	 * if frames is 0, each segment is shown in its color.
	 */
	private Color color;

	/**
	 * Number of frames in which the organism will be drawn in _color.
	 */
	private int framesColor = 0;

	/**
	 * Number of frame that need to pass between two reproductions, even if they are not
	 * successfully.
	 */
	private int timeToReproduce = 0;
	
	public BaseOrganism(World world, GeneticCode geneticCode, BaseOrganism parent) {
		this.world = world;
		this.geneticCode = geneticCode;
		if (parent != null) {
			parentId = parent.getParentId();
			generation = parent.getGeneration() + 1;
		} else {
			parentId = -1;
			generation = -1;
		}
	}
	
	public Color getColor() {
		Color c = null;
		
		if (framesColor > 0)
			c = color;
		return c;
	}
	
	@Override
	public void draw(Graphics g) {
		if (framesColor > 0) {
			// Draw all the organism in the same color
			g.setColor(color);
			framesColor--;
		}
	}
	
	@Override
	public void die(Agent killingOrganism) {
		alive = false;
		moved = true;
		world.decreasePopulation();
		if (killingOrganism instanceof StatisticalAgent) {
			StatisticalAgent stAgent = (StatisticalAgent) killingOrganism;
			stAgent.increaseKills();
			world.agentHasDied(this, stAgent);
		} else
			world.agentHasDied(this, null);
	}
	
	public boolean checkReproduce() {
		boolean canReproduce = false;
		// Substract one to the time needed to reproduce
		if (timeToReproduce > 0)
			timeToReproduce--;
		// Check if it can reproduce: it needs enough energy and to be adult
		if (timeToReproduce==0 && isAlive())
			canReproduce = true;
		return canReproduce;
	}
	
	@Override
	public double getPosX() {
		return posX;
	}
	@Override
	public double getPosY() {
		return posY;
	}
	@Override
	public double getTheta() {
		return theta;
	}
	
	@Override
	public void setTheta(double theta) {
		this.theta = theta;
	}
	
	/**
	 * Returns true if this organism is alive, false otherwise.
	 * 
	 * @return  true if this organism is alive, false otherwise.
	 */
	@Override
	public boolean isAlive() {
		return alive;
	}
	
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	/**
	 * Returns the number of children that this organism produced.
	 * 
	 * @return  The number of children that this organism produced.
	 */
	@Override
	public int getTotalChildren() {
		return nTotalChildren;
	}
	/**
	 * Returns the number of organisms killed by this organism.
	 * 
	 * @return  The number of organisms killed by this organism.
	 */
	@Override
	public int getTotalKills() {
		return nTotalKills;
	}
	/**
	 * Returns the number of organisms infected by this organism.
	 * 
	 * @return  The number of organisms infected by this organism.
	 */
	@Override
	public int getTotalInfected() {
		return nTotalInfected;
	}
	/**
	 * Returns a reference to this organism's genetic code.
	 * 
	 * @return  A reference to this organism's genetic code.
	 */
	@Override
	public GeneticCode getGeneticCode() {
		return geneticCode;
	}
	/**
	 * Returns the amount of chemical energy stored by this organism.
	 * 
	 * @return  The amount of chemical energy stored by this organism.
	 */
	@Override
	public double getEnergy() {
		return energy;
	}
	
	@Override
	public void setEnergy(double energy) {
		this.energy = energy;
	}
	
	@Override
	public boolean isMoved() {
		return moved;
	}
	
	@Override
	public Rectangle getLastFrame() {
		return lastFrame;
	}
	
	/**
	 * Returns the identification number of this organism.
	 * 
	 * @return  The identification number of this organism.
	 */
	@Override
	public int getId() {
		return id;
	}
	/**
	 * Returns the identification number of this organism's parent.
	 * 
	 * @return  The identification number of this organism's parent.
	 */
	@Override
	public int getParentId() {
		return parentId;
	}
	/**
	 * Returns the generation number of this organism.
	 * 
	 * @return  The generation number of this organism.
	 */
	@Override
	public int getGeneration() {
		return generation;
	}
	/**
	 * Returns the age of this organism.
	 * 
	 * @return  The age of this organism, in number of frames.
	 */
	@Override
	public int getAge() {
		return age;
	}
	
	@Override
	public void setAge(int age) {
		this.age = age;
	}
	
	/**
	 * Returns the total mass of this organism.
	 * 
	 * @return  The total mass of this organism calculated as the sum
	 * of all its segments length.
	 */
	@Override
	public double getMass() {
		return mass;
	}
	
	public void setMass(double mass) {
		this.mass = mass;
	}
	
	@Override
	public void revive() {
		alive = true;
		moved = true;
		age = 0;
	}
	
	@Override
	public JPopupMenu getPopupMenu() {
		if (isAlive())
			return MenuFactory.getInstance().getAlivePopupMenu();
		else
			return MenuFactory.getInstance().getDeadPopupMenu();
	}
	
	@Override
	public Rectangle getCurrentFrame() {
		return this;
	}
	
	@Override
	public void increaseChildren() {
		nTotalChildren++;
	}
	@Override
	public void increaseInfected() {
		nTotalInfected++;
	}
	@Override
	public void increaseKills() {
		nTotalKills++;
	}
	@Override
	public World getWorld() {
		return world;
	}
	/**
	 * Infects this organism with the genetic code of another organism.
	 * Tells the world about this event.
	 * 
	 * @param infectingOrganism  The organism that is infecting this one.
	 */
	@Override
	public void setInfectedGeneticCode(GeneticCode infectingGeneticCode) {
		infectedGeneticCode = infectingGeneticCode;
	}
	@Override
	public GeneticCode getInfectedGeneticCode() {
		return infectedGeneticCode;
	}
	@Override
	public void setColor(Color c) {
		if (c!=null) {
			color = c;
			framesColor = 10;
		} else {
			color = null;
			framesColor = -1;
		}
	}
	@Override
	public void setId(int newID) {
		id = newID;
	}
	/**
	 * Makes the organism spend an amount of energy using the
	 * respiration process.
	 * 
	 * @param q  The quantity of energy to spend.
	 * @return  true if the organism has enough energy and there are
	 * enough oxygen in the atmosphere, false otherwise.
	 */
	@Override
	public boolean useEnergy(double q) {
		if (energy < q) {
			return false;
		}
		double respiration = getWorld().getAtmosphere().respiration(q);
		energy -= respiration;
		if (respiration < q)
			return false;
		return true;
	}
	protected GeneticCode createChildGeneticCode() {
		GeneticCode inheritGeneticCode;
		
		// Create the inherited genetic code
		if (infectedGeneticCode != null)
			inheritGeneticCode = infectedGeneticCode;
		else
			inheritGeneticCode = geneticCode;
		return new GeneticCode(inheritGeneticCode);
	}
	
	/**
	 * Moves the organism and rotates it.
	 * 
	 * @param offsetx  displacement on the x axis.
	 * @param offsety  displacement on the y axis.
	 * @param offsettheta  rotation degree.
	 */
	public void offset(double offsetx, double offsety, double offsettheta) {
		posX += offsetx; posY += offsety; theta += offsettheta;
	}
	
	@Override
	public void setPosition(double posx, double posy) {
		posX = posx;
		posY = posy;
	}
	
	/**
	 * Executes the organism's movement for this frame.
	 * This includes segments upkeep and activation,
	 * movement, growth, collision detection, reproduction,
	 * respiration and death.
	 */
	@Override
	public boolean update() {
		initUpdate();
		// Apply segment effects for this frame.
		segmentsFrameEffects();
		// Apply rubbing effects
		rubbingFrameEffects();
		// Check if it can grow or shrink
		grow();
		moved = move();
		if (checkReproduce())
			if (reproduce())
				timeToReproduce = 20;
		checkMaxEnergy();
		// Maintenance
		return breath();
	}
	
	public void checkMaxEnergy() {
		// Check that it don't exceed the maximum chemical energy
		if (energy > 2*getGeneticCode().getReproduceEnergy())
			useEnergy(energy - 2*getGeneticCode().getReproduceEnergy());
	}
	public abstract boolean move();
	public abstract void grow();
	public abstract void rubbingFrameEffects();
	public abstract void segmentsFrameEffects();
	public void initUpdate() {
		moved = false;
		lastFrame.setBounds(this);
	}

	/**
	 * Realize the respiration process to maintain its structure.
	 * Aging is applied here too.
	 */
	public boolean breath() {
		if (isAlive()) {
			setAge(getAge() + 1);
			// Respiration process
			boolean canBreath = useEnergy(Math.min(mass / Utils.getSEGMENT_COST_DIVISOR(), energy));
			if ((getAge() >> 8) > getGeneticCode().getMaxAge() || !canBreath) {
				// It's dead, but still may have energy
				die(null);
			} else {
				if (energy <= Utils.tol) {
					alive = false;
					world.decreasePopulation();
					world.agentHasDied(this, null);
				}
			}
		} else {
			// The corpse slowly decays
			useEnergy(Math.min(energy, Utils.getDECAY_ENERGY()));
		}
		return energy > Utils.tol;
	}
	@Override
	public int getZOrder() {
		return 10;
	}
}
