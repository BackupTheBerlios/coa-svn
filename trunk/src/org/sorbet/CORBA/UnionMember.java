package org.sorbet.CORBA; 
 
import org.sorbet.CORBA.portable.*; 
/** 
 *Classe vide
*@author Millo Jean-Vivien
*@version 2.5 
 **/ 
public class UnionMember implements IDLEntity 
{ 
	 Any label;  
	String name; 
	TypeCode type; 
	IDLType type_def;  
  
	UnionMember() {} 
 
	UnionMember(String __name, Any __label, TypeCode __type, IDLType __type_def)  
	{ 
		label=__label; 
		name=__name; 
		type=__type; 
		type_def=__type_def; 
	}
}
