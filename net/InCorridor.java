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
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.SwingUtilities;

import agents.Agent;
import geneticcodes.GeneticCode;
import world.World;

import biogenesis.Utils;

public class InCorridor extends Corridor {
	private static final long serialVersionUID = Utils.FILE_VERSION;
	private Queue<GeneticCode> pendingOrganisms = new LinkedList<GeneticCode>();
	
	public InCorridor(World w) {
		super(w);
	}

	public void receiveOrganism(final GeneticCode code) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				pendingOrganisms.add(code);	
			}
		});
	}
	
	@Override
	public boolean update() {
		GeneticCode nextCode = pendingOrganisms.peek();
		if (nextCode != null)
			if (getWorld().createAliveAgentAtPosition(nextCode, x+Utils.random.nextInt(width),
					y+Utils.random.nextInt(height)))
				pendingOrganisms.poll();
		return true;
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.RED);
		g.drawRect(x,y,width,height);
		g.setColor(Utils.ColorLIGHT_RED);
		g.fillRect(x+1,y+1,width-1,height-1);
	}

	@Override
	public void draw(Graphics g, int width, int height) {
		g.setColor(Color.RED);
		g.drawRect(0, 0, width, height);
		g.setColor(Utils.ColorLIGHT_RED);
		g.fillRect(1,1,width-1,height-1);
	}

	@Override
	public boolean contact(Agent agent) {
		return false;
	}

}
