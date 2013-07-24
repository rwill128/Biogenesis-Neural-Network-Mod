package organisms;

public interface StatisticalAgent extends Agent {
	public int getTotalChildren();
	public int getTotalInfected();
	public int getTotalKills();
	public void increaseChildren();
	public void increaseInfected();
	public void increaseKills();
}
