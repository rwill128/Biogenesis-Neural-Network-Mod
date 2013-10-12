package gui;

import agents.AliveAgent;

public interface OrganismSelector {
	public void setSelectedAgent(AliveAgent baseOrganism);
	public AliveAgent getSelectedAgent();
}
