package org.jacorb.orb;

import org.sorbet.CORBA.portable.*;
import org.sorbet.Messaging.*;
import org.sorbet.CORBA.IIOP.*;
import org.sorbet.CORBA.IIOP.threading.*;
/**
 * Cette objet represente le thread dedie a un proxy sur le client
 *il est donc utile que en politique un thread par proxy
 *@author Millo Jean-Vivien
 *@version 2.5
 **/
public class taskClient  extends Thread
{
    ObjectImpl obj;
    String op;
    boolean reponseExpected;
    OutputStream  _os;
    ReplyHandler  ami_handler;

    boolean inUse=false;
    boolean isDying=false;
    /**
     *Cette fonction fait la même chose que la methode _invoke de l'objet org.jacorb.orb.Delegate
     *sauf quelle le fait pour chaque requete d'un proxy
     **/
    public synchronized void run()
	{
	    try 
	    {
		ConnexionClient temp;
		while(!isDying)
		{
		    org.sorbet.CORBA.ORB _orb=org.sorbet.CORBA.ORBLoader.init();
		    
		    temp=(ConnexionClient)ConnexionFactory.createConnexionClient(obj,false,_orb.initiationDispatcher());
		    
		    temp.setPeerHandle(new Handle(new ListenPoint(obj._get_protocol().distantHost().host(), obj._get_protocol().distantHost().port())));
		    temp.set_ami_handler(ami_handler);
		    if(reponseExpected)
		    {
			temp.initiationDispatcher().eventInfoManager.registerHandler((EventHandler)temp);
			if(!temp.initiationDispatcher().isAlive())
			    temp.initiationDispatcher().start();
		    }
		    temp.send_deferred(op,_os,reponseExpected,org.sorbet.PortableServer.POA.THREAD_PER_PROXY);
		    inUse=false;
		    try{wait();}catch(InterruptedException e){e.printStackTrace();}
		}
	    }catch (Exception e)
	    {
		System.err.println(e.getMessage());
		e.printStackTrace();
	    }
	}
    void init(){}
    /**permet de parametre la requete*/
    void param(ObjectImpl obj,String op, boolean reponseExpected ,OutputStream  _os,ReplyHandler  ami_handler)
    {	
	this.obj=obj;
	this.op=op;
	this.reponseExpected=reponseExpected;
	this._os=_os;
	this.ami_handler=ami_handler;
    }
    /**lance la requete*/
    public synchronized void Notify(){notify();}
    /**@return inUse si il est en cours d'utilisation*/
    public boolean busy(){return inUse;}
    /**permet de tuer le thread*/
    public void kill(){isDying=true;}
    /**prend la ressource que represente ce thread*/
    public void take(){inUse=true;}
}
