package modeleur;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;


public class GraphHeight extends JPanel{
	
	public JPanel j;
	
	private int wallheight=100;
	private int windowHeightA=75;
	private int windowHeightB=30;
	private int doorHeight=80;
	private int mode=0;
	
	
	public int getwallheight(){
		return wallheight;
		
	}
	public void setwallheight(int wallheight){
		this.wallheight=wallheight;
	}
	
	public int getwindowHeightA(){
		return windowHeightA;
		
	}
	public void setwindowHeightA(int windowHeightA){
		this.windowHeightA=windowHeightA;
	}
	public int getwindowHeightB(){
		return windowHeightB;
		
	}
	public void setwindowHeightB(int windowHeightB){
		this.windowHeightB=windowHeightB;
	}
	
	public int getdoorHeight(){
		return doorHeight;
		
	}
	public void setdoorHeight(int doorHeight){
		this.doorHeight=doorHeight;
	}
	public void changemode(int mode){
		this.mode=mode;
	}
	
	public void paintComponent (Graphics g) {
		
		super.paintComponent(g);
		this.setBackground(Color.white);
		System.out.println(mode);
		
		if(mode==1){
		g.setColor(Color.GRAY);g.fill3DRect(125,(300-wallheight)/2,50,wallheight,true);
		
		}
		else if(mode==2){
			g.setColor(Color.green);g.fill3DRect(100,(300-wallheight)/2,100,wallheight,true);
			g.setColor(Color.yellow);g.fill3DRect(125,(300-wallheight)/2+wallheight-windowHeightA,50,windowHeightA-windowHeightB,true);
		}
		else{
			g.setColor(Color.green);g.fill3DRect(100,(300-wallheight)/2,100,wallheight,true);
			g.setColor(Color.yellow);g.fill3DRect(125,(300-wallheight)/2+wallheight-doorHeight,50,doorHeight,true);
		}
		
	}
}

