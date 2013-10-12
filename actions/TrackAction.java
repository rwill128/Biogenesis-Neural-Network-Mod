package actions;

import gui.OrganismSelector;
import gui.OrganismTracker;
import gui.OrganismTrackerObserver;

import java.awt.event.ActionEvent;

import agents.AliveAgent;
import auxiliar.Messages;

public class TrackAction extends StdAction implements OrganismTrackerObserver {
	private static final long serialVersionUID = 1L;
	private String nameKey2;
	private String descKey2;
	private OrganismTracker tracker;
	private OrganismSelector selector;
		
	public TrackAction(OrganismTracker tracker, OrganismSelector selector, String text_key,
			String textKey2, String icon_path, String desc, String desc2) {
		super(text_key, icon_path, desc);
		nameKey2 = textKey2;
		descKey2 = desc2;
		this.tracker = tracker;
		this.selector = selector;
		tracker.addObserver(this);
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		AliveAgent b = selector.getSelectedAgent();
		if (b != null && b.isAlive()) {
			if (tracker.getTrackedOrganism() == b)
				tracker.setTrackedOrganism(null);
			else
				tracker.setTrackedOrganism(b);
		}
	}

	@Override
	public void update(AliveAgent agent) {
		if (agent != null) {
			putValue(NAME, Messages.getInstance().getString(nameKey2));
			putValue(SHORT_DESCRIPTION, Messages.getInstance().getString(descKey2));
			putValue(MNEMONIC_KEY, Messages.getInstance().getMnemonic(nameKey2));
		} else {
			putValue(NAME, Messages.getInstance().getString(name_key));
			putValue(SHORT_DESCRIPTION, Messages.getInstance().getString(desc_key));
			putValue(MNEMONIC_KEY, Messages.getInstance().getMnemonic(name_key));
		}
	}
}
