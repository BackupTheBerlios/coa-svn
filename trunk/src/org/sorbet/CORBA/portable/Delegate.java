package org.sorbet.CORBA.portable; 
/** 
 *Cette classe se charge d'ouvrir la connexion à la place de l'ObjectImpl
*@author Millo Jean-Vivien
*@version 2.5 
 **/ 
public class Delegate extends org.jacorb.orb.Delegate
{ 
	private org.sorbet.CORBA.IIOP.Connexion protocol; 
	/** 
	 *Ouvre la connexion avec le serveur 
	 **/ 
	public void connect() 
    	{ 
		protocol.connect(); 
	}	 
	/** 
	 *Constructeur : il ouvre la connexion 
	 *@param protocole Objet represantant la connexion 
	 **/ 
	public Delegate(org.sorbet.CORBA.IIOP.Connexion protocol) 
	{ 
		this.protocol=protocol;
		connect();
	} 
	/** 
	 *@return la connexion ouverte 
	 **/ 
	public org.sorbet.CORBA.IIOP.Connexion getConnexion() 
	{return protocol;} 
	public void run(){}
 
}
