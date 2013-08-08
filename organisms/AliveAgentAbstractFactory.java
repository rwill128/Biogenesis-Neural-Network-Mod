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
package organisms;

import java.io.Serializable;

import world.World;

/**
 * This interface defines the methods for an alive agent factory. An alive agent factory
 * is a class that can create alive agents of one concrete type. A {@link World} is assigned
 * an alive agent factory that use to create its agents.
 * 
 * To add a new type of alive agent, you have to create a new class that implements
 * {@link AliveAgent} or extends {@link BaseOrganism} and a class that implements AliveAgentAbstractFactory
 * that creates agents of your new type. Then, you can create worlds that uses your factory
 * and are populated by your new kind of organism.
 */
public interface AliveAgentAbstractFactory extends Serializable {
	/**
	 * This method creates an agent and associates it to a world. Usually that means that
	 * the agent will keep a reference to this world, so it can notify it of the different events
	 * that may occur, as when the agent dies.
	 * 
	 * Implementing this is usually as simple as calling an appropiate constructor of the
	 * concrete AliveAgent class.
	 * 
	 * @param world  The world where the agent will live.
	 * @return  The newly created alive agent.
	 */
	public AliveAgent createAliveAgent(World world);
	/**
	 * This method creates an agent based on a given GeneticCode and associates it to a world.
	 * This method is usually called when a user wants to create an alive agent based on
	 * another genetic code that is cloning, or that is received by net.
	 * 
	 * Implementing this is usually as simple as calling an appropiate constructor of the
	 * concrete AliveAgent class.
	 * 
	 * @param world  The world where the agent will live.
	 * @param gc  The genetic code of the new agent.
	 * @return  The newly create alive agent.
	 */
	public AliveAgent createAliveAgent(World world, GeneticCode gc);
	/**
	 * This method creates an array of alive agents used to initially populate a world.
	 * Usually, implementing it is as easy as creating an array and filling it using createAliveAgent(world).
	 * 
	 * @param world  The world where the agents will live.
	 * @return  An array of newly created alive agents.
	 */
	public AliveAgent[] initialPopulation(World world);
}
