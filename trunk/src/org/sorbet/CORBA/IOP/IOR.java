package org.sorbet.CORBA.IOP; 
 
import org.sorbet.CORBA.*; 
 
/** 
 * Cette classe permet la gestion des IOR<P> 
 * Classe issue de l'IDL fourni par l'sorbet.<P> 
 * @author Groupe IOP,IIOP,GIOP 
 * @version 2.5 
 */ 
 
public class IOR 
{ 
	private static String codes="0123456789ABCDEF"; 
	String		_type_id; 
	TaggedProfile[]	_profiles; 
 
        /** 
         * Retourne le Type_ID 
         * @return Type ID  
         */ 
	public String type_id() 
	{ 
		return _type_id; 
	} 
 
        /** 
         * Permet de modifier le Type_ID 
         * @param type_id Type_ID à utiliser 
         */ 
	public void type_id( String type_id ) 
	{ 
		_type_id = type_id; 
	} 
 
        /** 
         * Retourne les Profiles contenus dans l'IOR 
         * @return Profiles contenus dans l'IOR 
         */ 
	public TaggedProfile[] profiles() 
	{ 
		return _profiles; 
	} 
 
        /** 
         * Permet de modifier les Profiles contenus dans l'IOR 
         * @param profiles Profiles à mettre dans l'IOR 
         */ 
	public void profiles( TaggedProfile[] profiles ) 
	{ 
		_profiles = profiles; 
	} 
 
        /** 
         * Constructeur par défaut.<P> 
         * Le Type_ID est une chaîne vide.<P> 
         * Une seul Profile est créé mais il est vide 
         */ 
	public IOR( ) 
	{ 
		_type_id = new String("");; 
		_profiles = new TaggedProfile[1]; 
	} 
	    /** 
         * Constructeur par défaut.<P> 
         * Le Type_ID est une chaîne vide.<P> 
         * @param nbProfile Profile sont créé mais ils sont vides 
         */ 
	public IOR(int nbProfile) 
	{ 
		_type_id = new String("");; 
		_profiles = new TaggedProfile[nbProfile]; 
	} 
	 
	public void set_second_Profile(int tag, byte[] profile_data) 
	{ 
		_profiles[1]=new TaggedProfile(tag,profile_data); 
	} 
        /** 
         * Constructeur à partir d'une chaîne de caractères hexadécimale.<P> 
         * Ce constructeur permet de créer un objet IOR à partir d'une IOR fournie 
         * sous forme d'une chaîne de caractères hexadécimale 
         * @param str_ior IOR sous forme d'une chaîne de caractères hexadécimale 
         */ 
	public IOR(String str_ior)  
	{ 
		if (str_ior.length()%2==0)  
		{ 
			if ( (str_ior.charAt(0) == 'I')&&(str_ior.charAt(1) == 'O')&&(str_ior.charAt(2) == 'R')&&(str_ior.charAt(3) == ':') ) { 
				byte[] tab_ior=new byte[(str_ior.length()-4)/2]; 
				int temp; 
				int test; 
				str_ior=str_ior.toUpperCase(); 
				for (int i=0;i<(str_ior.length()-4)/2;i++) { 
					test=codes.indexOf(str_ior.charAt(4+2*i)); 
					if (test==-1) { 
						// Ici on throw invalid IOR 
						return; 
					} 
					temp=16*test; 
					test=codes.indexOf(str_ior.charAt(5+2*i)); 
					if (test==-1) { 
						// Ici on throw invalid IOR 
						return; 
					} 
					tab_ior[i]=(byte)((short)((short)temp+(short)test)); 
				} 
				org.sorbet.CORBA.portable.InputStream flux = new org.sorbet.CORBA.portable.InputStream(tab_ior,false); 
				flux.set_byte_order(flux.read_boolean()); 
				_type_id=flux.read_string(); 
				_profiles=flux.read_tagged_profile(); 
				return;						 
			}		 
			 
		} 
		//Ici on throw InvalidIORException 
	} 
 
        /** 
         * Constructeur "personnalisé".<P> 
         * L'IOR est construite en utilisant les valeurs passées en argument.<P> 
         * @param type_id Type_ID à utiliser 
         * @param profiles Profiles à utiliser 
         */ 
	public IOR( String type_id, TaggedProfile[] profiles ) 
	{ 
		_type_id = type_id; 
		_profiles = profiles; 
	} 
	 
	public String toString() { 
		String chaine="IOR : \n"; 
		chaine+=_type_id+"\n"; 
		chaine+=_profiles[0].toString(); 
		return chaine; 
	} 
}
