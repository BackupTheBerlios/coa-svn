
package org.sorbet.CORBA; 
 
import java.lang.Exception; 
/** 
 *Lancée quand le type du message recu est invalide 
 * @author Equipe ORB&POA
 * @version 2.5
 **/ 
public class InvalidMessageTypeException extends SystemException 
{ 
	public InvalidMessageTypeException( String e ) 
	{ 
		super( e ); 
	} 
	 
	public InvalidMessageTypeException() 
	{ 
		super( "Erreur dans le type de message" ); 
	} 
}
