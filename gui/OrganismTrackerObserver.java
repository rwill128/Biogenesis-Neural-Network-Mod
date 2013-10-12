package gui;

import agents.AliveAgent;

public interface OrganismTrackerObserver {
	public void update(AliveAgent organism);
}
