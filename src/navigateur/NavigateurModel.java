/**
 * PT3 Poly Editor
 * DUT Informatique 2016/2017 
 * Auteur : HUANG Qijia
 * 			LU Yi
 * Tuteur : P. Even
 * */

package navigateur;

import java.awt.FileDialog;
import java.awt.MenuItem;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;

import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;

import blueprint.Corridor;
import blueprint.Door;
import blueprint.Room;
import blueprint.Wall;
public class NavigateurModel {

	protected MenuItem openItem= new MenuItem("Ouvrir");
	final JFrame frame = new JFrame();	 
	protected MenuItem closeItem = new MenuItem("Exit"); 
	protected FileDialog openDia=  new FileDialog(frame,"Ouvrir",FileDialog.LOAD);
	
	protected String filename;
	protected FileDialog textureDia = new FileDialog(frame,"Texture",FileDialog.LOAD);
	protected String textureFileName;
	protected String textureFileType;
	
	// un map qui contien le  path d'image et le type d'image 
	protected ArrayList<String> textures= new ArrayList<String>();
	protected float textureTop, textureBottom, textureLeft, textureRight;

	protected GLCanvas canvas = new GLCanvas();
	private static final int FPS = 60;
	protected final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);
	
	//private static float posX = 2;
	//private float posZ = 2;
	private float headingY = 0; // heading of player; about y-axis
	private float lookUpAngle =0.0f;

	private float moveIncrement = 0.05f;
	private float turnIncrement = 3.0f; // each turn in degree
	private float lookUpIncrement = 1.0f;

	private float walkBias = 0;
	private float walkBiasAngle = 0;

	private static boolean isLightOn = false;
	
	protected Room room = new Room();
	protected Corridor corridor = new Corridor();
	
	private static float posX = 2;
	private float posZ = 2;
	
	protected boolean isRoomFile = true;
	
	protected boolean modeNavigation  = false;

	/** retourner le coordonnée de X */
	public float getPosX(){
		return posX;
	}
	
	/** retourner le coordonnée de Z */
	public float getPosZ(){
		return posZ;
	}
	
	public void setPosX(float posX){
		this.posX = posX;
	}
	
	public void setPosZ(float posZ){
		this.posZ=posZ;
	}
	
	/** retourner le coordonnée de Y */
	public float getHeadingY(){
		return headingY;
	}
	
	/** retourner le lookupAngle*/
	public float getLookUpAngle(){
		return lookUpAngle;
	}
	
	/** retourner le moveIncrement*/
	public float getMoveIncrement() {
		return moveIncrement;
	}
	
	/** retrouner le TurnIncrement*/
	public float getTurnIncrement(){
		return turnIncrement;
	}
	
	/** retourner le LookUpTncrement*/
	public float getLookUpIncrement(){
		return lookUpIncrement;
	}
	
	/** retourner le WalkBias*/
	public float getWalkBias(){
		return walkBias;
	}
	
	/** retourner le WalkBiasAngle*/ 
	public float getWalkBiasAngle(){
		return walkBiasAngle;
	}
	
	/** méthode de tourner à gauche*/
	public void turnLeft(){
		headingY += turnIncrement;
	}

	/** éèthode de tourner à droit*/
	public void turnRight(){
		headingY -= turnIncrement;
	}
	
	/** l'état de la lumière, retourner True si la lumière est allumé, False si la lumière est étendre*/ 
	public boolean getIsLigntOn(){
		return isLightOn;
	}
	
	/** méthode de allumer la lumière*/ 
	public void turnLight(){
		isLightOn = !isLightOn;
	}
	
	/** méthode de bouger en avant */
	public void moveIn(){
		posX -= (float)Math.sin(Math.toRadians(headingY)) * moveIncrement;
		posZ -= (float)Math.cos(Math.toRadians(headingY)) * moveIncrement;
		walkBiasAngle = (walkBiasAngle >= 359.0f) ? 0.0f : walkBiasAngle + 10.0f;
		walkBias = (float)Math.sin(Math.toRadians(walkBiasAngle)) / 20.0f;
	}
	
	/** m¨¦thode de bouger en arrière*/
	public void moveOut() {
		posX += (float)Math.sin(Math.toRadians(headingY)) * moveIncrement;
		posZ += (float)Math.cos(Math.toRadians(headingY)) * moveIncrement;
		walkBiasAngle = (walkBiasAngle <=1.0f) ? 359.0f : walkBiasAngle - 10.0f;
		walkBias = (float)Math.sin(Math.toRadians(walkBiasAngle)) / 20.0f;
	}
	
	/** méthode de regarder en haut*/
	public void lookUp(){
		lookUpAngle -= lookUpIncrement;
	}
	
	/** méthode de regarder en bas */
	public void lookDown() {
		lookUpAngle += lookUpIncrement;
	}

	
	public void initTexture(){
		textures.add("textures/MetalPainted.jpg");
		textures.add("textures/MetalPainted(1).jpg");
		textures.add("textures/MetalPlatesDucts.jpg");
		textures.add("textures/WoodFine.jpg");
		
	}
	
	public float[] findRoomEntrance(){
		float[] entrance = new float[2];
		for (Wall w : room.getWalls()){
			if(w.getOpen() != null && w.getOpen() instanceof Door){
				Door d = (Door)w.getOpen();
				if(d.isEntrance()){
					entrance[0] = (float)d.getMidVertex().getX()/100;
					entrance[1] = (float)d.getMidVertex().getY()/100;
					return entrance;
				}
			}
		}
		System.out.println("dont have entrance");
		return null;
	}

	public void turnNavigation() {
		modeNavigation = !modeNavigation;
		
	}

	public boolean isInZoneNav() {
		
		return false;
	}

	public float[] findCorridorEntrance() {
		float[] entrance = new float[2];
		entrance[0] = (float) corridor.getTraces().get(corridor.getTraces().size()-1).getV2().getX()/100;
		entrance[1] = (float) corridor.getTraces().get(corridor.getTraces().size()-1).getV2().getY()/100;
		return entrance;
	}


}