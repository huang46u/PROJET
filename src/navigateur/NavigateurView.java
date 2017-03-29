/**
 * PT3 Poly Editor
 * DUT Informatique 2016/2017 
 * Auteur : HUANG Qijia
 * 			LU Yi
 * Tuteur : P. Even
 * */

package navigateur;

import static com.jogamp.opengl.GL.GL_COLOR_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL.GL_DONT_CARE;
import static com.jogamp.opengl.GL.GL_LEQUAL;
import static com.jogamp.opengl.GL.GL_LINEAR;
import static com.jogamp.opengl.GL.GL_NICEST;
import static com.jogamp.opengl.GL.GL_TEXTURE_2D;
import static com.jogamp.opengl.GL.GL_TEXTURE_MAG_FILTER;
import static com.jogamp.opengl.GL.GL_TEXTURE_MIN_FILTER;
import static com.jogamp.opengl.GL2ES1.GL_EXP;
import static com.jogamp.opengl.GL2ES1.GL_EXP2;
import static com.jogamp.opengl.GL2ES1.GL_FOG;
import static com.jogamp.opengl.GL2ES1.GL_FOG_COLOR;
import static com.jogamp.opengl.GL2ES1.GL_FOG_DENSITY;
import static com.jogamp.opengl.GL2ES1.GL_FOG_END;
import static com.jogamp.opengl.GL2ES1.GL_FOG_HINT;
import static com.jogamp.opengl.GL2ES1.GL_FOG_MODE;
import static com.jogamp.opengl.GL2ES1.GL_FOG_START;
import static com.jogamp.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_AMBIENT;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_DIFFUSE;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_LIGHT1;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_LIGHTING;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_POSITION;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;
import com.jogamp.opengl.util.texture.TextureIO;

import blueprint.Corridor;
import blueprint.Room;

@SuppressWarnings("serial")
public class NavigateurView extends GLCanvas implements GLEventListener{
	private GLU glu;

	private NavigateurModel model;
	protected static int currTextureFilter = 0; 
	protected static int texturestat=0;
	protected static boolean[] textured= {false,true}; 
	//Tableau de texture qui contient des texture
	protected static Texture[] texturebox=new Texture[4];


	// ** Constructeur par default */
	public NavigateurView(NavigateurModel model) {
		this.model = model;
		this.addGLEventListener(this);
	}

	// ----- implement methods declared in GLEventListener -----

	

	/**
	 * Called back before the OpenGL context is destroyed. Release resource such as buffers.
	 * */
	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub

	}

	/**
	 * Called back immediately. after the OpenGL context is initialized. Can be used 
	 * to perform one-time initialization. Run only once.
	 * */
	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		glu = new GLU();
		gl.glClearColor(0.8f, 0.8f, 0.8f, 1.0f); // clear to the color of the fog
		//gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glDepthFunc(GL_LEQUAL);
		gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		gl.glShadeModel(GL_SMOOTH);
		gl.glEnable (GL2.GL_TEXTURE_2D);
		model.initTexture();
		if(model.textures!=null){
			try {
				// Create a OpenGL Texture object from (URL, mipmap, file suffix)
				// Use URL so that can read from JAR and disk file.
				for(int i=0;i<model.textures.size();i++){
					texturebox[i] = TextureIO.newTexture(
							new File(model.textures.get(i)), // relative to project root 
							false);		
				}
				// Use linear filter for texture if image is larger than the original texture
				gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
				// Use linear filter for texture if image is smaller than the original texture
				gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
				// Texture image flips vertically. Shall use TextureCoords class to retrieve
				// the top, bottom, left and right coordinates, instead of using 0.0f and 1.0f.
				TextureCoords textureCoords = texturebox[0].getImageTexCoords();
				model.textureTop = textureCoords.top();
				model.textureBottom = textureCoords.bottom();
				model.textureLeft = textureCoords.left();
				model.textureRight = textureCoords.right();
			}
			catch (GLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// Set up the lighting for Light-1
		// Ambient light does not come form a particular direction. Need some ambient 
		// light to light up the scene. Ambient's value in RGBA
		float[] lightAmbientValue = {1.0f,1.5f, 1.0f, 1.0f};
		// Diffuse light comes from a particulear location. Diffuse's value in RGBA
		float[] lightDiffuseValue = {2.0f, 2.0f, 2.0f, 2.0f};
		// Diffuse light location xyz (in front of the screen).
		float[] lightDiffusePosion = {0.0f, 0.0f, -2.0f, 1.0f};

		 gl.glLightfv(GL_LIGHT1, GL_AMBIENT, lightAmbientValue, 0);
			gl.glLightfv(GL_LIGHT1, GL_DIFFUSE, lightDiffuseValue, 0);
			gl.glLightfv(GL_LIGHT1, GL_POSITION, lightDiffusePosion, 0);
			gl.glEnable(GL_LIGHT1); // Enable Light-1
			gl.glDisable(GL_LIGHTING); // But disable lighting
			
		
	}
	/** 
	 * Called back by the animator to perform rendering
	 * */
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();

		// Rotate up and down to look up and down
		gl.glRotatef(model.getLookUpAngle(), 1.0f, 0, 0);

		// Player at headingY, Rotate the scene by -headingY instead (add 360 to get
		// positive angle)
		gl.glRotatef(360.0f - model.getHeadingY(), 0, 1.0f, 0);

		// Player is at (posX, 0, posZ), Translate the scene to (-posX, 0, -posZ)
		gl.glTranslatef(-model.getPosX(), -model.getWalkBias() - 0.5f, -model.getPosZ());
		//gl.glFogi(GL_FOG_MODE, fogModes[currFogFilter]); // Fog Mode
		// Lighting
		if (model.getIsLigntOn()) {
			gl.glEnable(GL_LIGHTING);
		} else {
			gl.glDisable(GL_LIGHTING);
		}
		if(textured[texturestat]){
		 // Enables this texture's target in the current GL context's state.
	      texturebox[currTextureFilter].enable(gl);
	      // Bind the texture with the currently chosen filter to the current OpenGL graphics context.
	      texturebox[currTextureFilter].bind(gl);
		}
		
		// On dessine la chambre ou le couloir
		if(!textured[texturestat]){
			if(model.isRoomFile){
				model.room.draw(gl);
				}
			else 
				model.corridor.draw(gl);
			if(model.hasNext()){
				
			}
		}
		else{
			if(model.isRoomFile)
				model.room.draw(gl, model.textureTop, model.textureBottom, model.textureLeft,model.textureRight);
			else 
				model.corridor.draw(gl, model.textureTop, model.textureBottom, model.textureLeft,model.textureRight);
		}
		
	     
	}
	/**
	 * Call-back handler for window re-size event. Also called when the drawable is
	 * first set to visible.
	 * */
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();

		if (height == 0) height =1; // eviter de diviser par zero
		float aspect = (float) width / height;

		// set the view port (display area) to cover the entire window
		gl.glViewport(0, 0, width, height);

		// set up perspective projection, with aspect ratio mathes viewport
		gl.glMatrixMode(GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(45.0, aspect, 0.1, 100.0);

		// Enable the model-view transform
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity();

	}

}