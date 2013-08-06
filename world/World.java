/* Copyright (C) 2006-2013  Joan Queralt Molina
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

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import organisms.Agent;
import organisms.AliveAgent;
import organisms.AliveAgentAbstractFactory;
import organisms.BaseOrganism;
import organisms.GeneticCode;
import organisms.MovingAgent;
import organisms.Organism;
import organisms.StatisticalAgent;

import biogenesis.Utils;

/**
 * This class contains all the information needed to run a world:
 * a list of the agents in it, its atmosphere, and its statistics.
 * 
 * There are methods to add and remove agents, to make time pass,
 * and to detect collisions between agents.
 */
public class World implements Serializable {
	/**
	 * Version number of the class
	 */
	private static final long serialVersionUID = Utils.FILE_VERSION;
	/**
	 * Constant returned by isInsideWorld() if an agent is completely
	 * inside the world.
	 */
	public final static int INSIDE_WORLD = 0;
	/**
	 * Constant returned by isInsideWorld() if an agent is at least
	 * partially outside the world, at the right.
	 */
	public final static int RIGHT_WORLD = 1;
	/**
	 * Constant returned by isInsideWorld() if an agent is at least
	 * partially outside the world, at the left.
	 */
	public final static int LEFT_WORLD = 2;
	/**
	 * Constant returned by isInsideWorld() if an agent is at least
	 * partially outside the world, at the top.
	 */
	public final static int UP_WORLD = 4;
	/**
	 * Constant returned by isInsideWorld() if an agent is at least
	 * partially outside the world, at the bottom.
	 */
	public final static int DOWN_WORLD = 8;
	/**
	 * This world's atmosphere
	 */
	private Atmosphere atmosphere = new Atmosphere();
	/**
	 * World width in pixels
	 */
	private int width;
	/**
	 * World height in pixels
	 */
	private int height;
	/**
	 * Tick counter for this world.
	 */
	private long tick;
	/**
	 * A list of the agents in the world. 
	 */
	private List<Agent> agents;
	/**
	 * List of agents to be removed at the end of one iteration.
	 */
	private List<Agent> removedAgents;
	/**
	 * List of agents to be added at the end of one iteration.
	 */
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
	private AliveAgentAbstractFactory aliveAgentFactory;
	/**
	 * The set of WorldPaintListener that will be notified of the parts of the world
	 * that need repainting after every iteration.
	 */
	private transient Set<WorldPaintListener> paintListeners = new HashSet<WorldPaintListener>();
	/**
	 * The set of WorldEventListener that will be notified when an event occurs in this world,
	 * such the addition of a new agent.
	 */
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
	 * Finds an alive agent that has the given coordinates inside its bounding box and
	 * returns a reference to it. If more than one agent satisfies this condition,
	 * if possible, an agent that's alive is returned. If no agent satisfies this
	 * condition, this method returns null.
	 * 
	 * @param x  X coordinate
	 * @param y  Y coordinate
	 * @return  An alive agent with the point (x,y) inside its bounding box, or null
	 * if such agent doesn't exist.
	 */
	public AliveAgent findAliveAgentFromPosition(int x, int y) {
		AliveAgent deadAgent = null;
		AliveAgent aa;
		
		for (Agent a : agents) {
			if (a instanceof AliveAgent) {
				aa = (AliveAgent) a;
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
	 * Normally, it is called by BaseOrganism.die or BaseOrganism.breath,
	 * but in some cases it may be used directly.
	 */
	public void decreasePopulation() {
		population--;
		worldStatistics.eventPopulationDecrease(population);
	}
	/**
	 * Constructor of the World class. All internal structures are initialized and
	 * the world's size is obtained from preferences.
	 */
	public World(AliveAgentAbstractFactory aliveAgentFactory) {
		width = Utils.getWORLD_WIDTH();
		height = Utils.getWORLD_HEIGHT();
		agents = new ArrayList<Agent>(Utils.getORGANISMS_VECTOR_SIZE());
		addedAgents = new ArrayList<Agent>();
		removedAgents = new ArrayList<Agent>();
		worldStatistics = new WorldStatistics();
		this.aliveAgentFactory = aliveAgentFactory;
	}
	/**
	 * Populate the word with a new set of organisms.
	 * This is used to destroy a world and create a new one.
	 * 
	 * @param factory  The AliveAgentFactory that will create
	 * this world's initial population. Different kinds of
	 * factory can populate the world with different types of
	 * agents.
	 */
	public void genesis() {
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
		newAgents = aliveAgentFactory.initialPopulation(this);
		for (AliveAgent agent : newAgents) {
			double energy = Math.min(Utils.getINITIAL_ENERGY(), atmosphere.getCO2());
			// Only add the new agent if it can be placed in the world
			if (placeRandom(agent)) {
				agent.setEnergy(energy);
				addAgent(agent, null);
				atmosphere.decreaseCO2(energy);
				atmosphere.addO2(energy);
			}
		}
	}
	
	/**
	 * Tries to find a spare place in the world for the given alive agent and place it.
	 * It also generates an identification number for the agent if it can be placed
	 * somewhere.
	 * 
	 * @param aa  The alive agent to place.
	 * @return  true if a suitable place has been found, false if not.
	 */
	private boolean placeRandom(AliveAgent aa) {
		int posx, posy;
		/* We try to place the agent in 12 different positions. If all of them
		 * are occupied, we return false.
		 */
		for (int i=12; i>0; i--) {
			posx = Utils.random.nextInt(width+20)-10;
			posy = Utils.random.nextInt(height+20)-10;
			aa.setPosition(posx,posy);
			// Check that the position is not occupied.
			if (isInsideWorld(aa)==INSIDE_WORLD && fastCheckHit(aa) == null) {
				return true;
			}
		}
		// If we get here, we haven't find a place for this agent.
		return false;
	}
	/**
	 * Places the given alive agent at the specified position, if possible.
	 * 
	 * @param aa  The alive agent to place.
	 * @param posx  The x coordinate of the position in the world where we want to put this organism.
	 * @param posy  The y coordinate of the position in the world where we want to put this organism.
	 * @return  true if there were enough space to put the organism, false otherwise.
	 */
	public boolean placeAt(AliveAgent aa, int posx, int posy) {
		aa.setPosition(posx, posy);
		// Check that the position is inside the world
		// Check that the organism will not overlap other organisms
		if (isInsideWorld(aa) == INSIDE_WORLD && checkHit(aa) == null) {
			addAgent(aa, null);
			atmosphere.decreaseCO2(aa.getEnergy());
			atmosphere.addO2(aa.getEnergy());
			return true;
		}
		// It can't be placed		
		return false;
	}
	/**
	 * Checks if an agent is inside the world.
	 * 
	 * @param agent  The agent to check. It must be an agent contained in this world.
	 * @return  INSIDE_WORLD if the agent is completely inside the world. Otherwise,
	 * an OR operation between LEFT_WORLD, RIGHT_WORLD, UP_WORLD and/or DOWN_WORLD,
	 * indicating the borders where the agent exceeds the world limits.
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
	
	// This should be taken out of here.
	public boolean createOrganismAtPosition(int x, int y) {
		double energy = Math.min(Utils.getINITIAL_ENERGY(), atmosphere.getCO2());
		BaseOrganism o = new Organism(this);
		o.setEnergy(energy);
		return placeAt(o, x, y);
	}
	// This should be taken out of here.
	public boolean createOrganismAtPosition(GeneticCode gc, int x, int y) {
		double energy = Math.min(Utils.getINITIAL_ENERGY(), atmosphere.getCO2());
		BaseOrganism o = new Organism(this, gc);
		o.setEnergy(energy);
		return placeAt(o, x, y);
	}
	/**
	 * Draws all world's agents to a graphics context.
	 * Called from {@link biogenesis.VisibleWorld.paintComponent}.
	 * 
	 * @param g  The graphics context to draw to.
	 */
	public void draw(Graphics g) {			
		for (Agent a : agents)
			a.draw(g);
	}
	/**
	 * Determines the world's region that needs to be repainted. Notifies all
	 * WorldPaintListener of this region.
	 * 
	 * For optimization, only paints agents that has moved in the last frame.
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
	 * Executes a tick. This method iterates through all agents in the world
	 * and make them do a movement. Here is the place where all action
	 * occurs: organism movement, interaction, birth and death.
	 */
	public void tick() {
		tick++;
		
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
		// 256 ticks is a time unit
		if (tick % 256 == 0) {
			worldStatistics.eventTime(population, atmosphere.getO2(), atmosphere.getCO2());
		}
	}
	/**
	 * Remove an agent from the world. If it is an alive agent,
	 * notify WorldEventListeners of this.
	 * 
	 * @param agent  The agent to remove.
	 * @return  true if the agent was removed, that is, if the agent
	 * was actually in this world. False otherwise.
	 */
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
	 * Checks if an agent has a high probability of being in touch with
	 * another agent. This is done by checking if the bounding rectangles
	 * of both agents overlaps. 
	 * 
	 * @param agent  The agent that is being checked.
	 * @return  The agent which bounding rectangle is touching the bounding
	 * rectangle of {@code agent} or null if there is no such agent. 
	 */
	public Agent fastCheckHit(Agent agent) {
		for (Agent a : agents) {
			if (agent != a) {
				if (agent.getCurrentFrame().intersects(a.getCurrentFrame())) {
					return agent;
				}
			}
		}
		return null;
	}
	/**
	 * Checks if an agent hits another agent.
	 * 
	 * @param agent  The agent to check.
	 * @return  The agent that is touching {@code agent} or null if not such
	 * agent exists. 
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
	
	/**
	 * Adds an agent to the list of agents. Agents are inserted
	 * using a binary search algorithm and are ordered according to
	 * their z-order (so they can be drawn in the same order that are
	 * stored).
	 * 
	 * @param newAgent  The agent to add.
	 */
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
	 * Adds an agent to the world. Once added, the new agent will move at every
	 * frame and interact with other agents in the world.
	 * 
	 * Notifies WorldEventListeners.
	 * 
	 * @param newAgent  The agent that needs to be added.
	 * @param parent  The parent of the added agent, or null if there is no parent.
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
	 * Informs the world of a death event. This will update statistics.
	 * 
	 * @param dyingAgent  The agent that has just died.
	 * @param killingAgent  The agent that has killed the other agent, if any.
	 */
	public void agentHasDied(AliveAgent dyingAgent, StatisticalAgent killingAgent) {
		worldStatistics.eventAgentHasDied(dyingAgent, killingAgent);
		for (WorldEventListener listener : eventListeners)
			listener.eventAgentHasDied(dyingAgent, killingAgent);
	}
	/**
	 * Informs the world of an infection event. This will update statistics.
	 * 
	 * @param infectedAgent  The agent that has just been infected.
	 * @param infectingAgent  The agent that has infected the other agent.
	 */
	public void agentHasBeenInfected(AliveAgent infectedAgent, StatisticalAgent infectingAgent) {
		worldStatistics.eventAgentHasBeenInfected(infectedAgent, infectingAgent);
		for (WorldEventListener listener : eventListeners)
			listener.eventAgentHasBeenInfected(infectedAgent, infectingAgent);
	}
	/**
	 * Add a WorldPaintListener to this World, that will be notified of
	 * the regions that need repainting at every frame.
	 * 
	 * @param listener  The WorldPaintListener to add.
	 */
	public void addWorldPaintListener(WorldPaintListener listener) {
		paintListeners.add(listener);
	}
	/**
	 * Remove a WorldPaintListener from the list of listeners to be
	 * notified by this world.
	 * 
	 * @param listener  The listener to remove.
	 */
	public void deleteWorldPaintListener(WorldPaintListener listener) {
		paintListeners.remove(listener);
	}
	/**
	 * Remove all WorldPaintListener from the list of listeners to be
	 * notified by this world.
	 */
	public void deleteWorldPaintListeners() {
		paintListeners.clear();
	}
	/**
	 * Add a WorldEventListener to this World, that will be notified of
	 * the events that occurs in this World.
	 * 
	 * @param listener  The WorldEventListener to add.
	 */
	public void addWorldEventListener(WorldEventListener listener) {
		eventListeners.add(listener);
	}
	/**
	 * Remove a WorldEventListener from the list of listeners to be
	 * notified by this world.
	 * 
	 * @param listener  The listener to remove.
	 */
	public void deleteWorldEventListener(WorldEventListener listener) {
		eventListeners.remove(listener);
	}
	/**
	 * Remove all WorldEventListener from the list of listeners to be
	 * notified by this world.
	 */	
	public void deleteWorldEventListeners() {
		eventListeners.clear();
	}
}
