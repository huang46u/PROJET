/**
 * PT3 Poly Editor
 * DUT Informatique 2016/2017 
 * Auteur : HUANG Qijia
 * 			LU Yi
 * Tuteur : P. Even
 * */

package modeleur;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.Observable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import blueprint.Corridor;
import blueprint.Room;

/** 
 * class ModeleurModel 
 * celui-ci contient tous les variables et constantes prevues, ainsi que les methodes de fonctionnalite. 
 * */
public class ModeleurModel extends Observable{
	// Les constances de couleur  
	public static final Color BLACK = new Color(0 ,0 ,0);
	public static final Color DARKGREY1 = new Color(183, 183, 183); 
	public static final Color DARKGREY2 = new Color(153, 153, 153);
	public static final Color DARKGREY3 = new Color(102, 102, 102); 
	public static final Color DARKGREY4 = new Color(67, 67, 67); 
	public static final Color GREY = new Color(204, 204, 204); 
	public static final Color LIGHTGREY1 = new Color(217, 217, 217); 
	public static final Color LIGHTGREY2 = new Color(239, 239, 239);
	public static final Color LIGHTGREY3 = new Color(243, 243, 243);
	public static final Color WHITE = new Color(255, 255, 255); 
	
	/** Le Graphe qui dessine le Blueprint */
	protected Graph graph;
	
	/** Le bouton d'enregistrement */
	protected JButton bSave, bOpen;
	
	/** Une entiere qui signifie le mode de dessiner: 1 - Chambre(Room) ; 2 - Couloir(Corridor) */
	protected int mode=0;
	
	protected JPanel bg, toolbar, optsMode, optsRoom, optsCorridor, optsWall, optsTraces, optsVertex, save;
	
	protected JButton 	bRoom, bRoomAnnuler, bCorridor, bRectangle, bHexagon, bOctogon, bRoomHeight, bVertex, bAddVertex, bDoor, bWindow, 
						bDelVertex, bWidth, bNextRoom, bHeight, bCorridorAnnuler, bNavigation;
	
	protected FileDialog saveDia =  new FileDialog(new JFrame(),"ENREGISTREMENT",FileDialog.SAVE);
	protected FileDialog openDia = new FileDialog(new JFrame(), "OUVRIR", FileDialog.LOAD);
	
	int screenHeight,screenWidth;
	
	protected Room room;
	
	protected Corridor corridor;
	
	/** rayon de Vertex */
	protected float r=(float)25/2;
	
