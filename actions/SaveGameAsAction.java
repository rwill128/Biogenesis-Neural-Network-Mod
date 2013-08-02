package actions;

import java.awt.event.ActionEvent;

import world.CurrentWorld;

import aux.BioSaver;

import biogenesis.Process;

public class SaveGameAsAction extends StdAction {
	private static final long serialVersionUID = 1L;
	private Process process;
	private CurrentWorld currentWorld;
	
	public SaveGameAsAction(Process process, CurrentWorld currentWorld, String text, String icon_path, String desc) {
		super(text, icon_path, desc);
		this.process = process;
		this.currentWorld = currentWorld;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		boolean processState = process.isProcessActive();
		process.setProcessActive(false);
		BioSaver.saveObjectAs(currentWorld.getWorld());
		process.setProcessActive(processState);
	}
}
