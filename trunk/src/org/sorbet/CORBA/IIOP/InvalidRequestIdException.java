
package org.sorbet.CORBA.IIOP;

import java.lang.Exception;

/**
 * @author Millo Jean-Vivien
 * @version 2.5
 */
public class InvalidRequestIdException extends org.sorbet.CORBA.SystemException
{
	InvalidRequestIdException( String e )
	{
		super( e );
	}
	
	InvalidRequestIdException()
	{
		super( "Erreur dans l'identifiant de requete" );
	}
}
