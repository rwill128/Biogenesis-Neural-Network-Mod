package actions;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JOptionPane;

import net.NetServerThread;
import auxiliar.Messages;
import biogenesis.MainWindow;
import biogenesis.Utils;

public class QuitAction extends StdAction implements WindowListener {
	private static final long serialVersionUID = 1L;
	private MainWindow mainWindow;
	private NetServerThread netServer;
	
	public QuitAction(MainWindow mainWindow, NetServerThread netServer, String text, String icon_path, String desc) {
		super(text, icon_path, desc);
		this.mainWindow = mainWindow;
		this.netServer = netServer;
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		quit();
	}

	/**
	 * Asks the user if the world should be saved before quitting.
	 * The user may cancel the action.
	 * 
	 * @return  true if the user agree to quit the program, false if she cancels the action.
	 */
	private boolean askQuit() {
		int save = JOptionPane.showConfirmDialog(mainWindow,Messages.getInstance().getString("T_SAVE_BEFORE_QUIT"), //$NON-NLS-1$
				Messages.getInstance().getString("T_SAVE_WORLD"),JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE); //$NON-NLS-1$
		
		if (save != JOptionPane.CANCEL_OPTION) {
			if (save == JOptionPane.YES_OPTION)
				// It would be better to get a button from the menu and call doClick on it, but
				// can't think of an easy way to find the button.
				ActionFactory.getInstance().getSaveGameAction().actionPerformed(null);
			return true;
		}
		return false;
	}
	
	private void quit() {
		if (askQuit()) {
			Utils.quitProgram(mainWindow);
			netServer.closeServer();
			try {
				System.exit(0);
			} catch (SecurityException ex) {
				mainWindow.dispose();
			}
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		;
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		;		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		quit();		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		;
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		;
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		;
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		;		
	}
}
