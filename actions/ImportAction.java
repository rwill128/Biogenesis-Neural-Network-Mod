package actions;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.xml.sax.SAXException;

import biogenesis.VisibleWorld;
import biogenesis.Process;
import organisms.GeneticCode;
import world.CurrentWorld;

import aux.BioFileChooser;
import aux.BioXMLParser;
import aux.Messages;

public class ImportAction extends StdAction {
	private static final long serialVersionUID = 1L;
	private Process process;
	private CurrentWorld currentWorld;
	private VisibleWorld visibleWorld;
	
	public ImportAction(Process process, CurrentWorld currentWorld, VisibleWorld visibleWorld,
			String text_key, String icon_path, String desc) {
		super(text_key, icon_path, desc);
		this.process = process;
		this.currentWorld = currentWorld;
		this.visibleWorld = visibleWorld;
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		GeneticCode g;
		boolean processState = process.isProcessActive();
		// Stop time
    	process.setProcessActive(false);
    	try {
    		JFileChooser chooser = BioFileChooser.getGeneticCodeChooser();
    		int returnVal = chooser.showOpenDialog(null);
    		if (returnVal == JFileChooser.APPROVE_OPTION) {
    			try {
    				// Read XML code from file
    				BioXMLParser parser = new BioXMLParser();
					g = parser.parseGeneticCode(chooser.getSelectedFile());
					// Create organism
					currentWorld.getWorld().createAliveAgentAtPosition(g, visibleWorld.getMouseX(),
							visibleWorld.getMouseY());
    			} catch (SAXException ex) {
    				System.err.println(ex.getMessage());
    				JOptionPane.showMessageDialog(null,Messages.getInstance().getString("T_WRONG_FILE_VERSION"),Messages.getInstance().getString("T_READ_ERROR"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
    			} catch (IOException ex) {
    				System.err.println(ex.getMessage());
    				JOptionPane.showMessageDialog(null,Messages.getInstance().getString("T_CANT_READ_FILE"),Messages.getInstance().getString("T_READ_ERROR"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
				}
    		}
    	} catch (SecurityException ex) {
    		System.err.println(ex.getMessage());
    		JOptionPane.showMessageDialog(null,Messages.getInstance().getString("T_PERMISSION_DENIED"),Messages.getInstance().getString("T_PERMISSION_DENIED"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
    	}
    	process.setProcessActive(processState);
	}
}
