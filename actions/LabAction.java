package actions;

import java.awt.event.ActionEvent;

import biogenesis.MainWindow;

import gui.LabWindow;

public class LabAction extends StdAction {
	private static final long serialVersionUID = 1L;
	private MainWindow mainWindow;
	
	public LabAction(MainWindow mainWindow, String text, String icon_path, String desc) {
		super(text, icon_path, desc);
		this.mainWindow = mainWindow;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		new LabWindow(mainWindow);
	}
}
