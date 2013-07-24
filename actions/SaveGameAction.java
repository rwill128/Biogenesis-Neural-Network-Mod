package actions;

import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;

import biogenesis.Process;

import world.CurrentWorld;

import aux.BioFileChooser;
import aux.BioSaver;
import aux.Messages;

public class SaveGameAction extends StdAction {
	private static final long serialVersionUID = 1L;
	private CurrentWorld currentWorld;
	private Process process;
	
	public SaveGameAction(Process process, CurrentWorld currentWorld, String text, String icon_path, String desc) {
		super(text, icon_path, desc);
		this.process = process;
		this.currentWorld = currentWorld;
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(Messages.getInstance().getString("T_SAVE_ACCELERATOR")));
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		BioFileChooser fileChooser = BioFileChooser.getWorldChooser();
		
		if (fileChooser.getLastFile() != null)
			BioSaver.saveObject(currentWorld.getWorld(), fileChooser.getLastFile());
		else {
			boolean processState = process.isProcessActive();
			process.activateProcess(false);
			BioSaver.saveObjectAs(currentWorld.getWorld());
			process.activateProcess(processState);
		}
	}
		
	@Override
	public void changeLocale() {
		super.changeLocale();
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(Messages.getInstance().getString("T_SAVE_ACCELERATOR")));
	}
}
