package org.sorbet.CORBA.IIOP.threading;

import org.sorbet.CORBA.IIOP.*;
import org.sorbet.CORBA.GIOP.*;
/**
 *Acceptor dédié au objets connexion Serveur
 *Qaund une connexion est crée, il crée un objet connexion serveur
 *@author Millo Jean-Vivien
 *@version 2.5
 **/
public class ConnexionAcceptor extends Acceptor
{
    byte threadPolicy;
    /**
     *constructeur
     *@param referance vers l'initiation dispatcher
     *@param port
     *@param nb maximum de connexion
     *@param politique de thread
     **/
    public ConnexionAcceptor(InitiationDispatcher ID,int port,int maxClient,byte t,boolean thread)
    {
	super(ID,port,maxClient,thread);
	threadPolicy=t;
    }
    /**
     *on crée un objet connexion serveur correctement paramètré
     **/
    public void makeServiceHandler()
    {
	if(threadPolicy==org.sorbet.PortableServer.POA.THREAD_PER_REQUEST)
	    clone=(EventHandler)ConnexionFactory.createConnexionServeur(initiationDispatcher,null,true,threadPolicy);
	else
	    clone=(EventHandler)ConnexionFactory.createConnexionServeur(initiationDispatcher,null,false,threadPolicy);
    }
     /**
     *Executé quand il recoit une demande de connexion
     **/
    public void handleEvent()
    {
        try{
        if(getHandle().getSpecialSocket()!=null&&getHandle().getAvailable()>0)
        {
            byte[] buffer =new byte[getHandle().getAvailable()];
            getHandle().readSpecial(buffer);
            org.sorbet.CORBA.portable.InputStream flux = new org.sorbet.CORBA.portable.InputStream( buffer, false );
            MessageHeader_1_0 header = flux.read_Message_Header();
            if(header.message_type()==MessageHeader_1_0.CloseConnection)
                Stop();
        }
	else if(nbReponse>=nbdeconnection&&nbdeconnection!=0)//:maximum de @nbdeconnection requetes : just for test
        	Stop();
        else
        {
            makeServiceHandler();
            acceptServiceHandler();
            activateServiceHandler();
            nbReponse++;
	    if(nbReponse==nbdeconnection)
		 Stop();
	}
	}catch(java.io.IOException e){e.printStackTrace();}
    }
}
