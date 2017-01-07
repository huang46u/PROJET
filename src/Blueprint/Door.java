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

public class Door extends Open {
	private boolean entrant;

	public Door(String id, Vertex v1, Vertex v2) {
		super(id, v1, v2);
	}
	

	@Override
	public void draw(Graphics g) {
		getV1().drawOpen(g);
		getV2().drawOpen(g);
		getMidVertex().draw(g);
	}

	@Override
	public void draw(GL2 gl) {
		float weight = 10f;
		float X1 = v1.getX();
		float Z1 = v1.getY();
		float X2 = v2.getX();
		float Z2 = v2.getY();
		
		gl.glBegin(GL2.GL_QUADS);
		
		gl.glColor3f(0.4f, 0.3f, 0.8f);
			gl.glVertex3f(X1/100, 1.0f, Z1/100);
			gl.glVertex3f(X2/100, 1.0f, Z2/100);
			gl.glVertex3f(X2/100, 0.75f, Z2/100);
			gl.glVertex3f(X1/100, 0.75f, Z1/100);

		gl.glEnd();
	}

	@Override
	public void move(float x1, float y1, float x2, float y2) {
		getV1().move(x1, y1);
		getV2().move(x2, y2);
		getMidVertex().move((x1+x2)/2,(y1+y2)/2);
	}

}
