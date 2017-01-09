/**
 * PT3 Poly Editor
 * DUT Informatique 2016/2017 
 * Auteur : HUANG Qijia
 * 			LU Yi
 * Tuteur : P. Even
 * */

package blueprint;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.jogamp.opengl.GL2;

import modeleur.ModeleurModel;

public class Room implements Space {
	private String id;
	private ArrayList<Wall> walls = new ArrayList<Wall>();
	private ArrayList<Wall> zone = new ArrayList<Wall>();
	private boolean modeNavigation;
	private static JDialog ig1, ig2;
	
	public Room(){
	}
	
	public Room(int nb, String id, int screenWidth) {
		this.id=id;
		modeNavigation = false;
		switch (nb) {
		case 4: walls.add(new Wall(new Vertex(screenWidth/4,screenWidth/4),new Vertex(screenWidth/4,screenWidth/4*3)));
				walls.add(new Wall(new Vertex(screenWidth/4,screenWidth/4*3),new Vertex(screenWidth/4*3,screenWidth/4*3)));
				walls.add(new Wall(new Vertex(screenWidth/4*3,screenWidth/4*3),new Vertex(screenWidth/4*3,screenWidth/4)));
				walls.add(new Wall(new Vertex(screenWidth/4*3,screenWidth/4),new Vertex(screenWidth/4,screenWidth/4)));
				walls.get(1).addDoor("Door", true);;
				break;
		case 6: walls.add(new Wall(new Vertex(screenWidth/3,screenWidth/4),new Vertex(screenWidth/4,screenWidth/2)));
				walls.add(new Wall(new Vertex(screenWidth/4,screenWidth/2),new Vertex(screenWidth/3,screenWidth/4*3)));
				walls.add(new Wall(new Vertex(screenWidth/3,screenWidth/4*3),new Vertex(screenWidth/3*2,screenWidth/4*3)));
				walls.add(new Wall(new Vertex(screenWidth/3*2,screenWidth/4*3),new Vertex(screenWidth/4*3,screenWidth/2)));
				walls.add(new Wall(new Vertex(screenWidth/4*3,screenWidth/2),new Vertex(screenWidth/3*2,screenWidth/4)));
				walls.add(new Wall(new Vertex(screenWidth/3*2,screenWidth/4),new Vertex(screenWidth/3,screenWidth/4)));
				walls.get(2).addDoor("Door", true);
				break;
		case 8: walls.add(new Wall(new Vertex(screenWidth/3,screenWidth/4),new Vertex(screenWidth/4,screenWidth/3)));
				walls.add(new Wall(new Vertex(screenWidth/4,screenWidth/3),new Vertex(screenWidth/4,screenWidth/3*2)));
				walls.add(new Wall(new Vertex(screenWidth/4,screenWidth/3*2),new Vertex(screenWidth/3,screenWidth/4*3)));
				walls.add(new Wall(new Vertex(screenWidth/3,screenWidth/4*3),new Vertex(screenWidth/3*2,screenWidth/4*3)));
				walls.add(new Wall(new Vertex(screenWidth/3*2,screenWidth/4*3),new Vertex(screenWidth/4*3,screenWidth/3*2)));
				walls.add(new Wall(new Vertex(screenWidth/4*3,screenWidth/3*2),new Vertex(screenWidth/4*3,screenWidth/3)));
				walls.add(new Wall(new Vertex(screenWidth/4*3,screenWidth/3),new Vertex(screenWidth/3*2,screenWidth/4)));
				walls.add(new Wall(new Vertex(screenWidth/3*2,screenWidth/4),new Vertex(screenWidth/3,screenWidth/4)));
				walls.get(3).addDoor("Door", true);
				break;
		default: break;
		}
		
	}
	
	public ArrayList<Wall> getWalls(){
		return walls;
	}
	
	public void addVertex(){
		Wall tmp = null;
		Vertex v1 = null,v2 = null,v3 = null, v4 = null;
		
		for(Wall w: walls){
			if(w.isSelected()){    //il faut verifier si Open dans le mur est NULL
				v1=w.getV1();
				v2 = w.getV2();	
				tmp=w;
			}
		}
		if (tmp != null){
			v3 = new Vertex((v1.getX()+v2.getX())/2, (v1.getY()+v2.getY())/2);
			v4 = new Vertex((v1.getX()+v2.getX())/2, (v1.getY()+v2.getY())/2);
			int index = walls.indexOf(tmp);
			walls.remove(tmp);
			walls.add(index,new Wall(v4, v2));
			walls.add(index,new Wall(v1, v3));
			v3.select();
			v4.select();
		}
	}
	
