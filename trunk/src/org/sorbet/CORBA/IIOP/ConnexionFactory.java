 
package org.sorbet.CORBA.IIOP; 
 
import java.net.*; 
import org.sorbet.CORBA.IOP.*; 
import org.sorbet.CORBA.*; 
import org.sorbet.CORBA.portable.*; 
import org.sorbet.CORBA.IIOP.threading.*; 
 
/** 
 * Classe de gérant tous les types de connexion. C'est cette classe qui crée les connexions. 
 * @author Millo Jean-Vivien 
 * @version 2.5
 */ 
 
public class ConnexionFactory 
{ 
	/** 
	 * Crée une connexion de type client à partir d'une IOR 
     * @param ior IOR à utiliser pour créer la connexion 
     * @throws InvalidIORStringException IOR invalide 
	 */ 
	public static Connexion createConnexionClient( String ior ) 
		throws InvalidIORStringException 
	{ 
		IOR iorKey = new IOR(ior); 
		TaggedProfile[] tag_profiles=iorKey.profiles(); 
		byte[] data=tag_profiles[0].profile_data(); 
		org.sorbet.CORBA.portable.InputStream flux=new org.sorbet.CORBA.portable.InputStream(data,false); 
		ProfileBody_1_0 profile_body=flux.read_ProfileBody_1_0(); 
                 
                ConnexionClient temp=new ConnexionClient( profile_body.host(), profile_body.port(), profile_body.object_key() ); 
                 
		if(tag_profiles[1].tag()==TaggedProfile.TAG_THREAD_POLICY) 
			temp.set_thread_policy(tag_profiles[1].profile_data()[0]); 
 
		// Ajout de l'Object Key 
		return temp; 
	} 
	 
	/** 
	 * Crée une connexion de type client à partir d'un nom d'hôte et d'un port 
     * @param host Nom de l'hôte à contacter 
     * @param port Numéro du port à utiliser sur l'hôte distant 
	 */ 
	public static Connexion createConnexionClient( String host, int port ) 
	{ 
		return (new ConnexionClient( host, port )); 
		 
	} 
	 
	/** 
	 * Crée une connexion de type client à partir d'un nom d'hôte, d'un port et d'un Identifiant d'objet. 
     * @param host Nom de l'hôte à contacter 
     * @param port Numéro du port à utiliser sur l'hôte distant 
     * @param object_key Identifiant de l'objet à invoquer 
	 */ 
	public static Connexion createConnexionClient( String host, int port, byte[] object_key ) 
	{ 
		return (new ConnexionClient( host, port, object_key )); 
	} 

     /**
     * Crée une connexion de type client à partir d'un nom d'hôte, d'un port et d'un Identifiant d'objet.
     * @param host Nom de l'hôte à contacter
     * @param port Numéro du port à utiliser sur l'hôte distant
     * @param object_key Identifiant de l'objet à invoquer
     * @param thread is a thread
     * @param ID current initiation dispatcher
	 */ 
	public static Connexion createConnexionClient( String host, int port, byte[] object_key,boolean thread,InitiationDispatcher ID ) 
	{ 
		return (new ConnexionClient( host, port, object_key,thread,ID )); 
	} 
 /**
     * Crée une connexion de type client à partir d'un nom d'hôte, d'un port et d'un Identifiant d'objet.
     * @param obj Identifiant du proxy
     * @param thread is a thread
     * @param ID current initiation dispatcher
	 */ 
	public static Connexion createConnexionClient(ObjectImpl obj,boolean thread,InitiationDispatcher ID ) 
	{ 
		return (new ConnexionClient(obj,thread,ID )); 
	} 
	/** 
	 * Cree une connexion de type serveur à partir d'un socket 
     * @param Initiation Dispatcher courant 
     * @param Handle contenant la socket 
	 */ 
	public static Connexion createConnexionServeur(InitiationDispatcher ID,Handle H) 
	{ 
		ConnexionServeur cs = new ConnexionServeur(ID,H); 
 
		return cs; 
	} 
	/** 
	 * Cree une connexion de type serveur à partir d'un socket 
     * @param Initiation Dispatcher courant 
     * @param Handle contenant la socket 
     * @param Connexin est un Thread ou pas  
     * @param Politique de Thread utilisé 
	 */ 
	public static Connexion createConnexionServeur(InitiationDispatcher ID,Handle H,boolean T,byte ThreadPolicy) 
	{ 
		return new ConnexionServeur(ID,H,T,ThreadPolicy); 
	} 
 
    /** 
	 * Cree une connexion de type serveur à partir d'un socket, d'un nom d'hôte et d'un port 
     * @param socket Socket à utiliser pour effectuer la connexion 
     * @param distantHostName Nom de l'hôte 
     * @param port Port à utiliser 
     */ 
	public static Connexion createConnexionServeur(InitiationDispatcher ID,Handle H, String distantHostName, int port ) 
	{ 
		return (new ConnexionServeur(ID,H, distantHostName, port )); 
	} 
	 
	/** 
	 * Un construteur qui empeche l'instanciation de la classe. 
	 */ 
	private ConnexionFactory() 
	{ 
	} 
} 
