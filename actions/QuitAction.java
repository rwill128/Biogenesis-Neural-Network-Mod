package actions;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import net.NetServerThread;

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
	
	private void quit() {
		if (mainWindow.quit()) {
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