	public void delVertex(){
		Wall tmp =null;
		Vertex v1=null;
		for (Wall w:walls){
			if(w.getV2().isSelected()){
				tmp=w;
				v1=w.getV1();
			}
		}
		if (tmp != null){
			Wall next = nextWall(tmp);
			Vertex v2 = next.getV2();
			int index = walls.indexOf(tmp);
			walls.remove(tmp);
			walls.add(index, new Wall(v1,v2));
			walls.remove(next);
			
		}
		
		
	}
	
	public static int ptPosition(float x, float y, Vertex vA, Vertex vB){
		float position = (vB.getX()-vA.getX())*(y-vA.getY())-(vB.getY()-vA.getY())*(x-vA.getX());
		if (position > 0) return 1;
		else if (position < 0 ) return -1;
		return 0;
	}
	
	public boolean isInZoneNav(float x, float y){
		if(zone!=null){
			int n = zone.size();
			for(int i=0; i<n; i++){
				int pt = ptPosition(x, y, zone.get(i).getV1(), zone.get(i).getV2());
				if (pt == 1)
					return false;
			}
			return true;
		}
		return false;
	}
	
	public float[] isInZoneNav(float x1, float y1, float x2, float y2){
		float[] coords = new float[2];
		float eps = (float) 0.001;
		coords[0] = x2;
		coords[1] = y2;
		
		if(zone!=null){
			int n = zone.size();
			for(int i=0; i<n; i++){
				
				int pt = ptPosition(x1, y1, zone.get(i).getV1(), zone.get(i).getV2());
				if (pt == 1){
					float vx1 = zone.get(i).getV1().getX();
					float vy1 = zone.get(i).getV1().getY();
					float vx2 = zone.get(i).getV2().getX();
					float vy2 = zone.get(i).getV2().getY();
					if(vx1 - vx2 < eps){
						coords[0] = vx1/100;
						float maxy, miny;
						if (vy1 < vy2){
							maxy = vy2;
							miny = vy1;
						}
						else{
							maxy = vy1;
							miny = vy2;
						}
						if (y1>maxy){
							//System.out.println(" x max min");
							coords[1]=maxy/100;
							}
						else if (y1<miny)
							coords[1]=miny/100; 
						else 
							coords[1]=y1/100;
						
						return coords;
					}else if (vy1 - vy2 <eps){
						coords[1] = vy1/100;
						float maxx, minx;
						if (vx1 < vx2){
							maxx = vx2;
							minx = vx1;
						}else{
							maxx = vx1;
							minx = vx2;
						}
						if (x1>maxx)
							coords[0]=maxx/100;
						else if (x1<minx)
							coords[0]=minx/100; 
						else 
							coords[0]=x1/100;
						//System.out.println(" y max min");
						return coords;
					}else{
						
						float a1 = (vy1-vy2)/(vx1-vx2);
						float b1 = vy1-a1*vx1;
						float a2 = -1/a1;
						float b2 = y1 - a2*x1;
						float X = (b2-b1)/(a1-a2);
							//System.out.println(X);
						float Y = a2*X+b2;
						coords[0] = X/100;
						coords[1] = Y/100;
							//System.out.println(" a 1<> a2");
						return coords;
					}
				} 
			} 
			coords[0] = x1;
			coords[1] = y1;
			//System.out.println(" true");
			return coords;
		}
		return coords;
	}
	
	public static float[] findIntersectionPoint(Wall w1, Wall w2){
		float x1 = w1.getV1().getX();
		float y1 = w1.getV1().getY();
		float x2 = w1.getV2().getX();
		float y2 = w1.getV2().getY();
		float x3 = w2.getV1().getX();
		float y3 = w2.getV1().getY();
		float x4 = w2.getV2().getX();
		float y4 = w2.getV2().getY();
		float a1 = (y1-y2)/(x1-x2);
		float a2 = (y3-y4)/(x3-x4);
		if(a1==a2) return null;
		
		float b1 = y1-x1*a1;
		float b2 = y3-x3*a2;
		
		float[] point = new float[2];
		point[0]=(b2-b1)/(a1-a2);
		point[1]=a1*point[0]+b1;
		
		return point;
	}
	
