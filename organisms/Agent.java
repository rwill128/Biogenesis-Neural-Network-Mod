package organisms;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JPopupMenu;

/**
 * This is the base interface for all agents in Biogenesis. Agents include anything that
 * interact with the world and with other agents, including all organisms.
 * 
 * There are more specialized interfaces, such as MovingAgent or AliveAgent.
 */
public interface Agent {
	/**
	 * Draws the agent in the provided Graphics object.
	 * 
	 * @param g  The Graphics where the agent is to be drawn.
	 */
	public void draw(Graphics g);
	/**
	 * Draws the agent in the provided Graphics object.
	 * 
	 * @param g  The Graphics where the agent is to be drawn.
	 * @param width  The width of the frame where the agent is drawn to.
	 * @param height  The height of the frame where the agent is drawn to.
	 */
	public void draw(Graphics g, int width, int height);
	/**
	 * Get the current bounding box for this agent, that is the rectangle that
	 * completely surrounds the agent inside the world.
	 *  
	 * @return  The Rectangle that surrounds the agent.
	 */
	public Rectangle getCurrentFrame();
	/**
	 * 
	 * 
	 * @return
	 */
	public JPopupMenu getPopupMenu();
	public boolean contact(Agent agent);
	public boolean update();
	public int getZOrder();
}
