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
         * Construit l'automate avec la matrice d'états/transitions donnée 
         * en argument avec 0 comme état initial. 
         * @param matrice Matrice états/transitions à utiliser 
         */ 
	public Automate(int[][] matrice) { 
		matrice_etats_transitions=matrice; 
		etat=0; 
	} 
	 
        /** 
         * Construit l'automate avec la matrice d'états/transitions donnée 
         * en argument avec pour état initial la valeur donnée en argument. 
         * @param matrice Matrice états/transitions à utiliser 
         * @param etat_initial Etat initial de l'automate 
         */ 
	public Automate(int[][] matrice,int etat_initial) { 
		matrice_etats_transitions=matrice; 
		etat=etat_initial; 
	} 
	 
        /** 
         * Effectue une transition à partir de l'état en cours 
         * @param transition Transition à effectuer 
         * @return Nouvel état 
         */ 
	public int transition(int transition) { 
		return (etat=matrice_etats_transitions[etat][transition]); 
	} 
	 
        /** 
         * Retourne l'état en cours 
         * @return Etat en cours 
         */ 
	public int getEtat() { 
		return etat; 
	} 
	public void setEtat(int E) { 
		etat=E; 
	} 
}
