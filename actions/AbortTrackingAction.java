package actions;

import gui.OrganismTracker;
import gui.OrganismTrackerObserver;

import java.awt.event.ActionEvent;

import organisms.AliveAgent;

public class AbortTrackingAction extends StdAction implements OrganismTrackerObserver {
	private static final long serialVersionUID = 1L;
	private OrganismTracker tracker;
	
	public AbortTrackingAction(OrganismTracker tracker, String text, String icon_path, String desc) {
		super(text, icon_path, desc);
		this.tracker = tracker;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		tracker.setTrackedOrganism(null);
	}

	public void update(AliveAgent agent) {
		setEnabled(agent != null);
	}
}