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
package world;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Collections;
import java.util.Set;
import java.io.*;
import java.awt.*;

import biogenesis.Utils;
import biogenesis.VisibleWorld;

import organisms.Agent;
import organisms.AliveAgent;
import organisms.AliveAgentFactory;
import organisms.BaseOrganism;
import organisms.GeneticCode;
import organisms.MovingAgent;
import organisms.Organism;
import organisms.StatisticalAgent;

/**
 * This class contains all the information needed to run a world:
 * the organisms, the substances and the biological corridors. It
 * also contains a reference to the visible part of the world,
 * {@link VisibleWorld}, and its statistics {@link WorldStatistics}.
 * There are methods to do all needed operations to the world: manage
 * organisms and substances.
 */
public class World implements Serializable {
	/**
	 * Version number of the class
	 */
	private static final long serialVersionUID = Utils.FILE_VERSION;
	
	private Atmosphere atmosphere = new Atmosphere();
	/**
	 * World width
	 */
	private int width;
	/**
	 * World height
	 */
	private int height;
	
	private long time;
	/**
	 * A list of the agents in the world, even dead ones.
	 * Note that this must be a synchronized list so it is mandatory to
	 * manually synchronize when iterating over it. 
	 */
	private List<Agent> agents;
	private List<Agent> removedAgents;
	private List<Agent> addedAgents;
	/**
	 * Number of living organisms in the world
	 */
	private int population = 0;
	/**
	 * The next identification number that will be assigned to an organism
	 * in this world
	 */
	private int nextId;
	/**
	 * Reference to the object that keeps track of all world statistics. 
	 */
	private WorldStatistics worldStatistics;
	
	private transient Set<WorldPaintListener> paintListeners = new HashSet<WorldPaintListener>();
	private transient Set<WorldEventListener> eventListeners = new HashSet<WorldEventListener>();
	/**
	 * Called by the JRE when an instance of this class is read from a file
	 * 
	 * @param in  The stream from where the object comes from
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	private void readObject(ObjectInputStream in)
    throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		paintListeners = new HashSet<WorldPaintListener>();
		eventListeners = new HashSet<WorldEventListener>();
	}
	
	/**
	 * Get this world's statistics.
	 * 
	 * @return This world's statistics
	 */
	public WorldStatistics getWorldStatistics() {
		return worldStatistics;
	}
	
	/**
	 * Get an unmodifiable view of this world's agents.
	 * 
	 * @return An unmodifiable list containing all agents in this world.
	 */
	public List<Agent> getAgents() {
		return Collections.unmodifiableList(agents);
	}
	
