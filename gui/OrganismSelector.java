package gui;

import organisms.AliveAgent;

public interface OrganismSelector {
	public void setSelectedOrganism(AliveAgent baseOrganism);
	public AliveAgent getSelectedOrganism();
}
