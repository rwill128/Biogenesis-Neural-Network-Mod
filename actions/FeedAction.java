package actions;

import gui.OrganismSelector;

import java.awt.event.ActionEvent;

import organisms.AliveAgent;

import world.Atmosphere;
import world.CurrentWorld;

public class FeedAction extends StdAction {
	private static final long serialVersionUID = 1L;
	private OrganismSelector selector;
	private CurrentWorld currentWorld;
	
	public FeedAction(CurrentWorld currentWorld, OrganismSelector selector, String text_key,
			String icon_path, String desc) {
		super(text_key, icon_path, desc);
		this.currentWorld = currentWorld;
		this.selector = selector;
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		AliveAgent o = selector.getSelectedOrganism();
		Atmosphere atm = currentWorld.getWorld().getAtmosphere();
		if (o != null && o.isAlive()) {
			double q = Math.min(10, atm.getCO2());
			atm.decreaseCO2(q);
			o.setEnergy(o.getEnergy()+q);
			atm.addO2(q);
		}
	}
}