	/** Constructeur par default */
	public ModeleurModel(){
		Font font = new Font("Arial", Font.BOLD, 20);
		GridBagConstraints g = new GridBagConstraints();
		
		// Options pour les modes
		optsMode = new JPanel(new GridBagLayout());
		optsMode.setBackground(ModeleurModel.DARKGREY2);
		optsMode.setPreferredSize(new Dimension(130*2,410*2));
		
		bRoom= new JButton("CHAMBRE");
		bRoom.setFont(font);
		bRoom.setForeground(ModeleurModel.BLACK);
		bRoom.setBackground(ModeleurModel.DARKGREY4);
		bRoom.setPreferredSize(new Dimension(110*2,70));
		bRoom.setFocusPainted(false);
		
		bCorridor = new JButton("COULOIR");
		bCorridor.setFont(font);
		bCorridor.setForeground(ModeleurModel.BLACK);
		bCorridor.setBackground(ModeleurModel.DARKGREY4);
		bCorridor.setPreferredSize(new Dimension(110*2,70));
		bCorridor.setFocusPainted(false);
		
		g.gridx = 0;
		g.gridy = 1;
		g.insets= new Insets(10,0,10,0);
		optsMode.add(bRoom,g);
		g.gridy = 2;
		optsMode.add(bCorridor, g);
		
		// Options pour la chambre
		optsRoom = new JPanel(new GridBagLayout());
		optsRoom.setBackground(ModeleurModel.DARKGREY2);
		optsRoom.setPreferredSize(new Dimension(130*2,420*2));
		
		bRectangle= new JButton("RECTANGLE");
		bRectangle.setFont(font);
		bRectangle.setForeground(ModeleurModel.BLACK);
		bRectangle.setBackground(ModeleurModel.DARKGREY4);
		bRectangle.setPreferredSize(new Dimension(110*2,70));
		bRectangle.setFocusPainted(false);
		
		bHexagon= new JButton("HEXAGONE");
		bHexagon.setFont(font);
		bHexagon.setForeground(ModeleurModel.BLACK);
		bHexagon.setBackground(ModeleurModel.DARKGREY4);
		bHexagon.setPreferredSize(new Dimension(110*2,70));
		bHexagon.setFocusPainted(false);
		
		bOctogon= new JButton("OCTOGONE");
		bOctogon.setFont(font);
		bOctogon.setForeground(ModeleurModel.BLACK);
		bOctogon.setBackground(ModeleurModel.DARKGREY4);
		bOctogon.setPreferredSize(new Dimension(110*2,70));
		bOctogon.setFocusPainted(false);
		
		bRoomHeight= new JButton("HAUTEUR");
		bRoomHeight.setFont(font);
		bRoomHeight.setForeground(ModeleurModel.BLACK);
		bRoomHeight.setBackground(ModeleurModel.DARKGREY4);
		bRoomHeight.setPreferredSize(new Dimension(110*2,70));
		bRoomHeight.setFocusPainted(false);
		
		bNavigation= new JButton("ZONE NAV.");
		bNavigation.setFont(font);
		bNavigation.setForeground(ModeleurModel.BLACK);
		bNavigation.setBackground(ModeleurModel.DARKGREY4);
		bNavigation.setPreferredSize(new Dimension(110*2,70));
		bNavigation.setFocusPainted(false);
		
		bRoomAnnuler= new JButton("ANNULER");
		bRoomAnnuler.setFont(font);
		bRoomAnnuler.setForeground(ModeleurModel.BLACK);
		bRoomAnnuler.setBackground(ModeleurModel.DARKGREY4);
		bRoomAnnuler.setPreferredSize(new Dimension(110*2,70));
		bRoomAnnuler.setFocusPainted(false);
		
		g.gridx = 0;
		g.gridy = 1;
		g.insets= new Insets(10,0,10,0);
		optsRoom.add(bRectangle,g);
		g.gridy = 2;
		optsRoom.add(bHexagon,g);
		g.gridy = 3;
		optsRoom.add(bOctogon,g);
		g.gridy = 4;
		optsRoom.add(bRoomHeight,g);
		g.gridy = 5;
		optsRoom.add(bNavigation,g);
		g.gridy = 6;
		optsRoom.add(bRoomAnnuler, g);
		
		// Options pour les murs
		optsWall = new JPanel(new GridBagLayout());
		optsWall.setBackground(ModeleurModel.DARKGREY2);
		optsWall.setPreferredSize(new Dimension(130*2,420*2));
		
		bVertex= new JButton("+ POINT");
		bVertex.setFont(font);
		bVertex.setForeground(ModeleurModel.BLACK);
		bVertex.setBackground(ModeleurModel.DARKGREY4);
		bVertex.setPreferredSize(new Dimension(110*2,70));
		bVertex.setFocusPainted(false);
		
		bDoor= new JButton("+ PORTE");
		bDoor.setFont(font);
		bDoor.setForeground(ModeleurModel.BLACK);
		bDoor.setBackground(ModeleurModel.DARKGREY4);
		bDoor.setPreferredSize(new Dimension(110*2,70));
		bDoor.setFocusPainted(false);
		
		bWindow= new JButton("+ FENETRE");
		bWindow.setFont(font);
		bWindow.setForeground(ModeleurModel.BLACK);
		bWindow.setBackground(ModeleurModel.DARKGREY4);
		bWindow.setPreferredSize(new Dimension(110*2,70));
		bWindow.setFocusPainted(false);
		
		g.gridx = 0;
		g.gridy = 1;
		g.insets= new Insets(10,0,10,0);
		optsWall.add(bVertex,g);
		g.gridy = 2;
		optsWall.add(bDoor,g);
		g.gridy = 3;
		optsWall.add(bWindow,g);
		
		// Options pour le vertex
		optsVertex = new JPanel(new GridBagLayout());
		optsVertex.setBackground(ModeleurModel.DARKGREY2);
		optsVertex.setPreferredSize(new Dimension(130*2,420*2));
		
		bDelVertex = new JButton("- POINT");
		bDelVertex.setFont(font);
		bDelVertex.setForeground(ModeleurModel.BLACK);
		bDelVertex.setBackground(ModeleurModel.DARKGREY4);
		bDelVertex.setPreferredSize(new Dimension(110*2,70));
		bDelVertex.setFocusPainted(false);
		
		g.gridx = 0;
		g.gridy = 1;
		g.insets= new Insets(10,0,10,0);
		optsVertex.add(bDelVertex,g);
		
		// Options pour la trace
		optsTraces = new JPanel(new GridBagLayout());
		optsTraces.setBackground(ModeleurModel.DARKGREY2);
		optsTraces.setPreferredSize(new Dimension(130*2,420*2));
		
		bAddVertex= new JButton("+ POINT");
		bAddVertex.setFont(font);
		bAddVertex.setForeground(ModeleurModel.BLACK);
		bAddVertex.setBackground(ModeleurModel.DARKGREY4);
		bAddVertex.setPreferredSize(new Dimension(110*2,70));
		bAddVertex.setFocusPainted(false);
		
		g.gridx = 0;
		g.gridy = 1;
		g.insets= new Insets(10,0,10,0);
		optsTraces.add(bAddVertex,g);
		
		
		// Options pour l'enregistrement
		bSave = new JButton("ENREGISTRER");
		bSave.setFont(font);
		bSave.setForeground(ModeleurModel.BLACK);
		bSave.setBackground(ModeleurModel.DARKGREY4);
		bSave.setPreferredSize(new Dimension(110*2,70));
		bSave.setFocusPainted(false);
		
		bOpen = new JButton("OUVRIR");
		bOpen.setFont(font);
		bOpen.setForeground(ModeleurModel.BLACK);
		bOpen.setBackground(ModeleurModel.DARKGREY4);
		bOpen.setPreferredSize(new Dimension(110*2,70));
		bOpen.setFocusPainted(false);
		
		// Options pour la couloir
		optsCorridor = new JPanel(new GridBagLayout());
		optsCorridor.setBackground(ModeleurModel.DARKGREY2);
		optsCorridor.setPreferredSize(new Dimension(130*2,420*2));
		
		bWidth = new JButton("LARGEUR");
		bWidth.setFont(font);
		bWidth.setForeground(ModeleurModel.BLACK);
		bWidth.setBackground(ModeleurModel.DARKGREY4);
		bWidth.setPreferredSize(new Dimension(110*2,70));
		bWidth.setFocusPainted(false);
		
		bHeight = new JButton("HAUTEUR");
		bHeight.setFont(font);
		bHeight.setForeground(ModeleurModel.BLACK);
		bHeight.setBackground(ModeleurModel.DARKGREY4);
		bHeight.setPreferredSize(new Dimension(110*2,70));
		bHeight.setFocusPainted(false);
		
		bCorridorAnnuler = new JButton("ANNULER");
		bCorridorAnnuler.setFont(font);
		bCorridorAnnuler.setForeground(ModeleurModel.BLACK);
		bCorridorAnnuler.setBackground(ModeleurModel.DARKGREY4);
		bCorridorAnnuler.setPreferredSize(new Dimension(110*2,70));
		bCorridorAnnuler.setFocusPainted(false);
		
		bNextRoom = new JButton("SORTANT");
		bNextRoom.setFont(font);
		bNextRoom.setForeground(ModeleurModel.BLACK);
		bNextRoom.setBackground(ModeleurModel.DARKGREY4);
		bNextRoom.setPreferredSize(new Dimension(110*2,70));
		bNextRoom.setFocusPainted(false);
		
		g.gridx = 0;
		g.gridy = 1;
		g.insets= new Insets(10,0,10,0);
		optsCorridor.add(bWidth, g);
		g.gridy = 2;
		optsCorridor.add(bHeight, g);
		g.gridy = 3;
		optsCorridor.add(bNextRoom, g);
		g.gridy = 4;
		optsCorridor.add(bCorridorAnnuler, g);
		
		
		// save contient bsave
		save = new JPanel();
		save.setBackground(ModeleurModel.DARKGREY2);
		save.setPreferredSize(new Dimension(130*2,160));
		save.add(bOpen, BorderLayout.NORTH);
		save.add(bSave, BorderLayout.CENTER);
		
		// bg contient le toolbar et le graphe
		bg = new JPanel();
		bg.setLayout(new BorderLayout(10,10));
		bg.setBackground(ModeleurModel.GREY);
		bg.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		// toolbar contient menu et save
		toolbar = new JPanel();
		toolbar.setLayout(new BorderLayout(10,10));
		toolbar.setBackground(ModeleurModel.DARKGREY2);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenHeight = (int) screenSize.getHeight();
		screenWidth = screenHeight-screenHeight/10;
		
		graph = new Graph();
		graph.setPreferredSize(new Dimension(screenHeight, screenWidth));
	
		//toolbar.add(menu, BorderLayout.CENTER);
		toolbar.add(optsMode, BorderLayout.CENTER);
		//toolbar.add(save, BorderLayout.SOUTH);
				
		bg.add(graph, BorderLayout.CENTER);
		bg.add(toolbar, BorderLayout.WEST);
		
		room = new Room(4,"ROOM", screenWidth);
		corridor = new Corridor("CORRIDOR", screenWidth);
		
	}
	
	/**
	 * class interne Graph
	 * On dessine une chambres ou un couloirs dans cette class 
	 * */
	class Graph extends JPanel {
		public void paintComponent (Graphics g) {
			super.paintComponent(g);
			this.setBackground(ModeleurModel.DARKGREY3);
			if(mode==1){ // mode pour dessiner le chambre
				room.draw(g);
			} else if (mode==2){
				//s=new Corridor();
				corridor.draw(g);
			}
		}
	}
}
