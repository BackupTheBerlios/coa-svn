package org.sorbet.CORBA.GIOP; 
 
import org.sorbet.CORBA.IOP.*; 
 
/** 
 * Cette classe permet la gestion des ent�tes GIOP du type "ReplyHeader' 
 * d�fini dans la version 1.0 du protocole GIOP<P> 
 * Classe issue de l'IDL fourni par l'sorbet.<P> 
 * @author Groupe IOP,IIOP,GIOP 
 * @version 2.5
 */ 
 
public class ReplyHeader_1_0 { 
        /** 
         * Aucune Exception g�n�r�e 
         */ 
	public final static int NO_EXCEPTION=0; 
        /** 
         * Exception Utilisateur g�n�r�e 
         */ 
	public final static int USER_EXCEPTION=1; 
        /** 
         * Exception Syst�me g�n�r�e 
         */ 
        public final static int SYSTEM_EXCEPTION=2; 
        /** 
         * L'objet de trouve sur un autre serveur 
         */ 
	public final static int LOCATION_FORWARD=3; 
	 
//	public final static int cost_EXCEPETION=4; 
 
//	public final static int Deadline_EXCEPTION=5; 
 
//	public final static int Laxity_EXCEPTION=6; 
 
	ServiceContext[]	_service_context; 
	int			_request_id; 
	int			_reply_status; 
	 
        /** 
         * Construit l'ent�te 
         * @param service_context Contextes � utiliser 
         * @param request_id Identifiant de la requ�te 
         * @param reply_status Etat de la r�ponse 
         */ 
	public ReplyHeader_1_0(ServiceContext[] service_context, int request_id, int reply_status) { 
		this._service_context=service_context; 
		this._request_id=request_id; 
		this._reply_status=reply_status; 
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
         * @return Contextes 
         */ 
	public ServiceContext[] service_context() { 
		return this._service_context; 
	} 
	 
        /** 
         * Permet de modifier l'identifiant de la requ�te 
         * @param Identifiant � donner � la requ�te 
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
         * Permet de modifier l'�tat de la r�ponse 
         * @param reply_status Etat � donner � la r�ponse 
         */ 
	public void reply_status(int reply_status) { 
		this._reply_status=reply_status; 
	} 
	 
        /** 
         * Retourne l'�tat de la r�ponse 
         * @return Etat de la r�ponse 
         */ 
	public int reply_status() { 
		return this._reply_status; 
	} 
} 
