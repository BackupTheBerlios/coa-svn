package org.sorbet.CORBA.IIOP;

/**
 * Cette classe permet la gestion du champ version de l'entête des messages IIOP<P>
 * Classe issue de l'IDL fourni par l'sorbet.<P>
 * @author Groupe IOP,IIOP,GIOP
 * @version 2.5
 */
public class Version
{
	byte	_major;
	byte	_minor;

        /**
         * Retourne le numéro de version majeur
         * @return Numéro de version majeur
         */
	public byte major()
	{
		return _major;
	}
	
        /**
         * Permet de modifier le numéro de version majeur
         * @param major Numéro de version majeur
         */
	public void major( byte major )
	{
		_major = major;
	}
	
         /**
         * Retourne le numéro de version mineur
         * @return Numéro de version mineur
         */
	public byte minor()
	{
		return _minor;
	}
	
        /**
         * Permet de modifier le numéro de version mineur
         * @param minor Numéro de version mineur
         */
	public void minor( byte minor )
	{
		_minor = minor;
	}
	
        /**
         * Constructeur
         * @param major Numéro de version majeur
         * @param minor Numéro de version mineur
         */
	public Version( byte major, byte minor )
	{
		_major = major;
		_minor = minor;
	}	

}
