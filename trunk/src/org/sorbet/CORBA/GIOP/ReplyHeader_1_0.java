package org.sorbet.CORBA.GIOP; 
 
import org.sorbet.CORBA.IOP.*; 
 
/** 
 * Cette classe permet la gestion des entêtes GIOP du type "ReplyHeader' 
 * défini dans la version 1.0 du protocole GIOP<P> 
 * Classe issue de l'IDL fourni par l'sorbet.<P> 
 * @author Groupe IOP,IIOP,GIOP 
 * @version 2.5
 */ 
 
public class ReplyHeader_1_0 { 
        /** 
         * Aucune Exception générée 
         */ 
	public final static int NO_EXCEPTION=0; 
        /** 
         * Exception Utilisateur générée 
         */ 
	public final static int USER_EXCEPTION=1; 
        /** 
         * Exception Système générée 
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
         * Construit l'entête 
         * @param service_context Contextes à utiliser 
         * @param request_id Identifiant de la requête 
         * @param reply_status Etat de la réponse 
         */ 
	public ReplyHeader_1_0(ServiceContext[] service_context, int request_id, int reply_status) { 
		this._service_context=service_context; 
		this._request_id=request_id; 
		this._reply_status=reply_status; 
	} 
	 
        /** 
         * Permet de modifier les Contextes 
         * @param service_context Contextes à utiliser 
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
         * Permet de modifier l'identifiant de la requête 
         * @param Identifiant à donner à la requête 
         */ 
	public void request_id(int request_id) { 
		this._request_id=request_id; 
	} 
	 
        /** 
         * Retourne l'identifiant de la requête 
         * @return Identifiant de la requête 
         */ 
	public int request_id() { 
		return this._request_id; 
	} 
	 
        /** 
         * Permet de modifier l'état de la réponse 
         * @param reply_status Etat à donner à la réponse 
         */ 
	public void reply_status(int reply_status) { 
		this._reply_status=reply_status; 
	} 
	 
        /** 
         * Retourne l'état de la réponse 
         * @return Etat de la réponse 
         */ 
	public int reply_status() { 
		return this._reply_status; 
	} 
} 