	/**
	 * Get this world's atmosphere.
	 * 
	 * @return This world's atmosphere
	 */
	public Atmosphere getAtmosphere() {
		return atmosphere;
	}
	/**
	 * Finds an organism that has the given coordinates inside its bounding box and
	 * returns a reference to it. If more than on organism satisfies this condition,
	 * if possible, an alive organism is returned. If no organism satisfies this
	 * condition, this method returns null.
	 * 
	 * @param x  X coordinate
	 * @param y  Y coordinate
	 * @return  An organism with the point (x,y) inside its bounding box, or null
	 * if such organism doesn't exist.
	 */
	public AliveAgent findAliveAgentFromPosition(int x, int y) {
		AliveAgent deadAgent = null;
		AliveAgent aa;
		
		for (Agent o : agents) {
			if (o instanceof AliveAgent) {
				aa = (AliveAgent) o;
				if (aa.getCurrentFrame().contains(x,y)) {
					if (aa.isAlive())
						return aa;
					deadAgent = aa;
				}
			}
		}
		return deadAgent;
	}
	/**
	 * Returns the world's width.
	 * 
	 * @return  The world's width.
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * Returns the world's height.
	 * 
	 * @return  The world's height.
	 */
	public int getHeight() {
		return height;
	}
	/**
	 * Returns the actual time.
	 * 
	 * @return  The actual time.
	 */
	public long getTime() {
		return worldStatistics.getTime();
	}
	/**
	 * Returns the number of corpses that still have energy and drawn in the
	 * world.
	 * 
	 * @return  The number of corpses in the world.
	 */
	public int getNCorpses() {
		return agents.size() - population;
	}
	/**
	 * Returns the number of alive organisms that populate the world.
	 * 
	 * @return  The number of alive organisms in the world.
	 */
	public int getPopulation() {
		return population;
	}
	/**
	 * Increase the population counter by one.
	 * 
	 * This method should be called every time a new organism is
	 * created. Normally, it is called by addOrganism, but in some
	 * cases it may be used directly.
	 */
	public void increasePopulation() {
		population++;
		worldStatistics.eventPopulationIncrease(population);
	}
	/**
	 * Decrease the population counter by one.
	 * 
	 * This method should be called every time an organism dies.
	 * Normally, it is called by Organism.die or Organism.breath,
	 * but in some cases it may be used directly.
	 */
	public void decreasePopulation() {
		population--;
		worldStatistics.eventPopulationDecrease(population);
	}
	/**
	 * Constructor of the World class. All internal structures are initialized and
	 * the world's size is obtained from parameters.
	 * 
	 * @param visibleWorld  A reference to the visual representation of this world.
	 */
	public World() {
		width = Utils.getWORLD_WIDTH();
		height = Utils.getWORLD_HEIGHT();
		agents = new ArrayList<Agent>(Utils.getORGANISMS_VECTOR_SIZE());
		addedAgents = new ArrayList<Agent>();
		removedAgents = new ArrayList<Agent>();
		worldStatistics = new WorldStatistics();
	}
	/**
	 * Populate the word with a new set of organisms.
	 * This is used to destroy a world and create a new one.
	 */
	public void genesis(AliveAgentFactory factory) {
		AliveAgent[] newAgents;
		// Reset atributs
		nextId = 0;
		population = 0;
		agents.clear();
		atmosphere = new Atmosphere();
		// Initialize size
		width = Utils.getWORLD_WIDTH();
		height = Utils.getWORLD_HEIGHT();
		for (WorldEventListener listener : eventListeners)
			listener.eventGenesis();
		// Create statistics
		worldStatistics = new WorldStatistics();
		// Create organisms
		newAgents = factory.initialPopulation(this);
		for (AliveAgent agent : newAgents) {
			double energy = Math.min(Utils.getINITIAL_ENERGY(), atmosphere.getCO2());
			// Only add the new organism if it can be placed in the world
			if (placeRandom(agent)) {
				agent.setEnergy(energy);
				addAgent(agent, null);
				atmosphere.decreaseCO2(energy);
				atmosphere.addO2(energy);
			}
		}
	}
	
	/**
	 * Tries to find a spare place in the world for this organism and place it.
	 * It also generates an identification number for the organism if it can be placed
	 * somewhere.
	 * 
	 * @return  true if a suitable place has been found, false if not.
	 */
	private boolean placeRandom(AliveAgent o) {
		int posx, posy;
		/* We try to place the organism in 12 different positions. If all of them
		 * are occupied, we return false.
		 */
		for (int i=12; i>0; i--) {
			posx = Utils.random.nextInt(width+20)-10;
			posy = Utils.random.nextInt(height+20)-10;
			o.setPosition(posx,posy);
			// Check that the position is not occupied.
			if (isInsideWorld(o)==INSIDE_WORLD && fastCheckHit(o) == null) {
				return true;
			}
		}
		// If we get here, we haven't find a place for this organism.
		return false;
	}
	/**
	 * Places the organism at the specified position in the world and initializes its
	 * variables. The organism must has an assigned genetic code.
	 * 
	 * @param posx  The x coordinate of the position in the world we want to put this organism.
	 * @param posy  The y coordinate of the position in the world we want to put this organism.
	 * @return  true if there were enough space to put the organism, false otherwise.
	 */
	public boolean pasteOrganism(AliveAgent o, int posx, int posy) {
		o.setPosition(posx, posy);
		// Check that the position is inside the world
		// Check that the organism will not overlap other organisms
		if (isInsideWorld(o) == INSIDE_WORLD && checkHit(o) == null) {
			addAgent(o, null);
			atmosphere.decreaseCO2(o.getEnergy());
			atmosphere.addO2(o.getEnergy());
			return true;
		}
		// It can't be placed		
		return false;
	}
	/**
	 * Checks if the organism is inside the world. If it is not, calculates its
	 * speed after the collision with the world border.
	 * This calculation should be updated to follow the parallel axis theorem, just
	 * like the collision between two organisms.
	 * 
	 * @return  true if the organism is inside the world, false otherwise.
	 */
	public int isInsideWorld(Agent agent) {
		Rectangle r = agent.getCurrentFrame();
		int result = INSIDE_WORLD;
		if (r.getX() < 0)
			result |= LEFT_WORLD;
		if (r.getX() + r.getWidth() >= width)
			result |= RIGHT_WORLD;
		if (r.getY() < 0)
			result |= UP_WORLD;
		if (r.getY() + r.getHeight() >= height)
			result |= DOWN_WORLD;
		return result;
	}
	public final static int INSIDE_WORLD = 0;
	public final static int RIGHT_WORLD = 1;
	public final static int LEFT_WORLD = 2;
	public final static int UP_WORLD = 4;
	public final static int DOWN_WORLD = 8;
	
