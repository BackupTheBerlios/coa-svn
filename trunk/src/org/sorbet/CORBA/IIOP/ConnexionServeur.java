package org.sorbet.CORBA.IIOP; 
 
import org.sorbet.CORBA.*; 
import org.sorbet.CORBA.GIOP.*; 
import org.sorbet.CORBA.IOP.*; 
import org.sorbet.CORBA.IIOP.threading.*; 
import java.util.*; 
import org.sorbet.PortableServer.*; 
 
import java.net.*; 
import java.io.*; 
 
/** 
 * Classe de connexion de type serveur. 
 * @author MILLO Jean-Vivien 
 * @version 2.5 
 */ 
 
public class ConnexionServeur extends Connexion 
{
     
    private Automate automate; 
    private static ORB orb=ORB.init(); //ORB momentanément désactivé 
 
    private static HashMap listTaskServer=null;
    
    public Automate getAutomate(){return automate;}
    
    public static HashMap listTaskServer(){return listTaskServer;}
    
    /**
     * La methode handleEvent associée au serveur.
     */
    public synchronized void handleEvent()
    {
	try
	{
	    byte[] buffer = getMessage();
	    if (buffer==null)
		throw new Exception("Buffer NULL");
	    
	    System.out.println("taille: "+buffer.length);
	    
	    org.sorbet.CORBA.portable.InputStream flux = new org.sorbet.CORBA.portable.InputStream( buffer, false );
	    try 
	    {
		MessageHeader_1_0 header = flux.read_Message_Header();
		
		switch (automate.transition(header.message_type())) 
		{
		    case 0 : // Déconnecté
			System.out.println("Déconnecté");
			disconnect();
			//getAutomate().setEtat(1);
			break;
		    case 1 : // Connecté en Attente
			System.out.println("de retour en attente");
			break;
		    case 2 : // En Attente de réponse
			
			org.sorbet.CORBA.portable.InputStream flux_suite=flux.read_CDRInputStream();
			
			org.sorbet.PortableServer.Servant servant1=orb._servant(flux_suite);
			
			if(!(ThreadPolicy==org.sorbet.PortableServer.POA.THREAD_PER_SERVANT))
			{
			    taskserveur tache=new taskserveur(servant1,this,orb.operation(),orb.input().read_CDRInputStream(),orb.request_id());
			    tache.run();
			}else
			{
			    taskserveur tache=getTaskServeur(servant1);
			    tache.parametrer(this,orb.operation(),orb.input().read_CDRInputStream(),orb.request_id());
			    if(!tache.isAlive())
				tache.start();
			    else
			    {
				while(tache.inUse())
				    Thread.yield();
				tache.Notify();
			    }
			}
			
			break;
		    default :
			System.err.println("Erreur dans l'automate");
			// disconnect();
			break;
		}
	    } catch (Exception e) 
	    {
		System.err.println(e.getMessage());
		e.printStackTrace();
		disconnect();
	    }
	} catch (Exception e) 
	{
	    System.err.println("Erreur dans l'automate");
	    disconnect();
	}
	if(!(ThreadPolicy==org.sorbet.PortableServer.POA.THREAD_PER_SERVANT))
	    Stop();
    }
    
    /**
         * Constructeur pour le serveur à partir d'un socket, d'un nom d'hôte et d'un port 
         * @param H Handle à utiliser 
         * @param distantHostName Nom de l'hôte 
         * @param port Numéro du port à utiliser 
         */ 
	public ConnexionServeur(Handle H, String distantHostName, int port ) 
	{ 
		super(H, distantHostName, port );
	 
	} 
	public ConnexionServeur(InitiationDispatcher ID,Handle H, String distantHostName, int port ) 
	{ 
		super(ID,H, distantHostName, port ); 
	 
	} 
	public ConnexionServeur(InitiationDispatcher ID,Handle H) 
	{ 
		super(ID,H); 
		automate=new AutomateServeur(1); // 1=Connecté 
	 
	} 
    public ConnexionServeur(InitiationDispatcher ID,Handle H,boolean T,byte threadPolicy) 
	{ 
		super(ID,H,T); 
		ThreadPolicy=threadPolicy; 
		automate=new AutomateServeur(1); // 1=Connecté 
	 
	} 
	/** 
	 *Permet de récuperer le bon Thread TaskServeur. 
	 *Cette fonction est utilisé que quand la politique de gestion de  Thread est 
	 *THREAD_PER_SERVANT 
	 **/ 
    public taskserveur getTaskServeur(Servant servant1) 
    { 
        taskserveur temp; 
         if(ConnexionServeur.listTaskServer==null) 
             ConnexionServeur.listTaskServer=new HashMap(); 
         try 
         { 
             if((temp=(taskserveur)(listTaskServer.get(servant1)))!=null) 
                 return temp; 
             else  
             { 
             	temp=new taskserveur(servant1); 
                listTaskServer.put(servant1,temp); 
                return temp;	 
             } 
         }catch(NullPointerException NPE) 
             { 
                 temp=new taskserveur(servant1); 
                 listTaskServer.put(servant1,temp); 
                 return temp; 
             } 
    } 
}
