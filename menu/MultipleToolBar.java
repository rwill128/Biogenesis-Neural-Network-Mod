package menu;

import java.util.HashMap;

import javax.swing.JToolBar;
import javax.swing.Action;

public class MultipleToolBar extends JToolBar {
	private static final long serialVersionUID = 1L;
	private HashMap<String, Action[]> actions = new HashMap<String, Action[]>();
	
	public MultipleToolBar() {
		super();
	}

	public MultipleToolBar(int orientation) {
		super(orientation);
	}

	public MultipleToolBar(String name, int orientation) {
		super(name, orientation);
	}

	public MultipleToolBar(String name) {
		super(name);
	}

	public void addActionArray(String key, Action[] actionArray) {
		actions.put(key, actionArray);
	}
	
	public Action[] getActionArray(String key) {
		return actions.get(key);
	}
	
	public void selectActionArray(String key) throws IllegalArgumentException {
		Action[] actionArray = actions.get(key);
		
		if (actionArray == null)
			throw new IllegalArgumentException("Key doesn't exists");
		removeAll();
		for (Action action : actionArray)
			add(action);
		invalidate();
		repaint();
	}
}
