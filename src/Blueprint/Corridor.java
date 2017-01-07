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

public class Corridor implements Space{
	private String id;
	private ArrayList<Wall> traces = new ArrayList<Wall>();
	private ArrayList<Wall> leftWalls = new ArrayList<Wall>();
	private ArrayList<Wall> rightWalls = new ArrayList<Wall>();
	private int height;
	private int width;
	private int nbStairs;
	private String idNextRoom;
	private String idLastRoom;
	
	/** Constructeur par default */
	public Corridor(String id){
		this.id=id;
		traces.add(new Wall(new Vertex(520,120),new Vertex(520,800)));
		height=400;
		width=120;
		nbStairs=0;
		idNextRoom=null;
		idLastRoom=null;
		updateWalls();
	}
	
	public Corridor() {
		// TODO Auto-generated constructor stub
	}
	
	public static Vertex findIntersectionPoint(Wall w1, Wall w2){
		float x,y;
		float x1 = w1.getV1().getX();
		float y1 = w1.getV1().getY();
		float x2 = w1.getV2().getX();
		float y2 = w1.getV2().getY();
		float x3 = w2.getV1().getX();
		float y3 = w2.getV1().getY();
		float x4 = w2.getV2().getX();
		float y4 = w2.getV2().getY();
		if(x1==x2 && x3==x4){
			return null;
		}
		else if(y1==y2 && y3==y4){
			return null;
		}
		else if(x1==x2){
			float a2 = (y3-y4)/(x3-x4);
			float b2 = y3-x3*a2;
			
			x=x1;
			y=a2*x+b2;
		}
		else if(x3==x4){
			float a1 = (y1-y2)/(x1-x2);
			float b1 = y1-x1*a1;
			
			x=x3;
			y=a1*x+b1;
		}
		else if(y1==y2){
			float a2 = (y3-y4)/(x3-x4);
			float b2 = y3-x3*a2;
			
			y=y1;
			x=(y-b2)/a2;
		}
		else if(y3==y4){
			float a1 = (y1-y2)/(x1-x2);
			float b1 = y1-x1*a1;
			
			y=y3;
			x=(y-b1)/a1;
		}
		else{
			float a1 = (y1-y2)/(x1-x2);
			float a2 = (y3-y4)/(x3-x4);
			if(Math.abs(a1-a2)<0.0001) return null;
			
			float b1 = y1-x1*a1;
			float b2 = y3-x3*a2;
			
			x=(b2-b1)/(a1-a2);
			y=a1*x+b1;
		}
		return new Vertex(x,y);
	}
	
	public void updateWalls(){
		leftWalls.clear();
		rightWalls.clear();
		
		for (int i=0;i<traces.size();i++){
			ArrayList<Wall> sidWalls = new ArrayList<Wall>();
			sidWalls = traces.get(i).findWalls(width);
			leftWalls.add(sidWalls.get(0));
			rightWalls.add(sidWalls.get(1));
		}
		
		for (int i=0; i< leftWalls.size()-1;i++){
			Vertex v = findIntersectionPoint(leftWalls.get(i), leftWalls.get(i+1));
			if (v!=null){
				leftWalls.get(i).setV2(v);
				leftWalls.get(i+1).setV1(v);
			}
		}
		
		for (int i=0; i< rightWalls.size()-1;i++){
			Vertex v = findIntersectionPoint(rightWalls.get(i), rightWalls.get(i+1));
			if (v!=null){
				rightWalls.get(i).setV2(v);
				rightWalls.get(i+1).setV1(v);
			}
		}
	}

	public void addVertex(){
		Wall tmp = null;
		Vertex v1 = null,v2 = null,v3 = null, v4 = null;
		
		for(Wall w: traces){
			if(w.isSelected()){    //il faut verifier si Open dans le mur est NULL
				v1=w.getV1();
				v2 = w.getV2();	
				tmp=w;
			}
		}
		if (tmp != null){
			v3 = new Vertex((v1.getX()+v2.getX())/2, (v1.getY()+v2.getY())/2);
			v4 = new Vertex((v1.getX()+v2.getX())/2, (v1.getY()+v2.getY())/2);
			int index = traces.indexOf(tmp);
			traces.remove(tmp);
			traces.add(index,new Wall(v4, v2));
			traces.add(index,new Wall(v1, v3));
			v3.select();
			v4.select();
		}
	}
	
