package org.sorbet.CORBA.GIOP; 
 
/** 
 * Cette classe permet la gestion des ent�tes GIOP du type "LocateRequestHeader' 
 * d�fini dans la version 1.0 du protocole GIOP.<P> 
 * Classe issue de l'IDL fourni par l'sorbet.<P> 
 * @author Groupe IOP,IIOP,GIOP 
 * @version 2.5
 */ 
 
public class LocateRequestHeader_1_0 { 
	int	_request_id; 
	byte[]	_object_key; 
	 
        /** 
         * Construit l'ent�te 
         * @param request_id Identifiant de la requ�te de localisation 
         * @param object_key Identifiant de l'objet � localiser 
         */ 
	public LocateRequestHeader_1_0(int request_id, byte[] object_key) { 
		this._request_id=request_id; 
		this._object_key=object_key; 
	} 
	 
        /** 
         * Permet de modifier l'identifiant de la requ�te de localisation 
         * @param request_id Identifiant � donner � la requ�te de localisation 
         */ 
	public void request_id(int request_id) { 
		this._request_id=request_id; 
	} 
	 
        /** 
         * Retourne l'identifiant de la requ�te de localisation 
         * @return Identifiant de la requ�te de localisation 
         */ 
	public int request_id(){ 
		return this._request_id; 
	} 
	 
        /** 
         * Permet de modifier l'identifiant de l'objet sur lequel porte la requ�te 
         * de localisation 
         * @param object_key Identifiant de l'objet � localiser 
         */ 
	public void object_key(byte[] object_key) { 
		this._object_key=object_key; 
	} 
	 
        /** 
         * Retourne l'identifiant de l'objet concern� par la requ�te de localisation 
         * @return Identifiant de l'objet concern� par la requ�te de localisation 
         */ 
	public byte[] object_key(){ 
		return this._object_key; 
	}
}	
