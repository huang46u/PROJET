/**
 * PT3 Poly Editor
 * DUT Informatique 2016/2017 
 * Auteur : HUANG Qijia
 * 			LU Yi
 * Tuteur : P. Even
 * */

package blueprint;

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

/** class Corridor : elle consiste a des traces, une suite de mur a droit et une suite de mur a gauche, 
 *  les duex suites de mur devraient etre genere automatiquement depandant a le largeur qu'on donne .
 *  elle implementer l'interface Space */
public class Corridor implements Space{
	// ----- attributs ------
	/** identifiant de couloir */
	private String id;
	/** une suite de traces representant la parcours du couloir */
	private ArrayList<Wall> traces = new ArrayList<Wall>();
	/** une suite de murs a gauche */
	private ArrayList<Wall> leftWalls = new ArrayList<Wall>();
	/** une suite de murs a droit */
	private ArrayList<Wall> rightWalls = new ArrayList<Wall>();
	/** largeur du couloir */
	private int width;
	/** le nom de fichier qui devrait charger la chambre prochaine */
	private String idNextRoom;
	
	private int hauteur;
	
	// ------ constructeur ------
	/** Constructeur par default */
	public Corridor(String id, int screenWidth){
		this.id=id;
		traces.add(new Wall(new Vertex(screenWidth/2,screenWidth/8),new Vertex(screenWidth/2,screenWidth/8*7)));
		width=120;
		idNextRoom=null;
		hauteur=100;
		updateWalls();
	}
	
	/** Constructeur qui construit une objet null, pour les charger de fichiers.
	 *  il n'est pas securise. # A optimiser. # */
	public Corridor() {
		// TODO Auto-generated constructor stub
	}
	
	// ------ methodes ------
	/** retourner une Vertex pour la mise en jour de les duex suites de murs. 
	 *  il recheche le point d'intersection de deux murs */
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
		
		if(x1==x2 && x3==x4){ // pas d'intersection
			return null;
		}
		else if(y1==y2 && y3==y4){ // pas d'intersection
			return null;
		}
		// en cas d'avoir le meme x
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
		// en cas d'avoir le meme y
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
			if(Math.abs(a1-a2)<0.0001) return null; // pas d'intersection
			
			float b1 = y1-x1*a1;
			float b2 = y3-x3*a2;
			
