package Blueprint;

import java.awt.Graphics;
import java.util.ArrayList;

public abstract class Space {
	protected ArrayList<Wall> walls = new ArrayList();
	protected String id;
	
	public abstract void draw(Graphics g);
}
