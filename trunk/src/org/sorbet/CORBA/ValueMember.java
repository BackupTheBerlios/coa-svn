package org.sorbet.CORBA; 
 
import org.sorbet.CORBA.portable.*; 
/** 
 *Classe vide
*@author Millo Jean-Vivien
*@version 2.5 
 **/ 
public class ValueMember implements IDLEntity 
{ 
	short access; 
	String defined_in; 
	String id; 
	String name; 
	TypeCode type; 
	IDLType type_def; 
	String version; 
 
	public ValueMember(){} 
 
	public ValueMember(String __name, String __id, String __defined_in, String __version, TypeCode __type, IDLType __type_def, short __access) 
	{ 
		access=__access; 
		defined_in=__defined_in; 
		id=__id; 
		name=__name; 
		type=__type; 
		type_def=__type_def; 
		version=__version; 
	} 
}
