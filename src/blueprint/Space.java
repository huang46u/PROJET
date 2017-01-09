package blueprint;

import java.awt.Graphics;
import java.io.IOException;
import com.jogamp.opengl.GL2;

public interface Space {
	public void addVertex();
	public void delVertex();
	public int getHeight();
	public void setHeight(int height);
	public Wall nextWall(Wall w);
	public Wall lastWall(Wall w);
	public void draw(Graphics g);
	public void draw(GL2 gl);
	public void draw(GL2 gl, float tT, float tB, float tL, float tR);
	public void write(String filepath) throws IOException;
	public void read(String filename) throws IOException;
	
}
