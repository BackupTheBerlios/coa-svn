package org.sorbet.CORBA.IOP; 
 
import java.lang.Exception; 
/* 
 *Cette exception se déclanche quand l'IOR string est invalide
 * @author Groupe IOP,IIOP,GIOP 
 * @version 2.5  
 **/ 
public class InvalidIORStringException extends Exception 
{ 
	InvalidIORStringException( String e ) 
	{ 
		super( e ); 
	} 
	 
	InvalidIORStringException() 
	{ 
		super( "Erreur dans l'IOR String" ); 
	}
}
