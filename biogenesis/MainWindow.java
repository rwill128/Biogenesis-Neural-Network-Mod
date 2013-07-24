/* Copyright (C) 2006-2011  Joan Queralt Molina
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

import gui.InfoToolbar;
import gui.OrganismTracker;
import gui.StatusBar;

import javax.swing.*;

import organisms.AliveAgentFactory;

import menu.MultipleToolBar;

import world.CurrentWorld;

import aux.*;

import java.io.*;
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

	private CurrentWorld currentWorld;
	private Process process;
	private File gameFile = null;
	private OrganismTracker scrollPane;
	private MultipleToolBar toolBar = new MultipleToolBar(Messages.getInstance().getString("T_PROGRAM_NAME")); //$NON-NLS-1$
	private StatusBar statusBar;
	private InfoToolbar infoToolbar;
		
	public InfoToolbar getInfoPanel() {
		return infoToolbar;
	}
	
	public StatusBar getStatusBar() {
		return statusBar;
	}
	
	public OrganismTracker getOrganismTracker() {
		return scrollPane;
	}
	
	public MainWindow(CurrentWorld currentWorld, Process process) {
		this.process = process;
		this.currentWorld = currentWorld;
		scrollPane = new OrganismTracker();
		Messages.getInstance().addLocaleChangeListener(this);
		setControls();
		configureApp();
	}

	public MultipleToolBar getToolBar() {
		return toolBar;
	}
	
	public void newGame() {
		currentWorld.getWorld().genesis(AliveAgentFactory.getInstance());
		scrollPane.setTrackedOrganism(null);
		scrollPane.repaint();
		//scrollPane.setViewportView(_visibleWorld);
		process.resetNFrames();
		process.activateProcess(true);
		statusBar.setStatusMessage(Messages.getInstance().getString("T_NEW_WORLD_CREATED")); //$NON-NLS-1$
	}
	
	public boolean quit() {
		int save = JOptionPane.showConfirmDialog(this,Messages.getInstance().getString("T_SAVE_BEFORE_QUIT"), //$NON-NLS-1$
				Messages.getInstance().getString("T_SAVE_WORLD"),JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE); //$NON-NLS-1$
		
		if (save != JOptionPane.CANCEL_OPTION) {
			if (save == JOptionPane.YES_OPTION) {
				if (gameFile != null)
					BioSaver.saveObject(currentWorld.getWorld(), gameFile);
				else {
					if (BioSaver.saveObjectAs(currentWorld.getWorld()) == null)
						return false;
				}
			}
			return true;
		}
		return false;
	}

	private void setControls () {
		JPanel centralPanel = new JPanel();
		centralPanel.setLayout(new BorderLayout());
		
        scrollPane.getHorizontalScrollBar().setUnitIncrement(10);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.setMaxSpeed((int)Utils.MAX_VEL);
        //scrollPane.addObserver(abortTrackingAction);
        setLocation(Utils.WINDOW_X, Utils.WINDOW_Y);
        setExtendedState(Utils.WINDOW_STATE);
        getContentPane().setLayout(new BorderLayout());
        
        infoToolbar = new InfoToolbar(null, this);
        centralPanel.add(scrollPane, BorderLayout.CENTER);
        centralPanel.add(infoToolbar, BorderLayout.SOUTH);
        
        getContentPane().add(centralPanel, BorderLayout.CENTER);
        
        statusBar = new StatusBar(process, currentWorld	);
        getContentPane().add(statusBar, BorderLayout.SOUTH);
        getContentPane().add(toolBar, BorderLayout.NORTH);
    }

	public void configureApp() {
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
	
	@Override
	public void dispose() {
		Messages.getInstance().removeLocaleChangeListener(this);
		process.cancel();
		super.dispose();
	}

	@Override
	public void time() {
		if (process.getNFrames() % 20 == 0) {
			statusBar.updateStatusLabel();
			getInfoPanel().recalculate();
		}
		scrollPane.track();
	}
}