	public static boolean betweenWalls(float x, float y, Wall w, Wall w1, Wall w2){
		float r=(float)25/2;
		int pos = ptPosition(x, y, w.getV1(), w2.getV1());
		if (pos == -1){
			return false;
		}
		else if(pos == 0)
		{
			return true;
		}
		else {
			Line2D l1 = new Line2D.Float(w1.getV1().getX()-r, w1.getV1().getY()-r, w1.getV2().getX()-r, w1.getV2().getY()-r);
			Line2D l2 = new Line2D.Float(w2.getV1().getX()-r, w2.getV1().getY()-r, w2.getV2().getX()-r, w2.getV2().getY()-r);
			float disLines = w.getV1().getX()-w2.getV1().getX();
			if(disLines == 0){
				disLines = w.getV1().getY()-w2.getV1().getY();
			}
			if(disLines < 0){
				disLines=-disLines;
			}
			double disPtLines = l1.ptLineDist(x-r, y-r)+l2.ptLineDist(x-r, y-r);
			if(disPtLines - disLines < 0.001) return true;
			return false;
		}
	}
	
	public static boolean isInTriangle(float x, float y, float x3, float y3, Vertex v1, Vertex v2){
		float x1 = v1.getX(), y1 = v1.getY();
		float x2 = v2.getX(), y2 = v2.getY();

		double ABC = Math.abs (x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2));
		double ABP = Math.abs (x1 * (y2 - y) + x2 * (y - y1) + x * (y1 - y2));
		double APC = Math.abs (x1 * (y - y3) + x * (y3 - y1) + x3 * (y1 - y));
		double PBC = Math.abs (x * (y2 - y3) + x2 * (y3 - y) + x3 * (y - y2));

		boolean isInTriangle = ABP + APC + PBC == ABC;
		
