package org.sorbet.CORBA.GIOP; 
 
/** 
 * Cette classe permet la gestion des entêtes GIOP du type "CancelRequestHeader'.<P> 
 * Classe issue de l'IDL fourni par l'sorbet.<P> 
 * @author Groupe IOP,IIOP,GIOP 
 * @version 2.5
 */ 
 
public class CancelRequestHeader { 
	int	_request_id; 
	 
        /** 
         * Construit l'entête 
         * @param request_id Identifiant de la requête à annuler 
         */ 
	public CancelRequestHeader(int request_id) { 
		this._request_id=request_id; 
	} 
	 
        /** 
         * Permet de modifier l'identifiant de la requête à annuler 
         * @param request_id Identifiant de la requête à annuler 
         */ 
	public void request_id(int request_id) { 
		this._request_id=request_id; 
	} 
	 
        /** 
         * Retourne l'identifiant de la requête à annuler 
         * @return Identifiant de la requête à annuler 
         */ 
	public int request_id() { 
		return this._request_id; 
	} 
}
