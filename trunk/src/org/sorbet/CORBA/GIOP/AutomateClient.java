package org.sorbet.CORBA.GIOP; 
 
/** 
 * Cette classe permet d'utiliser l'automate du Client.<P> 
 * Classe Inutilisée.<P> 
 * @author Groupe IOP,IIOP,GIOP 
 * @version 2.5
 */ 
 
public class AutomateClient extends Automate { 
	private static int[][] matrice_etats_transitions= 
		{ 
			{	-1,	-1,	-1,	-1,	-1,	-1,	-1	}, // Etat Déconnecté - 7 messages GIOP 
			{	2,	-1,	-1,	-1,	-1,	0,	-1	}, // Etat Connecté (IDLE) 
			{	-1,	1,	-1,	-1,	-1,	-1,	-1	}  // Etat Connecté Traitement 
		}; 
			 
	private int etat; 
	 
        /** 
         * Construit l'automate du client. 
         */ 
	public AutomateClient() { 
		super(matrice_etats_transitions); 
		 
	} 
	 
        /** 
         * Construit l'automate du client avec l'état initial donné en argument 
         */ 
	public AutomateClient(int etat_initial) { 
		super(matrice_etats_transitions,etat_initial); 
		 
	}
}
