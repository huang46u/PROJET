package Blueprint;

import java.awt.Graphics;
import java.util.ArrayList;

public class Corridor extends Space{
	ArrayList<Vertex> vertice = new ArrayList<Vertex>();
	
	/** Constructeur par default */
	public Corridor(){
		vertice.add(new Vertex(520,120));
		vertice.add(new Vertex(520,800));
	}
	
	@Override
	public void draw(Graphics g) {
		for (Vertex v : vertice){
			v.draw(g);
		}
	}

}
