
package org.sorbet.CORBA.IIOP;

import org.sorbet.CORBA.IOP.*;

/**
 * Cette classe permet la gestion des corps des profiles IIOP 1.2.<P>
 * Cette objet se place au sein d'une IOR.<P>
 * Pour information, cet objet ne diffère de ProfileBody_1_1 que par le numéro de la version IIOP.<P>
 * Classe issue de l'IDL fourni par l'sorbet.<P>
 * @author Groupe IOP,IIOP,GIOP
 * @version 2.5
 */

public class ProfileBody_1_2 extends ProfileBody_1_1
{
	// version 1.2 as the same body as 1.1
	
        /**
         * Constructeur
         * @param host Nom de l'hôte
         * @param port Numéro du port
         * @param object_key Identifiant de l'objet concerné
         * @param components Composants à utiliser
         */
	public ProfileBody_1_2( String host, short port, byte[] object_key, TaggedComponent[] components )
	{
		_iiop_version = new Version( (byte)1, (byte)2 );
		_host = host;	
		_port = port;
		_object_key = object_key;
		_components = components;
	}
}
