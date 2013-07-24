package actions;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import net.NetConfigWindow;
import net.NetServerThread;

public class NetConfigAction extends StdAction {
	private static final long serialVersionUID = 1L;
	private Frame parent;
	private NetServerThread netServer;
	
	public NetConfigAction(Frame parent, NetServerThread netServer, String text, String icon_path, String desc) {
		super(text, icon_path, desc);
		this.parent = parent;
		this.netServer = netServer;
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		new NetConfigWindow(parent, netServer);
	}
}
