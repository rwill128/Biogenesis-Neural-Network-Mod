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

import gui.InfoToolBar;
import gui.OrganismTracker;
import gui.StatusBar;

import javax.swing.*;

import organisms.AliveAgentFactory;

import menu.MultipleToolBar;

import world.CurrentWorld;

import aux.*;

import java.awt.*;

/**
 * This class represents the main application window. It includes the menu, the
 * space where the world will be drawn, the tool bar and the status bar.
 *
 */
public class MainWindow extends JFrame implements TimeListener, LocaleChangeListener {
	/**
	 * The version of this class.
	 */
	private static final long serialVersionUID = Utils.FILE_VERSION;

	/**
	 * A reference to the world currently displayed.
	 */
	private CurrentWorld currentWorld;
	/**
	 * The timer that controls the simulation process.
	 */
	private Process process;
	/**
	 * The scroll panel that tracks the selected organism (if any).
	 */
	private OrganismTracker organismTracker;
	/**
	 * The main window tool bar.
	 */
	private MultipleToolBar toolBar = new MultipleToolBar(Messages.getInstance().getString("T_PROGRAM_NAME")); //$NON-NLS-1$
	/**
	 * The main window status bar.
	 */
	private StatusBar statusBar;
	/**
	 * The info tool bar shows information about the selected agent.
	 */
	private InfoToolBar infoToolBar;
	
	/** 
	 * Getter for the tool bar, that shows different options depending
	 * on the selected agent.
	 * 
	 * @return  The main window tool bar.
	 */
	public MultipleToolBar getToolBar() {
		return toolBar;
	}
	/**
	 * Getter for the info tool bar, that shows info about
	 * the selected organisms.
	 * 
	 * @return  The main window info tool bar.
	 */
	public InfoToolBar getInfoToolBar() {
		return infoToolBar;
	}
	/**
	 * Getter for the status bar, used to show messages about the state
	 * of the world or the application.
	 * 
	 * @return  The main window status bar.
	 */
	public StatusBar getStatusBar() {
		return statusBar;
	}
	/**
	 * Getter for the organism tracker, the scroll panel that automatically
	 * tracks the selected agent.
	 * 
	 * @return  The main window organism tracker.
	 */
	public OrganismTracker getOrganismTracker() {
		return organismTracker;
	}
	/**
	 * Constructor that receives the current world and the process timer. The current
	 * world will be used to get a reference to the active world every time is needed.
	 * The process is used to know when to update the status bar and to activate or pause
	 * the simulation when needed.
	 * 
	 * This constructor creates and sets up all the elements of the main window.
	 * 
	 * @param currentWorld  A reference to the current world for this instance of the application.
	 * @param process  A reference to the process timer used.
	 */
	public MainWindow(CurrentWorld currentWorld, Process process) {
		this.process = process;
		this.currentWorld = currentWorld;
		organismTracker = new OrganismTracker();
		Messages.getInstance().addLocaleChangeListener(this);
		setControls();
		configureApp();
	}
	/**
	 * Starts a new simulation. Resets the world, the process timer and the main window controls.
	 */
	public void newGame() {
		currentWorld.getWorld().genesis(AliveAgentFactory.getInstance());
		organismTracker.setTrackedOrganism(null);
		organismTracker.repaint();
		process.resetNFrames();
		process.setProcessActive(true);
		statusBar.setStatusMessage(Messages.getInstance().getString("T_NEW_WORLD_CREATED")); //$NON-NLS-1$
	}
	/**
	 * Creates and configures the main window controls.
	 */
	private void setControls () {
		JPanel centralPanel = new JPanel();
		centralPanel.setLayout(new BorderLayout());
		
        organismTracker.getHorizontalScrollBar().setUnitIncrement(10);
        organismTracker.getVerticalScrollBar().setUnitIncrement(10);
        organismTracker.setMaxSpeed((int)Utils.MAX_VEL);
        //scrollPane.addObserver(abortTrackingAction);
        setLocation(Utils.WINDOW_X, Utils.WINDOW_Y);
        setExtendedState(Utils.WINDOW_STATE);
        getContentPane().setLayout(new BorderLayout());
        
        infoToolBar = new InfoToolBar(null, this);
        centralPanel.add(organismTracker, BorderLayout.CENTER);
        centralPanel.add(infoToolBar, BorderLayout.SOUTH);
        
        getContentPane().add(centralPanel, BorderLayout.CENTER);
        
        statusBar = new StatusBar(process, currentWorld	);
        getContentPane().add(statusBar, BorderLayout.SOUTH);
        getContentPane().add(toolBar, BorderLayout.NORTH);
    }
	/**
	 * Configure general options.
	 */
	private void configureApp() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setTitle(Messages.getInstance().getString("T_BIOGENESIS")); //$NON-NLS-1$
		UIManager.put("OptionPane.yesButtonText", Messages.getInstance().getString("T_YES"));
		UIManager.put("OptionPane.noButtonText", Messages.getInstance().getString("T_NO"));
		UIManager.put("OptionPane.cancelButtonText", Messages.getInstance().getString("T_CANCEL"));
		
		setResizable(true);
		setSize(new Dimension(Utils.WINDOW_WIDTH,Utils.WINDOW_HEIGHT));
		setVisible(true);
		//abortTrackingAction.setEnabled(scrollPane.getTrackedOrganism() != null);
	}
	
	@Override
	public void changeLocale() {
		UIManager.put("OptionPane.yesButtonText", Messages.getInstance().getString("T_YES"));
		UIManager.put("OptionPane.noButtonText", Messages.getInstance().getString("T_NO"));
		UIManager.put("OptionPane.cancelButtonText", Messages.getInstance().getString("T_CANCEL"));
		setTitle(Messages.getInstance().getString("T_BIOGENESIS")); //$NON-NLS-1$
	}
	/**
	 * In addition to calling Window.dispose(), removes itself as locale listener and
	 * cancels the process timer.
	 */
	@Override
	public void dispose() {
		Messages.getInstance().removeLocaleChangeListener(this);
		process.cancel();
		super.dispose();
	}
	/**
	 * Updates the status bar, the info tool bar and the organism tracker.
	 */
	@Override
	public void time() {
		if (process.getNFrames() % 20 == 0) {
			statusBar.updateStatusLabel();
			getInfoToolBar().recalculate();
		}
		organismTracker.track();
	}
}
