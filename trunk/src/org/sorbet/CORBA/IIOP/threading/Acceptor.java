package org.sorbet.CORBA.IIOP.threading; 
 
import java.util.Vector; 
import java.io.IOException; 
import org.sorbet.CORBA.IIOP.*; 
/** 
 *Decrit un Acceptor générique.
 * @author Millo Jean-Vivien 
 * @version 2.5
 **/ 
public class Acceptor extends EventHandler 
{ 
	/** 
	 *reference vers le service considéré 
	 **/ 
	ServiceHandler serviceHandler; 
	/** 
	 *reference vers le clone 
	 **/ 
        EventHandler clone; 
	/** 
	 * Port d'ecoute local 
	 **/ 
	int Port; 
	/** 
	 * Nombre de connexion qu'il accepte avant de se fermer 
	 **/ 
	int nbdeconnection=0; 
	int nbReponse=0; 
	/** 
	 *@param l'initiation dispatcher courant 
	 **/ 
    public Acceptor(InitiationDispatcher ID) 
    { 
        super(ID); 
    } 
	/** 
	 *@param l'initiation dispatcher courant 
	 *@param Thread : permet de savoir si l'acceptor doit être un thread ou pas 
	 **/ 
    public Acceptor(InitiationDispatcher ID,boolean t) 
    { 
        super(ID,t); 
    } 
	/** 
	 *@param l'initiation dispatcher courant 
	 *@param le nombre de connexion qu'il acceptera 
	 **/ 
    public Acceptor(InitiationDispatcher ID,int nbC) 
    { 
        super(ID); 
        nbdeconnection=nbC; 
    } 
	/** 
	 *@param l'initiation dispatcher courant 
	 *@param Le port local 
	 *@param le nombre de connexion qu'il acceptera 
	 *@param if it is a thread 
	 **/ 
    public Acceptor(InitiationDispatcher ID, int port, int nbC,boolean t) 
    { 
        super(ID,t); 
        Port = port; 
        nbdeconnection=nbC; 
    }/** 
	 *@param l'initiation dispatcher courant 
	 *@param Le port local 
	 *@param le nombre de connexion qu'il acceptera 
	 *@param instance du service à cloner 
	 **/ 
    public Acceptor(InitiationDispatcher ID,int port, int nbConnection,ServiceHandler service) 
    { 
        super(ID); 
        Port =port; 
        nbdeconnection=nbConnection; 
        serviceHandler=service; 
    }/** 
	 *@param l'initiation dispatcher courant 
	 *@param Le port local 
	 *@param le nombre de connexion qu'il acceptera 
	 *@param instance du service à cloner 
	 *@param Thread : permet de savoir si l'acceptor doit être un thread ou pas 
	 **/ 
    public Acceptor(InitiationDispatcher ID,int port, int nbConnection,ServiceHandler service,boolean T) 
    { 
        super(ID,T); 
        Port =port; 
        nbdeconnection=nbConnection; 
        serviceHandler=service; 
         
    } 
    /** 
     *Initialise l'acceptor 
     *ouvre la connexion et l'enregistre auprés de l'initiation dispatcher 
     **/ 
    public void init() 
    { 
        open(); 
        initiationDispatcher.eventInfoManager.registerHandler(this); 
    } 
    /** 
	 *Met le Handle à l'écoute 
	 **/ 
    public void open()
    {
        this.setPeerHandle(new Handle(Port));
    }
    /**
     *Executé quand il recoit une demande de connexion
     **/
	public void handleEvent()
    {
	//System.out.println("Acceptor:handleEvent");
        if(nbReponse>=nbdeconnection&&nbdeconnection!=0)//:maximum de @nbdeconnection requetes : just for test
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
    } 
    /** 
	 *clone le service. 
	 **/ 
    public void makeServiceHandler() 
    { 
    	//clone=(ServiceHandler)serviceHandler.clone(); 
    } 
    /** 
	 *Transmet la connexion au service 
	 **/ 
    void acceptServiceHandler() 
    { 
        clone.setPeerHandle(new Handle(this.getHandle().getSocket())); 
        this.getHandle().setSocket(null); 
    } 
    /** 
	 *enregistre le service auprés de l'ID 
	 **/ 
    public void activateServiceHandler()
    {
	initiationDispatcher.eventInfoManager.registerHandler(clone);
	if(clone.getthread())  
	    clone.start();
	clone=null;
    }
    public EventHandler getClone(){return clone;} 
    public void setClone(ServiceHandler serv){clone=serv;} 
    public ServiceHandler getServiceHandler(){return serviceHandler;} 
    public void setServiceHandler(ServiceHandler serv){serviceHandler=serv;} 
}
