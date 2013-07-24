package actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import net.CheckVersionThread;

public class CheckLastVersionAction extends StdAction {
	private static final long serialVersionUID = 1L;
	private Component parent;
	
	public CheckLastVersionAction(Component parent, String text, String icon_path, String desc) {
		super(text, icon_path, desc);
		this.parent = parent;
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		CheckVersionThread thread = new CheckVersionThread(parent);
		thread.start();
	}
}