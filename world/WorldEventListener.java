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

import agents.Agent;
import agents.AliveAgent;

/**
 * This interface defines the methods that should implement classes that need to listen
 * to world events, such as the birth or death of new agents.
 * 
 * Use {@link World.addWorldEventListener()} to add the listener object to the objects that will
 * be notified by a world.
 */
public interface WorldEventListener {
	/**
	 * This event is fired when an alive agent is added to the world, no matters the cause.
	 * 
	 * @param child  The new alive agent added.
	 * @param parent  If the new agent is born from an existing agent, the agent that created
	 * the new agent, otherwise null.
	 */
	public void eventAliveAgentAdded(AliveAgent child, Agent parent);
	/**
	 * This event indicates that the population has changed.
	 * 
	 * Usually, if the population increases,
	 * an alive agent added event will be fired at the same time than this, but not always. For
	 * example, if an already existing agent is dead and something revives it, the population will
	 * change, but there is no a new agent added because the agent already existed.
	 * 
	 * @param population
	 */
	public void eventPopulationChanged(int oldPopulation, int newPopulation);
	/**
	 * This event is fired whenever that an alive agent dies.
	 * 
	 * @param dyingAgent  The agent that has died.
	 * @param killingAgent  If any, the agent that has killed the dying agent, or null.
	 */
	public void eventAgentHasDied(AliveAgent dyingAgent, Agent killingAgent);
	/**
	 * This event is fired when an alive agent is infected, that is, when its genetic code
	 * is modified by another agent.
	 * 
	 * @param infectedAgent  The agent that is being infected.
	 * @param infectingAgent  The agent that infects the alive agent.
	 */
	public void eventAgentHasBeenInfected(AliveAgent infectedAgent, Agent infectingAgent);
	/**
	 * This event is activated when an alive agent is removed from the world. Note that usually
	 * this event is not related with the death of the agent, because it is fired when its
	 * (dead) body is removed from the world. 
	 * 
	 * @param agent  The agent that is being removed.
	 */
	public void eventAgentRemoved(AliveAgent agent);
	/**
	 * This event is fired when a new set of initial agents is created, usually when the user
	 * chooses to create a new world.
	 */
	public void eventGenesis();
}
