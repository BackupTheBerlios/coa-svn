
package org.sorbet.CORBA.IIOP;

import org.sorbet.CORBA.IOP.*;

/**
 * Cette classe permet la gestion des corps des profiles IIOP 1.1.<P>
 * Cette objet se place au sein d'une IOR.<P>
 * Classe issue de l'IDL fourni par l'sorbet.<P>
 * @author Groupe IOP,IIOP,GIOP
 * @version 2.5
 */

public class ProfileBody_1_1 extends ProfileBody_1_0
{
	TaggedComponent[] _components;
	
        /**
         * Constructeur
         * @param host Nom de l'hôte
         * @param port Numéro du port
         * @param object_key Identifiant de l'objet concerné
         * @param components Composants à utiliser
         */
	public ProfileBody_1_1( String host, short port, byte[] object_key, TaggedComponent[] components )
	{
		_iiop_version = new Version( (byte)1, (byte)1 );
		_host = host;	
		_port = port;
		_object_key = object_key;
		_components = components;
	}
	
        /**
         * Constructeur vide non utilisé
         */
	protected ProfileBody_1_1()
	{
		System.out.println("ProfileBody_1_1(protected) de la classe org.sorbet.CORBA.IIOP.ProfileBody_1_1");
	}	
}
