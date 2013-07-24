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


import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.*;

import javax.swing.*;

import aux.Messages;
import biogenesis.Utils;

public class NetConfigWindow extends JDialog {
	private static final long serialVersionUID = Utils.FILE_VERSION;
	private JCheckBox acceptConnectionsCheck;
	private boolean acceptConnections;
	private JTextField localPortText;
	private JTextField maxConnectionsText;
	private JButton cancelButton;
	private JButton okButton;
	
	private NetServerThread netServer;
	
	public NetConfigWindow(Frame parent, NetServerThread netServerThread) {
		super(parent,Messages.getInstance().getString("T_NETWORK_CONFIGURATION"),true); //$NON-NLS-1$+
		netServer = netServerThread;
		acceptConnections = netServer.isAcceptingConnections();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);	
		setComponents();
		pack();
		setResizable(false);
		cancelButton.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(ActionEvent evt) {
            	dispose();
            }
            });
		okButton.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(ActionEvent evt) {
            	int oldPort = Utils.getLOCAL_PORT();
            	checkParams();
            	Utils.savePreferences();
            	netServer.setAcceptConnections(acceptConnections);
            	if (oldPort != Utils.getLOCAL_PORT()) {
            		netServer.closeServer();
            		if (acceptConnections)
            			netServer.startServer();
            	}
            	dispose();
            }
            });
		setVisible(true);
	}

	protected void setComponents() {
		getContentPane().setLayout(new BorderLayout());
		// Set up buttons
		JPanel buttonsPanel = new JPanel();
		okButton = new JButton(Messages.getInstance().getString("T_OK")); //$NON-NLS-1$
		cancelButton = new JButton(Messages.getInstance().getString("T_CANCEL")); //$NON-NLS-1$
		buttonsPanel.add(okButton);
		buttonsPanel.add(cancelButton);
		getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
		// Accept connections + use server
		JPanel generalPanel = new JPanel();
		generalPanel.setLayout(new BoxLayout(generalPanel,BoxLayout.Y_AXIS));
		JPanel panel = new JPanel();
		acceptConnectionsCheck = new JCheckBox(Messages.getInstance().getString("T_ALLOW_CONNECTIONS_FROM_OTHER_USERS"), //$NON-NLS-1$
				acceptConnections);
		acceptConnectionsCheck.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				acceptConnections = (arg0.getStateChange() == ItemEvent.SELECTED);	
			}
		});
		panel.add(acceptConnectionsCheck);
		generalPanel.add(panel);
		panel = new JPanel();
		JLabel label = new JLabel(Messages.getInstance().getString("T_MAXIMUM_NUMBER_OF_ALLOWED_CONNECTIONS")); //$NON-NLS-1$
		panel.add(label);
		maxConnectionsText = new JTextField(String.valueOf(Utils.getMAX_CONNECTIONS()),4);
		panel.add(maxConnectionsText);
		generalPanel.add(panel);
		panel = new JPanel();
		label = new JLabel(Messages.getInstance().getString("T_LOCAL_PORT_TO_RECEIVE_CONNECTIONS")); //$NON-NLS-1$
		panel.add(label);
		localPortText = new JTextField(String.valueOf(Utils.getLOCAL_PORT()),6);
		panel.add(localPortText);
		generalPanel.add(panel);
		getContentPane().add(generalPanel, BorderLayout.CENTER);
	}
	
	private void checkParams() {
		int i;
		Utils.setAcceptConnections(acceptConnections);
		
		try {
			i = Integer.parseInt(localPortText.getText());
			if (i>=0) Utils.setLocalPort(i);
		} catch (NumberFormatException e) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(maxConnectionsText.getText());
			if (i>=0) Utils.setMaxConnections(i);
		} catch (NumberFormatException e) {
			// Keep old value if there is a problem
		}
	}
}
