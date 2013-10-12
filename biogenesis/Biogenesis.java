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


import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import organisms.OrganismFactory;
import actions.ActionFactory;
import menu.MainMenu;
import menu.MultipleToolBar;
import net.NetServerThread;
import world.CurrentWorld;
import world.World;


/**
 * 
 * This is the class that starts the program. It creates the main objects and starts
 * the timer.
 *
 */
public class Biogenesis {
	/**
	 * The program logo
	 */
	private ImageIcon imageIcon = new ImageIcon(getClass().getResource(
			"images/bullet.jpg")); //$NON-NLS-1$
	/**
	 * The main application window
	 */
	private MainWindow mainWindow;
	/**
	 * The frame in the MainWindow where the world is drawn.
	 */
	private VisibleWorld visibleWorld;
	/**
	 * A reference to the world that is currently active.
	 */
	private CurrentWorld currentWorld;
	/**
	 * The timer that control the simulation process.
	 */
	private Process process;
	/**
	 * The net server to receive incoming connections.
	 */
	private NetServerThread netServer;

	/**
	 * 
	 * The starting point. Checks arguments, read preferences from disk, and starts the program.
	 * 
	 * @param args  Only one optional argument at this time: the random number generator seed.
	 */
	public static void main(String[] args) {
		if (args.length > 1) {
			System.err.println("java -jar biogenesis.jar [random seed]");
		} else if (args.length == 1) {
			try {
				long seed = Long.parseLong(args[0]);
				Utils.random.setSeed(seed);
			} catch (NumberFormatException e) {
				System.err.println("java -jar biogenesis.jar [random seed]");
			}
		}
		Utils.readPreferences();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
            	new Biogenesis().start(); 
			}
		});
	}
	
	/**
	 * Starts the program, creating the main objects.
	 * 
	 * @return  A reference to the application's MainWindow.
	 */
	public MainWindow start() {
		ActionFactory actionFactory;
		
		// create the main objects and link them together
		process = new Process();
		currentWorld = new CurrentWorld(new World(new OrganismFactory()));
		mainWindow = new MainWindow(currentWorld, process);
		visibleWorld = new VisibleWorld(mainWindow, currentWorld);
		mainWindow.setIconImage(imageIcon.getImage());
		currentWorld.getWorld().genesis();
		netServer = new NetServerThread(currentWorld);
		currentWorld.addListener(netServer);
		// sets net server
		if (Utils.ACCEPT_CONNECTIONS) {
			netServer.setAcceptConnections(true);
			netServer.startServer();
		}
		// sets listeners
		netServer.addStatusListener(mainWindow.getStatusBar());
		process.addTimeListener(mainWindow);
		process.addTimeListener(currentWorld);
		// starts timer
		process.startLifeProcess(Utils.DELAY);
		// initialize actions
		ActionFactory.init(mainWindow, process, currentWorld, visibleWorld, netServer);
		actionFactory = ActionFactory.getInstance();
		// set specific actions as listeners
		mainWindow.addWindowListener(actionFactory.getQuitAction());
		mainWindow.getOrganismTracker().addObserver(actionFactory.getAbortTrackingAction());
		process.addPauseListener(actionFactory.getStartStopAction());
		// create menu bars
		initToolBar();
		mainWindow.setJMenuBar(new MainMenu());
		
		return mainWindow;
	}
	
	/**
	 * Initialize toolbars. Crates a tool bar to show when selecting an alive agent,
	 * another for dead agents, and a third one when no agent is selected.
	 * 
	 * I'm not sure if this is the best way to create the tool bars.
	 * 
	 * To add a new toolbar for new types of agents, see MultipleToolBar and ActionFactory.
	 */
	private void initToolBar() {
		MultipleToolBar toolBar = mainWindow.getToolBar();
		ActionFactory actionFactory = ActionFactory.getInstance();
		Action[] normalActions = {actionFactory.getNewGameAction(),
				actionFactory.getStartStopAction(),
				actionFactory.getSaveGameAction(),
				actionFactory.getIncreaseCO2Action(),
				actionFactory.getDecreaseCO2Action(),
				actionFactory.getManageConnectionsAction(),
				actionFactory.getAbortTrackingAction(),
				actionFactory.getZoomInAction(),
				actionFactory.getZoomOutAction(),
				actionFactory.getToggleEfficiencyModeAction()};
		Action[] aliveActions = {actionFactory.getFeedAction(),
				actionFactory.getWeakenAction(),
				actionFactory.getKillAction(),
				actionFactory.getCopyAction(),
				actionFactory.getSaveImageAction(),
				actionFactory.getTrackAction(),
				actionFactory.getAbortTrackingAction(),
				actionFactory.getZoomInAction(),
				actionFactory.getZoomOutAction()};
		Action[] deadActions = {actionFactory.getReviveAction(),
				actionFactory.getDisperseAction()};
		
		toolBar.addActionArray("normal", normalActions);
		toolBar.selectActionArray("normal");
		toolBar.addActionArray("alive", aliveActions);
		toolBar.addActionArray("dead", deadActions);
		
	}
}
