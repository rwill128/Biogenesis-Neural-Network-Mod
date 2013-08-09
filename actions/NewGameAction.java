package actions;

import java.awt.event.ActionEvent;

import auxiliar.BioFileChooser;
import biogenesis.MainWindow;

public class NewGameAction extends StdAction {
	private static final long serialVersionUID = 1L;
	private MainWindow mainWindow;
	
	public NewGameAction(MainWindow mainWindow, String text_key, String icon_path, String desc) {
		super(text_key, icon_path, desc);
		this.mainWindow = mainWindow;
	}
	
	public void setMainWindow(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		mainWindow.newGame();
		BioFileChooser.getWorldChooser().setLastFile(null);
	}
}
