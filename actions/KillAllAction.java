package actions;

import java.awt.event.ActionEvent;
import java.util.List;

import organisms.Agent;
import organisms.AliveAgent;

import world.CurrentWorld;
	
public class KillAllAction extends StdAction {
	private static final long serialVersionUID = 1L;
	private CurrentWorld currentWorld;
	
	public KillAllAction(CurrentWorld currentWorld, String text, String icon_path, String desc) {
		super(text, icon_path, desc);
		this.currentWorld = currentWorld;
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		List<Agent> agents = currentWorld.getWorld().getAgents();
		AliveAgent aa;
		
		for (Agent org : agents) {
			if (org instanceof AliveAgent) {
				aa = (AliveAgent) org;
				if (aa.isAlive())
					aa.die(null);
			}
		}
	}
}
