 
package org.sorbet.CORBA; 
 
import java.lang.Exception; 
/** 
 *lanc�e quand la version du protocole utilis� n'est pas support�
 * @author Equipe ORB&POA
 * @version 2.5
 **/ 
public class InvalidVersionNumberException extends SystemException 
{ 
	public InvalidVersionNumberException( String e ) 
	{ 
 
		super( e ); 
	} 
	 
	public InvalidVersionNumberException() 
	{ 
 
		super( "Erreur dans l'identifiant de requete" ); 
	} 
}