			x=(b2-b1)/(a1-a2);
			y=a1*x+b1;
		}
		return new Vertex(x,y);
	}
	
	
	
	/** Mettre a jour les deux suites de murs pour poursuivre la transformation de traces */
	public void updateWalls(){
		leftWalls.clear();
		rightWalls.clear();
		for (int i=0;i<traces.size();i++){
			ArrayList<Wall> sidWalls = new ArrayList<Wall>();
			sidWalls = traces.get(i).findWalls(width, traces.get(i).getHeight());
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
	
	
	public float[] corridorangle(){
		float[] angle=new float[2];
		float x1 =leftWalls.get(this.traces.size()-1).getV2().getX()/100;
		float y1 =leftWalls.get(this.traces.size()-1).getV2().getY()/100;
		float x2 =rightWalls.get(this.traces.size()-1).getV2().getX()/100;
		float y2 =rightWalls.get(this.traces.size()-1).getV2().getY()/100;
		
		float x3 =leftWalls.get(0).getV1().getX()/100;
		float y3 =leftWalls.get(0).getV1().getY()/100;
		float x4 =rightWalls.get(0).getV1().getX()/100;
		float y4 =rightWalls.get(0).getV1().getY()/100;
		double tan_sortant_angle=(y2-y1)/(x2-x1);
		double tan_entrant_angle=(y4-y3)/(x3-x4);
		angle[0]=(float) (Math.atan(tan_entrant_angle)/Math.PI*2*360);
		angle[1]=(float) (Math.atan(tan_sortant_angle)/Math.PI*2*360);
		
		return angle;
	}
	
	public float[] findCorridorEntrance() {
		float[] entrance = new float[2];
		entrance[0] = (float) this.getTraces().get(this.getTraces().size()-1).getV2().getX()/100;
		entrance[1] = (float) this.getTraces().get(this.getTraces().size()-1).getV2().getY()/100;
		return entrance;
	}
	
	public float[] findCorridorSortance() {
		float[] entrance = new float[2];
		entrance[0] = (float) this.getTraces().get(0).getV1().getX()/100;
		entrance[1] = (float) this.getTraces().get(0).getV1().getY()/100;
		return entrance;
	}
	
	
	/** retourne une suite de traces */
	public ArrayList<Wall> getTraces(){
		return traces;
	}
	
	/** retourner le largeur de couloir */
	public int getWidth() {
		return width;
	}

	/** definir le largeur de couloir */
	public void setWidth(int width) {
		this.width = width;
	}
	
	/** retourn le nom de fichier de la chambre prochaine */
	public String getIdNextRoom() {
		return idNextRoom;
	}

	/** definir le chambre prochaine */
	public void setIdNextRoom(String idNextRoom) {
		this.idNextRoom = idNextRoom;
	}
	
	// les methodes qu'on a surcharger d'interface Space

	@Override
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

	@Override
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
	
	

	@Override
	public Wall nextWall(Wall w){
		int n=traces.indexOf(w);
		if(n==traces.size()-1){
			return null;
		}
		return traces.get(n+1);
	}
	
	@Override
	public Wall lastWall(Wall w){
		int n=traces.indexOf(w);
		if(n==0){
			return null;
		}
		return traces.get(n-1);
	}
	
	@Override
	public int getHeight(){
		return leftWalls.get(0).getHeight();
	}
	
	@Override
	public void setHeight(int height){
		for(Wall w : traces)
			w.setHeight(height);
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
	
	@Override
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
	        	 in.print(" ");
	        	 in.print(w.getHeight());
	        	 in.print("\n");
	         }
	         in.print("WIDTH");
	         in.print(" ");
	         in.print(width);
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
	
	@Override
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
	        	 if(l.startsWith("WIDTH")) width=scanner.nextInt();
	        	 else if(l.startsWith("NEXT")) idNextRoom=scanner.next();
	        	 else traces.add(new Wall(new Vertex(scanner.nextFloat(),scanner.nextFloat()),new Vertex(scanner.nextFloat(),scanner.nextFloat()),scanner.nextInt()));
	         }
	         updateWalls();
		} finally {
			if (in != null)
				in.close();
		}	
	}
	
	@Override
	public void draw(GL2 gl) {
		gl.glPushMatrix();
		
		for (Wall w : leftWalls){
			w.drawcorridor(gl, hauteur);
		}
		for (Wall w : rightWalls){
			w.draw(gl);
		}
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3f(0.2f, 0.8f, 0.2f);
		
		for (int i=0 ; i< leftWalls.size();i++){	
			gl.glVertex3f(leftWalls.get(i).getV1().getX()/100, 
					0.0f, leftWalls.get(i).getV1().getY()/100);
			gl.glVertex3f(leftWalls.get(i).getV2().getX()/100, 
					0.0f, leftWalls.get(i).getV2().getY()/100);
			gl.glVertex3f(rightWalls.get(i).getV2().getX()/100, 
					0.0f, rightWalls.get(i).getV2().getY()/100);
			gl.glVertex3f(rightWalls.get(i).getV1().getX()/100, 
					0.0f, rightWalls.get(i).getV1().getY()/100);
		}
		gl.glEnd();
		
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3f(0.2f, 0.8f, 0.2f);
		for (int i=0 ; i< leftWalls.size();i++){
			gl.glVertex3f(leftWalls.get(i).getV1().getX()/100, 
					leftWalls.get(i).getHeight()/100, 
					leftWalls.get(i).getV1().getY()/100);
			gl.glVertex3f(leftWalls.get(i).getV2().getX()/100,
					leftWalls.get(i).getHeight()/100,
					leftWalls.get(i).getV2().getY()/100);
			gl.glVertex3f(rightWalls.get(i).getV2().getX()/100, 
					rightWalls.get(i).getHeight()/100, 
					rightWalls.get(i).getV2().getY()/100);
			gl.glVertex3f(rightWalls.get(i).getV1().getX()/100, 
					rightWalls.get(i).getHeight()/100, 
					rightWalls.get(i).getV1().getY()/100);	
		}
		gl.glEnd();
		gl.glPopMatrix();
		Room r =new Room();
		try {
			r.read("rooms/"+this.getIdNextRoom());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		float rotateangle=(180.0f-this.corridorangle()[1]+r.doorangle()[0]);
		float translateX=this.findCorridorSortance()[0]-r.findRoomEntrance()[0];
		float translateZ=this.findCorridorSortance()[1]-r.findRoomEntrance()[1];
		//gl.glTranslatef(translateX, 0,translateZ-0.01f);
		gl.glTranslatef(this.findCorridorSortance()[0], 0,this.findCorridorSortance()[1]);
		
		gl.glRotatef(rotateangle, 0, 1, 0);
		
		System.out.println(" cooridorangle: "+this.corridorangle()[1]+"\n"+ " doorangle: "+ r.doorangle()[0]);
		
		r.draw(gl);
		
		
	}

	public int getHauteur() {
		return hauteur;
	}

	public void setHauteur(int hauteur) {
		this.hauteur = hauteur;
	}

	@Override
	public void draw(GL2 gl, float tT, float tB, float tL, float tR) {
		gl.glPushMatrix();
		for (Wall w : leftWalls){
			w.draw(gl, tT, tB, tL, tR);
		}
		
		for (Wall w : rightWalls){
			w.draw(gl, tT, tB, tL, tR);
		}
		
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3f(0.2f, 0.8f, 0.2f);
		
		for (int i=0 ; i< leftWalls.size();i++){
			gl.glVertex3f(leftWalls.get(i).getV1().getX()/100,
					0.0f, leftWalls.get(i).getV1().getY()/100);
			gl.glVertex3f(leftWalls.get(i).getV2().getX()/100, 
					0.0f, leftWalls.get(i).getV2().getY()/100);
			gl.glVertex3f(rightWalls.get(i).getV2().getX()/100, 
					0.0f, rightWalls.get(i).getV2().getY()/100);
			gl.glVertex3f(rightWalls.get(i).getV1().getX()/100, 
					0.0f, rightWalls.get(i).getV1().getY()/100);
		}
		
		gl.glEnd();
		
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3f(0.2f, 0.8f, 0.2f);
		
		for (int i=0 ; i< leftWalls.size();i++){
			gl.glVertex3f(leftWalls.get(i).getV1().getX()/100,
					leftWalls.get(i).getHeight()/100,
					leftWalls.get(i).getV1().getY()/100);
			gl.glVertex3f(leftWalls.get(i).getV2().getX()/100,
					leftWalls.get(i).getHeight()/100,
					leftWalls.get(i).getV2().getY()/100);
			gl.glVertex3f(rightWalls.get(i).getV2().getX()/100,
					rightWalls.get(i).getHeight()/100,
					rightWalls.get(i).getV2().getY()/100);
			gl.glVertex3f(rightWalls.get(i).getV1().getX()/100,
					rightWalls.get(i).getHeight()/100,
					rightWalls.get(i).getV1().getY()/100);	
		}
		gl.glEnd();
		Room r =new Room();
		try {
			r.read("rooms/"+this.getIdNextRoom());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		float rotateangle=(this.corridorangle()[1]-r.doorangle()[0]);
		float translateX=this.findCorridorSortance()[0]-r.findRoomEntrance()[0];
		float translateZ=this.findCorridorSortance()[1]-r.findRoomEntrance()[1];
		gl.glTranslatef(translateX, 0,translateZ);
		gl.glRotatef(rotateangle, 0, 1, 0);
		
		r.draw(gl, tT, tB, tL, tR);
		
	}

}
