/* Copyright (C) 2006-2010  Joan Queralt Molina
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */
package net;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;

import aux.Messages;
import biogenesis.Utils;

public class NetConnectionsWindow extends JDialog {
	private static final long serialVersionUID = Utils.FILE_VERSION;
	
	private JTextField ipText, portText;
	private JPanel connectionsPanel;
	private JScrollPane connectionsScroll;
	
	private NetServerThread netServer;
	
	public String getPortText() {
		return portText.getText();
	}
	public String getIPText() {
		return ipText.getText();
	}
	
	public NetConnectionsWindow(Frame parent, NetServerThread netServerThread) {
		super(parent);
		netServer = netServerThread;
		setTitle(Messages.getInstance().getString("T_NETWORK_CONNECTIONS")); //$NON-NLS-1$
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);	
		setComponents();
		pack();
		setResizable(false);
		setVisible(true);
	}
	
	private void setComponents() {
		JPanel newConnectionPanel = new JPanel();
		newConnectionPanel.add(new JLabel(Messages.getInstance().getString("T_IP"))); //$NON-NLS-1$
		ipText = new JTextField(15);
		newConnectionPanel.add(ipText);
		newConnectionPanel.add(new JLabel(Messages.getInstance().getString("T_PORT"))); //$NON-NLS-1$
		portText = new JTextField(5);
		newConnectionPanel.add(portText);
		JButton newConnectionButton = new JButton(Messages.getInstance().getString("T_NEW_CONNECTION")); //$NON-NLS-1$
		newConnectionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				try {
					int port = Integer.parseInt(getPortText());
					if (port > 0) {
						try {
							InetAddress address = InetAddress.getByName(getIPText());
							netServer.startServer();
							Connection connection = netServer.newConnection(address, port);
							if (connection != null)
								connection.connect();
							refreshConnectionsPanel();
						} catch (UnknownHostException e) {
							JOptionPane.showMessageDialog(null,Messages.getInstance().getString("T_CONNECTION_FAILED"), //$NON-NLS-1$
									Messages.getInstance().getString("T_CANT_STABLISH_CONNECTION")+e.getLocalizedMessage(),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
						}
					}
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null,Messages.getInstance().getString("T_CONNECTION_FAILED"), //$NON-NLS-1$
							Messages.getInstance().getString("T_PORT_MUST_BE_A_NUMBER"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
				}
			}
		});
		newConnectionPanel.add(newConnectionButton);
		
		connectionsPanel = new JPanel();
		connectionsScroll = new JScrollPane(connectionsPanel);
		connectionsScroll.setPreferredSize(new Dimension(440,300));
		refreshConnectionsPanel();
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(newConnectionPanel, BorderLayout.NORTH);
		getContentPane().add(connectionsScroll,BorderLayout.CENTER);
	}
	
	protected void refreshConnectionsPanel() {
		int i;
		Iterator<Connection> it;
		GridBagConstraints constraints = new GridBagConstraints();
		Connection c;
		List<Connection> connections = netServer.getConnections();
		
		connectionsPanel.removeAll();	
		constraints.insets = new Insets(2,4,2,4);
		connectionsPanel.setLayout(new GridBagLayout());
		
		synchronized(connections) {
			for (i=0, it = connections.iterator(); it.hasNext(); i++) {
				c = it.next();
				constraints.gridx = 0;
				constraints.gridy = i;
				connectionsPanel.add(new JLabel(c.getRemoteAddress().toString()), constraints);
				constraints.gridx = 1;
				connectionsPanel.add(new JLabel(Integer.toString(c.getRemotePort())), constraints);
				JButton button = new JButton(Messages.getInstance().getString("T_DISCONNECT")); //$NON-NLS-1$
				button.addActionListener(new disconnectAction(c));
				constraints.gridx = 2;
				connectionsPanel.add(button, constraints);
			}
		}
		validate();
		repaint();
	}
	
	private class disconnectAction implements ActionListener {
		private Connection connection;
		
		public disconnectAction(Connection c) {
			connection = c;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			connection.send(NetServerThread.DISCONNECT);
			connection.setState(Connection.STATE_DISCONNECTED);
			refreshConnectionsPanel();
		}
	}
}
