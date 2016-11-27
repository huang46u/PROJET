/**
 * PT3 Poly Editor
 * DUT Informatique 2016/2017 
 * Auteur : HUANG Qijia
 * 			LU Yi
 * Tuteur : P. Even
 * */

package Modeleur;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.swing.JButton;

import Blueprint.Room;
import Blueprint.Wall;

/**
 * class ModeleurController 
 * celui-ci dirige tous les instructions de fonctionnalite de modeleur.
 * */
public class ModeleurController implements ActionListener, MouseListener, MouseMotionListener {
	/** ModeleurModel qui fournit tous les variables et instances, ainsi que les methodes de fonctionnalite */
	ModeleurModel mm;
	
	public ModeleurController(ModeleurModel mm) {
		this.mm = mm;
	}

	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		Font font = new Font("Arial", Font.BOLD, 20);
		GridBagConstraints g = new GridBagConstraints();
		
		for (Wall w : mm.room.getWalls()){
			w.getV1().select(x, y);
			w.getV2().select(x, y);
			w.select(x, y);
			if(w.isSelected()){
				mm.opts.remove(mm.bRectangle);
				mm.opts.remove(mm.bHexagon);
				mm.opts.remove(mm.bOctogon);
				
				mm.bVertx.addActionListener(this);
				mm.bDoor.addActionListener(this);
				mm.bWindow.addActionListener(this);
				
				g.gridx = 0;
				g.gridy = 1;
				g.insets= new Insets(10,0,10,0);
				mm.opts.add(mm.bVertx,g);
				g.gridy = 2;
				mm.opts.add(mm.bDoor,g);
				g.gridy = 3;
				mm.opts.add(mm.bWindow,g);
				
				mm.opts.validate();
				mm.opts.repaint();
			}
			if(w.getOpen()!=null){
				w.getOpen().getV1().select(x, y);
				w.getOpen().getV2().select(x, y);
			}
			mm.graph.repaint();
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		float x = e.getX();
		float y = e.getY();
		
		for (Wall w : mm.room.getWalls()){
			if(w.isSelected()){
				float[] list=w.move(x, y);
				mm.room.nextWall(w).getV1().move(list[2], list[3]);
				mm.room.lastWall(w).getV2().move(list[0], list[1]);
				mm.graph.repaint();
			}	
			else if (w.getV1().isSelected()){
				w.getV1().move(x-mm.r, y-mm.r);
				mm.graph.repaint();
			}
			else if (w.getV2().isSelected()){
				w.getV2().move(x-mm.r, y-mm.r);
				mm.graph.repaint();
			}
			else if (w.getOpen() != null){
				float[] l=w.moveOpen(x, y);
				float r=w.ratioOpen(x, y);
				if(w.getOpen().getV1().isSelected()){
					w.getOpen().getV1().move(l[0]-mm.r, l[1]-mm.r);
					w.getOpen().setR1(r);
				}
				else if (w.getOpen().getV2().isSelected()){
					w.getOpen().getV2().move(l[0]-mm.r, l[1]-mm.r);
					w.getOpen().serR2(r);
				}
				mm.graph.repaint();
			}
		}
		
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		Font font = new Font("Arial", Font.BOLD, 20);
		GridBagConstraints g = new GridBagConstraints();
		
		if (source == mm.bSave){
			try {
				mm.room.write();
				Room room2= new Room();
				room2.read("test.txt");
			} catch (IOException exception){
				System.out.println("cant even!");
			}
		} else if (source == mm.bRoom){
			mm.mode=1;
			mm.opts.remove(mm.bRoom);
			mm.opts.remove(mm.bCorridor);
			
			mm.bRectangle.addActionListener(this);
			mm.bHexagon.addActionListener(this);
			mm.bOctogon.addActionListener(this);
			
			g.gridx = 0;
			g.gridy = 1;
			g.insets= new Insets(10,0,10,0);
			mm.opts.add(mm.bRectangle,g);
			g.gridy = 2;
			mm.opts.add(mm.bHexagon,g);
			g.gridy = 3;
			mm.opts.add(mm.bOctogon,g);
			
			mm.opts.validate();
			mm.opts.repaint();
			mm.graph.validate();
			mm.graph.repaint();
		} else if (source == mm.bCorridor){
			mm.mode=2;
			mm.graph.validate();
			mm.graph.repaint();
		} else if (source == mm.bRectangle){
			mm.room = new Room(4,"Rectangle");
			mm.graph.repaint();
		} else if (source == mm.bHexagon){
			mm.room = new Room(4,"Rectangle");
			mm.graph.repaint();
		} else if (source == mm.bOctogon){
			mm.room = new Room(4,"Rectangle");
			mm.graph.repaint();
		} else if (source == mm.bVertx){
			mm.room.addVertex();
			mm.graph.repaint();
		} else if (source == mm.bDoor){
			mm.room.addDoor("Door");
			mm.graph.repaint();
		} else if (source == mm.bWindow){
			mm.room.addWindow("Window");
			mm.graph.repaint();
		}
	}

}