	public boolean createOrganismAtPosition(int x, int y) {
		double energy = Math.min(Utils.getINITIAL_ENERGY(), atmosphere.getCO2());
		BaseOrganism o = new Organism(this);
		o.setEnergy(energy);
		return pasteOrganism(o, x, y);
	}
	
	public boolean createOrganismAtPosition(GeneticCode gc, int x, int y) {
		double energy = Math.min(Utils.getINITIAL_ENERGY(), atmosphere.getCO2());
		BaseOrganism o = new Organism(this, gc);
		o.setEnergy(energy);
		return pasteOrganism(o, x, y);
	}
	/**
	 * Draws all visible components of the world to a graphic context.
	 * This includes organisms and corridors. Called from {@link biogenesis.VisibleWorld.paintComponents}.
	 * 
	 * @param g  The graphic context to draw to.
	 */
	public void draw(Graphics g) {			
		for (Agent b : agents)
			b.draw(g);
	}
	/**
	 * Determines the world's region that needs to be repainted in the associated
	 * {@link biogenesis.VisualWorld} and instructs it to do it.
	 * 
	 * For optimization, only paints organisms that has moved in the last frame.
	 */
	public void setPaintingRegion() {
		MovingAgent ma;
		for (Agent o : agents) {
			if (o instanceof MovingAgent) {
				ma = (MovingAgent) o;
				if (ma.isMoved()) {
					for (WorldPaintListener listener : paintListeners) {
						listener.repaint(ma.getLastFrame());
						listener.repaint(ma.getCurrentFrame());
					}
				}
			}
		}
	}
	/**
	 * Executes a frame. This method iterates through all objects in the world
	 * and make them to execute a movement. Here is the place where all action
	 * occurs: organism movement, interaction, birth and death.
	 * 
	 * Additionally, every 20 frames the {@link InfoWindow} is updated, if showed,
	 * and every 256 frames the time counter is increased by 1.
	 */
	public void time() {
		time++;
		
		for (Agent o : agents) {
			if (!o.update()) {
				// Organism has no energy -> remove from the list
				removeAgent(o);
			}
		}
		
		agents.removeAll(removedAgents);
		for (Agent a : addedAgents)
			insertAgent(a);
		removedAgents.clear();
		addedAgents.clear();
		// paint again where needed
		setPaintingRegion();
		
		if (time % 256 == 0) {
			worldStatistics.eventTime(population, atmosphere.getO2(), atmosphere.getCO2());
		}
	}
	
