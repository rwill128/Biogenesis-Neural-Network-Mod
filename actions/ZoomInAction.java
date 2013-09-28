package actions;

import java.awt.event.ActionEvent;
import biogenesis.VisibleWorld;

public class ZoomInAction extends StdAction {
	private static final long serialVersionUID = 1L;
	private VisibleWorld visibleWorld;
	static final double ZOOM_FACTOR_CHANGE = 1.3;
	
	public ZoomInAction(VisibleWorld visibleWorld,
			String text_key, String icon_path, String desc) {
		super(text_key, icon_path, desc);
		this.visibleWorld = visibleWorld;
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		visibleWorld.setZoomFactor(visibleWorld.getZoomFactor() * ZOOM_FACTOR_CHANGE);
	}
}
