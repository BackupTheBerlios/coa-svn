package org.sorbet.CORBA.GIOP; 
 
/** 
 * Cette classe permet la gestion du champ version de l'ent�te des messages GIOP<P> 
 * Classe issue de l'IDL fourni par l'sorbet.<P> 
 * @author Groupe IOP,IIOP,GIOP 
 * @version 2.5
 */ 
 
public class Version { 
	byte _major; 
	byte _minor; 
	 
        /** 
         * Constructeur 
         * @param major Num�ro de version majeur 
         * @param minor Num�ro de version mineur 
         */ 
	public Version(byte major,byte minor) { 
		this._major=major; 
		this._minor=minor; 
	} 
	 
        /** 
         * Permet de modifier le num�ro de version majeur 
         * @param major Num�ro de version majeur 
         */ 
	public void major(byte major) { 
		this._major=major; 
	} 
         
        /** 
         * Retourne le num�ro de version majeur 
         * @return Num�ro de version majeur 
         */ 
	public byte major() { 
		return _major; 
	} 
	 
        /** 
         * Permet de modifier le num�ro de version mineur 
         * @param minor Num�ro de version mineur 
         */ 
	public void minor(byte minor) { 
		this._minor=minor; 
	} 
	 
        /** 
         * Retourne le num�ro de version mineur 
         * @return Num�ro de version mineur 
         */ 
	public byte minor() { 
		return _minor; 
	} 
}
