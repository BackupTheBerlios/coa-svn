package org.sorbet.CORBA;

import java.lang.Exception;
/**
 *lancée quand l'entête du message est mauvaise
*@author Millo Jean-Vivien
*@version 2.5
 **/
public class UNKNOWN extends SystemException
{
	public UNKNOWN( String e )
	{
				super( e );
	}
	
	public UNKNOWN()
	{
		super( "Local invocation not supported" );
	}
}