		return isInTriangle;
	}

	public synchronized ArrayList<Vertex> getVertice(){
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		for (Wall w: walls){
			vertices.add(w.getV1());
		}
		for(Vertex v: vertices)
			System.out.println(v.getX());
		System.out.println("===================");
		return vertices;
	}
	
	/** verifier si le polygon est convexe */
	public boolean isConvexe(){
		if (walls.size()<4){
			return true;}
		int sign=0;
		int n = walls.size();
		for(int i=0; i<n; i++){
			int pt =ptPosition(walls.get(i).getV2().getX(), walls.get(i).getV2().getY(), walls.get(i).getV1(), nextWall(walls.get(i)).getV2());
			if (i == 0)
				sign = pt;
			else if (sign != pt)
					return false;
		}
		return true;
	}
	
	public void addDoor(String id, boolean entrant){
		for(Wall w: walls){
			if(w.isSelected()){
				w.addDoor(id, entrant);
				
				if (ig1 == null) ig1=new JDialog();
				
				JTextField input; //Composants textuels de l'interface
				 
				Font font = new Font("Arial", Font.BOLD, 20);
				 //JPanel Nord
				input= new JTextField(10);
				input.setPreferredSize(new Dimension(200,30));
				input.addActionListener(new ActionListener() {
			         public void actionPerformed(ActionEvent e) {
			        	JTextField jt = (JTextField) e.getSource();
			     		String s = jt.getText();
			     		int n = Integer.parseInt(s);
			     		w.setOpenHeight(n);
			            }
			          });
				input.setFont(font);
				input.setText(""+w.getOpenHeight());
				 
				JPanel controlPanel= new JPanel(new GridLayout(1,2));
				JLabel nb = new JLabel("HAUTEUR ",JLabel.CENTER);
				nb.setFont(font);
				nb.setForeground(ModeleurModel.BLACK);
				controlPanel.setBackground(ModeleurModel.DARKGREY4);
				nb.setPreferredSize(new Dimension(110*2,70));
				controlPanel.add(nb);
				controlPanel.add(input);
				
				JTextField input2; //Composants textuels de l'interface
				 //JPanel Nord
				input2= new JTextField(10);
				input2.setPreferredSize(new Dimension(200,30));
				input2.addActionListener(new ActionListener() {
			         public void actionPerformed(ActionEvent e) {
			        	JTextField jt = (JTextField) e.getSource();
			     		String s = jt.getText();
			     		w.setNext(s);
			            }
			          });
				input2.setFont(font);
				input2.setText(""+w.getNext());
				 
				JPanel controlPanel2= new JPanel(new GridLayout(1,2));
				JLabel nb2 = new JLabel("CONNECTER A",JLabel.CENTER);
				nb2.setFont(font);
				nb2.setForeground(ModeleurModel.BLACK);
				controlPanel2.setBackground(ModeleurModel.DARKGREY4);
				nb2.setPreferredSize(new Dimension(110*2,70));
				controlPanel2.add(nb2);
				controlPanel2.add(input2);
				
				File folder = new File("corridors");
				File[] listOffiles = folder.listFiles();
				JTextArea list = new JTextArea();
				list.append("\n Les couloirs a choisir : \n\n");
				for (File f: listOffiles)
					list.append(" # "+f.getName()+"\n");
				list.setFont(font);
				
				ig1.getContentPane().add(controlPanel, BorderLayout.NORTH);
				ig1.getContentPane().add(controlPanel2, BorderLayout.CENTER);
				ig1.getContentPane().add(new JScrollPane(list),BorderLayout.SOUTH);
				ig1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				ig1.pack();
				ig1.setVisible(true);
			}
		}
	}
	
	public void addWindow(String id) {
		for(Wall w: walls){
			if(w.isSelected()){
				w.addWindow(id);
				
				if (ig2 == null) ig2=new JDialog();
				
				JTextField input; //Composants textuels de l'interface
				 
				Font font = new Font("Arial", Font.BOLD, 20);
				 //JPanel Nord
				input= new JTextField(10);
				input.setPreferredSize(new Dimension(200,30));
				input.addActionListener(new ActionListener() {
			         public void actionPerformed(ActionEvent e) {
			        	JTextField jt = (JTextField) e.getSource();
			     		String s = jt.getText();
			     		int n = Integer.parseInt(s);
			     		w.setOpenHeight(n);
			            }
			          });
				input.setFont(font);
				input.setText(""+w.getOpenHeight());
				 
				JPanel controlPanel= new JPanel(new GridLayout(1,2));
				JLabel nb = new JLabel("HAUTEUR A",JLabel.CENTER);
				nb.setFont(font);
				nb.setForeground(ModeleurModel.BLACK);
				controlPanel.setBackground(ModeleurModel.DARKGREY4);
				nb.setPreferredSize(new Dimension(110*2,70));
				controlPanel.add(nb);
				controlPanel.add(input);
				
				JTextField input2; //Composants textuels de l'interface
				 //JPanel Nord
				input2= new JTextField(10);
				input2.setPreferredSize(new Dimension(200,30));
				input2.addActionListener(new ActionListener() {
			         public void actionPerformed(ActionEvent e) {
			        	JTextField jt = (JTextField) e.getSource();
			     		String s = jt.getText();
			     		int n = Integer.parseInt(s);
			     		w.setWindowHeight(n);
			            }
			          });
				input2.setFont(font);
				input2.setText(""+w.getWindowHeight());
				 
				JPanel controlPanel2= new JPanel(new GridLayout(1,2));
				JLabel nb2 = new JLabel("HAUTEUR B",JLabel.CENTER);
				nb2.setFont(font);
				nb2.setForeground(ModeleurModel.BLACK);
				controlPanel2.setBackground(ModeleurModel.DARKGREY4);
				nb2.setPreferredSize(new Dimension(110*2,70));
				controlPanel2.add(nb2);
				controlPanel2.add(input2);
				
				ig2.getContentPane().add(controlPanel, BorderLayout.NORTH);
				ig2.getContentPane().add(controlPanel2, BorderLayout.CENTER);
				ig2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				ig2.pack();
				ig2.setVisible(true);
			}
		}
		
	}
	
	public boolean isNavigationModeOn(){
		return modeNavigation;
	}
	
	public ArrayList<Wall> getNavigationZone(){
		return zone;
	}
	
	public void turnOnNavigation(){
		zone.clear();
		for(Wall w : walls)
		zone.add(new Wall(new Vertex(w.getV1().getX(),w.getV1().getY()), 
					new Vertex(w.getV2().getX(),w.getV2().getY())));
		modeNavigation=true;
	}
	
	public void turnOffNavigation(){
		modeNavigation=false;
	}
	
	public Wall nextWall(Wall w){
		int n=walls.indexOf(w);
		if(n==walls.size()-1){
			return walls.get(0);
		}
		return walls.get(n+1);
	}
	
	public Wall lastWall(Wall w){
		int n=walls.indexOf(w);
		if(n==0){
			return walls.get(walls.size()-1);
		}
		return walls.get(n-1);
	}
	
	public Wall nextTrace(Wall w){
		int n=zone.indexOf(w);
		if(n==zone.size()-1){
			return zone.get(0);
		}
		return zone.get(n+1);
	}
	
	public void draw(Graphics g){
		if (!modeNavigation){
			for (Wall w : zone){
				w.drawTrace(g);
			}
			for (Wall w : walls){
				w.draw(g);
			}
		} else{
			for (Wall w : walls){
				w.draw(g);
			}
			for (Wall w : zone){
				w.drawTrace(g);
			}
		}
	}

	public void draw(GL2 gl){
		for (Wall w : walls){
			w.draw(gl);
		}
		
		gl.glBegin(GL2.GL_POLYGON);
		gl.glColor3f(0.2f, 0.8f, 0.2f);
			for (Wall w: walls){
				gl.glVertex3f(w.getV1().getX()/100, 0.0f, w.getV1().getY()/100);
			}
		gl.glEnd();
		
		gl.glBegin(GL2.GL_POLYGON);
		gl.glColor3f(0.2f, 0.8f, 0.2f);
			for (Wall w: walls){
				gl.glVertex3f(w.getV1().getX()/100, w.getHeight()/100, w.getV1().getY()/100);
			}
		gl.glEnd();
	}
	
	public void draw(GL2 gl, float tT, float tB, float tL, float tR){
		for (Wall w : walls){
			w.draw(gl, tT,  tB,  tL, tR);
		}
		
		gl.glBegin(GL2.GL_POLYGON);
		for(Wall w : walls){
			gl.glVertex3f(w.getV1().getX()/100, 0.0f, w.getV1().getY()/100);
			}
		gl.glEnd();
		
		gl.glBegin(GL2.GL_POLYGON);
		for(Wall w : walls){
			gl.glVertex3f(w.getV1().getX()/100, w.getHeight()/100, w.getV1().getY()/100);
			}
		gl.glEnd();
	}
	

	
	public void write(String filepath) throws IOException{
		PrintWriter in = null;
		try {
	         in = new PrintWriter(
	               new OutputStreamWriter(new FileOutputStream(filepath)));
	         in.println(id);
	         for(Wall w : walls){
	        	 in.print(w.getV1().getX());
	        	 in.print(" ");
	        	 in.print(w.getV1().getY());
	        	 in.print(" ");
	        	 in.print(w.getV2().getX());
	        	 in.print(" ");
	        	 in.print(w.getV2().getY());
	        	 in.print(" ");
	        	 in.print(w.getHeight());
	        	 
	        	 if (w.getOpen()!=null){
	        		 in.print(" ");
	        		 in.print(w.getOpen().getID());
	        		 in.print(" ");
		        	 in.print(w.getOpen().getV1().getX());
		        	 in.print(" ");
		        	 in.print(w.getOpen().getV1().getY());
		        	 in.print(" ");
		        	 in.print(w.getOpen().getV2().getX());
		        	 in.print(" ");
		        	 in.print(w.getOpen().getV2().getY());
		        	 if(w.getOpen() instanceof Door){
		        		 Door d = (Door) w.getOpen();
		        		 in.print(" ");
		        		 in.print(d.isEntrance());
		        		 in.print(" ");
		        		 in.print(d.getHeight());
		        		 in.print(" ");
		        		 in.print(d.getNext());
		        	 } else if (w.getOpen() instanceof Window){
		        		 Window win = (Window) w.getOpen();
		        		 in.print(" ");
		        		 in.print(win.getHeight());
		        		 in.print(" ");
		        		 in.print(win.getHeightBottom());
		        	 }
	        	 }
	        	 
	        	 in.print("\n");
	         }
	         //in.println("ZONE");
	         for(Wall w : zone){
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
	        	 if(!line.startsWith("TRACE")){
		        	 Scanner scanner = new Scanner(line).useDelimiter(" ");
		        	 walls.add(new Wall(new Vertex(scanner.nextFloat(),scanner.nextFloat()),new Vertex(scanner.nextFloat(),scanner.nextFloat()), scanner.nextInt()));
		        	 if (scanner.hasNext()){
		        		 String id=scanner.next();
		        		 if(id.startsWith("Door")){
		        			 walls.get(walls.size()-1).addDoor(id, scanner.nextFloat(), scanner.nextFloat(),scanner.nextFloat(),scanner.nextFloat(), scanner.nextBoolean(), scanner.nextInt(), scanner.next());
		        		 } else if (id.startsWith("Window")){
		        			 walls.get(walls.size()-1).addWindow(id, scanner.nextFloat(), scanner.nextFloat(),scanner.nextFloat(),scanner.nextFloat(), scanner.nextInt(), scanner.nextInt());
		        		 }
		        	 }
	        	 }
	        	 else{
	        		 Scanner scanner = new Scanner(line).useDelimiter(" ");
	        		 String trace=scanner.next();
		        	 zone.add(new Wall(new Vertex(scanner.nextFloat(),scanner.nextFloat()),new Vertex(scanner.nextFloat(),scanner.nextFloat())));
	        	 }
	         }
		} finally {
			if (in != null)
				in.close();
		}	
	}

	public void setHeight(int height) {
		for (Wall w: walls){
			w.setHeight(height);
		}
	}

	public int getHeight() {
		return walls.get(0).getHeight();
	}

	public void setID(String id) {
		this.id = id;
	}
	
	public String getID(){
		return id;
	}

	
}
