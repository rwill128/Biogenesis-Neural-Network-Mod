package menu;

import javax.swing.JMenu;

import aux.LocaleChangeListener;
import aux.Messages;

public class LocalizedMenu extends JMenu implements LocaleChangeListener {
	private static final long serialVersionUID = 1L;
	private String name;

	public LocalizedMenu(String s) {
		super(s);
		this.name = s;
		changeLocale();
		Messages.getInstance().addLocaleChangeListener(this);
	}

	@Override
	public void changeLocale() {
		setText(Messages.getInstance().getString(name));
		setMnemonic(Messages.getInstance().getMnemonic(name).intValue());
	}
}
