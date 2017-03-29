/**
 * PT3 Poly Editor
 * DUT Informatique 2016/2017 
 * Auteur : HUANG Qijia
 * 			LU Yi
 * Tuteur : P. Even
 * */

package blueprint;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import com.jogamp.opengl.GL2;

import modeleur.ModeleurModel;

/** class Wall : elle sert a definir un mur qu'on peut selectionner et ajouter des ouvertures */
public class Wall {
	// ----- attrributs -----
	private Vertex v1;
	private Vertex v2;
	private int height;
	private boolean selected=false;
	private Open o=null;
	
	/** Rayon de Vertex */
	private float r = 25/2;

	// ----- constructeurs ----
	public Wall(Vertex v1, Vertex v2) {
		super();
		height=100;
		this.v1 = v1;
		this.v2 = v2;
	}
	
	public Wall(Vertex v1, Vertex v2, int height){
		super();
		this.height = height;
		this.v1 = v1;
		this.v2 = v2;
	}
	
	// ----- methodes -----
	public Vertex getV1(){
		return v1;
	}
	
	public Vertex getV2(){
		return v2;
	}
	
	public void setV1(Vertex v1){
		this.v1 = v1;
	}
	
	public void setV2(Vertex v2){
		this.v2 = v2;
	}

	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height){
		this.height=height;
	}
	
	public Open getOpen(){
		return o;
	}
	
	public int getOpenHeight() {
		return o.getHeight();
	}
	
	public void setOpenHeight(int height){
		o.setHeight(height);
	}

	
	public boolean isSelected(){
		return selected;
	}
	
	public void select() {
		selected = !selected;	
	}
	
	public int ptPosition(float x, float y){
		float position = (v2.getX()-v1.getX())*(y-v1.getY())-(v2.getY()-v1.getY())*(x-v1.getX());
		if (position > 0) return 1;
		else if (position < 0 ) return -1;
		return 0;
	}
	
	public boolean between(int x, int y, float x1, float y1, float x2, float y2){
		float maxX=x1;
		float minX=x2;
		float maxY=y1;
		float minY=y2;
		if(maxX<minX){
			float tmp=maxX;
			maxX=minX;
			minX=tmp;
		}
		if(maxY<minY){
			float tmp=maxY;
			maxY=minY;
			minY=tmp;
		}
		
		boolean betweenX= x <= maxX && x >= minX ;
		boolean betweenY= y <= maxY && y >= minY;
		
		if(minX==maxX) betweenX=true;
		if(maxY==minY) betweenY=true;
		
		return betweenX && betweenY;
	}
	
	public void select(int x, int y){
		if(selected) {
			selected=!selected;
			return;
			}
		
		float disX = v2.getX()-v1.getX();
		float disY = v2.getY()-v1.getY();
		float disXY = (float) Math.sqrt(disX*disX+disY*disY);
		float supX = r * (disX/disXY);
		float supY = r * (disY/disXY);
		
		Line2D l = new Line2D.Float(v1.getX()+r+supX*2, v1.getY()+r+supY*2, v2.getX()+r-supX*2, v2.getY()+r-supY*2);
		
			
		float maxX=v1.getX()+r+supX*2;
		float minX=v2.getX()+r-supX*2;
		float maxY=v1.getY()+r+supY*2;
		float minY=v2.getY()+r-supY*2;
			
		if(maxX<minX){
			float tmp=maxX;
			maxX=minX;
			minX=tmp;
		}
			
		if(maxY<minY){
			float tmp=maxY;
			maxY=minY;
			minY=tmp;
		}
			
		boolean betweenX= x <= maxX && x >= minX ;
		boolean betweenY= y <= maxY && y >= minY;
			
		if(minX==maxX) betweenX=true;
		if(maxY==minY) betweenY=true;
			
		if (l.ptLineDist(x, y) <= r && betweenX && betweenY){
			selected=true;			
		}
		else{
			selected=false;
		}
		
	}
	
	
	
	public float[] move(float x, float y){
		Line2D l = new Line2D.Float(v1.getX()+r, v1.getY()+r, v2.getX()+r, v2.getY()+r);
		float v1x,v1y,v2x,v2y;
		
		if (v1.getX()==v2.getX()){
			v1x=x; 
			v1y=v1.getY();
			v2x=x; 
			v2y=v2.getY();
		}
		else if (v1.getY()==v2.getY()){
			v1x=v1.getX();
			v1y=y;
			v2x=v2.getX();
			v2y=y;
		}
		else {
			
			float a1=(v2.getY()-v1.getY())/(v2.getX()-v1.getX());
			float b1=v1.getY()-a1*v1.getX();
			float a2=-1/a1;
			float b2=y-a2*x;
			float vx=(b2-b1)/(a1-a2);
			float vy=vx*a1+b1;
			float mx=x-vx;
			float my=y-vy;
			v1x=v1.getX()+mx;
			v1y=v1.getY()+my;
			v2x=v2.getX()+mx;
			v2y=v2.getY()+my;
	
		}
		
		float[] list= {v1x,v1y,v2x,v2y};
		
		return list;
	}
	
	/** find the the wall on each side of the trace */
	public ArrayList<Wall> findWalls(int width, int height){
		ArrayList<Wall> sidWalls = new ArrayList<Wall>();
		
			float Vx=v2.getX()-v1.getX();
			float Vy=v2.getY()-v1.getY();
			
			float longeur=(float) Math.sqrt((Vx*Vx)+(Vy*Vy));
			
			float Dincx= (Vy/longeur)*(width/2);
			float Dincy=-(Vx/longeur)*(width/2);
			
			float Lincx= -(Vy/longeur)*(width/2);
			float Lincy=(Vx/longeur)*(width/2);
			
			sidWalls.add(new Wall(new Vertex(v1.getX()+Lincx, v1.getY()+Lincy), 
							new Vertex(v2.getX()+Lincx, v2.getY()+Lincy), height));
			
			sidWalls.add(new Wall(new Vertex(v1.getX()+Dincx, v1.getY()+Dincy), 
					new Vertex(v2.getX()+Dincx, v2.getY()+Dincy), height));
		
		return sidWalls;
	}
	
	public void addDoor(String id, boolean entrant) {
		float midX=(v1.getX()+v2.getX())/2;
		float midY=(v1.getY()+v2.getY())/2;
		Vertex c1 = new Vertex((v1.getX()+midX)/2,(v1.getY()+midY)/2);
		Vertex c2 = new Vertex((v2.getX()+midX)/2,(v2.getY()+midY)/2);
		o = new Door(id, c1, c2, entrant);		
	}

	public void addDoor(String id, float f, float g, float h, float i, boolean entrant, int height, String next){
		Vertex c1 = new Vertex(f,g);
		Vertex c2 = new Vertex(h,i);
		o = new Door(id, c1, c2, entrant, height, next);
	}
	
	/** mettre a jour l'ouvrtre apres le depalcement de mur */
	public void updateOpen(){
		float disX = v2.getX()-v1.getX();
		float disY = v2.getY()-v1.getY();
		float disXY = (float) Math.sqrt(disX*disX+disY*disY);
		float f = o.getR() * disXY;
		float supX = f * (disX/disXY);
		float supY = f * (disY/disXY);
		if(v1.getX()==v2.getX()){
			o.getMidVertex().move(v1.getX()+supX, v1.getY()+supY);
			if(disY>0){
				o.getV1().move(o.getMidVertex().getX(), o.getMidVertex().getY()-o.getWidth()/2);
				o.getV2().move(o.getMidVertex().getX(), o.getMidVertex().getY()+o.getWidth()/2);
			}
			else{
				o.getV1().move(o.getMidVertex().getX(), o.getMidVertex().getY()+o.getWidth()/2);
				o.getV2().move(o.getMidVertex().getX(), o.getMidVertex().getY()-o.getWidth()/2);
			}
		}else{
			float a = (v1.getY()-v2.getY())/(v1.getX()-v2.getX());
			float dx = (float) Math.sqrt(((o.getWidth()/2)*(o.getWidth()/2))/((1+a*a)));
			float dy = a*dx;
			o.getMidVertex().move(v1.getX()+supX, v1.getY()+supY);
			float x1 = o.getMidVertex().getX()-dx;
			float y1 = o.getMidVertex().getY()-dy;
			float x2 = o.getMidVertex().getX()+dx;
			float y2 = o.getMidVertex().getY()+dy;
			if(v2.ptDisVt(x1, y1) < v1.ptDisVt(x1, y1)){
				o.getV1().move(x2, y2);
				o.getV2().move(x1, y1);
			} else {
				o.getV1().move(x1, y1);
				o.getV2().move(x2, y2);
			}
			
		}
	}
	
	public void addWindow(String id) {
		float midX=(v1.getX()+v2.getX())/2;
		float midY=(v1.getY()+v2.getY())/2;
		Vertex c1 = new Vertex((v1.getX()+midX)/2,(v1.getY()+midY)/2);
		Vertex c2 = new Vertex((v2.getX()+midX)/2,(v2.getY()+midY)/2);
		o = new Window(id, c1, c2);
	}
	
	public void addWindow(String id, float f, float g, float h, float i, int height, int hB) {
		Vertex c1 = new Vertex(f,g);
		Vertex c2 = new Vertex(h,i);
		o = new Window(id, c1, c2, height, hB);
	}
	
	public float angle_entre_deux_trace(Wall w1, Wall w2){
		float x1=w1.getV2().getX();
		float y1=w1.getV2().getY();
		float x2=w1.getV1().getX();
		float y2=w1.getV1().getY();
		float x3=w2.getV2().getX();
		float y3=w2.getV2().getY();
		
		float longeurw1=w1.getV1().VtDisVt(w1.getV2());
		float longeurw2=w2.getV1().VtDisVt(w2.getV2());
		
		float w1x=x3-x1;
		float w1y=y3-y1;
		float w2x=x2-x1;
		float w2y=y2-y1;
		
		float produit_scalaire= w1x*w2x+w1y*w2y;
		float cosangle=produit_scalaire/(longeurw1*longeurw2);
		return cosangle;
		
	}
	public float[] moveOpen(float x, float y){
		float mx,my;
		if (v1.getX()==v2.getX()){
			mx=v1.getX();
			my=y;
		} else if (v1.getY()==v2.getY()){
			my=v1.getY();
			mx=x;
		} else {
			float a1=(v1.getY()-v2.getY())/(v1.getX()-v2.getX());
			float b1=v1.getY()-a1*v1.getX();
			float a2=-1/a1;
			float b2=y-a2*x;
			mx=(b2-b1)/(a1-a2);
			my=a1*mx+b1;
		}
		
		float[] l={mx,my};

		return l;
	}
	
	public void updateRatio(){
		float disX = v2.getX()-v1.getX();
		float disY = v2.getY()-v1.getY();
		float disXY = (float) Math.sqrt(disX*disX+disY*disY);
		float disV1X = v1.getX()-o.getMidVertex().getX();
		float disV1Y = v1.getY()-o.getMidVertex().getY();
		float disV1XY = (float) Math.sqrt(disV1X*disV1X+disV1Y*disV1Y);
		o.setR(disV1XY/disXY);
	}
	
	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if (selected){
			g2.setColor(ModeleurModel.WHITE);
		}
		else{
			g2.setColor(ModeleurModel.BLACK);
		}
		g2.setStroke(new BasicStroke(10));
		g2.draw(new Line2D.Float(v1.getX()+r, v1.getY()+r, v2.getX()+r, v2.getY()+r));
		
		v1.draw(g);
		v2.draw(g);
		
		if(o!=null){
			updateOpen();
			o.draw(g);
		}
	
	}
	
	public void drawTrace(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if (selected){
			g2.setColor(ModeleurModel.WHITE);
		}
		else{
			g2.setColor(ModeleurModel.GREY);
		}
		g2.setStroke(new BasicStroke(10, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
		g2.draw(new Line2D.Float(v1.getX()+r, v1.getY()+r, v2.getX()+r, v2.getY()+r));
		v1.draw(g);
		v2.draw(g);
	
	}
	
	public void draw (GL2 gl)
	{
		if(o==null){
			float X1 = v1.getX();
			float Z1 = v1.getY();
			float X2 = v2.getX();
			float Z2 = v2.getY();
			gl.glBegin(GL2.GL_QUADS);
			gl.glColor3f(0.4f, 0.3f, 0.8f);
			
				gl.glVertex3f(X1/100, height/100, Z1/100);
				gl.glVertex3f(X2/100, height/100, Z2/100);
				gl.glVertex3f(X2/100, 0.0f, Z2/100);
				gl.glVertex3f(X1/100, 0.0f, Z1/100);
			
			gl.glEnd();
		} else {
			new Wall(v1,o.getV1(), height).draw(gl);
			o.draw(gl, height);
			new Wall(o.getV2(),v2, height).draw(gl);
		}
	}
	
	public void drawcorridor(GL2 gl,float h){
		float X1 = v1.getX();
		float Z1 = v1.getY();
		float X2 = v2.getX();
		float Z2 = v2.getY();
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3f(0.4f, 0.3f, 0.8f);
		
			gl.glVertex3f(X1/100, height/100, Z1/100);
			gl.glVertex3f(X2/100, (height+h)/100, Z2/100);
			gl.glVertex3f(X2/100, h/100, Z2/100);
			gl.glVertex3f(X1/100, 0.0f, Z1/100);
		
		gl.glEnd();
	}
	
	public void draw(GL2 gl, float tT, float tB, float tL, float tR){
		
		if(o==null){
			float X1 = v1.getX();
			float Z1 = v1.getY();
			float X2 = v2.getX();
			float Z2 = v2.getY();
			gl.glBegin(GL2.GL_QUADS);			
				gl.glTexCoord2f(tL,tB);
				gl.glVertex3f(X1/100, height/100, Z1/100);
				gl.glTexCoord2f(tR, tB);
				gl.glVertex3f(X2/100, height/100, Z2/100);
				gl.glTexCoord2f(tR, tT);
				gl.glVertex3f(X2/100, 0.0f, Z2/100);
				gl.glTexCoord2f(tL, tT);
				gl.glVertex3f(X1/100, 0.0f, Z1/100);
			
			gl.glEnd();
		} else {
			new Wall(v1,o.getV1(), height).draw(gl, tT, tB, tL, tR);
			o.draw(gl, tT, tB, tL, tR, height);
			new Wall(o.getV2(),v2, height).draw(gl, tT, tB, tL, tR);
		}
	}

	public void setWindowHeight(int n) {
		if (o != null && o instanceof Window){
			Window win = (Window) o;
			win.setHeightBottom(n);
		}
	}

	public int getWindowHeight() {
		if (o != null && o instanceof Window){
			Window win = (Window) o;
			return win.getHeightBottom();
		}
		System.out.println("not a window");
		return -1;
	}

	public void setNext(String s) {
		if (o != null && o instanceof Door) {
			Door d = (Door) o;
			d.setNext(s);
		}
		
	}

	public String getNext() {
		if (o != null && o instanceof Door) {
			Door d = (Door) o;
			return d.getNext();
		}
		return null;
	}



	
	
}
