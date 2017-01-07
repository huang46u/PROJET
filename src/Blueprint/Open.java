/**
 * PT3 Poly Editor
 * DUT Informatique 2016/2017 
 * Auteur : HUANG Qijia
 * 			LU Yi
 * Tuteur : P. Even
 * */

package Blueprint;

import java.awt.Graphics;

import com.jogamp.opengl.GL2;

/** class Open */
public abstract class Open {
	private String id, next=null;
	private float width;
	protected Vertex v1,v2,midv;
	private float r;
	private String nextOpen=null;
	
	public Open(String id, Vertex v1, Vertex v2){
		this.id=id;
		width=v1.VtDisVt(v2);
		this.v1=v1;
		this.v2=v2;
		this.r=(float)1/2;
		midv=new Vertex((v1.getX()+v2.getX())/2, (v1.getY()+v2.getY())/2);
	}

	public String getID(){
		return id;
	}
	
	public Vertex getV1(){
		return v1;
	}
	
	public Vertex getV2(){
		return v2;
	}
	
	public Vertex getMidVertex(){
		return midv;
	}
	
	public float getR(){
		return r;
	}
	
	public void setR(float r){
		this.r=r;
	}
	
	public float getWidth(){
		return width;
	}
	
	public void setWidth(float width){
		this.width=width;
	}
	
	public abstract void move(float x1, float y1, float x2, float y2);
	
	public abstract void draw(Graphics g);

	public abstract void draw(GL2 gl);


}
