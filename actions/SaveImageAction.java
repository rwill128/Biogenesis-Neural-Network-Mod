package actions;

import gui.OrganismSelector;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import agents.AliveAgent;
import auxiliar.BioFileChooser;
import auxiliar.Messages;
import biogenesis.Process;

public class SaveImageAction extends StdAction {
	private static final long serialVersionUID = 1L;
	private Process process;
	private OrganismSelector selector;
	
	public SaveImageAction(Process process, OrganismSelector selector, String text_key,
			String icon_path, String desc) {
		super(text_key, icon_path, desc);
		this.process = process;
		this.selector = selector;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		AliveAgent o = selector.getSelectedAgent();
		if (o != null && o.isAlive()) {
			boolean processState =process.isProcessActive();
			// Stop time while asking for a file name
			process.setProcessActive(false);
			// Get the image to save
			BufferedImage image = o.getImage();
			try {
				// Ask for file name
				BioFileChooser chooser = BioFileChooser.getImageChooser();
				int returnVal = chooser.showSaveDialog(null);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					int canWrite = JOptionPane.YES_OPTION;
					File f = chooser.getSelectedFile();
					// Check if file already exists and ask for confirmation
					if (f.exists()) {
						canWrite = JOptionPane.showConfirmDialog(null,Messages.getInstance().getString("T_CONFIRM_FILE_OVERRIDE"), //$NON-NLS-1$
								Messages.getInstance().getString("T_FILE_EXISTS"),JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
					}
					if (canWrite == JOptionPane.YES_OPTION) {
						// Write image to file
						try {
							ImageIO.write(image,"PNG",f); //$NON-NLS-1$
						} catch (FileNotFoundException ex) {
							System.err.println(ex.getMessage());
						} catch (IOException ex) {
							System.err.println(ex.getMessage());
						}
					}
				}
			} catch (SecurityException ex) {
				System.err.println(ex.getMessage());
				JOptionPane.showMessageDialog(null,Messages.getInstance().getString("T_PERMISSION_DENIED"),Messages.getInstance().getString("T_PERMISSION_DENIED"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			}
			process.setProcessActive(processState);
		}
	}
}
