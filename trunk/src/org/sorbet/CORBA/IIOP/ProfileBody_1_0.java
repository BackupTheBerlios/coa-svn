
package org.sorbet.CORBA.IIOP;

/**
 * Cette classe permet la gestion des corps des profiles IIOP 1.0.<P>
 * Cette objet se place au sein d'une IOR.<P>
 * Classe issue de l'IDL fourni par l'sorbet.<P>
 * @author Groupe IOP,IIOP,GIOP
 * @version 2.5
 */

public class ProfileBody_1_0
{
	Version	_iiop_version;
	String	_host;
	int	_port;
	byte[]	_object_key;

        /**
         * Retourne la version IIOP.<P>
         * @return Version IIOP
         */
	public Version iiop_version()
	{
		return _iiop_version;
	}

        /**
         * Retourne le nom de l'hôte.
         * @return Nom de l'hôte
         */
	public String host()
	{
		return _host;
	}
	
        /**
         * Permet de modifier le nom de l'hôte
         * @param host Nom de l'hôte à utiliser
         */
	public void host( String host )
	{
		_host = host;	
	}

        /**
         * Retourne le numéro du port
         * @return Port
         */
	public int port()
	{
		return _port;
	}

        /**
         * Permet de modifier le numéro du port
         * @param port Port à utiliser
         */
	public void port( int port )
	{
		_port = port;
	}
	
        /**
         * Retourne l'Identifiant de l'objet concerné
         * @return Identifiant de l'objet concerné
         */
	public byte[] object_key()
	{
		return _object_key;
	}
	
        /**
         * Permet de modifier l'Identifiant de l'objet concerné
         * @param object_key Identifiant de l'objet concerné
         */
	public void object_key( byte[] object_key )
	{
		_object_key = object_key;
	}
	
        /**
         * Constructeur
         * @param host Nom de l'hôte
         * @param port Numéro du port
         * @param object_key Identifiant de l'objet concerné
         */
	public ProfileBody_1_0( String host, int port, byte[] object_key )
	{
		_iiop_version = new Version( (byte)1, (byte)0 );
		_host = host;	
		_port = port;
		_object_key = object_key;
	}
	
        /**
         * Constructeur vide non utilisé
         */
	protected ProfileBody_1_0()
	{
	}
	
        /* Pour Tests */
	public String toString() {

		String chaine;
		
		chaine="Version : "+_iiop_version.major()+"."+_iiop_version.minor()+"\n";
		chaine+="Host : "+_host+":"+_port+"\n";
		chaine+="Object Key : "+new String(_object_key);
		return chaine;
	}
	
}
