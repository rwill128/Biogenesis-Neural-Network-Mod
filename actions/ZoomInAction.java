package actions;

import java.awt.event.ActionEvent;

import biogenesis.Utils;
import biogenesis.VisibleWorld;

public class ZoomInAction extends StdAction {
	private static final long serialVersionUID = 1L;
	private VisibleWorld visibleWorld;
	
	public ZoomInAction(VisibleWorld visibleWorld,
			String text_key, String icon_path, String desc) {
		super(text_key, icon_path, desc);
		this.visibleWorld = visibleWorld;
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		visibleWorld.setZoomFactor(visibleWorld.getZoomFactor() * Utils.getZOOM_FACTOR_CHANGE());
	}
}
