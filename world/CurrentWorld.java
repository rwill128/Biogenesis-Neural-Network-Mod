package world;

import java.util.ArrayList;
import java.util.List;

import aux.TimeListener;

public class CurrentWorld implements TimeListener {
	private World world;
	private List<CurrentWorldChangeListener> listeners = 
		new ArrayList<CurrentWorldChangeListener>();
	
	public CurrentWorld(World world) {
		this.world = world;
	}
	
	public World getWorld() {
		return world;
	}
	
	public void setWorld(World world) {
		World oldWorld = this.world;
		this.world = world;
		
		if (oldWorld != world)
			for (CurrentWorldChangeListener listener : listeners)
				listener.eventCurrentWorldChanged(oldWorld, world);
	}
	
	public void addListener(CurrentWorldChangeListener listener) {
		listeners.add(listener);
	}
	
	public boolean deleteListener(CurrentWorldChangeListener listener) {
		return listeners.remove(listener);
	}
	
	public void deleteListeners() {
		listeners.clear();
	}
	
	@Override
	public void time() {
		world.time();
	}
}
