package gui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import organisms.AliveAgent;

import biogenesis.Utils;

public class OrganismTracker extends JScrollPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AliveAgent trackedAgent = null;
	private List<OrganismTrackerObserver> observers = new ArrayList<OrganismTrackerObserver>();
	private int maxSpeed = 5;
	
	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	
	public int getMaxSpeed() {
		return maxSpeed;
	}
	
	public void track() {
		if (trackedAgent != null) {
			if (!trackedAgent.isAlive()) {
				setTrackedOrganism(null);
				//abortTrackingAction.setEnabled(false);
			}
			else {
				JScrollBar bar = getHorizontalScrollBar();
				bar.setValue(Utils.between((int)trackedAgent.getCurrentFrame().getCenterX() - getWidth()/2,
						bar.getValue()-2*maxSpeed,bar.getValue()+2*maxSpeed));
				bar = getVerticalScrollBar();
				bar.setValue(Utils.between((int)trackedAgent.getCurrentFrame().getCenterY() - getHeight()/2,
						bar.getValue()-2*maxSpeed,bar.getValue()+2*maxSpeed));
			}
		}
	}
	
	public AliveAgent getTrackedOrganism() {
		return trackedAgent;
	}
	
	public OrganismTracker() {
		super();
	}

	public OrganismTracker(Component arg0, int arg1, int arg2) {
		super(arg0, arg1, arg2);
	}

	public OrganismTracker(Component arg0) {
		super(arg0);
	}

	public OrganismTracker(int arg0, int arg1) {
		super(arg0, arg1);
	}

	public void setTrackedOrganism(AliveAgent o) {
		trackedAgent = o;
		for (OrganismTrackerObserver observer : observers)
			observer.update(trackedAgent);
	}

	public void addObserver(OrganismTrackerObserver observer) {
		observers.add(observer);
	}

	public void deleteObserver(OrganismTrackerObserver observer) {
		observers.remove(observer);
	}
}
