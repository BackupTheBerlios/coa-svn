 
package org.sorbet.CORBA; 
 
import java.lang.Exception; 
/** 
 *lanc�e quand l'ent�te du message est mauvaise
 * @author Equipe ORB&POA
 * @version 2.5
 **/ 
public class NonCORBAMessageException extends SystemException 
{ 
	public NonCORBAMessageException( String e ) 
	{ 
				super( e ); 
	} 
	 
	public NonCORBAMessageException() 
	{ 
		super( "Erreur dans l'identifiant de requete" ); 
	} 
}
