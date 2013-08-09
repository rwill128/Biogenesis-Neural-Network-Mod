package actions;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import auxiliar.Messages;

public class AboutAction extends StdAction {
	private static final long serialVersionUID = 1L;
	private ImageIcon imageIcon;
	private Frame parent;
	
	public AboutAction(ImageIcon imageIcon, Frame parent, String text, String icon_path, String desc) {
		super(text, icon_path, desc);
		this.imageIcon = imageIcon;
		this.parent = parent;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String aboutString = Messages.getInstance().getString("T_PROGRAM_NAME")+"http://biogenesis.sourceforge.net/"  //$NON-NLS-1$//$NON-NLS-2$
		+Messages.getInstance().getString("T_VERSION")+ //$NON-NLS-1$ 
				Messages.getInstance().getString("T_COPYRIGHT")+"joanq@users.sourceforge.net\n"+ //$NON-NLS-1$ //$NON-NLS-2$
				Messages.getInstance().getString("T_ARTWORK_BY")+" Ananda Daydream, Florian Haag (http://toolbaricons.sourceforge.net/)";  //$NON-NLS-1$//$NON-NLS-2$
		JOptionPane.showMessageDialog(parent, aboutString, Messages.getInstance().getString("T_ABOUT"), //$NON-NLS-1$
				JOptionPane.INFORMATION_MESSAGE,imageIcon);
	}
}