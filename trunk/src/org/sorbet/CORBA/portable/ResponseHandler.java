package org.sorbet.CORBA.portable;

import org.sorbet.CORBA.*;
/**
 *Cette classe crée un flux de sortie pour répondre à une requête
 *@author Millo Jean-Vivien
 *@version 2.5 
 **/
public interface ResponseHandler
{
	public org.sorbet.CORBA.portable.OutputStream createExceptionReply() ;
	public org.sorbet.CORBA.portable.OutputStream createReply();
}
