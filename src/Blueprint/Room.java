/**
 * PT3 Poly Editor
 * DUT Informatique 2016/2017 
 * Auteur : HUANG Qijia
 * 			LU Yi
 * Tuteur : P. Even
 * */

package Blueprint;

import java.awt.Graphics;
import java.util.ArrayList;

public class Room {
	private ArrayList<Wall> walls = new ArrayList();
	private String id;
	
	public Room(int nb, String id) {
		if (nb == 4){
			walls.add(new Wall(new Vertex(170,170),new Vertex(170,510)));
			walls.add(new Wall(new Vertex(170,510),new Vertex(510,510)));
			walls.add(new Wall(new Vertex(510,510),new Vertex(510,170)));
			walls.add(new Wall(new Vertex(510,170),new Vertex(170,170)));
		}
		this.id = id;
	}
	
	public ArrayList<Wall> getWalls(){
		return walls;
	}
	
	public void draw(Graphics g){
		for (Wall w : walls){
			w.draw(g);
		}
	}
	
}
