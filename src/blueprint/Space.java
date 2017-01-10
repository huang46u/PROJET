/**
 * PT3 Poly Editor
 * DUT Informatique 2016/2017 
 * Auteur : HUANG Qijia
 * 			LU Yi
 * Tuteur : P. Even
 * */

package blueprint;

import java.awt.Graphics;
import java.io.IOException;
import com.jogamp.opengl.GL2;

/** interface Space: il doit etre implemente par la class Room et la class Corridor */
public interface Space {
	/** ajouter un Vertex a la piece */
	public void addVertex();
	/** supperimer un Vertex a la piece */
	public void delVertex();
	/** retourne le hauteur de piece */
	public int getHeight();
	/** definir le hauteur de piece */
	public void setHeight(int height);
	/** Pour la class Room,
	 * 	on retourne la mur prochaine.
	 * 	Pour la class couloir, 
	 *  on retourne la prochaine trace de la suite de traces 
	 *  elle ne prend une trace en argument
	 *  les traces sont des objets de Wall.
	 *  elle retourne null si il y a plus de trace a choisir */
	public Wall nextWall(Wall w);
	/** Pour la class Room,
	 * 	on retourne la mur derniere
	 * 	Pour la class couloir, 
	 * 	on retourne trace derniere dans la suite de traces
	 *  elle ne prend une trace en argument
	 *  elle retourne null s'il n'y a plus de trace a choisir */
	public Wall lastWall(Wall w);
	/** dessiner le piece dans le modeleur 2D */
	public void draw(Graphics g);
	/** dessiner le piece dans le navigateur 3D */
	public void draw(GL2 gl);
	/** dessiner le piece dans le navigateur 3D */
	public void draw(GL2 gl, float tT, float tB, float tL, float tR);
	/** sauvegarder une piece */
	public void write(String filepath) throws IOException;
	/** charger une piece*/
	public void read(String filename) throws IOException;
	
}
