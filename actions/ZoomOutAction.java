package actions;

import java.awt.event.ActionEvent;
import biogenesis.VisibleWorld;

public class ZoomOutAction extends StdAction {
	private static final long serialVersionUID = 1L;
	private VisibleWorld visibleWorld;
	
	public ZoomOutAction(VisibleWorld visibleWorld,
			String text_key, String icon_path, String desc) {
		super(text_key, icon_path, desc);
		this.visibleWorld = visibleWorld;
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		visibleWorld.setZoomFactor(visibleWorld.getZoomFactor() / ZoomInAction.ZOOM_FACTOR_CHANGE);
	}
}
