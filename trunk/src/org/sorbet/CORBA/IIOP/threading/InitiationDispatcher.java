package org.sorbet.CORBA.IIOP.threading; 
 
import org.sorbet.CORBA.IIOP.*; 
/**
 *Ce Thread parcourt les handles des different EventHandler (Acceptor ConnexionServeur cote serveur)
 *(Connexion Client cote client) et lance les methodes handleEvent (run) de ces objet (ou thread)
 * quand il recoivent quelque chose dans leur socket.
 * @author Millo Jean-Vivien from Alexandre Delarue
 * @version 2.5
 */
public class InitiationDispatcher extends Thread 
{ 
	/** 
	 *Liste de tout les EventHandler 
	 **/ 
	public EventInfoManager eventInfoManager; 
	public int currentIndex; 
	boolean IamAlive=false; 
	/** 
	 *Temps d'écoute de la socket 
	 **/ 
	int timeout; 
	/** 
	 *Temps de repos aprés avoir écouté une socket 
	 **/ 
	long pollingDelay; 
	/** 
	 *Temps de repos a chaque boucle où il n'y a pas de socket à écouter 
	 **/ 
	long noHandleAvailableDelay; 
	/** 
	 *Permet de savoir si les Objets EventHandlers qu'il gère sont des threads ou pas 
	 **/ 
	private boolean thread=false; 
	/** 
	 *Initialisation des temps d'ecoute, de repos et du container 
	 **/ 
	public void init() 
	{ 
		eventInfoManager=new EventInfoManager(); 
		IamAlive=true; 
		timeout=100; 
		pollingDelay=10; 
		noHandleAvailableDelay=20; 
	} 
	 
/** 
* The thread run() method that performs the handleEvents loop 
*/ 
	public void run() 
	{ 
		Handle h = null; 
		// loop that handles events 
		while (IamAlive) 
        { 
			// what is the index of the next Handle to pool ? 
			currentIndex = eventInfoManager.nextIndex(); 
			    //System.out.println("Index courant : "+currentIndex); 
			if (currentIndex >-1) 
			{ 
				 
				// get the Handle to listen on 
				h = eventInfoManager.getHandle(currentIndex); 
				// reserve the right to use the handle				 
				if(h!=null) 
				{ 
					//System.out.println("H différent de null");
					h.setInUse(true);
					// wait for an event to occur on it
					if (h.waitForEvent(timeout, this))
					{
                                            // call the corresponding Service Handler fs handleEvent method
					    //System.out.println("wait for event ok :"+ eventInfoManager.getCurrentEventHandler(currentIndex).getthread());
					    if(eventInfoManager.getCurrentEventHandler(currentIndex).getthread())
						eventInfoManager.getCurrentEventHandler(currentIndex).Notify();
					    else
					    	eventInfoManager.getCurrentEventHandler(currentIndex).handleEvent();
					    //System.out.println("fin du Handle Event :"+h.getServeurSocket());
					}
					// free the right of use 
					h.setInUse(false); 
				} 
				// makes the thread wait for a while before querrying another Handle 
				// to limit CPU resource use 
				try { sleep(pollingDelay); } 
				catch(InterruptedException ie) { System.out.println(ie.getMessage()); } 
			} 
			else 
			// there is no Handle available 
			{ 
//kill(); 
				// makes the thread wait for a while if there is no registered Handle 
				// to limit CPU resource use 
				try { sleep(noHandleAvailableDelay); } 
				catch(InterruptedException ie) { System.out.println(ie.getMessage()); } 
			} 
		} 
	} 
	/** 
	 *Permet d'arrêter la boucle d'écoute 
	 *et de tuer les Threads encore vivants 
	 **/ 
	public synchronized void kill() 
	{ 
		IamAlive=false; 
		currentIndex = eventInfoManager.nextIndex(); 
		while(currentIndex>-1) 
		{ 
			eventInfoManager.unregisterHandler(eventInfoManager.getCurrentEventHandler(currentIndex)); 
			currentIndex = eventInfoManager.nextIndex(); 
		} 
		if(ConnexionServeur.listTaskServer()!=null) 
		{ 
			taskserveur temp; 
			java.util.Vector V=new java.util.Vector(ConnexionServeur.listTaskServer().values()); 
			while(!V.isEmpty()) 
			{ 
					temp=((taskserveur)V.remove(0)); 
					temp.alive(false); 
					temp.Notify(); 
			} 
		} 
	} 
	public void setthread(boolean T){thread=T;} 
	public boolean getthread(){return thread;} 
}
