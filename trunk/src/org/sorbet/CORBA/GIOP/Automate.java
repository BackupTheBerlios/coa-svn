package org.sorbet.CORBA.GIOP; 
 
/** 
 * Cette classe permet la gestion des automates des protocoles pour le client 
 * et pour le serveur.<P> 
 * @author Groupe IOP,IIOP,GIOP 
 * @version 2.5
 */ 
 
public abstract class Automate{ 
	private int[][] matrice_etats_transitions; 
	private int etat; 
	 
        /** 
         * Construit l'automate avec la matrice d'�tats/transitions donn�e 
         * en argument avec 0 comme �tat initial. 
         * @param matrice Matrice �tats/transitions � utiliser 
         */ 
	public Automate(int[][] matrice) { 
		matrice_etats_transitions=matrice; 
		etat=0; 
	} 
	 
        /** 
         * Construit l'automate avec la matrice d'�tats/transitions donn�e 
         * en argument avec pour �tat initial la valeur donn�e en argument. 
         * @param matrice Matrice �tats/transitions � utiliser 
         * @param etat_initial Etat initial de l'automate 
         */ 
	public Automate(int[][] matrice,int etat_initial) { 
		matrice_etats_transitions=matrice; 
		etat=etat_initial; 
	} 
	 
        /** 
         * Effectue une transition � partir de l'�tat en cours 
         * @param transition Transition � effectuer 
         * @return Nouvel �tat 
         */ 
	public int transition(int transition) { 
		return (etat=matrice_etats_transitions[etat][transition]); 
	} 
	 
        /** 
         * Retourne l'�tat en cours 
         * @return Etat en cours 
         */ 
	public int getEtat() { 
		return etat; 
	} 
	public void setEtat(int E) { 
		etat=E; 
	} 
}
