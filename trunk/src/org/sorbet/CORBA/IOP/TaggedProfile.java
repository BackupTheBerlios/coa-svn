package org.sorbet.CORBA.IOP; 
 
/** 
 * Cette classe permet la gestion des TaggedProfile<P> 
 * Classe issue de l'IDL fourni par l'sorbet.<P> 
 * @author Groupe IOP,IIOP,GIOP 
 * @version 2.5
 */ 
 
public class TaggedProfile 
{ 
	public final static int TAG_INTERNET_IOP		= 0; 
	public final static int TAG_MULTIPLE_COMPONENTS	= 1; 
	public final static int TAG_SCCP_IOP			= 2; 
	public final static int TAG_THREAD_POLICY		= 3; 
 
	int	_tag; 
	byte[]	_profile_data; 
	 
        /** 
         * Retourne le TAG de ce Profile 
         * @return TAG de ce Profile 
         */ 
	public int tag() 
	{ 
		return _tag; 
	} 
 
        /** 
         * Permet de modifier le TAG de ce Profile 
         * @param tag TAG à utiliser 
         */ 
	public void tag( int tag ) 
	{ 
		_tag = tag; 
	} 
	 
        /** 
         * Retourne les données de ce Profile 
         * @return Données de ce Profile 
         */ 
	public byte[] profile_data() 
	{ 
		return _profile_data; 
	} 
 
        /** 
         * Permet de modifier les données de ce Profile 
         * @param profile_data Données à utiliser 
         */ 
	public void profile_data( byte[] profile_data ) 
	{ 
		_profile_data = profile_data; 
	} 
	 
        /** 
         * Constructeur 
         * @param tag TAG à utiliser 
         * @param profile_date Données à utiliser 
         */ 
	public TaggedProfile( int tag, byte[] profile_data ) 
	{ 
		_tag = tag; 
		_profile_data = profile_data; 
	} 
	 
        /* Pour Tests */ 
        public String toString() { 
	        String chaine="Profile : \n"+_tag+"\n"; 
		chaine+=new String(_profile_data); 
		return chaine; 
	} 
}