	public void delVertex(){
		Wall tmp =null;
		Vertex v1=null;
		for (Wall w:traces){
			if(w.getV2().isSelected()){
				tmp=w;
				v1=w.getV1();
			}
		}
		if (tmp != null){
			Wall next = nextWall(tmp);
			Vertex v2 = next.getV2();
			int index = traces.indexOf(tmp);
			traces.remove(tmp);
			traces.add(index, new Wall(v1,v2));
			traces.remove(next);
			
		}	
		
	}
	
	public Wall nextWall(Wall w){
		int n=traces.indexOf(w);
		if(n==traces.size()-1){
			return null;
		}
		return traces.get(n+1);
	}
	
	public Wall lastWall(Wall w){
		int n=traces.indexOf(w);
		if(n==0){
			return null;
		}
		return traces.get(n-1);
	}
	
	
	
	public ArrayList<Wall> getTraces(){
		return traces;
	}
	
	public int getHeight(){
		return height;
	}
	
	public void setHeight(int height){
		this.height=height;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getNbStairs(){
		return nbStairs;
	}
	
	public void setNbStairs(int nbStairs) {
		this.nbStairs = nbStairs;
	}
	
	public String getIdNextRoom() {
		return idNextRoom;
	}

	public void setIdNextRoom(String idNextRoom) {
		this.idNextRoom = idNextRoom;
	}

	public String getIdLastRoom() {
		return idLastRoom;
	}

	public void setIdLastRoom(String idLastRoom) {
		this.idLastRoom = idLastRoom;
	}
	
	@Override
	public void draw(Graphics g) {
		for (Wall w : traces){
			w.drawTrace(g);
		}
		for (Wall w : leftWalls){
			w.draw(g);
		}
		for (Wall w : rightWalls){
			w.draw(g);
		}
	}
	
	public void write(String filepath) throws IOException{
		PrintWriter in = null;
		try {
	         in = new PrintWriter(
	               new OutputStreamWriter(new FileOutputStream(filepath)));
	         in.println(id);
	         for(Wall w : traces){
	        	 in.print("TRACE");
	        	 in.print(" ");
	        	 in.print(w.getV1().getX());
	        	 in.print(" ");
	        	 in.print(w.getV1().getY());
	        	 in.print(" ");
	        	 in.print(w.getV2().getX());
	        	 in.print(" ");
	        	 in.print(w.getV2().getY());
	        	 in.print("\n");
	         }
	         in.print("HEIGHT");
	         in.print(" ");
	         in.print(height);
	         in.print("\n");
	         in.print("WIDTH");
	         in.print(" ");
	         in.print(width);
	         in.print("\n");
	         in.print("STAIRS");
	         in.print(" ");
	         in.print(nbStairs);
	         in.print("\n");
	         in.print("LAST");
	         in.print(" ");
	         in.print(idLastRoom);
	         in.print("\n");
	         in.print("NEXT");
	         in.print(" ");
	         in.print(idNextRoom);
	         in.print("\n");
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
	        	 String l=scanner.next();
	        	 if(l.startsWith("HEIGHT")) height=scanner.nextInt();
	        	 else if(l.startsWith("WIDTH")) width=scanner.nextInt();
	        	 else if(l.startsWith("STAIRS")) nbStairs=scanner.nextInt();
	        	 else if(l.startsWith("LAST")) idLastRoom=scanner.next();
	        	 else if(l.startsWith("NEXT")) idNextRoom=scanner.next();
	        	 else traces.add(new Wall(new Vertex(scanner.nextFloat(),scanner.nextFloat()),new Vertex(scanner.nextFloat(),scanner.nextFloat())));
	         }
	         updateWalls();
		} finally {
			if (in != null)
				in.close();
		}	
	}

}
