package org.sorbet.CORBA; 
/** 
*Ne pas utiliser cette class, elle existe pour la compatibilité de sorbet 
*avec les compilateurs IDL. Le Type Any n'est pas supporté. 
*@author Millo Jean-Vivien
*@version 2.5
*/ 
public class TypeCodeImpl extends TypeCode 
{	 
	TypeCode content_type(){return null ;} 
	int default_index(){return nb_elem ;} 
	TypeCode discriminator_type(){return null ;} 
	TypeCode get_compact_typecode(){return null ;} 
	int member_count(){return nb_elem ;} 
	String member_name(int index){return null ;} 
	TypeCode member_type(int index){return null ;} 
	Any member_label(int index){throw new non_implante_Exception( "La fonction ou le service semandé n'est pas implanté" );} 
	 
	 
	 
	boolean equal(TypeCode tc){return kind==tc.kind()&&id==tc.id()&&name==tc.name();} 
	boolean equivalent(TypeCode tc){return kind==tc.kind();} 
	TCKind kind(){return kind;} 
	String id(){return id;} 
	String name(){return name;} 
	//int length(){return liste_elem!=null&&liste_elem.length||liste_union!=null&&liste_union.length||liste_value!=null&&liste_value.length;}  
	int length(){return 0;}
	// nom implementé 
	short type_modifier(){return 0;} 
	TypeCode concrete_base_type(){return null ;} 
	short member_visibility(int index){return 0 ;} 
	short fixed_digits(){return 0 ;} 
	short fixed_scale(){return 0 ;}  
}
