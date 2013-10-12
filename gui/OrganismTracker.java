package gui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import agents.AliveAgent;

import biogenesis.Utils;
import biogenesis.VisibleWorld;

/**
 * OrganismTracker is a scroll pane that scrolls to follow the agent it is tracking.
 * When not tracking anything, it allows the user to scroll manually.
 * 
 * Currently in Biogenesis, the view for the OrganismTracker's JViewport is set 
 * (in the VisibleWorld constructor) as the VisibleWorld object.
 */
public class OrganismTracker extends JScrollPane {
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
	
	/**
	 * If the tracked agent's isAlive() returns true, this scrolls the view toward it.
	 * (Assumes that the view is a VisibleWorld, and uses its coordinate conversion function.)
	 */
	public void track() {
		if (trackedAgent != null) {
			if (!trackedAgent.isAlive()) {
				setTrackedOrganism(null);
				//abortTrackingAction.setEnabled(false);
			}
			else {
				VisibleWorld view = (VisibleWorld) getViewport().getView();
				double agentLocationX = view.toVisibleCoord(trackedAgent.getCurrentFrame().getCenterX());
				double agentLocationY = view.toVisibleCoord(trackedAgent.getCurrentFrame().getCenterY());
				moveScrollBarsToward((int)agentLocationX, (int)agentLocationY);
			}
		}
	}
	
	/**
	 * Scrolls instantly to the given location in the view
	 */
	public void centerScrollBarsOn(int x, int y) {
		int newValue = x - getWidth()/2;
		getHorizontalScrollBar().setValue(newValue);
		
		newValue = y - getHeight()/2;
		getVerticalScrollBar().setValue(newValue);
	}
	
	/**
	 * Scrolls (at max speed) toward the given location in the view
	 */
	private void moveScrollBarsToward(int x, int y) {
		JScrollBar bar = getHorizontalScrollBar();
		bar.setValue(Utils.between(x - getWidth()/2,
				bar.getValue()-2*maxSpeed,bar.getValue()+2*maxSpeed));
		bar = getVerticalScrollBar();
		bar.setValue(Utils.between(y - getHeight()/2,
				bar.getValue()-2*maxSpeed,bar.getValue()+2*maxSpeed));
	}

	public AliveAgent getTrackedOrganism() {
		return trackedAgent;
	}
	
	public OrganismTracker() {
		super();
	}
	
	@Override
	public void setViewportView(Component view) {
		super.setViewportView(view);
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
