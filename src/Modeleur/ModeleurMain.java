/**
 * PT3 Poly Editor
 * DUT Informatique 2016/2017 
 * Auteur : HUANG Qijia
 * 			LU Yi
 * Tuteur : P. Even
 * */

package Modeleur;

import java.awt.EventQueue;

import javax.swing.JFrame;

/** 
 * class ModeleurMain
 * celui-ci va appeler la methode main et construire la fenetre de modeleur
 * */
public class ModeleurMain {
	/** nom de la fenetre */
	private static String TITLE = "Poly Editor - Modeleur";
	
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				ModeleurView mv = new ModeleurView(TITLE);
				mv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				mv.pack();
				mv.setVisible(true);
			}
		});

	}

}
