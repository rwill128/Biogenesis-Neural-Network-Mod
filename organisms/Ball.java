package organisms;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JPopupMenu;

import biogenesis.Utils;

import segments.Segment;
import world.World;

public class Ball extends Rectangle implements MovingAgent {
	private static final long serialVersionUID = 1L;
	private double posx, posy;
	private double dx, dy;
	private Rectangle lastFrame = new Rectangle();
	private World world;

	public Ball(World world, double posx, double posy) {
		this.world = world;
		height = 8;
		width = 8;
		dx = Utils.random.nextDouble()*4;
		dy = Utils.random.nextDouble()*4;
		setPosition(posx, posy);
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.ORANGE);
		g.drawOval(x, y, width, height);
	}

	@Override
	public void draw(Graphics g, int width, int height) {
		g.setColor(Color.ORANGE);
		g.drawOval(x, y, Math.min(width, this.width), Math.min(height, this.height));
	}

	@Override
	public JPopupMenu getPopupMenu() {
		return null;
	}

	@Override
	public boolean contact(Agent agent) {
		boolean contacting = false;
		if (agent instanceof SegmentBasedOrganism) {
			SegmentBasedOrganism organism = (SegmentBasedOrganism) agent;
			for (Segment s : organism.getSegments()) {
				if (s.intersects(this)) {
					contacting = true;
					organism.setDx(dx+4);
					organism.setDy(dy+4);
					organism.setDtheta(0);
					dx = -dx;
					dy = -dy;
					break;
				}
			}
		}
		return contacting;
	}

	@Override
	public boolean update() {
		boolean collision = false;
		double dxbak = dx;
		double dybak = dy;
		lastFrame.setBounds(getCurrentFrame());
		x += dx;
		y += dy;
		
		if (world.checkHit(this) != null)
			collision = true;
		// Check it is inside the world
		int insideWorld = world.isInsideWorld(this);
					
		if ((insideWorld & World.RIGHT_WORLD) != 0 || (insideWorld & World.LEFT_WORLD) != 0) {
			dx = -dx;
			collision = true;
		}
		if ((insideWorld & World.UP_WORLD) != 0 || (insideWorld & World.DOWN_WORLD) != 0) {
			dy = -dy;
			collision = true;
		}
		if (collision) {
			x -= dxbak;
			y -= dybak;
		}
		return true;
	}

	@Override
	public int getZOrder() {
		return 10;
	}

	@Override
	public Rectangle getLastFrame() {
		return lastFrame;
	}

	@Override
	public boolean isMoved() {
		return true;
	}

	@Override
	public double getPosX() {
		return posx;
	}

	@Override
	public double getPosY() {
		return posy;
	}

	@Override
	public double getTheta() {
		return 0;
	}

	@Override
	public double getDx() {
		return dx;
	}

	@Override
	public double getDy() {
		return dy;
	}

	@Override
	public double getDtheta() {
		return 0;
	}

	@Override
	public void setDx(double dx) {
		this.dx = dx;
	}

	@Override
	public void setDy(double dy) {
		this.dy = dy;
	}

	@Override
	public void setDtheta(double dtheta) {
		;
	}

	@Override
	public void setTheta(double theta) {
		;
	}

	@Override
	public void setPosition(double posx, double posy) {
		this.posx = posx;
		this.posy = posy;
		x = (int) (posx-width/2);
		y = (int) (posy-height/2);
	}

	@Override
	public Rectangle getCurrentFrame() {
		return this;
	}

}
