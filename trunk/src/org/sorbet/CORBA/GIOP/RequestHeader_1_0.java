package org.sorbet.CORBA.GIOP; 
 
import org.sorbet.CORBA.IOP.*; 
 
/** 
 * Cette classe permet la gestion des ent�tes GIOP du type "RequestHeader' 
 * d�fini dans la version 1.0 du protocole GIOP<P> 
 * Classe issue de l'IDL fourni par l'sorbet.<P> 
 * @author Groupe IOP,IIOP,GIOP 
 * @version 2.5
 */ 
 
public class RequestHeader_1_0 { 
	ServiceContext[]	_service_context; 
	int			_request_id; 
	boolean			_response_expected; 
	byte[]			_object_key; 
	String			_operation; 
	byte[]			_requesting_principal; 
	 
        /** 
         * Construit l'ent�te 
         * @param service_context Contextes � utiliser 
         * @param request_id Identifiant de la requ�te 
         * @param response_expected Indique si une r�ponse est attendue. Attention, une m�thode "void" entra�ne une r�ponse. 
         * @param object_key Indique l'identifiant de l'objet auquel est destin�e la requ�te 
         * @param operation Indique le nom de la m�thode � invoquer sur l'objet 
         * @param requesting_principal D�pr�ci� utili� une tableau de byte de taille 1 par exemple 
         */ 
	public RequestHeader_1_0(ServiceContext[] service_context, int request_id, boolean response_expected, byte[] object_key, String operation, byte[] requesting_principal) { 
		this._service_context=service_context; 
		this._request_id=request_id; 
		this._response_expected=response_expected; 
		this._object_key=object_key; 
		this._operation=operation; 
		this._requesting_principal=requesting_principal; 
	} 
	 
        /** 
         * Permet de modifier les Contextes 
         * @param service_context Contextes � utiliser 
         */ 
	public void service_context(ServiceContext[] service_context) { 
		this._service_context=service_context; 
	} 
	 
        /** 
         * Retourne les Contextes 
         * @return Contextes utilis�s 
         */ 
	public ServiceContext[] service_context() { 
		return this._service_context; 
	} 
	 
        /** 
         * Permet de modifier l'identifiant de la requ�te 
         * @param request_id Identifiant � donner � la requ�te 
         */ 
	public void request_id(int request_id) { 
		this._request_id=request_id; 
	} 
	 
        /** 
         * Retourne l'identifiant de la requ�te 
         * @return Identifiant de la requ�te 
         */ 
	public int request_id() { 
		return this._request_id; 
	} 
	 
        /**  
         * Permet de pr�ciser si la requ�te attend ou non une r�ponse 
         * @param response_expected TRUE si une r�ponse est attendue, FALSE sinon 
         */ 
	public void response_expected(boolean response_expected) { 
		this._response_expected=response_expected; 
	} 
         
        /** 
         * Indique si la requ�te attend une r�ponse ou non 
         * @return TRUE si la requ�te attend une r�ponse, FALSE sinon 
         */ 
	public boolean response_expected() { 
		return this._response_expected; 
	} 
	 
        /** 
         * Permet de modifier l'identifiant de l'objet concern� par la requ�te 
         * @param object_key Identifiant de l'objet concern� par la requ�te 
         */ 
	public void object_key(byte[]object_key) { 
		this._object_key=object_key; 
	} 
	 
        /** 
         * Retourne l'identifiant de l'objet concern� par la requ�te 
         * @return Identifiant de l'objet concern� par la requ�te 
         */ 
	public byte[] object_key() { 
		return this._object_key; 
	} 
	 
        /** 
         * Permet de modifier le nom de la m�thode concern�e par la requ�te 
         * @param operation Nom de la m�thode concern�e par la requ�te 
         */ 
	public void operation(String operation) { 
		this._operation=operation; 
	} 
	 
        /**  
         * Retourne le nom de la m�thode concern�e par la requ�te 
         * @return Nom de la m�thode concern�e par la requ�te 
         */ 
	public String operation() { 
		return this._operation; 
	} 
	 
        /** 
         * Permet de modifier le "Requesting Principal" 
         * D�pr�ci� 
         */ 
	public void requesting_principal(byte[] requesting_principal) { 
		this._requesting_principal=requesting_principal; 
	} 
	 
        /** 
         * Retourne le "Requesting Principal" 
         * D�pr�ci� 
         */ 
	public byte[] requesting_principal() { 
		return this._requesting_principal; 
	} 
 
        /* Pour Tests */ 
        /** 
         * Affiche les �l�ments concernant la requ�te 
         * @return El�ments de la requ�tes sous forme d'une String 
         */ 
	public String toString() { 
		String desc="Contexte:"+_service_context[0].context_id()+"-"+new String(_service_context[0].context_data())+"\n"; 
		desc=desc+"Request ID : "+_request_id+"\n"; 
		desc=desc+"Response Expected : "+_response_expected+"\n"; 
		desc=desc+"Object Key : "+new String(_object_key)+"\n"; 
		desc=desc+"Operation : "+_operation+"\n"; 
		desc=desc+"Requesting Principal : "+new String(_requesting_principal)+"\n"; 
		return desc; 
	} 
}
