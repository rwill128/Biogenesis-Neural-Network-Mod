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

import java.util.ArrayList;
import java.util.List;

import aux.TimeListener;

/**
 * This class implements a simple way to keep track of which world
 * is currently active. Other objects that need to keep a reference
 * to the active world will keep instead a reference to a CurrentWorld
 * object. This object will be always the same during the execution of
 * the program.
 * 
 * When a reference to the current world is needed, a
 * call to getWorld() will return it. This reference should be used and
 * discarded.
 * 
 * An object can implement the interface CurrentWorldChangeListener
 * and register as listener to a CurrentWorld object. After that, this
 * object will be notified every time that the world changes.
 * 
 * A CurrentWorld is also a TimeListener. When it receive a tick it
 * delegates the tick to the active world. This way, it isn't necessary
 * to remove an old world and add the new one as listener to the Timer
 * every time the active world changes.
 */
public class CurrentWorld implements TimeListener {
	/**
	 * A reference to the current world.
	 */
	private World world;
	/**
	 * A list of all CurrentWorldChangeListeners registered to
	 * be notified when the current world changes.
	 */
	private List<CurrentWorldChangeListener> listeners = 
		new ArrayList<CurrentWorldChangeListener>();
	/**
	 * The only constructor of this class.
	 * 
	 * @param world  A reference to the active world.
	 */
	public CurrentWorld(World world) {
		this.world = world;
	}
	/**
	 * Returns the currently active world.
	 * 
	 * @return  A reference to the current world.
	 */
	public World getWorld() {
		return world;
	}
	/**
	 * Sets the current world and notify the listener of the change.
	 * 
	 * @param world  A reference to the new active world.
	 */
	public void setWorld(World world) {
		World oldWorld = this.world;
		this.world = world;
		
		if (oldWorld != world)
			for (CurrentWorldChangeListener listener : listeners)
				listener.eventCurrentWorldChanged(oldWorld, world);
	}
	/**
	 * Register a CurrentWorldChangeListener to be notified when there is
	 * a change in the active world.
	 * 
	 * @param listener  The listener to register.
	 */
	public void addListener(CurrentWorldChangeListener listener) {
		listeners.add(listener);
	}
	/**
	 * Remove a CurrentWorldChangeListener from the list of listeners to
	 * be notified when the active world changes.
	 * 
	 * @param listener  The listener to remove.
	 * @return  true if the listener was registered or false if not.
	 */
	public boolean deleteListener(CurrentWorldChangeListener listener) {
		return listeners.remove(listener);
	}
	/**
	 * Remove all listeners from the list of listener to be notified
	 * when the active world changes.
	 */
	public void deleteListeners() {
		listeners.clear();
	}
	/**
	 * When a timer tick is receive it is delegated to the active world.
	 */
	@Override
	public void tick() {
		world.tick();
	}
}
