package gui;

import java.awt.FlowLayout;
import java.text.NumberFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import world.CurrentWorld;
import world.World;

import biogenesis.Process;

import aux.Messages;

public class StatusBar extends JPanel implements StatusListener {
	private static final long serialVersionUID = 1L;

	private String statusMessage=""; //$NON-NLS-1$
	private JLabel statusLabel = new JLabel(" ");
	private StringBuilder statusLabelText = new StringBuilder(100);
	private NumberFormat nf;
	private CurrentWorld currentWorld;
	private Process process;
	
	public StatusBar(Process process, CurrentWorld currentWorld) {
		this.process = process;
		this.currentWorld = currentWorld;
		statusLabel.setBorder(new EtchedBorder());
        nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(1);
		setLayout(new FlowLayout(FlowLayout.LEADING));
		add(statusLabel);
	}
	/*
	public void setWorld(CurrentWorld currentWorld) {
		this.currentWorld = currentWorld;
		setStatusMessage(Messages.getInstance().getString("T_WORLD_LOADED_SUCCESSFULLY")); //$NON-NLS-1$
	}*/
	
	public void setStatusMessage(String str) {
		statusMessage = str;
		updateStatusLabel();
	}
	
	public String getStatusMessage() {
		return statusMessage;
	}
	
	public void updateStatusLabel() {
		World world = currentWorld.getWorld();
		
		statusLabelText.setLength(0);
		statusLabelText.append(Messages.getInstance().getString("T_FPS")); //$NON-NLS-1$
		statusLabelText.append(process.getFPS());
		statusLabelText.append("     "); //$NON-NLS-1$
		statusLabelText.append(Messages.getInstance().getString("T_TIME")); //$NON-NLS-1$
		statusLabelText.append(world.getTime());
		statusLabelText.append("     "); //$NON-NLS-1$
		statusLabelText.append(Messages.getInstance().getString("T_CURRENT_POPULATION")); //$NON-NLS-1$
		statusLabelText.append(world.getPopulation());
		statusLabelText.append("     "); //$NON-NLS-1$
		statusLabelText.append(Messages.getInstance().getString("T_O2")); //$NON-NLS-1$
		statusLabelText.append(nf.format(world.getAtmosphere().getO2()));
		statusLabelText.append("     "); //$NON-NLS-1$
		statusLabelText.append(Messages.getInstance().getString("T_CO2")); //$NON-NLS-1$
		statusLabelText.append(nf.format(world.getAtmosphere().getCO2()));
		statusLabelText.append("     "); //$NON-NLS-1$
		statusLabelText.append(getStatusMessage());
		statusLabel.setText(statusLabelText.toString());
	}

	@Override
	public void updateStatus(String message) {
		setStatusMessage(message);
	}
}