	public boolean removeAgent(Agent agent) {
		boolean removed = agents.contains(agent);
		removedAgents.add(agent);
		for (WorldPaintListener listener : paintListeners) {
			listener.repaint(agent.getCurrentFrame());
			if (agent instanceof MovingAgent)
				listener.repaint(((MovingAgent) agent).getLastFrame());
		}
		
		if (agent instanceof AliveAgent) {
			AliveAgent aliveAgent = (AliveAgent) agent;
			for (WorldEventListener listener : eventListeners)
				listener.eventAgentRemoved(aliveAgent);
			if (aliveAgent.isAlive())
				decreasePopulation();
		}
		
		return removed;
	}
	/**
	 * Checks if an organism has a high probability of being in touch with
	 * another organism. This is done by checking if the bounding rectangles
	 * of both organisms overlaps. 
	 * 
	 * @param b1  The organism that is being checked.
	 * @return  The organism which bounding rectangle is touching the bounding
	 * rectangle of {@code b1} or null if there is no such organism. 
	 */
	public Agent fastCheckHit(Agent b1) {
		for (Agent o : agents) {
			if (b1 != o) {
				if (b1.getCurrentFrame().intersects(o.getCurrentFrame())) {
					return b1;
				}
			}
		}
		return null;
	}
	/**
	 * Checks if an organism hits another organism.
	 * 
	 * @param agent  The organism to check.
	 * @return  The organism that is touching {@code org1} or null if not such
	 * organism exists. 
	 */
	public Agent checkHit(Agent agent) {
		for (Agent ag : agents) {
			if (agent != ag) {
				// First check if the bounding boxes intersect
				if (agent.getCurrentFrame().intersects(ag.getCurrentFrame())) {
					// Check if they are touching
					if (agent.contact(ag))
						return ag;
				}
			}
		}
		return null;
	}
	
	private void insertAgent(Agent newAgent) {
		int left = 0;
		int right = agents.size()-1;
		int middle=0;
		int z = newAgent.getZOrder();
		int zaux;
		
		while (left <= right) {
			middle = (left+right)/2;
			zaux = agents.get(middle).getZOrder();
			if (z < zaux) {
				right = middle-1;
			} else if (z > zaux) {
				left = middle+1;
			} else { // z==zaux
				left = right+1;
			}
		}
		agents.add(middle, newAgent);
	}
	
	/**
	 * Adds an organism to the world. Once added, the new organism will move at every
	 * frame and interact with other organisms in the world.
	 * 
	 * Updates world statistics, population and the {@link biogenesis.InfoWindow}, if necessary.
	 * 
	 * @param newAgent  The organism that needs to be added.
	 * @param parent  The parent of the added organism, or null if there is no parent.
	 */
	public void addAgent(Agent newAgent, StatisticalAgent parent) {
		if (!agents.contains(newAgent)) {
			addedAgents.add(newAgent);
			if (newAgent instanceof AliveAgent) {
				AliveAgent aliveAgent = (AliveAgent) newAgent;
				aliveAgent.setId(nextId++);
				for (WorldEventListener listener : eventListeners)
					listener.eventAliveAgentAdded(aliveAgent, parent);
				if (parent != null) {
					worldStatistics.eventOrganismBorn(aliveAgent, parent);
				}
				increasePopulation();
				worldStatistics.eventOrganismCreated();
			}
		}
	}
	
	/**
	 * Informs the world of a defunction event. This will update statistics.
	 * 
	 * @param dyingOrganism  The organism that has just died.
	 * @param killingOrganism  The organism that has killed the other organism, if any.
	 */
	public void organismHasDied(AliveAgent dyingOrganism, StatisticalAgent killingOrganism) {
		worldStatistics.eventOrganismDie(dyingOrganism, killingOrganism);
		for (WorldEventListener listener : eventListeners)
			listener.eventAgentHasDied(dyingOrganism, killingOrganism);
	}
	/**
	 * Informs the world of an infection event. This will update statistics.
	 * 
	 * @param infectedOrganism  The organism that has just been infected.
	 * @param infectingOrganism  The organism that has infected the other organism.
	 */
	public void organismHasBeenInfected(AliveAgent infectedOrganism, StatisticalAgent infectingOrganism) {
		worldStatistics.eventOrganismInfects(infectedOrganism, infectingOrganism);
		for (WorldEventListener listener : eventListeners)
			listener.eventAgentHasBeenInfected(infectedOrganism, infectingOrganism);
	}
	
	public void addWorldPaintListener(WorldPaintListener listener) {
		paintListeners.add(listener);
	}
	
	public void deleteWorldPaintListener(WorldPaintListener listener) {
		paintListeners.remove(listener);
	}
	
	public void deleteWorldPaintListeners() {
		paintListeners.clear();
	}
	
	public void addWorldEventListener(WorldEventListener listener) {
		eventListeners.add(listener);
	}
	
	public void deleteWorldEventListener(WorldEventListener listener) {
		eventListeners.remove(listener);
	}
	
	public void deleteWorldEventListeners() {
		eventListeners.clear();
	}
}
