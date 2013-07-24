package biogenesis;


import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import organisms.AliveAgentFactory;

import actions.ActionFactory;

import menu.MainMenu;
import menu.MultipleToolBar;
import net.NetServerThread;

import world.CurrentWorld;
import world.World;

public class Biogenesis {
	private ImageIcon imageIcon = new ImageIcon(getClass().getResource(
			"images/bullet.jpg")); //$NON-NLS-1$
	private MainWindow mainWindow;
	private VisibleWorld visibleWorld;
	private CurrentWorld currentWorld;
	private Process process;
	private NetServerThread netServer;

	/**
	 * @param args
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

	public void changeWorld(World newWorld) {
		process.activateProcess(true);
	}
	
	public MainWindow start() {
		ActionFactory actionFactory;
		
		process = new Process();
		currentWorld = new CurrentWorld(new World());
		mainWindow = new MainWindow(currentWorld, process);
		visibleWorld = new VisibleWorld(mainWindow, currentWorld);
		mainWindow.setIconImage(imageIcon.getImage());
		currentWorld.getWorld().genesis(AliveAgentFactory.getInstance());
		netServer = new NetServerThread(currentWorld);
		currentWorld.addListener(netServer);
		if (Utils.ACCEPT_CONNECTIONS) {
			netServer.setAcceptConnections(true);
			netServer.startServer();
		}
		netServer.addStatusListener(mainWindow.getStatusBar());
		process.addTimeListener(mainWindow);
		process.addTimeListener(currentWorld);
		process.startLifeProcess(Utils.DELAY);
		ActionFactory.init(mainWindow, process, currentWorld, visibleWorld, netServer);
		actionFactory = ActionFactory.getInstance();
		mainWindow.addWindowListener(actionFactory.getQuitAction());
		mainWindow.getOrganismTracker().addObserver(actionFactory.getAbortTrackingAction());
		process.addPauseListener(actionFactory.getStartStopAction());
		initToolBar();
		mainWindow.setJMenuBar(new MainMenu());
		return mainWindow;
	}
	

	

	
	private void initToolBar() {
		MultipleToolBar toolBar = mainWindow.getToolBar();
		ActionFactory actionFactory = ActionFactory.getInstance();
		Action[] normalActions = {actionFactory.getNewGameAction(),
				actionFactory.getStartStopAction(),
				actionFactory.getSaveGameAction(),
				actionFactory.getIncreaseCO2Action(),
				actionFactory.getDecreaseCO2Action(),
				actionFactory.getManageConnectionsAction(),
				actionFactory.getAbortTrackingAction()};
		Action[] aliveActions = {actionFactory.getFeedAction(),
				actionFactory.getWeakenAction(),
				actionFactory.getKillAction(),
				actionFactory.getCopyAction(),
				actionFactory.getSaveImageAction(),
				actionFactory.getTrackAction(),
				actionFactory.getAbortTrackingAction()};
		Action[] deadActions = {actionFactory.getReviveAction(),
				actionFactory.getDisperseAction()};
		
		toolBar.addActionArray("normal", normalActions);
		toolBar.selectActionArray("normal");
		toolBar.addActionArray("alive", aliveActions);
		toolBar.addActionArray("dead", deadActions);
		
	}
}
