package agents;

import java.awt.Color;
import java.awt.image.BufferedImage;
import organisms.GeneticCode;

import world.World;

public interface AliveAgent extends StatisticalAgent, MovingAgent {
	public void die(Agent killingAgent);
	public boolean isAlive();
	public boolean useEnergy(double q);
	public double getEnergy();
	public void setEnergy(double energy);
	public boolean reproduce();
	public GeneticCode getGeneticCode();
	public GeneticCode getInfectedGeneticCode();
	public double getMass();
	public int getAge();
	public void setAge(int age);
	public int getParentId();
	public int getGeneration();
	public BufferedImage getImage();
	public void revive();
	public void setId(int id);
	public int getId();
	public void setColor(Color c);
	public double getInertia();
	public void setInfectedGeneticCode(GeneticCode infectingGeneticCode);
	public World getWorld();
	
}
