package org.sorbet.CORBA.GIOP; 
 
/** 
 * Cette classe permet la gestion des entêtes GIOP du type "LocateReplyHeader' 
 * défini dans la version 1.0 du protocole GIOP<P> 
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
         * Construit l'entête 
         * @param request_id Identifiant de la requête de localisation 
         * @param locate_status Etat de la réponse parmi les trois états possibles 
         * qui sont UNKNOWN_OBJECT, OBJECT_HERE, OBJECT_FORWARD 
         */ 
	public LocateReplyHeader_1_0(int request_id, int locate_status) { 
		this._request_id=request_id; 
		this._locate_status=locate_status; 
	} 
	 
        /** 
         * Permet de modifier l'identifiant de la reqûete de localisation concernée 
         * @param request_id Identifiant à donner à la requête de localisation 
         */ 
	public void request_id(int request_id) { 
		this._request_id=request_id; 
	} 
	 
        /** 
         * Retourne l'identifiant de la requête de localisation concernée 
         * @return Identifiant de la requête 
         */ 
	public int request_id() { 
		return this._request_id; 
	} 
	 
        /** 
         * Permet de modifier l'état de la localisation 
         * @param locate_status Etat à donner à la localisation 
         */ 
	public void locate_status(int locate_status) { 
		this._locate_status=locate_status; 
	} 
	 
        /** 
         * Retourne l'état de la localisation 
         * @return Etat de la localisation 
         */ 
	public int locate_status() { 
		return this._locate_status; 
	} 
}
