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
package biogenesis;

import java.awt.EventQueue;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import auxiliar.PauseListener;
import auxiliar.TimeListener;

/**
 * This class implements a timer. Methods are provided to pause the timer and to
 * set the delay between ticks.
 * 
 * The delay is automatically updated depending on the effective speed reached
 * in the target machine.
 * 
 * You can add TimeListener to react to every tick and PauseListener to be
 * notified when the timer is paused and started again.
 */
public class Process implements Serializable {
	/**
	 * The version of this class.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Controls if the process is active or paused.
	 */
	private boolean processActive = false;
	/**
	 * The timer used.
	 */
	private transient Timer timer = new Timer();
	/**
	 * The task that will be executed each tick. This task will queue the 
	 * real task (lifeProcess) to the AWT event queue, and will measure the
	 * time consumed by the task, adapting the delay if necessary.
	 */
	private transient TimerTask updateTask = null;
	/**
	 * The set of TimeListener that will be notified each tick.
	 */
	private Set<TimeListener> timeListeners = new HashSet<TimeListener>();
	/**
	 * The set of PauseListener that will be notified each time
	 * the process is paused or activated.
	 */
	private Set<PauseListener> pauseListeners = new HashSet<PauseListener>();
	/**
	 * Frame counter. 256 frames are a time unit. This value is used to count
	 * time and to trigger some window updating at regular intervals.
	 */
	private long nFrames=0;
	/**
	 * Number of milliseconds between two invocations of the lifeProcess invocation.
	 * It starts as the user's preference (Utils.DELAY) but is adapted to the current 
	 * situation in the world and the machine speed.
	 */
	private int currentDelay = Utils.DELAY;
	/**
	 * Number of milliseconds it has been taking recently between two invocations of 
	 * the lifeProcess invocation.
	 */
	private double averageTimePerFrame;
	/**
	 * If positive, the consecutive number of frames that haven't accomplished the expected time contract.
	 * If negative, the consecutive number of frame that have accomplished the expected time contract.
	 * Used to adapt the program's speed.
	 */
	private int failedTime=0;
	/**
	 * The moment when the last painting of this visual world started. Used to control
	 * the program's speed.
	 */
	private long lastPaintTime;
	/**
	 * This is the task that will be queued in the AWT thread for execution at every tick.
	 * It will count the ticks and notify listeners.
	 */
	private final transient Runnable lifeProcess = new Runnable() {
	    @Override
		public void run() {
	    	if (isProcessActive()) {
	    		nFrames++;
	    		// notify listeners
	    		for (TimeListener listener : timeListeners)
	    			listener.tick();
	    	}
	    }
	};
	private long timeWhenLastChecked;
	private long numFramesSinceChecked;
	/**
	 * Returns the number of ticks processed.
	 * 
	 * @return  The number of ticks processed since the start or the last time
	 * the counter was reset.
	 */
	public long getNFrames() {
		return nFrames;
	}
	/**
	 * Resets to zero the number of ticks counted.
	 */
	public void resetNFrames() {
		nFrames = 0;
	}
	/**
	 * Adds a new TimeListener that will be notified every tick.
	 * 
	 * @param listener  The TimeListener to add.
	 */
	public void addTimeListener(TimeListener listener) {
		timeListeners.add(listener);
	}
	/**
	 * Adds a new PauseListener that will be notified every time that
	 * the process is paused or restarted.
	 * 
	 * @param listener  The PauseListener to add.
	 */
	public void addPauseListener(PauseListener listener) {
		pauseListeners.add(listener);
	}
	/**
	 * Creates a task to run the life-process. Schedules the task to repeat with the given 
	 * delay. The task will also adjust the delay as needed to stabilize program speed.
	 * 
	 * @param delay  The time between executions, in milliseconds.
	 */
	public void startLifeProcess(int delay) {
		if (updateTask != null)
			updateTask.cancel();
		if (Utils.isEFFICIENCY_MODE())
		{
			updateTask = new TimerTask() {
			    @Override
				public void run() {
			    	try {
						EventQueue.invokeAndWait(lifeProcess);
						long actualTime = System.currentTimeMillis();
						numFramesSinceChecked++;
						averageTimePerFrame = (averageTimePerFrame + actualTime - lastPaintTime) / 2;
	    				failedTime = 0;
    					startLifeProcess(currentDelay);
    					lastPaintTime = actualTime;
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
			    }
			};
			timer.schedule(updateTask, 0);
		} else {
			updateTask = new TimerTask() {
			    @Override
				public void run() {
			    	try {
						EventQueue.invokeAndWait(lifeProcess);
						/*
						 * Checks the actual drawing speed and increases or decreases the speed
						 * of the program in order to keep it running smoothly.
						 */
						long actualTime = System.currentTimeMillis();
						numFramesSinceChecked++;
						if (actualTime - lastPaintTime > currentDelay*1.5 || currentDelay < Utils.DELAY) {
			    			// We can't run so fast
			    			failedTime=Math.max(failedTime+1, 0);
			    			if (failedTime >= 2) {
			    				failedTime = 0;
			    				currentDelay*=1.5;
			    			}
			    		} else {
			    			if (actualTime - lastPaintTime < currentDelay*1.2 && currentDelay > Utils.DELAY) {
			    				// We can run faster
			    				failedTime=Math.min(failedTime-1, 0);
			    				if (failedTime <= -10) {
			    					currentDelay = Math.max(Utils.DELAY, currentDelay-1);
			    				}
			    			} else
			    				// Normal situation: we run at the expected speed
			    				failedTime = 0;
			    		}
    					startLifeProcess(currentDelay);
			    		if (currentDelay > 1000)
			    			setProcessActive(false);
			    		lastPaintTime = actualTime;
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
			    }
			};
			timer.schedule(updateTask, delay, delay);
		}
	}

	/**
	 * Returns the actual Frame Per Second rate of the program.
	 * 
	 * @return  The actual FPS.
	 */
	public int getFPS() {
		if (numFramesSinceChecked > 0) {
			long currentTime = System.currentTimeMillis();
			averageTimePerFrame = (averageTimePerFrame + 
					(currentTime - timeWhenLastChecked) / numFramesSinceChecked) / 2;
			timeWhenLastChecked = currentTime;
			numFramesSinceChecked = 0;
		}
		if (averageTimePerFrame > 0)
			return (int) (1000.0/averageTimePerFrame + 0.5);
		else
			return 99999;
	}
	/**
	 * Returns if the process is currently active or not.
	 * 
	 * @return  true if the process is active, or false if it is paused.
	 */
	public boolean isProcessActive() {
		return processActive;
	}
	/**
	 * Activates or pauses the process. Notifies PauseListener if this
	 * causes a change in the process state.
	 * 
	 * @param processActive  True to activate the process or false to pause it.
	 */
	public void setProcessActive(boolean processActive) {
		if (processActive != this.processActive) {
			this.processActive = processActive;
			for (PauseListener listener : pauseListeners)
				listener.pausePerformed(processActive);
		}
	}
	/**
	 * Cancels and discards this timer.
	 * 
	 * @see java.util.Timer.cancel()
	 */
	public void cancel() {
		timer.cancel();
	}
}
