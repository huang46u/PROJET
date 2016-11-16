/**
 * PT3 Poly Editor
 * DUT Informatique 2016/2017 
 * Auteur : HUANG Qijia
 * 			LU Yi
 * Tuteur : P. Even
 * */

package Blueprint;

import java.awt.Graphics;

import Modeleur.ModeleurModel;

public class Vertex {
	private int x;
	private int y;
	private boolean selected = false;
	
	public Vertex(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public float ptDisVt(int x, int Y){
		return (float) Math.sqrt((this.x-x)*(this.x-x)+(this.y-y)*(this.y-y));
	}
	
	public void select(){
		selected=!selected;
	}
	
	public void select(int x, int y){
		if(selected) {
			selected=!selected;
			return;
			}
		
		int disX, disY;
		disX=x-this.x;
		disY=y-this.y;
		
		if ( disX<25 && disX>0 && disY<25 && disY>0){
			selected = true;
		}
		else{
			selected = false;
		}
	}
	
	public void move(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void draw (Graphics g){
		if (selected) {
			g.setColor(ModeleurModel.WHITE);
			g.fillOval(x, y, 25, 25);
			return;
		}
		g.setColor(ModeleurModel.BLACK);
		g.fillOval(x, y, 25, 25);
		return;
	}
	
	public boolean isSelected(){
		return selected;
	}
}
