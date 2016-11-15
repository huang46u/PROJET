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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import com.jogamp.opengl.GL2;

public class Room {
	private ArrayList<Wall> walls = new ArrayList();
	private String id;
	
	public Room(){
		
	}
	
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

	public void draw (GL2 gl){
		for (Wall w : walls){
			w.draw(gl);
		}	
	}
	
	public void write() throws IOException{
		PrintWriter in = null;
		try {
	         in = new PrintWriter(
	               new OutputStreamWriter(new FileOutputStream("test.txt")));
	         in.println(id);
	         for(Wall w : walls){
	        	 in.print(w.getV1().getX());
	        	 in.print(" ");
	        	 in.print(w.getV1().getY());
	        	 in.print(" ");
	        	 in.print(w.getV2().getX());
	        	 in.print(" ");
	        	 in.print(w.getV2().getY());
	        	 in.print("\n");
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
	         id = in.readLine();
	         String line;
	         while ((line = in.readLine()) != null){
	        	 Scanner scanner = new Scanner(line).useDelimiter(" ");
	        	 walls.add(new Wall(new Vertex(scanner.nextInt(),scanner.nextInt()),new Vertex(scanner.nextInt(),scanner.nextInt())));
	         }
		} finally {
			if (in != null)
				in.close();
		}	
	}
}
