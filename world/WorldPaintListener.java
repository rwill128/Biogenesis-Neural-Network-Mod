package world;

import java.awt.Rectangle;

public interface WorldPaintListener {
	public void repaint(Rectangle r);
	public void repaint(long tm, int x, int y, int width, int height);
}
