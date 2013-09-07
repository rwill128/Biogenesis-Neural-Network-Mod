package actions;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import net.NetServerThread;
import biogenesis.MainWindow;
import biogenesis.VisibleWorld;
import biogenesis.Process;
import world.CurrentWorld;

public class ActionFactory {
	private static ActionFactory actionFactory = null;

	private static String imagePath = "../biogenesis/images/";

	private StdAction newGameAction;
	private StartStopAction startStopAction;
	private StdAction saveGameAction;
	private StdAction increaseCO2Action;
	private StdAction decreaseCO2Action;
	private StdAction manageConnectionsAction;
	private AbortTrackingAction abortTrackingAction;
	private StdAction openGameAction;
	private StdAction saveGameAsAction;
	private QuitAction quitAction;
	private StdAction statisticsAction;
	private StdAction labAction;
	private StdAction killAllAction;
	private StdAction disperseAllAction;
	private StdAction parametersAction;
	private StdAction aboutAction;
	private StdAction manualAction;
	private StdAction checkLastVersionAction;
	private StdAction netConfigAction;
	private TrackAction trackAction;
	private StdAction feedAction;
	private StdAction weakenAction;
	private StdAction killAction;
	private StdAction copyAction;
	private StdAction saveImageAction;
	private StdAction reviveAction;
	private StdAction disperseAction;
	private StdAction reproduceAction;
	private StdAction rejuvenateAction;
	private StdAction exportAction;
	private StdAction pasteAction;
	private StdAction randomCreateAction;
	private StdAction importAction;
	private StdAction createBallAction;
	private StdAction zoomInAction;
	private StdAction zoomOutAction;

	public static ActionFactory getInstance() throws NullPointerException {
		if (actionFactory == null)
			throw new NullPointerException("Not initialized");
		return actionFactory;
	}

	public static void init(MainWindow mainWindow, Process process,
			CurrentWorld currentWorld, VisibleWorld visibleWorld,
			NetServerThread netServer) {
		actionFactory = new ActionFactory(mainWindow, process, currentWorld,
				visibleWorld, netServer);
	}

