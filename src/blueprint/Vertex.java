/**
 * PT3 Poly Editor
 * DUT Informatique 2016/2017 
 * Auteur : HUANG Qijia
 * 			LU Yi
 * Tuteur : P. Even
 * */

package blueprint;

import java.awt.Graphics;

import modeleur.ModeleurModel;

/** class Vertex : cette classe definit un point avec un x et un y. 
 *  Et il consist tous les fonctions d'utilisqtions de vertices */
public class Vertex {
	// ------ attributs ------
	/** x de Vertex */
	private float x;
	/** y de Vertex */
	private float y;
	/** il aparait true si un objet Vertex est selectionne */
	private boolean selected = false;
	
	// ------ constructeur ------
	/** constructeur par default qui prend deux decimales pour les aurguments */
	public Vertex(float x, float y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	// ------ methodes ------
	/** retourne la valeur de X */
	public float getX() {
		return x;
	}
	
	/** retourne la valeur de Y */
	public float getY() {
		return y;
	}
	
	/** retourne la distence entre un point d'entier et ce Vertex */
	public float ptDisVt(int x, int Y){
		return (float) Math.sqrt((this.x-x)*(this.x-x)+(this.y-y)*(this.y-y));
	}
	
	/** retourne la distence entre un point de flaot et ce Vertex */
	public float ptDisVt(float x, float y){
		if(this.x == x){
			return Math.abs(this.y - y);
		}else if (this.y == y){
			return Math.abs(this.x - x);
		}
		return (float) Math.sqrt((this.x-x)*(this.x-x)+(this.y-y)*(this.y-y));
	}
	
	/** retourne la distence entre deux Vertex */
	public float VtDisVt(Vertex v){
		return (float) Math.sqrt((this.x-v.getX())*(this.x-v.getX())+(this.y-v.getY())*(this.y-v.getY()));
	}
	
	/** selectionner ou deselectionner un Vertex */
	public void select(){
		selected=!selected;
	}
	
	/** selectionner un Vertex si les aurgement est dans le rayon de Vertex */
	public void select(int x, int y){
		if(selected) {
			selected=!selected;
			return;
			}
		
		float disX, disY;
		disX=x-this.x;
		disY=y-this.y;
		
		if ( disX<25 && disX>0 && disY<25 && disY>0){
			selected = true;
		}
		else{
			selected = false;
		}
	}
	
	/** deplacer le vertex a le point */
	public void move(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	/** dessiner le Vertex */
	public void draw (Graphics g){
		if (selected) {
			g.setColor(ModeleurModel.WHITE);
			g.fillOval((int)x, (int)y, 25, 25);
			return;
		}
		g.setColor(ModeleurModel.BLACK);
		g.fillOval((int)x, (int)y, 25, 25);
		return;
	}
	
	/** dessiner le Vertex en blanch si il est electionner, sinon en gris */
	public void drawOpen (Graphics g){
		if (selected) {
			g.setColor(ModeleurModel.WHITE);
			g.fillOval((int)x, (int)y, 25, 25);
			return;
		}
		g.setColor(ModeleurModel.GREY);
		g.fillOval((int)x, (int)y, 25, 25);
		return;
	}
	
	/** dessiner le Vertex du porte entrant en blanch s‘il est selectionné, sinon en bleu */
	public void drawDoorEntrant (Graphics g){
		if (selected) {
			g.setColor(ModeleurModel.ENTRANTSELECTED);
			g.fillOval((int)x, (int)y, 25, 25);
			return;
		}
		g.setColor(ModeleurModel.ENTRANT);
		g.fillOval((int)x, (int)y, 25, 25);
		return;
	}
	
	/** dessiner le Vertex du porte sortant en blanch s‘il est selectionné, sinon en rouge */
	public void drawDoorSortant (Graphics g){
		if (selected) {
			g.setColor(ModeleurModel.SORTANTSELECTED);
			g.fillOval((int)x, (int)y, 25, 25);
			return;
		}
		g.setColor(ModeleurModel.SORTANT);
		g.fillOval((int)x, (int)y, 25, 25);
		return;
	}
	
	/** retourner true si le Vertex a ete selectionne, sinon false */
	public boolean isSelected(){
		return selected;
	}
	
	// surcharger les methodes hashCode() et equals()
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (selected ? 1231 : 1237);
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertex other = (Vertex) obj;
		if (selected != other.selected)
			return false;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}
}
