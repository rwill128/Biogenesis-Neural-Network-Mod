package actions;

import java.awt.event.ActionEvent;

import world.CurrentWorld;

public class IncreaseCO2Action extends StdAction {
	private static final long serialVersionUID = 1L;
	private CurrentWorld currentWorld;
	
	public IncreaseCO2Action(CurrentWorld currentWorld, String text, String icon_path, String desc) {
		super(text, icon_path, desc);
		this.currentWorld = currentWorld;
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		currentWorld.getWorld().getAtmosphere().addCO2(500);
	}
}
