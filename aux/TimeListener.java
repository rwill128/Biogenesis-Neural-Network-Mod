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
package aux;

/**
 * A TimeListener is an object that reacts to a Process' ticks.
 * To use it, you should register the TimeListener with the Process
 * using its addTimeListener() method. After that, the tick() method
 * will be called every tick.
 */
public interface TimeListener {
	/**
	 * The tick() method will be called every tick when the TimeListener
	 * is registered for listening to a Process.
	 */
	public void tick();
}
