package org.sorbet.CORBA.portable;

import org.sorbet.CORBA.*;
/**
 *Cette classe cr�e un flux de sortie pour r�pondre � une requ�te
 *@author Millo Jean-Vivien
 *@version 2.5 
 **/
public class ResponseHandlerImpl implements ResponseHandler
{
	/**
	 *Cr�e un flux pour acceuillir une exception en guise de r�ponse
	 **/
	public org.sorbet.CORBA.portable.OutputStream createExceptionReply()
	{
		return new org.sorbet.CORBA.portable.OutputStream(false);
	}
	/**
	 *Cr�e un flux pour acceuillir une r�ponse valide
	 **/
	public org.sorbet.CORBA.portable.OutputStream createReply()
	{
		return new org.sorbet.CORBA.portable.OutputStream(false);
	} 
}
