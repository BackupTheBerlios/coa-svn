package org.sorbet.CORBA.IIOP.threading;

import org.sorbet.CORBA.IIOP.*;
import org.sorbet.PortableServer.*;
/**
 *Acceptor dedie au objets connexion Serveur
 *Quand une connexion est cree, il cree un objet connexion serveur
 *@author Millo Jean-Vivien
 *@version 2.5
 **/
public class ClientAcceptor extends Acceptor
{
    byte threadPolicy;
    /**
     *constructeur
     *@param reference vers l'initiation dispatcher
     *@param port
     *@param nb maximum de connexion
     *@param politique de thread
     **/
    public ClientAcceptor(InitiationDispatcher ID,int port,int maxClient,byte t,boolean thread)
    {
	super(ID,port,maxClient,thread);
	threadPolicy=t;
    }
    /**
     *on crée un objet connexion serveur correctement paramètré
     **/
    public void makeServiceHandler()
    {
        if(threadPolicy==POA.SINGLE_THREAD)
            clone=(EventHandler)ConnexionFactory.createConnexionServeur(initiationDispatcher,null,false,threadPolicy);
        else
        {
            //a remplacer par un appel qui retourne le prochain port libre sur la machine local (ca existe ?)
            Port++;
            clone=new ConnexionAcceptor(initiationDispatcher,Port,0,threadPolicy,thread);
        }
    }
    void acceptServiceHandler()
    {
        if(threadPolicy==POA.SINGLE_THREAD)
        {
            clone.setPeerHandle(new Handle(this.getHandle().getSocket()));
            this.getHandle().setSocket(null);
        }
        else
        {
            clone.setPeerHandle(new Handle(Port));
            try{
                getHandle().write(((Object)new Integer(Port)));
                clone.getHandle().setSpecialSocket(this.getHandle().getSocket());
            }catch(java.io.IOException e){e.printStackTrace();}
            this.getHandle().setSocket(null);
        }
    }
}