	protected ActionFactory(MainWindow mainWindow, Process process,
			CurrentWorld currentWorld, VisibleWorld visibleWorld,
			NetServerThread netServer) {
		newGameAction = new NewGameAction(mainWindow,
				"T_NEW", imagePath + "menu_new.png", //$NON-NLS-1$ //$NON-NLS-2$
				"T_NEW_WORLD"); //$NON-NLS-1$
		startStopAction = new StartStopAction(
				process,
				"T_RESUME", "T_PAUSE", imagePath + "menu_start.png", //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
				"../biogenesis/images/menu_stop.png", "T_RESUME_PROCESS", "T_PAUSE_PROCESS"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		saveGameAction = new SaveGameAction(process, currentWorld,
				"T_SAVE", imagePath + "menu_save.png", //$NON-NLS-1$//$NON-NLS-2$
				"T_SAVE_WORLD"); //$NON-NLS-1$
		increaseCO2Action = new IncreaseCO2Action(currentWorld,
				"T_INCREASE_CO2", imagePath + "menu_increase_co2.png", //$NON-NLS-1$ //$NON-NLS-2$
				"T_INCREASE_CO2"); //$NON-NLS-1$
		decreaseCO2Action = new DecreaseCO2Action(currentWorld,
				"T_DECREASE_CO2", imagePath + "menu_decrease_co2.png", //$NON-NLS-1$ //$NON-NLS-2$
				"T_DECREASE_CO2"); //$NON-NLS-1$
		manageConnectionsAction = new ManageConnectionsAction(
				mainWindow,
				netServer,
				"T_MANAGE_CONNECTIONS", imagePath + "menu_manage_connections.png", //$NON-NLS-1$//$NON-NLS-2$
				"T_MANAGE_CONNECTIONS"); //$NON-NLS-1$
		abortTrackingAction = new AbortTrackingAction(
				mainWindow.getOrganismTracker(),
				"T_ABORT_TRACKING", imagePath + "menu_stop_tracking.png", //$NON-NLS-1$//$NON-NLS-2$
				"T_ABORT_TRACKING"); //$NON-NLS-1$
		openGameAction = new OpenGameAction(process, currentWorld,
				"T_OPEN", null, "T_OPEN_WORLD"); //$NON-NLS-1$//$NON-NLS-2$
		saveGameAsAction = new SaveGameAsAction(process, currentWorld,
				"T_SAVE_AS", null, "T_SAVE_WORLD_AS"); //$NON-NLS-1$//$NON-NLS-2$
		quitAction = new QuitAction(mainWindow, netServer,
				"T_QUIT", null, "T_QUIT_PROGRAM"); //$NON-NLS-1$ //$NON-NLS-2$
		statisticsAction = new StatisticsAction(mainWindow, currentWorld,
				"T_STATISTICS", null, "T_WORLD_STATISTICS"); //$NON-NLS-1$ //$NON-NLS-2$
		labAction = new LabAction(mainWindow,
				"T_GENETIC_LABORATORY", null, "T_GENETIC_LABORATORY"); //$NON-NLS-1$ //$NON-NLS-2$
		killAllAction = new KillAllAction(currentWorld,
				"T_KILL_ALL", null, "T_KILL_ALL_ORGANISMS"); //$NON-NLS-1$ //$NON-NLS-2$
		disperseAllAction = new DisperseAllAction(currentWorld,
				"T_DISPERSE_ALL", null, "T_DISPERSE_ALL_DEAD_ORGANISMS"); //$NON-NLS-1$ //$NON-NLS-2$
		parametersAction = new ParametersAction(mainWindow,
				"T_PARAMETERS", null, "T_EDIT_PARAMETERS"); //$NON-NLS-1$ //$NON-NLS-2$
		aboutAction = new AboutAction(new ImageIcon(mainWindow.getIconImage()), mainWindow,
				"T_ABOUT", null, "T_ABOUT"); //$NON-NLS-1$//$NON-NLS-2$
		manualAction = new ManualAction("T_USER_MANUAL", null, "T_USER_MANUAL"); //$NON-NLS-1$//$NON-NLS-2$
		checkLastVersionAction = new CheckLastVersionAction(mainWindow,
				"T_CHECK_LAST_VERSION", null, "T_CHECK_LAST_VERSION"); //$NON-NLS-1$ //$NON-NLS-2$
		netConfigAction = new NetConfigAction(mainWindow, netServer,
				"T_CONFIGURE_NETWORK", null, "T_CONFIGURE_NETWORK"); //$NON-NLS-1$ //$NON-NLS-2$

		trackAction = new TrackAction(mainWindow.getOrganismTracker(),
				visibleWorld,
				"T_TRACK", "T_ABORT_TRACKING", imagePath + "menu_track.png", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				"T_TRACK_ORGANISM", "T_ABORT_TRACKING_ORGANISM"); //$NON-NLS-1$ //$NON-NLS-2$
		feedAction = new FeedAction(currentWorld, visibleWorld,
				"T_FEED", imagePath + "menu_feed.png", //$NON-NLS-1$//$NON-NLS-2$
				"T_FEED_ORGANISM"); //$NON-NLS-1$
		weakenAction = new WeakenAction(visibleWorld,
				"T_WEAKEN", imagePath + "menu_weaken.png", //$NON-NLS-1$//$NON-NLS-2$
				"T_WEAKEN_ORGANISM"); //$NON-NLS-1$
		killAction = new KillAction(visibleWorld,
				"T_KILL", imagePath + "menu_kill.png", //$NON-NLS-1$//$NON-NLS-2$
				"T_KILL_ORGANISM"); //$NON-NLS-1$
		copyAction = new CopyAction(visibleWorld,
				"T_COPY", imagePath + "menu_copy.png", //$NON-NLS-1$//$NON-NLS-2$
				"T_COPY_GENETIC_CODE"); //$NON-NLS-1$
		saveImageAction = new SaveImageAction(process, visibleWorld,
				"T_SAVE_IMAGE", imagePath + "menu_save_image.png", //$NON-NLS-1$//$NON-NLS-2$
				"T_SAVE_IMAGE"); //$NON-NLS-1$
		reviveAction = new ReviveAction(currentWorld, mainWindow, visibleWorld,
				"T_REVIVE", imagePath + "menu_revive.png", //$NON-NLS-1$ //$NON-NLS-2$
				"T_REVIVE_ORGANISM"); //$NON-NLS-1$
		disperseAction = new DisperseAction(visibleWorld,
				"T_DISPERSE", imagePath + "menu_disperse.png", //$NON-NLS-1$ //$NON-NLS-2$
				"T_DISPERSE_ORGANISM"); //$NON-NLS-1$
		reproduceAction = new ReproduceAction(visibleWorld,
				"T_FORCE_REPRODUCTION", null, "T_FORCE_REPRODUCTION"); //$NON-NLS-1$ //$NON-NLS-2$
		rejuvenateAction = new RejuvenateAction(visibleWorld,
				"T_REJUVENATE", null, "T_REJUVENATE"); //$NON-NLS-1$ //$NON-NLS-2$
		exportAction = new ExportAction(visibleWorld,
				"T_EXPORT", null, "T_EXPORT_GENETIC_CODE"); //$NON-NLS-1$ //$NON-NLS-2$
		pasteAction = new PasteAction(visibleWorld, currentWorld,
				"T_PASTE", null, "T_PASTE_GENETIC_CODE"); //$NON-NLS-1$ //$NON-NLS-2$
		randomCreateAction = new RandomCreateAction(visibleWorld, currentWorld,
				"T_CREATE_RANDOMLY", null, "T_CREATE_RANDOMLY"); //$NON-NLS-1$ //$NON-NLS-2$
		importAction = new ImportAction(process, currentWorld, visibleWorld,
				"T_IMPORT", null, "T_IMPORT_GENETIC_CODE"); //$NON-NLS-1$ //$NON-NLS-2$
		createBallAction = new CreateBallAction(visibleWorld, currentWorld,
				"Create ball", null, "Create ball");
		zoomInAction = new ZoomInAction(visibleWorld,
				"T_ZOOM_IN", null, "T_ZOOM_IN"); //$NON-NLS-1$ //$NON-NLS-2$
		zoomOutAction = new ZoomOutAction(visibleWorld,
				"T_ZOOM_OUT", null, "T_ZOOM_OUT"); //$NON-NLS-1$ //$NON-NLS-2$

		abortTrackingAction.setEnabled(false);
		// Only enable file management menu options if at least there is
		// permission to read user's home directory
		SecurityManager sec = System.getSecurityManager();
		try {
			if (sec != null)
				sec.checkPropertyAccess("user.home"); //$NON-NLS-1$
		} catch (SecurityException ex) {
			exportAction.setEnabled(false);
			importAction.setEnabled(false);
			saveImageAction.setEnabled(false);
			openGameAction.setEnabled(false);
			saveGameAsAction.setEnabled(false);
			saveGameAction.setEnabled(false);
			manualAction.setEnabled(false);
			netConfigAction.setEnabled(false);
			manageConnectionsAction.setEnabled(false);
		}
	}

	public StdAction getNewGameAction() {
		return newGameAction;
	}

	public StartStopAction getStartStopAction() {
		return startStopAction;
	}

	public StdAction getSaveGameAction() {
		return saveGameAction;
	}

	public StdAction getIncreaseCO2Action() {
		return increaseCO2Action;
	}

	public StdAction getDecreaseCO2Action() {
		return decreaseCO2Action;
	}

	public StdAction getManageConnectionsAction() {
		return manageConnectionsAction;
	}

	public AbortTrackingAction getAbortTrackingAction() {
		return abortTrackingAction;
	}

	public StdAction getOpenGameAction() {
		return openGameAction;
	}

	public StdAction getSaveGameAsAction() {
		return saveGameAsAction;
	}

	public QuitAction getQuitAction() {
		return quitAction;
	}

	public StdAction getStatisticsAction() {
		return statisticsAction;
	}

	public StdAction getLabAction() {
		return labAction;
	}

	public StdAction getKillAllAction() {
		return killAllAction;
	}

	public StdAction getDisperseAllAction() {
		return disperseAllAction;
	}

	public StdAction getParametersAction() {
		return parametersAction;
	}

	public StdAction getAboutAction() {
		return aboutAction;
	}

	public StdAction getManualAction() {
		return manualAction;
	}

	public StdAction getCheckLastVersionAction() {
		return checkLastVersionAction;
	}

	public StdAction getNetConfigAction() {
		return netConfigAction;
	}

	public TrackAction getTrackAction() {
		return trackAction;
	}

	public StdAction getFeedAction() {
		return feedAction;
	}

	public StdAction getWeakenAction() {
		return weakenAction;
	}

	public StdAction getKillAction() {
		return killAction;
	}

	public StdAction getCopyAction() {
		return copyAction;
	}

	public StdAction getSaveImageAction() {
		return saveImageAction;
	}

	public StdAction getReviveAction() {
		return reviveAction;
	}

	public StdAction getDisperseAction() {
		return disperseAction;
	}

	public StdAction getReproduceAction() {
		return reproduceAction;
	}

	public StdAction getRejuvenateAction() {
		return rejuvenateAction;
	}

	public StdAction getExportAction() {
		return exportAction;
	}

	public StdAction getPasteAction() {
		return pasteAction;
	}

	public StdAction getRandomCreateAction() {
		return randomCreateAction;
	}

	public StdAction getImportAction() {
		return importAction;
	}
	
	public StdAction getCreateBallAction() {
		return createBallAction;
	}

	public StdAction getZoomInAction() {
		return zoomInAction;
	}

	public StdAction getZoomOutAction() {
		return zoomOutAction;
	}
}
