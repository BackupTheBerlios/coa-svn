package org.sorbet.CORBA.GIOP; 
 
/** 
 * Cette classe permet la gestion des ent�tes GIOP du type "LocateReplyHeader' 
 * d�fini dans la version 1.0 du protocole GIOP<P> 
 * Classe issue de l'IDL fourni par l'sorbet.<P> 
 * @author Groupe IOP,IIOP,GIOP 
 * @version 2.5 
 */ 
 
public class LocateReplyHeader_1_0 { 
        /** 
         * Objet inconnu pour le serveur 
         */ 
	public final static int UNKNOWN_OBJECT=0; 
        /** 
         * Objet sur le serveur 
         */ 
	public final static int OBJECT_HERE=1; 
        /** 
         * Objet sur un autre serveur 
         */ 
	public final static int OBJECT_FORWARD=2; 
	 
	int	_request_id; 
	int	_locate_status; 
	 
        /** 
         * Construit l'ent�te 
         * @param request_id Identifiant de la requ�te de localisation 
         * @param locate_status Etat de la r�ponse parmi les trois �tats possibles 
         * qui sont UNKNOWN_OBJECT, OBJECT_HERE, OBJECT_FORWARD 
         */ 
	public LocateReplyHeader_1_0(int request_id, int locate_status) { 
		this._request_id=request_id; 
		this._locate_status=locate_status; 
	} 
	 
        /** 
         * Permet de modifier l'identifiant de la req�ete de localisation concern�e 
         * @param request_id Identifiant � donner � la requ�te de localisation 
         */ 
	public void request_id(int request_id) { 
		this._request_id=request_id; 
	} 
	 
        /** 
         * Retourne l'identifiant de la requ�te de localisation concern�e 
         * @return Identifiant de la requ�te 
         */ 
	public int request_id() { 
		return this._request_id; 
	} 
	 
        /** 
         * Permet de modifier l'�tat de la localisation 
         * @param locate_status Etat � donner � la localisation 
         */ 
	public void locate_status(int locate_status) { 
		this._locate_status=locate_status; 
	} 
	 
        /** 
         * Retourne l'�tat de la localisation 
         * @return Etat de la localisation 
         */ 
	public int locate_status() { 
		return this._locate_status; 
	} 
}
