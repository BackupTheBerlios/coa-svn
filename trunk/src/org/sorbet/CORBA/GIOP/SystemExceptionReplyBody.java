package org.sorbet.CORBA.GIOP; 
 
/** 
 * Cette classe permet la gestion du corps d'une réponse lorsque celle-ci a 
 * renvoyé une exception.<P> 
 * Classe issue de l'IDL fourni par l'sorbet.<P> 
 * @author Groupe IOP,IIOP,GIOP 
 * @version 2.5
 */ 
 
public class SystemExceptionReplyBody { 
	String	_exception_id; 
	int	_minor_code_value; 
	int	_completion_status; 
	 
        /** 
         * Construit le corps 
         * @param exception_id Identifiant de l'exception 
         * @param minor_code_value Code mineur de l'exception 
         * @param completion_status Indication de l'avancée de l'exécution avant l'exception 
         */ 
	public SystemExceptionReplyBody(String exception_id, int minor_code_value, int completion_status) { 
		this._exception_id=exception_id; 
		this._minor_code_value=minor_code_value; 
		this._completion_status=completion_status; 
	} 
	 
        /** 
         * Permet de modifier l'identifiant de l'exception 
         * @param exception_id Identifiant de l'exception 
         */ 
	public void exception_id(String exception_id) { 
		this._exception_id=exception_id; 
	} 
	 
        /** 
         * Retourne l'identifiant de l'exception 
         * @return Identifiant de l'exception 
         */ 
	public String exception_id() { 
		return this._exception_id; 
	} 
	 
        /** 
         * Permet de modifier le code mineur de l'exception 
         * @param minor_code_value Code mineur de l'exception 
         */ 
	public void minor_code_value(int minor_code_value) { 
		this._minor_code_value=minor_code_value; 
	} 
	 
        /** 
         * Retourne le code mineur de l'exception 
         * @return Code mineur de l'exception 
         */ 
	public int minor_code_value() { 
		return this._minor_code_value; 
	}	 
	 
        /** 
         * Permet de modifier l'état d'avancement avant l'exception 
         * @param completion_status Etat d'avancement 
         */ 
	public void completion_status(int completion_status) { 
		this._completion_status=completion_status; 
	} 
	 
        /** 
         * Retourne l'état d'avancement avant l'exception 
         * @return Etat d'avancement avant l'exception 
         */ 
	public int completion_status() { 
		return this._completion_status; 
	} 
}
