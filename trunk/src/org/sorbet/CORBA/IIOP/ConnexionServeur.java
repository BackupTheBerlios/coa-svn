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
    private static ORB orb=ORB.init(); //ORB momentan�ment d�sactiv� 
 
    private static HashMap listTaskServer=null;
    
    public Automate getAutomate(){return automate;}
    
    public static HashMap listTaskServer(){return listTaskServer;}
    
    /**
     * La methode handleEvent associ�e au serveur.
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
		    case 0 : // D�connect�
			System.out.println("D�connect�");
			disconnect();
			//getAutomate().setEtat(1);
			break;
		    case 1 : // Connect� en Attente
			System.out.println("de retour en attente");
			break;
		    case 2 : // En Attente de r�ponse
			
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
         * Constructeur pour le serveur � partir d'un socket, d'un nom d'h�te et d'un port 
         * @param H Handle � utiliser 
         * @param distantHostName Nom de l'h�te 
         * @param port Num�ro du port � utiliser 
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
		automate=new AutomateServeur(1); // 1=Connect� 
	 
	} 
    public ConnexionServeur(InitiationDispatcher ID,Handle H,boolean T,byte threadPolicy) 
	{ 
		super(ID,H,T); 
		ThreadPolicy=threadPolicy; 
		automate=new AutomateServeur(1); // 1=Connect� 
	 
	} 
	/** 
	 *Permet de r�cuperer le bon Thread TaskServeur. 
	 *Cette fonction est utilis� que quand la politique de gestion de  Thread est 
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
