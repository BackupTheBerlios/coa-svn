package org.sorbet.CORBA.GIOP; 
 
/** 
 * Cette classe permet la gestion des entêtes GIOP du type "LocateRequestHeader' 
 * défini dans la version 1.0 du protocole GIOP.<P> 
 * Classe issue de l'IDL fourni par l'sorbet.<P> 
 * @author Groupe IOP,IIOP,GIOP 
 * @version 2.5
 */ 
 
public class LocateRequestHeader_1_0 { 
	int	_request_id; 
	byte[]	_object_key; 
	 
        /** 
         * Construit l'entête 
         * @param request_id Identifiant de la requête de localisation 
         * @param object_key Identifiant de l'objet à localiser 
         */ 
	public LocateRequestHeader_1_0(int request_id, byte[] object_key) { 
		this._request_id=request_id; 
		this._object_key=object_key; 
	} 
	 
        /** 
         * Permet de modifier l'identifiant de la requête de localisation 
         * @param request_id Identifiant à donner à la requête de localisation 
         */ 
	public void request_id(int request_id) { 
		this._request_id=request_id; 
	} 
	 
        /** 
         * Retourne l'identifiant de la requête de localisation 
         * @return Identifiant de la requête de localisation 
         */ 
	public int request_id(){ 
		return this._request_id; 
	} 
	 
        /** 
         * Permet de modifier l'identifiant de l'objet sur lequel porte la requête 
         * de localisation 
         * @param object_key Identifiant de l'objet à localiser 
         */ 
	public void object_key(byte[] object_key) { 
		this._object_key=object_key; 
	} 
	 
        /** 
         * Retourne l'identifiant de l'objet concerné par la requête de localisation 
         * @return Identifiant de l'objet concerné par la requête de localisation 
         */ 
	public byte[] object_key(){ 
		return this._object_key; 
	}
}	
