package gui;


import java.awt.Color;

import javax.swing.JComboBox;

import organisms.Pigment;

import aux.Messages;
import biogenesis.Utils;


public class ColorComboBox extends JComboBox {
	private static final long serialVersionUID = Utils.VERSION;
	private String[] colorValues = new String[colorStrings.length];
	private static final String[] colorStrings = {"T_RED", "T_GREEN", "T_BLUE", "T_CYAN",
		"T_WHITE", "T_GRAY", "T_YELLOW"};
	
	public void changeLocale() {
		for (int i=0; i<colorValues.length; i++)
			colorValues[i] = Messages.getInstance().getString(colorStrings[i]);
	}
	
	public ColorComboBox(Color c) {
		super();
		changeLocale();
		for (int i=0; i<colorValues.length; i++)
			addItem(colorValues[i]);
		setSelectedColor(c);
	}
	
	public Color getSelectedColor() {
		switch (getSelectedIndex()) {
		case 0: return Color.RED;
		case 1: return Color.GREEN;
		case 2: return Color.BLUE;
		case 3: return Color.CYAN;
		case 4: return Color.WHITE;
		case 5: return Color.GRAY;
		case 6: return Color.YELLOW;
		default: return Utils.ColorBROWN;
		}
	}
	
	public Pigment getSelectedPigment() {
		return Pigment.values()[getSelectedIndex()+1]; // +1 because brown is 0
	}
	
	public void setSelectedColor(Color c) {
		if (c.equals(Color.RED)) setSelectedIndex(0);
		if (c.equals(Color.GREEN)) setSelectedIndex(1);
		if (c.equals(Color.BLUE)) setSelectedIndex(2);
		if (c.equals(Color.CYAN)) setSelectedIndex(3);
		if (c.equals(Color.WHITE)) setSelectedIndex(4);
		if (c.equals(Color.GRAY)) setSelectedIndex(5);
		if (c.equals(Color.YELLOW)) setSelectedIndex(6);
	}
}
