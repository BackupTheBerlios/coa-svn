package org.sorbet.CORBA.portable;

import org.sorbet.CORBA.*;
/**
 *Cette classe crée un flux de sortie pour répondre à une requête
 *@author Millo Jean-Vivien
 *@version 2.5 
 **/
public class ResponseHandlerImpl implements ResponseHandler
{
	/**
	 *Crée un flux pour acceuillir une exception en guise de réponse
	 **/
	public org.sorbet.CORBA.portable.OutputStream createExceptionReply()
	{
		return new org.sorbet.CORBA.portable.OutputStream(false);
	}
	/**
	 *Crée un flux pour acceuillir une réponse valide
	 **/
	public org.sorbet.CORBA.portable.OutputStream createReply()
	{
		return new org.sorbet.CORBA.portable.OutputStream(false);
	} 
}
