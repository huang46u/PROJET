/**
 * PT3 Poly Editor
 * DUT Informatique 2016/2017 
 * Auteur : HUANG Qijia
 * 			LU Yi
 * Tuteur : P. Even
 * */

package Blueprint;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
	
	public void write() throws IOException{
		BufferedWriter in = null;
		try {
	         in = new BufferedWriter(
	               new OutputStreamWriter(new FileOutputStream("test.txt")));
	         in.write(id);
	         for(Wall w : walls){
	        	 in.write(w.getV1().getX());
	        	 in.write(w.getV1().getY());
	        	 in.write(w.getV2().getX());
	        	 in.write(w.getV2().getY());
	         }
		} finally {
			if (in != null)
				in.close();
		}	
	}
	
	public void read(String filename) throws IOException{
		BufferedReader in = null;
		try {
	         in = new BufferedReader(
	               new InputStreamReader(new FileInputStream(filename)));
	         String name = in.readLine();
	         System.out.println(name);
		} finally {
			if (in != null)
				in.close();
		}	
	}
}
