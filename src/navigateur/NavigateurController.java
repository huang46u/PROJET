/**
 * PT3 Poly Editor
 * DUT Informatique 2016/2017 
 * Auteur : HUANG Qijia
 * 			LU Yi
 * Tuteur : P. Even
 * */

package navigateur;

import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_D;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_L;
import static java.awt.event.KeyEvent.VK_S;
import static java.awt.event.KeyEvent.VK_UP;
import static java.awt.event.KeyEvent.VK_W;
import static java.awt.event.KeyEvent.VK_F;
import static java.awt.event.KeyEvent.VK_V;
import static java.awt.event.KeyEvent.VK_N;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

import blueprint.Room;



public class NavigateurController implements KeyListener,ActionListener,MouseListener {
	private NavigateurModel model;

	public NavigateurController(NavigateurModel model){
		this.model = model;

	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()){
		case VK_A: // player turns left (scene rotates right)
			model.turnLeft();
			break;
		case VK_D: // player turns right (scene rotates left)
			model.turnRight();
			break;
		case VK_W:	// player move in, posX and posZ become smaller
			if (!model.modeNavigation){
				model.moveIn();
				if(model.hasNext())
					model.next();
			}
			else{
				float orX = model.getPosX();
				float orZ = model.getPosZ();
				model.moveIn();
				if(!model.room.isInZoneNav(model.getPosX()*100, model.getPosZ()*100)){
					float[] coords = model.room.isInZoneNav(model.getPosX()*100, model.getPosZ()*100, orX, orZ);
					//System.out.println(coords[0]+" "+coords[1]);
					model.setPosX(coords[0]);
					model.setPosZ(coords[1]);
				}
			}
			break;
		case VK_S: //player move out, posX and posZ become bigger
			if (!model.modeNavigation)
				model.moveOut();
			else{
				float orX = model.getPosX();
				float orZ = model.getPosZ();
				model.moveOut();
				if(!model.room.isInZoneNav(model.getPosX()*100, model.getPosZ()*100)){
					model.setPosX(orX);
					model.setPosZ(orZ);
				}
			}
			break;
		case VK_UP: // player looks up, scene rotates in negative x-axis
			model.lookUp();
			break;
		case VK_DOWN: // player looks down, scene rotates in positive x-axis
			model.lookDown();
			break;
		case VK_F:
			NavigateurView.currTextureFilter = (NavigateurView.currTextureFilter + 1) % NavigateurView.texturebox.length;
			break;
		case VK_V:
			NavigateurView.texturestat = (NavigateurView.texturestat + 1) % NavigateurView.textured.length;
			model.turnLight();
			break;
		case VK_N:
			model.turnNavigation();
			break;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e ) {
		String dirPath = model.openDia.getDirectory();  
		String fileName = model.openDia.getFile(); 
		
		Object source = e.getSource();
		if ( source == model.openItem){
			model.openDia.setVisible(true);  
			dirPath = model.openDia.getDirectory();  
			fileName = model.openDia.getFile();
			
			try {
				BufferedReader br = new BufferedReader(new FileReader(dirPath+fileName));
			    StringBuilder sb = new StringBuilder();
			    String line = br.readLine();

			    if(line.startsWith("CORRIDOR"))
			    	model.isRoomFile = false;
			    
			    br.close();

			} catch (IOException e1) {
				e1.printStackTrace();
			} 
			
			model.loadFile(dirPath+fileName);
			
		}
		
		
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseWheelMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}