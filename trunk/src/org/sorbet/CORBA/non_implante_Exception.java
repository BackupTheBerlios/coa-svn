 
package org.sorbet.CORBA; 
 
import java.lang.Exception; 
/** 
 *lanc�e quand la fonction demand� n'est pas implant�
*@author Millo Jean-Vivien
*@version 2.5 
 **/ 
public class non_implante_Exception extends SystemException 
{ 
	public non_implante_Exception(String e) 
	{ 
				super( e ); 
	} 
	 
	public non_implante_Exception() 
	{ 
		super( "La fonction ou le service semand� n'est pas implant�" ); 
	} 
}
