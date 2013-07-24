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
package biogenesis;

import gui.OrganismSelector;

import javax.swing.*;

import actions.ActionFactory;

import java.awt.*;
import java.awt.event.*;

import organisms.Agent;
import organisms.AliveAgent;
import world.CurrentWorld;
import world.CurrentWorldChangeListener;
import world.World;
import world.WorldEventListener;
import world.WorldPaintListener;

/**
 * This class is associated with a {@link World} and represents its visible
 * part: it's the place where organisms are drawn and is in charge of
 * the context menus management.
 */
public class VisibleWorld extends JPanel implements WorldPaintListener, WorldEventListener,
													CurrentWorldChangeListener, OrganismSelector {
	/**
	 * The version of this class
	 */
	private static final long serialVersionUID = Utils.FILE_VERSION;
	/**
	 * A reference to the {@link MainWindow} where the VisibleWorld is.
	 */
	private MainWindow mainWindow;
	private CurrentWorld currentWorld;
	
	/**
	 * The context menu showed when right clicking on a void place in the world.
	 */
	private JPopupMenu popupVoid;
	
	/**
	 * This is the selected organism. It is drawn with an orange bounding rectangle
	 * and, if there is an {@link InfoWindow}, it shows information about this organism. 
	 */
	protected AliveAgent selectedOrganism = null;
	/**
	 * X coordinate of the mouse pointer when the user clicks or right clicks on the
	 * visible world.
	 */
	protected int mouseX;
	/**
	 * Y coordinate of the mouse pointer when the user clicks or right clicks on the
	 * visible world.
	 */
	protected int mouseY;
	
	public int getMouseX() {
		return mouseX;
	}
	
	public int getMouseY() {
		return mouseY;
	}
	
	/**
	 * Sets an organism as the selected organism. If required, it creates an
	 * {@link InfoWindow} with information of this organism.
	 * 
	 * @param baseOrganism  The new selected organism
	 * @param showInfo  true if an InfoWindow should be created
	 */
	@Override
	public void setSelectedOrganism(AliveAgent baseOrganism) {
		Agent lastSelectedOrganism = selectedOrganism;
		selectedOrganism = baseOrganism;
		if (lastSelectedOrganism != null)
			repaint(lastSelectedOrganism.getCurrentFrame());
		mainWindow.getInfoPanel().setSelectedOrganism(baseOrganism);
		if (selectedOrganism != null)
			repaint(selectedOrganism.getCurrentFrame());
		// Make sure to don't create the tool bar twice when starting the program
		// because this causes spurious exceptions.
		if (selectedOrganism != lastSelectedOrganism) {
			if (selectedOrganism != null) {
				if (selectedOrganism.isAlive())
					mainWindow.getToolBar().selectActionArray("alive");
				else
					mainWindow.getToolBar().selectActionArray("dead");
			}
			else
				mainWindow.getToolBar().selectActionArray("normal");
		}
	}
	/**
	 * Return the selected organism. 
	 * 
	 * @return  The selected organism, if any.
	 */
	@Override
	public AliveAgent getSelectedOrganism() {
		return selectedOrganism;
	}
	
	/**
	 * Creates a new VisibleWorld associated with a {@link MainWindow}.
	 * Creates the menus and the MouseAdapter.
	 * 
	 * @param mainWindow  The MainWindow associated with this VisibleWorld.
	 */
	public VisibleWorld(MainWindow mainWindow, CurrentWorld currentWorld) {
		this.mainWindow = mainWindow;
		this.currentWorld = currentWorld;
		currentWorld.addListener(this);
		currentWorld.getWorld().addWorldEventListener(this);
		currentWorld.getWorld().addWorldPaintListener(this);
		setPreferredSize(new Dimension(Utils.WORLD_WIDTH,Utils.WORLD_HEIGHT));
		setBackground(Color.BLACK);
		mainWindow.getOrganismTracker().setViewportView(this);
		//createActions();
		//createPopupMenu();
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					setSelectedOrganism(findOrganismFromPosition(e.getX(),e.getY()));
				}
			}
			@Override
			public void mousePressed(MouseEvent e) {
				maybeShowPopupMenu(e);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				maybeShowPopupMenu(e);
			}
		});
	}
	
	/**
	 * Finds an organism that has the given coordinates inside its bounding box and
	 * returns a reference to it. If more than on organism satisfies this condition,
	 * if possible, an alive organism is returned. If non organism satisfies this
	 * condition, this method returns null.
	 * 
	 * @param x  X coordinate
	 * @param y  Y coordinate
	 * @return  An organism with the point (x,y) inside its bounding box, or null
	 * if such organism doesn't exist.
	 */
	private AliveAgent findOrganismFromPosition(int x, int y) {
		return currentWorld.getWorld().findOrganismFromPosition(x, y);
	}
	/**
	 * Calls World.draw to draw all world elements and paints the bounding rectangle
	 * of the selected organism.
	 *
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent (Graphics g) {
		Rectangle r;
		super.paintComponent(g);
		currentWorld.getWorld().draw(g);
		if (getSelectedOrganism() != null) {
			g.setColor(Color.ORANGE);
			r = selectedOrganism.getCurrentFrame();
			g.drawRect(r.x, r.y,
					r.width-1, r.height-1);
		}
    }
	
	
	/**
	 * This method is called when a mouse event occurs. If the mouse event is
	 * a popup trigger, this method decide which popup menu is shown, based on
	 * the position of the mouse.
	 * 
	 * @param e
	 */
	private void maybeShowPopupMenu(MouseEvent e) {
		if (e.isPopupTrigger()) {
			mouseX = e.getX();
			mouseY = e.getY();
			AliveAgent b = findOrganismFromPosition(mouseX,mouseY);
			if (b != null) {
				setSelectedOrganism(b);
				JPopupMenu menu = b.getPopupMenu();
				if (menu != null)
					menu.show(e.getComponent(), mouseX, mouseY);
			} else {
				if (popupVoid == null) {
					ActionFactory actionFactory = ActionFactory.getInstance();
					popupVoid = new JPopupMenu();
				    popupVoid.add(new JMenuItem(actionFactory.getPasteAction()));
				    popupVoid.add(new JMenuItem(actionFactory.getRandomCreateAction()));
				    popupVoid.add(new JMenuItem(actionFactory.getImportAction()));
				    popupVoid.add(new JMenuItem(actionFactory.getCreateBallAction()));
				}
				popupVoid.show(e.getComponent(), mouseX, mouseY);
			}
		}
	}
	@Override
	public void eventOrganismAdded(AliveAgent child, Agent parent) {
		if (parent == getSelectedOrganism())
			mainWindow.getInfoPanel().changeNChildren();
	}
	@Override
	public void eventPopulationIncrease(int population) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void eventPopulationDecrease(int population) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void eventOrganismHasDied(AliveAgent dyingOrganism,
			Agent killingOrganism) {
		if (killingOrganism == getSelectedOrganism())
			mainWindow.getInfoPanel().changeNKills();
		if (dyingOrganism == getSelectedOrganism())
			mainWindow.getToolBar().selectActionArray("dead");
		
	}
	@Override
	public void eventOrganismHasBeenInfected(AliveAgent infectedOrganism,
			Agent infectingOrganism) {
		if (infectingOrganism == getSelectedOrganism())
			mainWindow.getInfoPanel().changeNInfected();
		
	}
	@Override
	public void eventOrganismRemoved(AliveAgent organism) {
		if (getSelectedOrganism() == organism)
			setSelectedOrganism(null);
	}
	@Override
	public void eventGenesis() {
		setSelectedOrganism(null);
		setPreferredSize(new Dimension(currentWorld.getWorld().getWidth(),
				currentWorld.getWorld().getHeight()));
	}
	
	@Override
	public void eventCurrentWorldChanged(World oldWorld, World newWorld) {
		oldWorld.deleteWorldEventListener(this);
		oldWorld.deleteWorldPaintListener(this);
		newWorld.addWorldEventListener(this);
		newWorld.addWorldPaintListener(this);
		setPreferredSize(new Dimension(Utils.WORLD_WIDTH,Utils.WORLD_HEIGHT));
		setSelectedOrganism(null);
		repaint();
	}
}
