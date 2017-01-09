/**
 * PT3 Poly Editor
 * DUT Informatique 2016/2017 
 * Auteur : HUANG Qijia
 * 			LU Yi
 * Tuteur : P. Even
 * */

package modeleur;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import blueprint.Corridor;
import blueprint.Door;
import blueprint.Room;
import blueprint.Wall;

/**
 * class ModeleurController 
 * celui-ci dirige tous les instructions de fonctionnalite de modeleur.
 * */
public class ModeleurController implements ActionListener, MouseListener, MouseMotionListener {
	/** ModeleurModel qui fournit tous les variables et instances, ainsi que les methodes de fonctionnalite */
	ModeleurModel mm;
	
	JDialog igBNR, igBH, igBW, igBRH;
	
	public ModeleurController(ModeleurModel mm) {
		this.mm = mm;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		boolean selected=false;
		GridBagConstraints g = new GridBagConstraints();
		
		if(mm.mode==1 && !mm.room.isNavigationModeOn()){
			for (Wall w : mm.room.getWalls()){
				w.getV1().select(x, y);
				if(w.getV1().isSelected()){
					mm.toolbar.removeAll();
					
					mm.toolbar.add(mm.optsVertex, BorderLayout.CENTER);
					mm.toolbar.add(mm.save, BorderLayout.SOUTH);
					mm.toolbar.validate();
					mm.toolbar.repaint();
					
					selected=true;
				}
				w.getV2().select(x, y);
				if(w.getV2().isSelected()){
					selected=true;
				}
				w.select(x, y);
				if(w.isSelected()){
					mm.toolbar.removeAll();
					
					mm.bVertex.addActionListener(this);
					mm.bDoor.addActionListener(this);
					mm.bWindow.addActionListener(this);
					
					mm.toolbar.add(mm.optsWall, BorderLayout.CENTER);
					mm.toolbar.add(mm.save, BorderLayout.SOUTH);
					mm.toolbar.validate();
					mm.toolbar.repaint();
					
					selected=true;
				}
				if(w.getOpen()!=null){
					w.getOpen().getV1().select(x, y);
					if(w.getOpen().getV1().isSelected()){
						if(w.isSelected()) w.select();
						selected=true;
					}
					w.getOpen().getV2().select(x, y);
					if(w.getOpen().getV2().isSelected()){
						if(w.isSelected()) w.select();
						selected=true;
					}
					w.getOpen().getMidVertex().select(x, y);
					if(w.getOpen().getMidVertex().isSelected()){
						if(w.isSelected()) w.select();
						selected=true;
					}
				}
				mm.graph.repaint();
				
				if(!selected){
					mm.toolbar.removeAll();
					mm.toolbar.add(mm.optsRoom, BorderLayout.CENTER);
					mm.toolbar.add(mm.save, BorderLayout.SOUTH);
					mm.toolbar.validate();
					mm.toolbar.repaint();
				}
			}
				
		}
		else if (mm.mode == 1 && mm.room.isNavigationModeOn()){
			for (Wall w : mm.room.getNavigationZone()){
				w.getV1().select(x, y);
				w.getV2().select(x, y);
				mm.graph.repaint();
			}
		}
		else if (mm.mode==2){
			for (Wall w : mm.corridor.getTraces()){
				w.getV1().select(x, y);
				if(w.getV1().isSelected()){
					mm.toolbar.removeAll();
					
					mm.toolbar.add(mm.optsVertex, BorderLayout.CENTER);
					mm.toolbar.add(mm.save, BorderLayout.SOUTH);
					mm.toolbar.validate();
					mm.toolbar.repaint();
					
					selected=true;
				}
				w.getV2().select(x, y);
				if(w.getV2().isSelected()){
					selected=true;
				}
				w.select(x, y);
				if(w.isSelected()){
					mm.toolbar.removeAll();
					
					mm.bAddVertex.addActionListener(this);
					
					mm.toolbar.add(mm.optsTraces, BorderLayout.CENTER);
					mm.toolbar.add(mm.save, BorderLayout.SOUTH);
					mm.toolbar.validate();
					mm.toolbar.repaint();
					
					selected=true;
				}
				mm.graph.repaint();
				
				if(!selected){
					mm.toolbar.removeAll();
					mm.toolbar.add(mm.optsCorridor, BorderLayout.CENTER);
					mm.toolbar.add(mm.save, BorderLayout.SOUTH);
					mm.toolbar.validate();
					mm.toolbar.repaint();
				}
			}
			
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
		
		if(mm.mode==1  && !mm.room.isNavigationModeOn()){
			for (Wall w : mm.room.getWalls()){
				if(w.isSelected()){
					float orX1 = w.getV1().getX();
					float orY1 = w.getV1().getY();
					float orX2 = w.getV2().getX();
					float orY2 = w.getV2().getY();
					
					float[] list=w.move(x, y);
					w.getV1().move(list[0], list[1]);
					w.getV2().move(list[2], list[3]);
					mm.room.nextWall(w).getV1().move(list[2], list[3]);
					mm.room.lastWall(w).getV2().move(list[0], list[1]);
					
					if(!mm.room.isConvexe()){
						w.getV1().move(orX1, orY1);
						w.getV2().move(orX2, orY2);
						mm.room.nextWall(w).getV1().move(orX2, orY2);
						mm.room.lastWall(w).getV2().move(orX1, orY1);
					}
					mm.graph.repaint();
				}	
				else if (w.getV2().isSelected()){
					float orX = w.getV2().getX();
					float orY = w.getV2().getY();
					w.getV2().move(x-mm.r, y-mm.r);
					mm.room.nextWall(w).getV1().move(x-mm.r, y-mm.r);
					if(!mm.room.isConvexe() || (w.getOpen()!=null && w.getOpen().getWidth()>w.getV1().VtDisVt(w.getV2()))){
						w.getV2().move(orX, orY);
						mm.room.nextWall(w).getV1().move(orX, orY);
					}else if(mm.room.nextWall(w).getOpen()!=null && mm.room.nextWall(w).getOpen().getWidth() > mm.room.nextWall(w).getV1().VtDisVt(mm.room.nextWall(w).getV2())){
						w.getV2().move(orX, orY);
						mm.room.nextWall(w).getV1().move(orX, orY);
					}
					mm.graph.repaint();
				}
				else if (w.getOpen() != null){
					float[] l=w.moveOpen(x, y);
					if(w.getOpen().getV1().isSelected()){
						float midX = (w.getOpen().getV2().getX()+l[0])/2;
						float midY = (w.getOpen().getV2().getY()+l[1])/2;
						if(mm.room.lastWall(w).ptPosition(l[0], l[1])<1 && mm.room.nextWall(w).ptPosition(l[0], l[1])<1){
							w.getOpen().getV1().move(l[0], l[1]);
							w.getOpen().getMidVertex().move(midX, midY);
							w.updateRatio();
							w.getOpen().setWidth(w.getOpen().getV1().VtDisVt(w.getOpen().getV2()));
						}
					}
					else if (w.getOpen().getV2().isSelected()){
						float midX = (w.getOpen().getV1().getX()+l[0])/2;
						float midY = (w.getOpen().getV1().getY()+l[1])/2;
						if(mm.room.lastWall(w).ptPosition(l[0], l[1])<1 && mm.room.nextWall(w).ptPosition(l[0], l[1])<1){
							w.getOpen().getV2().move(l[0], l[1]);
							w.getOpen().getMidVertex().move(midX, midY);
							w.updateRatio();
							w.getOpen().setWidth(w.getOpen().getV1().VtDisVt(w.getOpen().getV2()));
						}
					}
					else if(w.getOpen().getMidVertex().isSelected()){
						float mx = l[0]-w.getOpen().getMidVertex().getX();
						float my = l[1]-w.getOpen().getMidVertex().getY();
						float vx1 = w.getOpen().getV1().getX();
						float vy1 = w.getOpen().getV1().getY();
						float vx2 = w.getOpen().getV2().getX();
						float vy2 = w.getOpen().getV2().getY();
						if(mm.room.lastWall(w).ptPosition(vx1+mx, vy1+my)<0 && mm.room.nextWall(w).ptPosition(vx2+mx, vy2+my)<0){
							w.getOpen().getMidVertex().move(l[0], l[1]);
							w.getOpen().getV1().move(vx1+mx, vy1+my);
							w.getOpen().getV2().move(vx2+mx, vy2+my);
							w.updateRatio();
						}
					}
				}
				mm.graph.repaint();
				
			}
		} 
		else if (mm.mode == 1 && mm.room.isNavigationModeOn()){
			for (Wall w : mm.room.getNavigationZone()){
				if (w.getV2().isSelected()){
					float orX = w.getV2().getX();
					float orY = w.getV2().getY();
					w.getV2().move(x-mm.r, y-mm.r);
					mm.room.nextTrace(w).getV1().move(x-mm.r, y-mm.r);
					mm.graph.repaint();
				}
			}
		}
		else if (mm.mode == 2){
			for (Wall w : mm.corridor.getTraces()){
				if(w.isSelected()){
					float[] list=w.move(x, y);
					if(mm.corridor.nextWall(w)==null){
						
					}else{
						w.getV2().move(list[2], list[3]);
						mm.corridor.nextWall(w).getV1().move(list[2], list[3]);
					}
					if(mm.corridor.lastWall(w)==null){
						
					}else{
						w.getV1().move(list[0], list[1]);
						mm.corridor.lastWall(w).getV2().move(list[0], list[1]);
					}
				}
				else if (w.getV1().isSelected()){
					w.getV1().move(x-mm.r, y-mm.r);
				}
				else if (w.getV2().isSelected()){
					w.getV2().move(x-mm.r, y-mm.r);
				}
				mm.corridor.updateWalls();
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
			mm.saveDia.setVisible(true);
			final String dir = mm.saveDia.getDirectory();
			final String file = mm.saveDia.getFile();
			mm.saveDia.dispose();
			if (dir != null && file != null) {
				try {
					if(mm.mode==1){
						mm.room.write(dir+file);
					} else if(mm.mode==2){
						mm.corridor.write(dir+file);
					}
				} catch (IOException exception){
					System.out.println("can't save!");
				}
			}
		} else if (source == mm.bOpen){
			mm.openDia.setVisible(true);
			final String dir = mm.openDia.getDirectory();
			final String file = mm.openDia.getFile();
			mm.openDia.dispose();
			if (dir != null && file != null) {
				try {
					if(mm.mode==1){
						mm.room = new Room();
						mm.room.read(dir+file);
					} else if(mm.mode==2){
						mm.corridor = new Corridor();
						mm.corridor.read(dir+file);
					}
				} catch (IOException exception){
					System.out.println("can't read!");
				}
			}
			mm.graph.validate();
			mm.graph.repaint();
		}else if (source == mm.bRoom){
			mm.mode=1;
			mm.toolbar.removeAll();
			
			mm.bRectangle.addActionListener(this);
			mm.bHexagon.addActionListener(this);
			mm.bOctogon.addActionListener(this);
			mm.bRoomHeight.addActionListener(this);
			mm.bNavigation.addActionListener(this);
			mm.bRoomAnnuler.addActionListener(this);
			
			mm.toolbar.add(mm.optsRoom, BorderLayout.CENTER);
			mm.toolbar.add(mm.save, BorderLayout.SOUTH);
			mm.toolbar.validate();
			mm.toolbar.repaint();
			mm.graph.validate();
			mm.graph.repaint();
			
		} else if (source == mm.bCorridor ){
			mm.mode=2;
			mm.toolbar.removeAll();
			
			mm.bVertex.addActionListener(this);
			mm.bWidth.addActionListener(this);
			mm.bHeight.addActionListener(this);
			mm.bNextRoom.addActionListener(this);
			mm.bCorridorAnnuler.addActionListener(this);
			
			mm.toolbar.add(mm.optsCorridor, BorderLayout.CENTER);
			mm.toolbar.add(mm.save, BorderLayout.SOUTH);
			mm.toolbar.validate();
			mm.toolbar.repaint();
			mm.graph.validate();
			mm.graph.repaint();
		} else if (source == mm.bRectangle && mm.mode==1){
			mm.room = new Room(4,"Rectangle", mm.screenWidth);
			mm.graph.repaint();
		} else if (source == mm.bHexagon && mm.mode==1){
			mm.room = new Room(6,"Hexagone", mm.screenWidth);
			mm.graph.repaint();
		} else if (source == mm.bOctogon && mm.mode==1){
			mm.room = new Room(8,"Octogone", mm.screenWidth);
			mm.graph.repaint();
		} else if (source == mm.bRoomHeight && mm.mode==1){
			if (igBRH == null) igBRH=new JDialog();
			
			JTextField input; //Composants textuels de l'interface
			 
			 //JPanel Nord
			input= new JTextField(10);
			input.setPreferredSize(new Dimension(200,30));
			input.addActionListener(new ActionListener() {
		         public void actionPerformed(ActionEvent e) {
		        	JTextField jt = (JTextField) e.getSource();
		     		String w = jt.getText();
		     		int n = Integer.parseInt(w);
		     		mm.room.setHeight(n);
		     		
		            }
		          });
			input.setFont(font);
			input.setText(""+mm.room.getHeight());
			 
			JPanel controlPanel= new JPanel(new GridLayout(1,2));
			JLabel nb = new JLabel("HAUTEUR ",JLabel.CENTER);
			nb.setFont(font);
			nb.setForeground(ModeleurModel.BLACK);
			controlPanel.setBackground(ModeleurModel.DARKGREY4);
			nb.setPreferredSize(new Dimension(110*2,70));
			controlPanel.add(nb);
			controlPanel.add(input);
			
			igBRH.getContentPane().add(controlPanel);
			igBRH.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			igBRH.pack();
			igBRH.setVisible(true);
			
		} else if (source == mm.bNavigation && mm.mode==1){
			if (mm.room.isNavigationModeOn())
				mm.room.turnOffNavigation();
			else
				mm.room.turnOnNavigation();
			mm.graph.validate();
			mm.graph.repaint();
		} else if (source == mm.bRoomAnnuler && mm.mode==1){
			mm.mode = 0;
			mm.room = new Room(4,"Rectangle", mm.screenWidth);

			mm.toolbar.removeAll();
			
			mm.toolbar.add(mm.optsMode, BorderLayout.CENTER);
			//mm.toolbar.add(mm.save, BorderLayout.SOUTH);
			mm.toolbar.validate();
			mm.toolbar.repaint();
			mm.graph.validate();
			mm.graph.repaint();
		} else if (source == mm.bVertex && mm.mode==1){
			mm.room.addVertex();
			mm.graph.repaint();
			
			mm.toolbar.removeAll();
			mm.toolbar.add(mm.optsVertex, BorderLayout.CENTER);
			mm.toolbar.add(mm.save, BorderLayout.SOUTH);
			mm.toolbar.validate();
			mm.toolbar.repaint();
			
		} else if (source == mm.bDoor && mm.mode==1){
			mm.room.addDoor("Door", false);
			mm.graph.repaint();
		} else if (source == mm.bWindow && mm.mode==1){
			mm.room.addWindow("Window");
			mm.graph.repaint();
		} else if (source == mm.bDelVertex && mm.mode==1){
			mm.room.delVertex();
			mm.graph.repaint();
			
			mm.toolbar.removeAll();
			mm.toolbar.add(mm.optsRoom, BorderLayout.CENTER);
			mm.toolbar.add(mm.save, BorderLayout.SOUTH);
			mm.toolbar.validate();
			mm.toolbar.repaint();
			mm.graph.validate();
			mm.graph.repaint();
		} else if (source == mm.bAddVertex && mm.mode == 2){
			mm.corridor.addVertex();
			mm.corridor.updateWalls();
			mm.graph.repaint();
			
			mm.toolbar.removeAll();
			mm.toolbar.add(mm.optsVertex, BorderLayout.CENTER);
			mm.toolbar.add(mm.save, BorderLayout.SOUTH);
			mm.toolbar.validate();
			mm.toolbar.repaint();
		} else if (source == mm.bDelVertex && mm.mode==2){
			mm.corridor.delVertex();
			mm.corridor.updateWalls();
			mm.graph.repaint();
			
			mm.toolbar.removeAll();
			mm.toolbar.add(mm.optsCorridor, BorderLayout.CENTER);
			mm.toolbar.add(mm.save, BorderLayout.SOUTH);
			mm.toolbar.validate();
			mm.toolbar.repaint();
			mm.graph.validate();
			mm.graph.repaint();
		} else if (source == mm.bWidth && mm.mode==2){
			if (igBW == null) igBW=new JDialog();
			
			JTextField input; //Composants textuels de l'interface
			 
			 //JPanel Nord
			input= new JTextField(10);
			input.setPreferredSize(new Dimension(200,30));
			input.addActionListener(new ActionListener() {
		         public void actionPerformed(ActionEvent e) {
		        	JTextField jt = (JTextField) e.getSource();
		     		String w = jt.getText();
		     		int n = Integer.parseInt(w);
		     		mm.corridor.setWidth(n);
		     		mm.corridor.updateWalls();
		     		mm.graph.validate();
					mm.graph.repaint();
		            }
		          });
			input.setFont(font);
			input.setText(""+mm.corridor.getWidth());
			 
			JPanel controlPanel= new JPanel(new GridLayout(1,2));
			JLabel nb = new JLabel("LARGEUR ",JLabel.CENTER);
			nb.setFont(font);
			nb.setForeground(ModeleurModel.BLACK);
			controlPanel.setBackground(ModeleurModel.DARKGREY4);
			nb.setPreferredSize(new Dimension(110*2,70));
			controlPanel.add(nb);
			controlPanel.add(input);
			
			igBW.getContentPane().add(controlPanel);
			igBW.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			igBW.pack();
			igBW.setVisible(true);
			
		} else if (source == mm.bHeight && mm.mode==2){
			if (igBH == null) igBH=new JDialog();
			
			JTextField input;
			 
			input= new JTextField(10);
			input.setPreferredSize(new Dimension(200,30));
			input.addActionListener(new ActionListener() {
		         public void actionPerformed(ActionEvent e) {
		        	JTextField jt = (JTextField) e.getSource();
		     		String w = jt.getText();
		     		int n = Integer.parseInt(w);
		     		mm.corridor.setHeight(n);
		            }
		          });
			input.setFont(font);
			input.setText(""+mm.corridor.getHeight());
			 
			JPanel controlPanel= new JPanel(new GridLayout(1,2));
			JLabel nb = new JLabel("HAUTEUR ",JLabel.CENTER);
			nb.setFont(font);
			nb.setForeground(ModeleurModel.BLACK);
			controlPanel.setBackground(ModeleurModel.DARKGREY4);
			nb.setPreferredSize(new Dimension(110*2,70));
			controlPanel.add(nb);
			controlPanel.add(input);
			
			igBH.getContentPane().add(controlPanel);
			igBH.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			igBH.pack();
			igBH.setVisible(true);
		} else if (source == mm.bNextRoom && mm.mode==2){
			if (igBNR == null) igBNR=new JDialog();
			
			JTextField input2; //Composants textuels de l'interface
			 //JPanel Nord
			input2= new JTextField(10);
			input2.setPreferredSize(new Dimension(200,30));
			input2.addActionListener(new ActionListener() {
		         public void actionPerformed(ActionEvent e) {
		        	JTextField jt = (JTextField) e.getSource();
		     		String s = jt.getText();
		     		mm.corridor.setIdNextRoom(s);
		            }
		          });
			input2.setFont(font);
			input2.setText(""+mm.corridor.getIdNextRoom());
			 
			JPanel controlPanel2= new JPanel(new GridLayout(1,2));
			JLabel nb2 = new JLabel("CONNECTER A",JLabel.CENTER);
			nb2.setFont(font);
			nb2.setForeground(ModeleurModel.BLACK);
			controlPanel2.setBackground(ModeleurModel.DARKGREY4);
			nb2.setPreferredSize(new Dimension(110*2,70));
			controlPanel2.add(nb2);
			controlPanel2.add(input2);
			
			File folder = new File("rooms");
			File[] listOffiles = folder.listFiles();
			JTextArea list = new JTextArea();
			list.append("\n Les chambres a choisir : \n\n");
			for (File f: listOffiles)
				list.append(" # "+f.getName()+"\n");
			list.setFont(font);
			
			igBNR.getContentPane().add(controlPanel2, BorderLayout.NORTH);
			igBNR.getContentPane().add(new JScrollPane(list),BorderLayout.CENTER);
			igBNR.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			igBNR.pack();
			igBNR.setVisible(true);
		} else if (source == mm.bCorridorAnnuler && mm.mode==2){
			mm.mode = 0;
			mm.corridor = new Corridor("Couloir", mm.screenWidth);
			mm.toolbar.removeAll();
			
			mm.toolbar.add(mm.optsMode, BorderLayout.CENTER);
			//mm.toolbar.add(mm.save, BorderLayout.SOUTH);
			mm.toolbar.validate();
			mm.toolbar.repaint();
			mm.graph.validate();
			mm.graph.repaint();
		} 
	}
	
	

}
