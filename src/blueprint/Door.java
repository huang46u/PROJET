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

/** class Door : elle herite de la class Open, 
 *  avec une attibut pour differencier les entrees et sorties 
 *  et un nom de fichier du couloir prochaine */
public class Door extends Open {
	// ----- attributs -----
	/** un boolean pour differencier les entrees et sorties */
	private boolean entrant;
	/** le nom de fichier du couloir prochaine */
	private String next;

	// ----- constructeurs ----
	/** Constructeur qui prend un nom, deux vertices , et un boolean */
	public Door(String id, Vertex v1, Vertex v2, boolean entrant) {
		super(id, v1, v2);
		this.entrant = entrant;
		next=null;
	}
	
	/** Constructeur qui prend un nom, deux vertices, un boolean */
	public Door(String id, Vertex v1, Vertex v2, boolean entrant, int height, String next){
		super(id, v1, v2);
		this.entrant = entrant;
		this.height = height;
		this.next = next;
	}
	
	/** retourne le nom de fichier pour charger le couloir suivant */
	public String getNext(){
		return next;
	}
	
	/** definir le nom de fichier pour charger le couloir suivant */
	public void setNext(String next){
		this.next = next;
	}
	
	/** retourne true si la porte est une entree, false si la porte est une sortie */
	public boolean isEntrance(){
		return entrant;
	}
	
	// surcharger des methodes de la class Open

	@Override
	public void draw(Graphics g) {
		getV1().drawOpen(g);
		getV2().drawOpen(g);
		getMidVertex().draw(g);
	}

	@Override
	public void draw(GL2 gl, int heightWall) {
		float X1 = v1.getX();
		float Z1 = v1.getY();
		float X2 = v2.getX();
		float Z2 = v2.getY();
		float Y1 = (float) heightWall;
		float Y2 = (float) height;
		
		gl.glBegin(GL2.GL_QUADS);
		
		gl.glColor3f(0.4f, 0.3f, 0.8f);
			gl.glVertex3f(X1/100, Y1/100, Z1/100);
			gl.glVertex3f(X2/100, Y1/100, Z2/100);
			gl.glVertex3f(X2/100, Y2/100, Z2/100);
			gl.glVertex3f(X1/100, Y2/100, Z1/100);

		gl.glEnd();
	}
	@Override
	public void draw(GL2 gl, float tT, float tB, float tL, float tR, int heightWall) {
		float X1 = v1.getX();
		float Z1 = v1.getY();
		float X2 = v2.getX();
		float Z2 = v2.getY();
		float Y1 = (float) heightWall;
		float Y2 = (float) height;

		gl.glBegin(GL2.GL_QUADS);


		gl.glTexCoord2f(tL,tB);
		gl.glVertex3f(X1/100, Y1/100, Z1/100);
		gl.glTexCoord2f(tR, tB);
		gl.glVertex3f(X2/100, Y1/100, Z2/100);
		gl.glTexCoord2f(tR, tT);
		gl.glVertex3f(X2/100, Y2/100, Z2/100);
		gl.glTexCoord2f(tL, tT);
		gl.glVertex3f(X1/100, Y2/100, Z1/100);

		gl.glEnd();
	}

	@Override
	public void move(float x1, float y1, float x2, float y2) {
		getV1().move(x1, y1);
		getV2().move(x2, y2);
		getMidVertex().move((x1+x2)/2,(y1+y2)/2);
	}

}
