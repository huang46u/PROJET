/**
 * PT3 Poly Editor
 * DUT Informatique 2016/2017 
 * Auteur : HUANG Qijia
 * 			LU Yi
 * Tuteur : P. Even
 * */

package blueprint;

import java.awt.Graphics;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.math.Vert2fImmutable;

/** class abstrait Open : elle doit etre herite par la class Window et 
 *  la class Door */
public abstract class Open {
	// ----- attributs -----
	/** identifiant d'ouverture */
	private String id;
	/** largeur d'ouverture */
	private float width;
	/** hauteur d'ouverutre */
	protected int height;
	/** trois vertices pour les deux points d'ouverture, et le points de milieu */
	protected Vertex v1,v2,midv;
	/** ratio pour la mise a jour apres le depalcement d'un mur */
	private float r;
	
	// ----- constructeur -----
	/** Constructeur qui pren un nom et deux vertices */
	public Open(String id, Vertex v1, Vertex v2){
		this.id=id;
		width=v1.VtDisVt(v2); // on calcule le largeur automatiquement
		height = 75;
		this.v1=v1;
		this.v2=v2;
		this.r=(float)1/2;
		midv=new Vertex((v1.getX()+v2.getX())/2, (v1.getY()+v2.getY())/2);
	}

	// ----- methodes -----
	/** retourne le identifiant */
	public String getID(){
		return id;
	}
	
	/** retourne V1 */
	public Vertex getV1(){
		return v1;
	}
	
	/** retourne V2 */
	public Vertex getV2(){
		return v2;
	}
	
	/** retourne le point milieu */
	public Vertex getMidVertex(){
		return midv;
	}
	
	/** retourne le ratio */
	public float getR(){
		return r;
	}
	
	/** definir le ratio */
	public void setR(float r){
		this.r=r;
	}
	
	/** retourne le largeur d'ouverture */
	public float getWidth(){
		return width;
	}
	
	public void setWidth(float width){
		this.width=width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height){
		this.height=height;
	}
	
	/** methode abstrait a surcharger pour depalcer l'ouverture */
	public abstract void move(float x1, float y1, float x2, float y2);
	
	/** methode abstrait a surcharger pour dessiner l'ouverture sur le modeleur 2D */
	public abstract void draw(Graphics g);

	/** methode abstrait a surcharger pour dessiner l'ouverture sur le navigateur 3D */
	public abstract void draw(GL2 gl, int heightWall);

	/** methode abstrait a surcharger pour dessiner l'ouverture avec des texture sur le navigateur 3D */
	public abstract void draw(GL2 gl, float tT, float tB, float tL, float tR, int heightWall);
	
	


}
