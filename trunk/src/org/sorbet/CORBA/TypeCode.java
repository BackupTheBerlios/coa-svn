package org.sorbet.CORBA; 
/** 
*Ne pas utiliser cette class, elle existe pour la compatibilité de sorbet 
*avec les compilateurs IDL. Le Type Any n'est pas supporté.  
*@author Millo Jean-Vivien
*@version 2.5
*/

public abstract class TypeCode 
{ 
	TCKind kind; 
	String id; 
	String name; 
	int nb_elem; 
	StructMember[] liste_elem; 
	ValueMember[] liste_value; 
	UnionMember[] liste_union; 
	 
	abstract  TypeCode concrete_base_type();  
          //Returns the TypeCode object that describes the concrete base type of the value type that this TypeCode object describes.  
	abstract  TypeCode content_type();  
          //Returns the TypeCode object representing the IDL type for the members of the object described by this TypeCode object.  
	abstract  int default_index();  
          //Returns the index of the default member, or -1 if there is no default member.  
	abstract  TypeCode discriminator_type();  
          //Returns a TypeCode object describing all non-default member labels.  
	abstract  boolean equal(TypeCode tc);  
          //Compares this TypeCode object with the given one, testing for equality.  
	abstract  boolean equivalent(TypeCode tc);  
          //Tests to see if the given TypeCode object is equivalent to this TypeCode object.  
	abstract  short fixed_digits();  
          //Returns the number of digits in the fixed type described by this TypeCode object.  
	abstract  short fixed_scale();  
          //Returns the scale of the fixed type described by this TypeCode object.  
	abstract  TypeCode get_compact_typecode();  
          //Strips out all optional name and member name fields, but leaves all alias typecodes intact.  
	abstract  String id();  
          //Retrieves the RepositoryId globally identifying the type of this TypeCode object.  
	abstract  TCKind kind();  
          //Retrieves the kind of this TypeCode object.  
	abstract  int length();  
          //Returns the number of elements in the type described by this TypeCode object.  
	abstract  int member_count();  
          //Retrieves the number of members in the type described by this TypeCode object.  
	abstract  Any member_label(int index);  
          //Retrieves the label of the union member identified by the given index.  
	abstract  String member_name(int index);  
          //Retrieves the simple name of the member identified by the given index.  
	abstract  TypeCode member_type(int index);  
          //Retrieves the TypeCode object describing the type of the member identified by the given index.  
	abstract  short member_visibility(int index);  
          //Returns the constant that indicates the visibility of the member at the given index.  
	abstract  String name();  
          //Retrieves the simple name identifying this TypeCode object within its enclosing scope.  
//	abstract  short type_modifier();  
          //Returns a constant indicating the modifier of the value type that this TypeCode object describes  
}
