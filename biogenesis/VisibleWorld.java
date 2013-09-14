/* Copyright (C) 2006-2013  Joan Queralt Molina
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
import gui.OrganismTracker;

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
	/**
	 * The CurrentWorld to get a reference to the active world when needed.
	 */
	private CurrentWorld currentWorld;
	/**
	 * The context menu showed when right clicking on a void place in the world.
	 */
	private JPopupMenu popupVoid;
	/**
	 * This is the selected agent. It is drawn with an orange bounding rectangle. 
	 * 
	 * Programmer's Note: If nonliving agents are ever made clickable we can change 
	 * this to the Agent class without much trouble.
	 */
	private AliveAgent selectedAgent = null;
	/**
	 * X coordinate of the mouse pointer when the user clicks or right clicks on the
	 * Visible World.
	 */
	private int mouseX;
	/**
	 * Y coordinate of the mouse pointer when the user clicks or right clicks on the
	 * Visible World.
	 */
	private int mouseY;
	/**
	 * Multiplication factor for how big the world should look.
	 * (A zoomFactor of 0.5 means the world and all in it appear at half size.)
	 */
	private double zoomFactor;
	/**
	 * The width of the current world, used in determining the Visible Width.
	 */
	private int worldWidth;
	/**
	 * The height of the current world, used in determining the Visible Height.
	 */
	private int worldHeight;
	
	/**
	 * Returns the x coordinate of the last place where the user has clicked, in Visible Coordinates.
	 * 
	 * @return  X coordinate of the mouse pointer.
	 */
	public int getMouseX() {
		return mouseX;
	}
	/**
	 * Returns the y coordinate of the last place where the user has clicked, in Visible Coordinates.
	 * 
	 * @return  Y coordinate of the mouse pointer.
	 */
	public int getMouseY() {
		return mouseY;
	}
	/**
	 * Sets an agent as the selected agent. Tells the main window to update
	 * the info tool bar with information about this agent. Changes the shown tool bar
	 * in the main window depending on the selected agent.
	 * 
	 * @param agent  The new selected agent.
	 */
	@Override
	public void setSelectedAgent(AliveAgent agent) {
		Agent lastSelectedAgent = selectedAgent;
		selectedAgent = agent;
		if (lastSelectedAgent != null)
			repaint(lastSelectedAgent.getCurrentFrame());
		mainWindow.getInfoToolBar().setSelectedOrganism(agent);
		if (selectedAgent != null)
			repaint(selectedAgent.getCurrentFrame());
		// Make sure to don't create the tool bar twice when starting the program
		// because this causes spurious exceptions.
		if (selectedAgent != lastSelectedAgent) {
			if (selectedAgent != null) {
				if (selectedAgent.isAlive())
					mainWindow.getToolBar().selectActionArray("alive");
				else
					mainWindow.getToolBar().selectActionArray("dead");
			}
			else
				mainWindow.getToolBar().selectActionArray("normal");
		}
	}
	/**
	 * Return the selected agent.
	 * 
	 * @return  The selected agent, if any, or null.
	 */
	@Override
	public AliveAgent getSelectedAgent() {
		return selectedAgent;
	}
	/**
	 * Creates a new VisibleWorld associated with a {@link MainWindow}.
	 * Creates the menus and the MouseAdapter. Adds itself as listener to
	 * the world.
	 * 
	 * @param mainWindow  The MainWindow associated with this VisibleWorld.
	 * @param currentWorld  The CurrentWorld object used to access to the current world.
	 */
	public VisibleWorld(MainWindow mainWindow, CurrentWorld currentWorld) {
		this.mainWindow = mainWindow;
		this.currentWorld = currentWorld;
		currentWorld.addListener(this);
		currentWorld.getWorld().addWorldEventListener(this);
		currentWorld.getWorld().addWorldPaintListener(this);
		setZoomFactor(Utils.ZOOM_FACTOR);
		setWorldSize(currentWorld.getWorld().getWidth(), currentWorld.getWorld().getHeight());
		setBackground(Color.BLACK);
		mainWindow.getOrganismTracker().setViewportView(this);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					setSelectedAgent(findAliveAgentFromPosition(e.getX(), e.getY()));
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
	 * Finds an alive agent that has the given coordinates inside its bounding box and
	 * returns a reference to it. If more than one agent satisfies this condition,
	 * if possible, an agent that is alive is returned. If no alive agent satisfies this
	 * condition, this method returns null.
	 * 
	 * @param x  X coordinate
	 * @param y  Y coordinate
	 * @return  An alive agent with the point (x,y) inside its bounding box, or null
	 * if such agent doesn't exist.
	 */
	private AliveAgent findAliveAgentFromPosition(double x, double y) {
		return currentWorld.getWorld().findAliveAgentFromPosition(toWorldCoord(x), toWorldCoord(y));
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
		Graphics2D g2 = ((Graphics2D) g);
		g2.scale(zoomFactor, zoomFactor);
		currentWorld.getWorld().draw(g2);
		if (getSelectedAgent() != null) {
			g2.setColor(Color.ORANGE);
			r = selectedAgent.getCurrentFrame();
			g2.drawRect(r.x, r.y,
					r.width-1, r.height-1);
		}
    }
	
	
	/**
	 * This method is called when a mouse event occurs. If the mouse event is
	 * a pop up trigger, this method decide which pop up menu is shown, based on
	 * the position of the mouse.
	 * 
	 * @param e
	 */
	private void maybeShowPopupMenu(MouseEvent e) {
		if (e.isPopupTrigger()) {
			mouseX = e.getX();
			mouseY = e.getY();
			AliveAgent aa = findAliveAgentFromPosition(mouseX,mouseY);
			if (aa != null) {
				setSelectedAgent(aa);
				JPopupMenu menu = aa.getPopupMenu();
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
	/**
	 * When an alive agent is added, if is the selected agent, update the main window info toolbar.
	 * Should the toolbar be the listener instead of the visible world?
	 */
	@Override
	public void eventAliveAgentAdded(AliveAgent child, Agent parent) {
		if (parent == getSelectedAgent())
			mainWindow.getInfoToolBar().changeNChildren();
	}
	/**
	 * When an agent dies, update the main window info tool bar or the tool bar.
	 */
	@Override
	public void eventAgentHasDied(AliveAgent dyingOrganism,
			Agent killingOrganism) {
		if (killingOrganism == getSelectedAgent())
			mainWindow.getInfoToolBar().changeNKills();
		if (dyingOrganism == getSelectedAgent())
			mainWindow.getToolBar().selectActionArray("dead");
		
	}
	/**
	 * When an agent is infected, update the info tool bar, if necessary.
	 */
	@Override
	public void eventAgentHasBeenInfected(AliveAgent infectedOrganism,
			Agent infectingOrganism) {
		if (infectingOrganism == getSelectedAgent())
			mainWindow.getInfoToolBar().changeNInfected();
		
	}
	/**
	 * If the selected agent is removed from the world, set it to null.
	 */
	@Override
	public void eventAgentRemoved(AliveAgent organism) {
		if (getSelectedAgent() == organism)
			setSelectedAgent(null);
	}
	/**
	 * When a new world is created, adapt the visible world size to the
	 * new world's size and set the the selected organism to null.
	 */
	@Override
	public void eventGenesis() {
		setSelectedAgent(null);
		setWorldSize(currentWorld.getWorld().getWidth(), currentWorld.getWorld().getHeight());
	}
	/**
	 * If the world changes, update listeners, size, and selected agent.
	 */
	@Override
	public void eventCurrentWorldChanged(World oldWorld, World newWorld) {
		oldWorld.deleteWorldEventListener(this);
		oldWorld.deleteWorldPaintListener(this);
		newWorld.addWorldEventListener(this);
		newWorld.addWorldPaintListener(this);
		setWorldSize(newWorld.getWidth(), newWorld.getHeight());
		setSelectedAgent(null);
		repaint();
	}

	/**
	 * Updates the world size fields and the world's visible size
	 * @param width
	 * @param height
	 */
	private void setWorldSize(int width, int height) {
		worldWidth = width;
		worldHeight = height;
		updatePreferredSize();
	}
	
	/**
	 * @return the zoom factor (how big the world should look)
	 */
	public double getZoomFactor() {
		return zoomFactor;
	}
	/**
	 * @param zoomFactor How big the world should look relative to its actual size
	 */
	public void setZoomFactor(double zoomFactor) {
		this.zoomFactor = zoomFactor;
		updatePreferredSize();
	}
	
	/**
	 * Updates the visible world size, (according to the actual world size and the zoom factor,)
	 * while maintaining the ScrollPane center as much as possible.
	 */
	private void updatePreferredSize() {
		double centerX, centerY;
		OrganismTracker scrollPane = mainWindow.getOrganismTracker();
		
		//calculate center of scrollbar locations
		centerX = scrollPane.getHorizontalScrollBar().getValue() + scrollPane.getWidth() / 2;
		centerY = scrollPane.getVerticalScrollBar().getValue() + scrollPane.getHeight() / 2;
		
		//change the preferred size
		Dimension oldSize = getPreferredSize();
		Dimension newSize = new Dimension(toVisibleCoord(worldWidth), toVisibleCoord(worldHeight));
		setPreferredSize(newSize);
		
		//keep center relative to size
		if (oldSize.width > 0 && oldSize.height > 0)
		{
			centerX *= (double) newSize.width / oldSize.width;
			centerY *= (double) newSize.height / oldSize.height;
			scrollPane.centerScrollBarsOn((int) centerX, (int) centerY);
			repaint(); //cleans up artifacts
			scrollPane.getViewport().invalidate(); //refreshes scrollbars
		}

	}
	
	@Override
	public void eventPopulationChanged(int oldPopulation, int newPopulation) {
		// nothing to do
	}
	
	/**
	 * Marks the specified region for repainting, with a 1-pixel buffer on the 
	 * bottom and right to account for conversion error.
	 * @param r A rectangle given in world coordinates
	 */
	@Override
	public void repaint(Rectangle r) {
		super.repaint(new Rectangle(toVisibleCoord(r.x), toVisibleCoord(r.y), 
				toVisibleCoord(r.width) + 1, toVisibleCoord(r.height) + 1));
	}
	
	/**
	 * Converts horizontal or vertical locations from world coordinates (no zoom)
	 * to visible coordinates (zoom factor applied)
	 */
	public int toVisibleCoord(int worldCoord) {
		return (int) (worldCoord * zoomFactor);
	}

	/**
	 * Converts horizontal or vertical locations from world coordinates (no zoom)
	 * to visible coordinates (zoom factor applied)
	 */
	public double toVisibleCoord(double worldCoord) {
		return worldCoord * zoomFactor;
	}

	/**
	 * Converts horizontal or vertical locations from visible coordinates (zoom factor applied)
	 * to world coordinates (no zoom)
	 */
	public double toWorldCoord(double visibleCoord) {
		return visibleCoord / zoomFactor;
	}
}
