 
package org.sorbet.CORBA; 
 
import java.lang.Exception; 
/** 
 *lancée quand la version du protocole utilisé n'est pas supporté
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
