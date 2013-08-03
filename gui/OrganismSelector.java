package gui;

import organisms.AliveAgent;

public interface OrganismSelector {
	public void setSelectedAgent(AliveAgent baseOrganism);
	public AliveAgent getSelectedAgent();
}
