package actions;

import java.awt.event.ActionEvent;

import auxiliar.Messages;
import biogenesis.Utils;

public class ToggleEfficiencyModeAction extends StdAction {
	private static final long serialVersionUID = 1L;
	private String text1, text2, desc1, desc2;
	private String text1_key, text2_key, desc1_key, desc2_key;

	public ToggleEfficiencyModeAction(String text1, String text2, String icon_path, 
			String desc1, String desc2) {
		super(text1, icon_path, desc1);
		text1_key = text1;
		text2_key = text2;
		desc1_key = desc1;
		desc2_key = desc2;
		this.text1 = Messages.getInstance().getString(text1);
		this.text2 = Messages.getInstance().getString(text2);
		this.desc1 = Messages.getInstance().getString(desc1);
		this.desc2 = Messages.getInstance().getString(desc2);
		if (Utils.isEFFICIENCY_MODE())
			updateName();
	}

	private void updateName() {
		if (Utils.isEFFICIENCY_MODE()) {
			putValue(NAME, text2);
			putValue(SHORT_DESCRIPTION, desc2);
		} else {
			putValue(NAME, text1);
			putValue(SHORT_DESCRIPTION, desc1);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Utils.toggleEFFICIENCY_MODE();
		updateName();
	}
	
	@Override
	public void changeLocale() {
		super.changeLocale();
		text1 = Messages.getInstance().getString(text1_key);
		text2 = Messages.getInstance().getString(text2_key);
		desc1 = Messages.getInstance().getString(desc1_key);
		desc2 = Messages.getInstance().getString(desc2_key);
	}

}
