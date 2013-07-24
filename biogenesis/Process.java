package biogenesis;

import java.awt.EventQueue;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import aux.PauseListener;
import aux.TimeListener;

public class Process implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Integer FRAME = 0;
	public static Integer RESUME = 1;
	public static Integer PAUSE = 2;
	
	private boolean _isProcessActive=false;
	private transient Timer _timer = new Timer();
	private transient TimerTask updateTask = null;
	private Set<TimeListener> timeListeners = new HashSet<TimeListener>();
	private Set<PauseListener> pauseListeners = new HashSet<PauseListener>();
	
	/**
	 * Frame counter. 256 frames are a time unit. This value is used to count
	 * time and to trigger some window updating at regular intervals.
	 */
	private long nFrames=0;
	/**
	 * Number of milliseconds between two invocations to the lifeProcess invocation.
	 * It starts as the user's preference DELAY but is adapted to the current situation
	 * in the world and the machine speed.
	 */
	protected int currentDelay = Utils.DELAY;
	/**
	 * If positive, the consecutive number of frames that haven't accomplished the expected time contract.
	 * If negative, the consecutive number of frame that have accomplished the expected time contract.
	 * Used to adapt the program's speed.
	 */
	protected int failedTime=0;
	/**
	 * The moment when the last painting of this visual world started. Used to control
	 * the program's speed.
	 */
	protected long lastPaintTime;
	
	public long getNFrames() {
		return nFrames;
	}
	
	public void resetNFrames() {
		nFrames = 0;
	}
	
	public void addTimeListener(TimeListener listener) {
		timeListeners.add(listener);
	}
	
	public void addPauseListener(PauseListener listener) {
		pauseListeners.add(listener);
	}
	
	final transient Runnable lifeProcess = new Runnable() {
	    @Override
		public void run() {
	    	if (isProcessActive()) {
	    		// executa un torn
	    		nFrames++;
	    		for (TimeListener listener : timeListeners)
	    			listener.time();
	    	}
	    }
	};
	
	public void startLifeProcess(int delay) {
		if (updateTask != null)
			updateTask.cancel();
		updateTask = new TimerTask() {
		    @Override
			public void run() {
		    	try {
					EventQueue.invokeAndWait(lifeProcess);
					/**
					 * Checks the actual drawing speed and increases or decreases the speed
					 * of the program in order to keep it running smoothly.
					 */
					long actualTime = System.currentTimeMillis();
					if (actualTime - lastPaintTime > currentDelay*1.5 || currentDelay < Utils.DELAY) {
		    			// We can't run so fast
		    			failedTime=Math.max(failedTime+1, 0);
		    			if (failedTime >= 2) {
		    				failedTime = 0;
		    				currentDelay*=1.5;
		    				startLifeProcess(currentDelay);
		    			}
		    		} else {
		    			if (actualTime - lastPaintTime < currentDelay*1.2 && currentDelay > Utils.DELAY) {
		    				// We can run faster
		    				failedTime=Math.min(failedTime-1, 0);
		    				if (failedTime <= -10) {
		    					currentDelay = Math.max(Utils.DELAY, currentDelay-1);
		    					startLifeProcess(currentDelay);
		    				}
		    			} else
		    				// Normal situation: we run at the expected speed
		    				failedTime = 0;
		    		}
		    		if (currentDelay > 1000)
		    			activateProcess(false);
		    		lastPaintTime = actualTime;
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
		    }
		};
		_timer.schedule(updateTask, delay, delay);
	}

	/**
	 * Returns the actual Frame Per Second rate of the program.
	 * 
	 * @return  The actual FPS.
	 */
	public int getFPS() {
		return 1000/currentDelay;
	}
	
	
	public boolean isProcessActive() {
		return _isProcessActive;
	}
	
	public void activateProcess(boolean activate) {
		if (activate != _isProcessActive) {
			_isProcessActive = activate;
			for (PauseListener listener : pauseListeners)
				listener.pausePerformed(_isProcessActive);
		}
	}
	
	public void cancel() {
		_timer.cancel();
	}
}
