package actions;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import biogenesis.ParamDialog;

public class ParametersAction extends StdAction {
	private static final long serialVersionUID = 1L;
	private Frame parent;
	
	public ParametersAction(Frame parent, String text, String icon_path, String desc) {
		super(text, icon_path, desc);
		this.parent = parent;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		new ParamDialog(parent);
	}
}
