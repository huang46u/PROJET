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
	
	public boolean isSelected(){
		return selected;
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
	
	}
	
	public void draw (GL2 gl)
	{
		gl.glBegin(GL2.GL_QUADS);
		
			gl.glColor3f(0.8f, 0.3f, 0.8f);
			gl.glVertex3f(v1.getX()/100, 1.0f, v1.getY()/100);
			gl.glVertex3f(v2.getX()/100, 1.0f, v2.getY()/100);
			gl.glVertex3f(v2.getX()/100, 0.0f, v2.getY()/100);
			gl.glVertex3f(v1.getX()/100, 0.0f, v1.getY()/100);
			
		gl.glEnd();
	}

	public void select() {
		selected = !selected;	
	}
	
}
