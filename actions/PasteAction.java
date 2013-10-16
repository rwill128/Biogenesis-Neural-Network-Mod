package actions;

import java.awt.event.ActionEvent;

import biogenesis.VisibleWorld;
import geneticcodes.GeneticCode;
import world.CurrentWorld;
import world.World;
import auxiliar.Clipboard;

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
			world.createAliveAgentAtPosition(clippedGeneticCode, visibleWorld.getMouseX(), 
					visibleWorld.getMouseY());
		}
	}
}
