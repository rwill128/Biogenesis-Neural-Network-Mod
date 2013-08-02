package actions;

import gui.OrganismSelector;

import java.awt.event.ActionEvent;

import organisms.AliveAgent;

public class WeakenAction extends StdAction {
	private static final long serialVersionUID = 1L;
	private OrganismSelector selector;
	
	public WeakenAction(OrganismSelector selector, String text_key, String icon_path, String desc) {
		super(text_key, icon_path, desc);
		this.selector = selector;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		AliveAgent o = selector.getSelectedOrganism();
		if (o != null && o.isAlive()) {
			o.useEnergy(o.getEnergy()/2);
		}
	}
}