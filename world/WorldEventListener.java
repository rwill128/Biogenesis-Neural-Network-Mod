package world;

import organisms.Agent;
import organisms.AliveAgent;

public interface WorldEventListener {
	public void eventOrganismAdded(AliveAgent child, Agent parent);
	public void eventPopulationIncrease(int population);
	public void eventPopulationDecrease(int population);
	public void eventOrganismHasDied(AliveAgent dyingOrganism, Agent killingOrganism);
	public void eventOrganismHasBeenInfected(AliveAgent infectedOrganism, Agent infectingOrganism);
	public void eventOrganismRemoved(AliveAgent organism);
	public void eventGenesis();
}
