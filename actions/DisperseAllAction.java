package actions;

import java.awt.event.ActionEvent;
import java.util.List;

import agents.Agent;
import agents.AliveAgent;

import world.CurrentWorld;

public class DisperseAllAction extends StdAction {
	private static final long serialVersionUID = 1L;
	private CurrentWorld currentWorld;
	
	public DisperseAllAction(CurrentWorld currentWorld, String text, String icon_path, String desc) {
		super(text, icon_path, desc);
		this.currentWorld = currentWorld;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		List<Agent> agents = currentWorld.getWorld().getAgents();
		AliveAgent aa;
		for (Agent b : agents) {
			if (b instanceof AliveAgent) {
				aa = (AliveAgent) b;
				if (!aa.isAlive())
					aa.useEnergy(aa.getEnergy());
			}
		}
	}
}
