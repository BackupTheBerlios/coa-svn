package org.sorbet.CORBA.IIOP.threading; 
 
import java.util.Vector; 
 
/** 
 *Classe regroupant les fonctionnalités communes à
 *l'acceptor
 *le connector (pas utilise dans sorbet)
 *et les ServiceHandlers (qui correspondent aux objets connexion)
 *@author Millo Jean-Vivien
 *@version 2.5
 **/ 
public abstract class EventHandler extends Thread
{
	/** 
	 *Handle qui gère les echanges de messages 
	 **/ 
	protected Handle peerH=null; 
	/** 
	 *ref vers l'initiation dispatcher auquel il est rataché 
	 **/ 
	protected InitiationDispatcher initiationDispatcher; 
	 
	private boolean continu; 
	/** 
	 *Permet de determiner si il doit être considéré comme un thread ou 
	 *comme un simple objet 
	 **/ 
	protected boolean thread=false; 
	private boolean isDying=false; 
 
    public EventHandler(){} 
	public EventHandler(InitiationDispatcher ID) 
	{ 
		initiationDispatcher=ID; 
	} 
	public EventHandler(InitiationDispatcher ID,boolean t) 
	{ 
		initiationDispatcher=ID; 
		thread=t; 
	} 
	public EventHandler(InitiationDispatcher ID,Handle H) 
	{ 
		initiationDispatcher=ID; 
		peerH=H; 
	} 
	public EventHandler(InitiationDispatcher ID,Handle H,boolean t) 
	{ 
		initiationDispatcher=ID; 
		peerH=H; 
		thread=t; 
	} 
	synchronized public Handle getHandle(){return peerH;} 
	/** 
	 *@return le Handle associé au EventHandler 
	 **/ 
	synchronized public void setPeerHandle(Handle H){peerH=H;} 
	/** 
	 *Cette fonction permet de débloquer le thread si il est en attente (wait) 
	 **/ 
    synchronized public void Notify(){this.notify();} 
	/** 
	 *Cette fonction se déclenche quand le Handle associé recoit un evenement 
	 **/ 
	synchronized public void handleEvent(){}	//action lié a la réception d'un événement, à surcharge

	/** 
	 *Comportement: tant que je suis vivant, je lance la fonction handleEvent 
	 *et je m'endort jusqu'a ce que l'on me reveille pour relancer handleEvent 
	 **/ 
	synchronized public void run()
	{
	    continu=true;
	    while(continu)
		{
                    try{this.wait();}
                    catch(InterruptedException Ie){System.out.println(Ie.getMessage());}
                    if(!isDying)
                    	handleEvent();
		}
    }
    /** 
     *Permet de tuer l'objet-thread 
     **/ 
    synchronized public void Stop() 
    { 
        initiationDispatcher.eventInfoManager.unregisterHandler((EventHandler)this); 
		continu=false; 
		isDying=true;
		Notify();
    }
    /**
     *Permet de cloner l'objet
     **/
    synchronized protected Object clone()
	{
	    try{
		Object e=super.clone();
		return e;
	    }catch(CloneNotSupportedException CNSE)
	    {System.out.println(CNSE.getMessage());return null;}
	}
    public void setthread(boolean T){thread=T;}
    public boolean getthread(){return thread;}
    public InitiationDispatcher initiationDispatcher(){return initiationDispatcher;}
    public void initiationDispatcher(InitiationDispatcher _initiationDispatcher){initiationDispatcher=_initiationDispatcher;}
}
