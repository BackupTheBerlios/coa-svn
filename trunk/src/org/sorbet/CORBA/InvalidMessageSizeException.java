package org.sorbet.CORBA;

import java.lang.Exception;
/**
 *Cette exception est lanc� quand InputStream recoit un message de taille incorrect
 * @author	Equipe Stub&Skeleton 
 * @version	2.5
 **/
public class InvalidMessageSizeException extends SystemException
{
	InvalidMessageSizeException( String e )
	{
		super( e );
	}
	
	InvalidMessageSizeException()
	{
		super( "Erreur dans la taille du message" );
	}
}
