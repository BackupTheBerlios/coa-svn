package org.sorbet.CORBA; 
 
import org.sorbet.CORBA.portable.*; 
/** 
 *Classe vide 
 * @author  Millo Jean-Vivien
 * @version	2.5
 **/ 
public class StructMember implements IDLEntity 
{ 
	String name; 
	org.sorbet.CORBA.TypeCode type; 
	org.sorbet.CORBA.IDLType type_def; 
	 
	public StructMember(){} 
	public StructMember(String __name, org.sorbet.CORBA.TypeCode __type, org.sorbet.CORBA.IDLType __type_def) 
	{ 
		name=__name; 
		type=__type; 
		type_def=__type_def; 
	}
}
