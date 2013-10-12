package actions;

import gui.OrganismSelector;

import java.awt.event.ActionEvent;

import agents.AliveAgent;
import auxiliar.Clipboard;

public class CopyAction extends StdAction {
	private static final long serialVersionUID = 1L;
	private OrganismSelector selector;
	
	public CopyAction(OrganismSelector selector, String text_key, String icon_path, String desc) {
		super(text_key, icon_path, desc);
		this.selector = selector;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		AliveAgent o = selector.getSelectedAgent();
		if (o != null && o.isAlive()) {
			Clipboard.getInstance().setClippedGeneticCode(o.getGeneticCode());
		}
	}
}
