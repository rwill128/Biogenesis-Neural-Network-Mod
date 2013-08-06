package actions;

import java.awt.event.ActionEvent;

import biogenesis.Utils;
import biogenesis.VisibleWorld;

import organisms.BaseOrganism;
import organisms.GeneticCode;
import organisms.Organism;
import world.CurrentWorld;
import world.World;

import aux.Clipboard;

public class PasteAction extends StdAction {
	private static final long serialVersionUID = 1L;
	private VisibleWorld visibleWorld;
	private CurrentWorld currentWorld;
	
	public PasteAction(VisibleWorld visibleWorld, CurrentWorld currentWorld, String text_key,
			String icon_path, String desc) {
		super(text_key, icon_path, desc);
		this.visibleWorld = visibleWorld;
		this.currentWorld = currentWorld;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		GeneticCode clippedGeneticCode = Clipboard.getInstance().getClippedGeneticCode();
		if (clippedGeneticCode != null) {
			World world = currentWorld.getWorld();
			double energy = Math.min(Utils.getINITIAL_ENERGY(), world.getAtmosphere().getCO2());
			BaseOrganism newOrganism = new Organism(world, clippedGeneticCode);
			newOrganism.setEnergy(energy);
			world.placeAt(newOrganism, visibleWorld.getMouseX(), visibleWorld.getMouseY());
		}
	}
}