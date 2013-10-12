package agents;

import java.awt.Rectangle;

public interface MovingAgent extends Agent {
	public Rectangle getLastFrame();
	public boolean isMoved();
	public double getPosX();
	public double getPosY();
	public double getTheta();
	public double getDx();
	public double getDy();
	public double getDtheta();
	public void setDx(double dx);
	public void setDy(double dy);
	public void setDtheta(double dtheta);
	public void setTheta(double theta);
	public void setPosition(double posx, double posy);
}
