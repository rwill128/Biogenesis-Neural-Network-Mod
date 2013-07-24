package actions;

import gui.StatisticsWindow;

import java.awt.event.ActionEvent;

import biogenesis.MainWindow;

import world.CurrentWorld;

public class StatisticsAction extends StdAction {
	private static final long serialVersionUID = 1L;
	private MainWindow mainWindow;
	private CurrentWorld currentWorld;
	private StatisticsWindow statisticsWindow;
	
	public StatisticsAction(MainWindow mainWindow, CurrentWorld currentWorld, String text, String icon_path, String desc) {
		super(text, icon_path, desc);
		this.mainWindow = mainWindow;
		this.currentWorld = currentWorld;
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		if (statisticsWindow != null)
			statisticsWindow.dispose();
		statisticsWindow = new StatisticsWindow(mainWindow, currentWorld);
	}
}
