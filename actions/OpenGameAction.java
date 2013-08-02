package actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import aux.BioFileChooser;
import aux.Messages;

import world.CurrentWorld;
import world.World;

import biogenesis.Process;

public class OpenGameAction extends StdAction {
	private static final long serialVersionUID = 1L;
	private Process process;
	private CurrentWorld currentWorld;
	
	public OpenGameAction(Process process, CurrentWorld currentWorld, String text, String icon_path, String desc) {
		super(text, icon_path, desc);
		this.process = process;
		this.currentWorld = currentWorld;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		World newWorld;
		BioFileChooser fileChooser = BioFileChooser.getWorldChooser();
		boolean processState = process.isProcessActive();
		process.setProcessActive(false);
		try {
			int returnVal = fileChooser.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				ObjectInputStream inputStream;
				try {
					File f = fileChooser.getSelectedFile();
					FileInputStream fileStream = new FileInputStream(f);
					inputStream = new ObjectInputStream(fileStream);
					newWorld = (World) inputStream.readObject();
					inputStream.close();
					fileChooser.setLastFile(f);
					currentWorld.setWorld(newWorld);
					// TODO: revisar això
					// assignar a visualWorld el nou món
					//scrollPane.setViewportView(_visibleWorld);
					// Assegurem que s'ha dibuixat el m�n
					//_visibleWorld.repaint();
				} catch (IOException ex) {
					System.err.println(ex.getMessage());
					JOptionPane.showMessageDialog(null,Messages.getInstance().getString("T_CANT_READ_FILE"),Messages.getInstance().getString("T_READ_ERROR"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
				} catch (ClassNotFoundException ex) {
					System.err.println(ex.getMessage());
					JOptionPane.showMessageDialog(null,Messages.getInstance().getString("T_WRONG_FILE_TYPE"),Messages.getInstance().getString("T_READ_ERROR"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
				} catch (ClassCastException ex) {
					System.err.println(ex.getMessage());
					JOptionPane.showMessageDialog(null,Messages.getInstance().getString("T_WRONG_FILE_VERSION"),Messages.getInstance().getString("T_READ_ERROR"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		} catch (SecurityException ex) {
			System.err.println(ex.getMessage());
			JOptionPane.showMessageDialog(null,Messages.getInstance().getString("T_PERMISSION_DENIED"),Messages.getInstance().getString("T_PERMISSION_DENIED"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
		}
		process.setProcessActive(processState);
	}
}