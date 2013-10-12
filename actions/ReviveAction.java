package actions;

import java.awt.event.ActionEvent;

import agents.AliveAgent;

import world.CurrentWorld;

import biogenesis.MainWindow;
import biogenesis.VisibleWorld;

public class ReviveAction extends StdAction {
	private static final long serialVersionUID = 1L;
	private CurrentWorld currentWorld;
	private MainWindow mainWindow;
	private VisibleWorld visibleWorld;
	
	public ReviveAction(CurrentWorld currentWorld, MainWindow mainWindow, 
			VisibleWorld visibleWorld, String text_key,
			String icon_path, String desc) {
		super(text_key, icon_path, desc);
		this.currentWorld = currentWorld;
		this.mainWindow = mainWindow;
		this.visibleWorld = visibleWorld;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		AliveAgent o = visibleWorld.getSelectedAgent();
		if (o != null && !o.isAlive()) {
			o.revive();
			currentWorld.getWorld().increasePopulation();
			mainWindow.getToolBar().selectActionArray("alive");
		}
	}
}
