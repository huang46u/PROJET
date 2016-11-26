/**
 * PT3 Poly Editor
 * DUT Informatique 2016/2017 
 * Auteur : HUANG Qijia
 * 			LU Yi
 * Tuteur : P. Even
 * */

package Blueprint;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import com.jogamp.opengl.GL2;

import Modeleur.ModeleurModel;

public class Wall {
	private Vertex v1;
	private Vertex v2;
	private boolean selected=false;
	private Open o=null;
	
	//Rayon
	private float r = 25/2;

	public Wall(Vertex v1, Vertex v2) {
		super();
		this.v1 = v1;
		this.v2 = v2;
	}
	
	public Vertex getV1(){
		return v1;
	}
	
	public Vertex getV2(){
		return v2;
	}
	
	public Open getOpen(){
		return o;
	}
	
	public boolean isSelected(){
		return selected;
	}
	
	public void select() {
		selected = !selected;	
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
		
		if (o == null){
			
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
		
		} else{
			if(l.ptLineDist(x,y) <= r && ( between(x,y,v1.getX()+r+supX*2,v1.getY()+r+supY*2,o.getV1().getX()+r-supX*2,o.getV1().getY()+r-supY*2)
										|| between(x,y,o.getV1().getX()+r+supX*2,o.getV1().getY()+r+supY*2,o.getV2().getX()+r-supX*2,o.getV2().getY()+r-supY*2)
										|| between(x,y,o.getV2().getX()+r+supX*2,o.getV2().getY()+r+supY*2,v2.getX()+r-supX*2,v2.getY()+r-supY*2)
										)){
				selected = true;
			}else{
				selected=false;
			}
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
		v1.move(v1x, v1y);
		v2.move(v2x, v2y);
		
		float[] list= {v1x,v1y,v2x,v2y};
		
		return list;
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
			//addDoor(o.getID());
			o.draw(g);
		}
	
	}
	
	public void draw (GL2 gl)
	{
		if(o==null){
			gl.glBegin(GL2.GL_QUADS);
		
				gl.glColor3f(0.8f, 0.3f, 0.8f);
				gl.glVertex3f(v1.getX()/100, 1.0f, v1.getY()/100);
				gl.glVertex3f(v2.getX()/100, 1.0f, v2.getY()/100);
				gl.glVertex3f(v2.getX()/100, 0.0f, v2.getY()/100);
				gl.glVertex3f(v1.getX()/100, 0.0f, v1.getY()/100);
			
			gl.glEnd();
		} else {
			gl.glBegin(GL2.GL_QUADS);
			
			gl.glColor3f(0.8f, 0.3f, 0.8f);
			gl.glVertex3f(v1.getX()/100, 1.0f, v1.getY()/100);
			gl.glVertex3f(o.getV1().getX()/100, 1.0f, o.getV1().getY()/100);
			gl.glVertex3f(o.getV1().getX()/100, 0.0f, o.getV1().getY()/100);
			gl.glVertex3f(v1.getX()/100, 0.0f, v1.getY()/100);
			
			o.draw(gl);
			
			gl.glVertex3f(o.getV2().getX()/100, 1.0f, o.getV2().getY()/100);
			gl.glVertex3f(v2.getX()/100, 1.0f, v2.getY()/100);
			gl.glVertex3f(v2.getX()/100, 0.0f, v2.getY()/100);
			gl.glVertex3f(o.getV2().getX()/100, 0.0f, o.getV2().getY()/100);
		
		gl.glEnd();
		}
	}

	public void addDoor(String id) {
		float midX=(v1.getX()+v2.getX())/2;
		float midY=(v1.getY()+v2.getY())/2;
		Vertex c1 = new Vertex((v1.getX()+midX)/2,(v1.getY()+midY)/2);
		Vertex c2 = new Vertex((v2.getX()+midX)/2,(v2.getY()+midY)/2);
		o = new Door(id, c1, c2);		
	}

	public void addDoor(String id, float f, float g, float h, float i){
		Vertex c1 = new Vertex(f,g);
		Vertex c2 = new Vertex(h,i);
		o = new Door(id, c1, c2);
	}
	
	public void addWindow(String id) {
		float midX=(v1.getX()+v2.getX())/2;
		float midY=(v1.getY()+v2.getY())/2;
		Vertex c1 = new Vertex((v1.getX()+midX)/2,(v1.getY()+midY)/2);
		Vertex c2 = new Vertex((v2.getX()+midX)/2,(v2.getY()+midY)/2);
		o = new Window(id, c1, c2);
	}
	
}
