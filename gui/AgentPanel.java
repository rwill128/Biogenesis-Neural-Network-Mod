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
package gui;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import organisms.Agent;
import organisms.AliveAgent;
import biogenesis.Utils;
import auxiliar.Clipboard;
import auxiliar.Messages;

public class AgentPanel extends JPanel {
	private static final long serialVersionUID = Utils.FILE_VERSION;
	private static final Dimension defaultSize = new Dimension(100,100);
	private Agent agent;
	private JPopupMenu popup;
	
	public AgentPanel(Agent agent) {
		setBackground(Color.BLACK);
		setPreferredSize(defaultSize);
		setAgent(agent);
	}
	
	public void setAgent(Agent agent) {
		this.agent = agent;
		popup = new JPopupMenu();
		if (agent instanceof AliveAgent) {
			final AliveAgent aliveAgent = (AliveAgent) agent;
		    JMenuItem menuCopy = new JMenuItem(Messages.getInstance().getString("T_COPY")); //$NON-NLS-1$
		    menuCopy.addActionListener(new ActionListener() {
		    	@Override
				public void actionPerformed(ActionEvent e) {
					Clipboard.getInstance().setClippedGeneticCode(aliveAgent.getGeneticCode());
				}
		    	
		    });
		    popup.add(menuCopy);
		    addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (e.isPopupTrigger()) {
						popup.show(e.getComponent(), e.getX(), e.getY());
					}
				}
				@Override
				public void mouseReleased(MouseEvent e) {
					if (e.isPopupTrigger()) {
						popup.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			});
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (agent != null)
			agent.draw(g, getWidth(), getHeight());
	}
}
