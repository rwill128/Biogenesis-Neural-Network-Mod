package actions;

import java.awt.event.ActionEvent;

import organisms.Ball;

import biogenesis.VisibleWorld;

import world.CurrentWorld;

public class CreateBallAction extends StdAction {
	private static final long serialVersionUID = 1L;
	private CurrentWorld currentWorld;
	private VisibleWorld visibleWorld;
	
	public CreateBallAction(VisibleWorld visibleWorld, CurrentWorld currentWorld, String text_key, String icon_path, String desc) {
		super(text_key, icon_path, desc);
		this.currentWorld = currentWorld;
		this.visibleWorld = visibleWorld;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		currentWorld.getWorld().addAgent(new Ball(currentWorld.getWorld(),
				visibleWorld.getMouseX(), visibleWorld.getMouseY()), null);
	}
}