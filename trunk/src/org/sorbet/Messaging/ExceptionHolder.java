package org.sorbet.Messaging;

/**
 *lancée quand l'entête du message est mauvaise
 *@author Millo Jean-Vivien
 *@version 2.5
 **/
public class ExceptionHolder extends org.sorbet.CORBA.SystemException
{
	public ExceptionHolder( String e )
	{
				super( e );
	}
	
	public ExceptionHolder()
	{
		super( "Exception Holder" );
	}
}
