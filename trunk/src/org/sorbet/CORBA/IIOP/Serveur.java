package org.sorbet.CORBA.IIOP;

import java.net.*;
import java.io.*;
import org.sorbet.CORBA.IIOP.threading.*;
import org.sorbet.CORBA.*;
/**
 * Classe de Gestion des connexions au serveur.<P>
 * Cette classe se met en attente de connexions
 * @author Groupe IOP,IIOP,GIOP
 * @version 2.5
 */


public class Serveur
{
/**
 * Le numero du port d'écoute du serveur.
 */
	private int port; // Devrait etre un short d'apres l'idl

/**
 * Le nombre maximum de connexion acceptées.
 */
	private int maxClient;

/**
* Defini la politique de Thread utilisé
*/
    private byte ThreadPolicy;
	
/**
* Reference vers l'initiationDispatcher de l'orb
*/
    private InitiationDispatcher ID;
    
    private Acceptor acceptor;
    
    private Connexion connexion;

    private ORB _orb;
/**
 * Méthode génerale de démarage du serveur de connexion.
 */
    public void run()
    {				
        switch(ThreadPolicy)
            {
            case org.sorbet.PortableServer.POA.SINGLE_THREAD:
                acceptor=new ClientAcceptor(ID,port,maxClient,ThreadPolicy,false);
                //ID.init();
                acceptor.init();
                ID.start();
                break;
            case org.sorbet.PortableServer.POA.THREAD_PER_REQUEST :
                ID.setthread(true);
                acceptor=new ClientAcceptor(ID,port,maxClient,ThreadPolicy,true);
                //ID.init();
                acceptor.init();
                acceptor.start();
                ID.start();
                break;
            case org.sorbet.PortableServer.POA.THREAD_PER_SERVANT :
                acceptor=new ClientAcceptor(ID,port,maxClient,ThreadPolicy,false);
                //ID.init();
                acceptor.init();
                ID.start();
                break;
            case org.sorbet.PortableServer.POA.THREAD_PER_PROXY :
                ID.setthread(true);
                acceptor=new ClientAcceptor(ID,port,maxClient,ThreadPolicy,true);
                //ID.init();
                acceptor.init();
                acceptor.start();
                ID.start();
                break;
            }
        System.out.println( "[Server is Started]" );
    }
    
    
    /**
 * Démarre le serveur suivant la politique choisie, dans le constructeur.
 */
    /*public void start()
 	{
 		if( monThread==null )
 			run();
 		else
 			monThread.start();
                        }*/

        /**
         * Constructeur à partir d'un port  
         * @param serverPort Port d'écoute du serveur
         */
	public Serveur(int serverPort)
    {
		this.port = serverPort;
		this.maxClient = 10;
		run();
	}

        /**
         * Constructeur à partir d'un port et d'un nombre maximal de clients
         * @param serverPort Port d'écoute du serveur
         * @param maxClient Nombre maximal de clients
         * @param reference versl'initiation Dispatcher courant
         * @param ThreadPolicy indique la politique de thread utilise
         * @param reference vers l'ORB
         */
	public Serveur(int serverPort, int maxClient,InitiationDispatcher id,byte ThreadPolicy,ORB orb )
	{
        this.ThreadPolicy=ThreadPolicy;
        this.port = serverPort;
        this.maxClient = maxClient;
	    this.ID=id;
	    this._orb=orb;
	}	
}
