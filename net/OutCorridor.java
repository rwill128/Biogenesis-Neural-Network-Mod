/* Copyright (C) 2006-2010  Joan Queralt Molina
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */
package net;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import agents.Agent;
import agents.AliveAgent;
import agents.MovingAgent;
import world.World;

import biogenesis.Utils;

public class OutCorridor extends Corridor implements MovingAgent {
	private static final long serialVersionUID = Utils.FILE_VERSION;
	
	private double scale;
	private Rectangle lastFrame;
	
	public boolean canSendOrganism() {
		return travellingOrganism == null ? true : false;
	}
	
	public OutCorridor(World w, Connection c) {
		super(w);
		connection = c;
	}
	
	@Override
	public Rectangle getCurrentFrame() {
		Rectangle currentFrame;
		if (travellingOrganism != null)
			currentFrame = this.union(travellingOrganism.getCurrentFrame());
		else
			currentFrame = this;
		return currentFrame;
	}
	
	public boolean sendOrganism(AliveAgent org) {
		if (travellingOrganism == null && connection.getState() == Connection.STATE_CONNECTED) {
			connection.send(org.getGeneticCode());
			travellingOrganism = org;
			scale = 1;
			return true;
		}
		return false;
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.BLUE);
		g.drawRect(x,y,width,height);
		g.setColor(Utils.ColorLIGHT_BLUE);
		g.fillRect(x+1,y+1,width-1,height-1);
		if (travellingOrganism != null) {
			double posx = travellingOrganism.getPosX();
			double posy = travellingOrganism.getPosY();
			travellingOrganism.setPosition(0, 0);
			Graphics2D g2 = (Graphics2D) g;
			g2.translate(posx, posy);
			g2.scale(scale, scale);
			travellingOrganism.draw(g2);
			travellingOrganism.setPosition(posx, posy);
			g2.scale(1/scale, 1/scale);
			g2.translate(-posx, -posy);
		}
	}

	@Override
	public void draw(Graphics g, int width, int height) {
		g.setColor(Color.BLUE);
		g.drawRect(0,0,width,height);
		g.setColor(Utils.ColorLIGHT_BLUE);
		g.fillRect(1,1,width-1,height-1);
		if (travellingOrganism != null)
			travellingOrganism.draw(g, width, height);
	}

	@Override
	public boolean contact(Agent agent) {
		boolean collision = false;
		
		if (agent instanceof AliveAgent) {
			AliveAgent a = (AliveAgent) agent;
			if (a.isAlive() && contains(a.getPosX(), a.getPosY())) {
				if (canSendOrganism()) {
					if (sendOrganism(a)) {
						getWorld().removeAgent(a);
						a.useEnergy(a.getEnergy());
						a.setColor(null);
						collision = true;
					}
				}
			}
		}
		return collision;
	}

	@Override
	public boolean update() {
		if (travellingOrganism != null) {
			scale -= 0.1;
			if (scale <= 0.1) {
				travellingOrganism.setTheta(travellingOrganism.getTheta()+Math.PI/8);
				scale = 0;
				travellingOrganism = null;
			}
		} else {
			getWorld().checkHit(this);
		}
		lastFrame = getCurrentFrame();
		return true;
	}

	@Override
	public Rectangle getLastFrame() {
		return lastFrame;
	}

	@Override
	public boolean isMoved() {
		return travellingOrganism != null;
	}

	@Override
	public double getPosX() {
		return x+width/2;
	}

	@Override
	public double getPosY() {
		return y+height/2;
	}

	@Override
	public double getDx() {
		return 0;
	}

	@Override
	public double getDy() {
		return 0;
	}

	@Override
	public double getDtheta() {
		return 0;
	}

	@Override
	public void setDx(double between) {
		;
	}

	@Override
	public void setDy(double between) {
		;
	}

	@Override
	public void setDtheta(double between) {
		;
	}

	@Override
	public void setPosition(double posx, double posy) {
		x = (int) (posx-width/2);
		y = (int) (posy-height/2);
	}

	@Override
	public void setTheta(double theta) {
		;	
	}

	@Override
	public double getTheta() {
		return 0;
	}
}
