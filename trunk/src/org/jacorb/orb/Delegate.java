package org.jacorb.orb;

import org.sorbet.CORBA.portable.*;
import org.sorbet.Messaging.*;
import org.sorbet.CORBA.IIOP.*;
import org.sorbet.CORBA.IIOP.threading.*;
import org.sorbet.PortableServer.*;
/**
 *Cette classe gère les apples asynchrones coté client
 *Le nom du package auquel elle appartient est fixe par le compilateur
 *idl de jacorb sans lequel il n'y aurait pas d'appel asynchrone
 *@author Millo Jean-Vivien
 *@version 2.5
 **/

public class Delegate
{
    
    taskClient ts=null;
    public taskClient getTS(){return ts;}
    /**
     *fonction d'invocation asynchrone
     *si la politique de thread est THREAD_PER_SERVANT par défaut, l'execution est monotache.
     *mais le client peut changer cette politique si il le souhaite directement dans le main() client
     *apres le serviceHelper.narrow();
     **/
    public void invoke(ObjectImpl obj,OutputStream  _os,ReplyHandler  ami_handler)
    {	
	try 
	{
	    byte Thread_Policy=((ConnexionClient)obj._get_protocol()).get_thread_policy();
	    if(Thread_Policy<1||Thread_Policy>4)
		throw new Invalide_Thread_Policy_Exception("la politique de gestion de thread n'est pas valide");
            //pas d'appel asynchrone en single thread
	    //car le fait que le seveur soit compatible avec un client jdk en single thread,
	    // nous bloques les appels asynchrones
	    if(Thread_Policy==POA.SINGLE_THREAD)
		throw new Invalide_Thread_Policy_Exception("Pas d'appel asynchrone en single thread");
            //dans cette politique, on a qu'un seul thread pour toutes les requetes du meme proxy
	    //c'est l'objet task client. les objet delegate se le partage comme une resource critique
	    if(Thread_Policy==POA.THREAD_PER_PROXY)
	    {
		if((ts=obj._getTaskClient())==null)
		    ts=new taskClient();
		while(ts.busy())
		    Thread.yield();
		ts.take();
		ts.param(obj,obj._get_operation(),obj._get_responseExpected(),_os,ami_handler);
		
		if(!ts.isAlive())
		    ts.start();
		else 
		    ts.Notify();
	    }
	    //ce cas englobe le thread par requete et l'execution de base de thread par servant qui se fait en monotache
	    else 
	    {
	
		org.sorbet.CORBA.ORB _orb=org.sorbet.CORBA.ORBLoader.init();
		ConnexionClient temp;
		
		if(Thread_Policy==org.sorbet.PortableServer.POA.THREAD_PER_REQUEST)//c'est un thread
		    temp=(ConnexionClient)ConnexionFactory.createConnexionClient(obj,true,_orb.initiationDispatcher());
		else//c'est pas un thread
		    temp=(ConnexionClient)ConnexionFactory.createConnexionClient(obj,false,_orb.initiationDispatcher());
		
		temp.setPeerHandle(new Handle(temp.distantHost()));
		temp.set_ami_handler(ami_handler);
		if(obj._get_responseExpected())
		{
		    temp.initiationDispatcher().eventInfoManager.registerHandler((EventHandler)temp);
		    if(!temp.initiationDispatcher().isAlive())
		    {
			temp.initiationDispatcher().start();
		    }
		}
		//envoie effectif de la requete
		temp.send_deferred(obj._get_operation(),_os,obj._get_responseExpected(),Thread_Policy);
		
		if(obj._get_responseExpected()&&Thread_Policy==org.sorbet.PortableServer.POA.THREAD_PER_REQUEST)
		    temp.start();
	    }
	    //on enregistre le handle aupres du proxy pour qu'a sa fermeture, il attende bien que la reponse soit arrive
	    obj.getListHandler().add(ami_handler);
	}
	catch (Exception e)
	{
	    System.err.println(e.getMessage());
	    e.printStackTrace();
	}
    }
}
