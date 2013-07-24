package actions;

import java.awt.event.ActionEvent;

import net.BareBonesBrowserLaunch;

import aux.Messages;

public class ManualAction extends StdAction {
	private static final long serialVersionUID = 1L;
	public ManualAction(String text, String icon_path, String desc) {
		super(text, icon_path, desc);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		BareBonesBrowserLaunch.openURL("http://biogenesis.sourceforge.net/manual.php."+Messages.getInstance().getLanguage()); //$NON-NLS-1$
	}
}