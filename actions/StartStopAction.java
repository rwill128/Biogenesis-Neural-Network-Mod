package actions;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import aux.Messages;
import aux.PauseListener;

import biogenesis.Process;

public class StartStopAction extends StdAction implements PauseListener {
	private static final long serialVersionUID = 1L;
	private String name2;
	private String description2;
	private Integer mnemonic2;
	private ImageIcon icon2;
	
	private String name2_key;
	private String desc2_key;
	
	private boolean active;
	private Process process;
	
	public StartStopAction(Process process, String text1, String text2, String icon_path1,
			String icon_path2, String desc1, String desc2) {
		super(text1, icon_path1, desc1); 
		name2 = Messages.getInstance().getString(text2);
		description2 = Messages.getInstance().getString(desc2);
		icon2 = createIcon(icon_path2);
		mnemonic2 = Messages.getInstance().getMnemonic(text2);
		name2_key = text2;
		desc2_key = desc2;
		active = false;
		this.process = process;
		
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(Messages.getInstance().getString("T_PAUSE_ACCELERATOR")));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (process.isProcessActive()) {
			process.activateProcess(false);
			//statusBar.setStatusMessage(Messages.getInstance().getString("T_GAME_PAUSED")); //$NON-NLS-1$
		} else {
			process.activateProcess(true);
	    	//statusBar.setStatusMessage(Messages.getInstance().getString("T_GAME_RESUMED")); //$NON-NLS-1$
		}
	}
	
	public void toggle() {
		String aux;
		ImageIcon auxicon;
		Integer auxmnemonic;
		aux = (String) getValue(NAME);
		putValue(NAME, name2);
		name2 = aux;
		aux = (String) getValue(SHORT_DESCRIPTION);
		putValue(SHORT_DESCRIPTION, description2);
		description2 = aux;
		auxicon = (ImageIcon) getValue(LARGE_ICON_KEY);
		putValue(LARGE_ICON_KEY, icon2);
		icon2 = auxicon;
		auxmnemonic = (Integer) getValue(MNEMONIC_KEY);
		putValue(MNEMONIC_KEY, mnemonic2);
		mnemonic2 = auxmnemonic;
		active = !active;
	}
	
	@Override
	public void changeLocale() {
		super.changeLocale();
		name2 = Messages.getInstance().getString(name2_key);
		description2 = Messages.getInstance().getString(desc2_key);
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(Messages.getInstance().getString("T_PAUSE_ACCELERATOR")));
	}
	
	public void setActive(boolean newState) {
		if (newState != active) {
			toggle();
			active = newState;
		}
	}
	
	public boolean isActive() {
		return active;
	}

	@Override
	public void pausePerformed(boolean paused) {
		toggle();
	}
}