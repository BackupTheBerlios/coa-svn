package org.sorbet.CORBA.IIOP; 
 
import org.sorbet.CORBA.*; 
import org.sorbet.CORBA.GIOP.*; 
import org.sorbet.CORBA.IOP.*; 
import org.sorbet.CORBA.IIOP.threading.*;
import org.sorbet.CORBA.portable.*;
import org.sorbet.Messaging.*;
import org.sorbet.PortableServer.*;
 
import java.net.*;
import java.io.*; 
 
/**
 * Classe de connexion de type client.
 * @author MILLO jean-Vivien
 * @version 2.5
 */

public class ConnexionClient extends Connexion 
{ 
    /** 
     *Operation à transmettre au serveur 
     **/ 
    String operation; 
    /** 
     *Canal de communication 
     **/ 
    org.sorbet.CORBA.portable.OutputStream os; 
    /** 
     *réponse à la requête souhaité 
     **/ 
    boolean responseExpected;
    /**
     * Handler dedie à l'ecoute de la reponse envoye par le client
     **/
    ReplyHandler  ami_handler;
    /**
     * Reference vers le proxy doit provient la requete
     **/
    ObjectImpl object;
    
    public void parametrer( String operation, org.sorbet.CORBA.portable.OutputStream os, boolean responseExpected,byte ThreadPolicy) 
	{	 
	    this.operation=operation; 
	    this.os=os; 
	    this.responseExpected=responseExpected; 
	    this.ThreadPolicy=ThreadPolicy; 
	} 
    public org.sorbet.CORBA.portable.InputStream send() throws UnexpectedMessageTypeException,InvalidRequestIdException,NonCORBAMessageException, InvalidVersionNumberException, InvalidMessageTypeException, InvalidMessageSizeException, UnknownHostException 
	{return send(operation,os,responseExpected,ThreadPolicy);} 
    
    public org.sorbet.CORBA.portable.InputStream send( String operation, org.sorbet.CORBA.portable.OutputStream os, boolean responseExpected,byte ThreadPolicy) throws UnexpectedMessageTypeException,InvalidRequestIdException,NonCORBAMessageException, InvalidVersionNumberException, InvalidMessageTypeException, InvalidMessageSizeException, UnknownHostException
	{
	    this.operation=operation;
	    this.os=os;
	    this.responseExpected=responseExpected;
	    this.ThreadPolicy=ThreadPolicy;
	    
	    try{
		if(ThreadPolicy==POA.SINGLE_THREAD&&_socket().isClosed())
		    setPeerHandle(new Handle(distantHost()));
		
	    }catch(IOException e){e.printStackTrace();}
	    // Génération d'un requesting_principal bidon car déprécié
	    byte[] requesting_principal={0};
        
	    
	    // Génération du Request Header
	    // Le service context est bidon
	    ServiceContext[] service_context=new ServiceContext[0];
	    
	    RequestHeader_1_0 request_header
		= new RequestHeader_1_0 (
		    service_context,
		    request_id,
		    responseExpected,
		    object_key,
		    operation,
		    requesting_principal);
	    
        // Encapsulation du Request Header dans un flux temporaire
        
        
        org.sorbet.CORBA.portable.OutputStream request_header_stream = new org.sorbet.CORBA.portable.OutputStream(false);
        request_header_stream.write_Header(request_header);
        
        
        
        // Generation du Message Header
        MessageHeader_1_0 message_header
            = new MessageHeader_1_0 (
                                     false,
                                     MessageHeader_1_0.Request,
                                     request_header_stream.get_donnees().length + os.get_donnees().length);
        
        // Génération du flux final
        org.sorbet.CORBA.portable.OutputStream request = new org.sorbet.CORBA.portable.OutputStream(false);
        request.write_Message_Header(message_header);
        request.write_CDROutputStream(request_header_stream);
        request.write_CDROutputStream(os);
        sendMessage(request.get_donnees());
        if (responseExpected==false) 
            return null;
        
        byte[] buffer_reponse= new byte[12];
        buffer_reponse = getMessage();
        
        org.sorbet.CORBA.portable.InputStream reponse = new org.sorbet.CORBA.portable.InputStream(buffer_reponse,false);
        MessageHeader_1_0 message_header2 = reponse.read_Message_Header();
        if (message_header2.message_type() != MessageHeader_1_0.Reply) 
            throw (new UnexpectedMessageTypeException());
        
        ReplyHeader_1_0 reponse_2_entete=reponse.read_ReplyHeader_1_0();
        int request_id_reponse=reponse_2_entete.request_id();
        if (request_id_reponse!=request_id) 
            throw new InvalidRequestIdException();
        
        request_id++;
        try{
            getHandle().close();
        }catch(IOException e){e.printStackTrace();}
        return reponse;   
        
    }
public void send_deferred( String operation, org.sorbet.CORBA.portable.OutputStream os, boolean responseExpected,byte ThreadPolicy) throws InvalidVersionNumberException, InvalidMessageTypeException, InvalidMessageSizeException, UnknownHostException 
 
	{	 
		this.operation=operation; 
		this.os=os; 
		this.responseExpected=responseExpected; 
		this.ThreadPolicy=ThreadPolicy;
                
                 try{
                    if(ThreadPolicy==POA.SINGLE_THREAD&&_socket().isClosed())
                        setPeerHandle(new Handle(distantHost()));
                }catch(IOException e){e.printStackTrace();}
                 
		// Generation d'un requesting_principal bidon car deprecie 
		byte[] requesting_principal={0}; 
		 
		 
		// Génération du Request Header 
		// Le service context est bidon 
		ServiceContext[] service_context=new ServiceContext[0]; 
 
 
		RequestHeader_1_0 request_header 
			= new RequestHeader_1_0 ( 
									service_context, 
									request_id, 
									responseExpected, 
									object_key, 
									operation, 
									requesting_principal); 
		 
		// Encapsulation du Request Header dans un flux temporaire 
	 
		 
		org.sorbet.CORBA.portable.OutputStream request_header_stream = new org.sorbet.CORBA.portable.OutputStream(false); 
		request_header_stream.write_Header(request_header); 
		 
			 
		 
		// Génération du Message Header 
		MessageHeader_1_0 message_header 
			= new MessageHeader_1_0 ( 
									false, 
									MessageHeader_1_0.Request, 
									request_header_stream.get_donnees().length + os.get_donnees().length); 
		 
		// Génération du flux final 
		org.sorbet.CORBA.portable.OutputStream request = new org.sorbet.CORBA.portable.OutputStream(false); 
		request.write_Message_Header(message_header); 
		request.write_CDROutputStream(request_header_stream); 
		request.write_CDROutputStream(os); 
		sendMessage(request.get_donnees()); 
		 
} 
public org.sorbet.CORBA.portable.InputStream get_response() throws UnexpectedMessageTypeException, InvalidRequestIdException, NonCORBAMessageException, InvalidVersionNumberException, InvalidMessageTypeException, InvalidMessageSizeException, UnknownHostException 
{

		if (responseExpected==false)  
			return null; 

		byte[] buffer_reponse= new byte[12]; 
		buffer_reponse = getMessage(); 

		org.sorbet.CORBA.portable.InputStream reponse = new org.sorbet.CORBA.portable.InputStream(buffer_reponse,false); 
		MessageHeader_1_0 message_header2 = reponse.read_Message_Header(); 
		if (message_header2.message_type() != MessageHeader_1_0.Reply)  
			throw (new UnexpectedMessageTypeException()); 

		ReplyHeader_1_0 reponse_2_entete=reponse.read_ReplyHeader_1_0(); 
		int request_id_reponse=reponse_2_entete.request_id(); 
		if (request_id_reponse!=request_id)  
			throw new InvalidRequestIdException(); 

		request_id++;
		//a supprimer pour les appels périodiques
                if(ThreadPolicy!=POA.SINGLE_THREAD)
                {
                    try{
                        getHandle().close();
                    }catch(IOException e){e.printStackTrace();}
                }
		return reponse;
	}
    /**
     *Cette fonction est execute quand la reponse a la requete est arrivee
     **/
    public void handleEvent()
	{
	    try
	    {
		org.sorbet.CORBA.portable.InputStream IS=get_response();
		((Servant)ami_handler)._invoke(operation,IS,(ResponseHandler)new ResponseHandlerImpl());
		
	    }catch(Exception e){e.printStackTrace();}
	    
	    //a supprimer pour le RT
	    object.getListHandler().remove(ami_handler);
	    this.Stop();
	}

	/**
	 * Constructeur pour le client. Etablie la connexion avec l'hote distant.
	 * @param distantHostName Nom de l'hote distant à contacter.
	 * @param port Port à ouvrir sur l'hote distant à contacter.
	 * @param object_key Identifiant de l'objet lié à la connexion.
	 */
	public ConnexionClient( String distantHostName, int port, byte[] object_key )
	{ 
		// On ajoute l'object Key
		super( distantHostName, port, object_key );
		
	} 
	/** 
	 * Constructeur pour le client. Etablie la connexion avec l'hote distant. 
	 * @param distantHostName Nom de l'hote distant à contacter. 
	 * @param port Port à ouvrir sur l'hote distant à contacter. 
	 * @param object_key Identifiant de l'objet lié à la connexion. 
	 * @param thread is a thread
	 * @param ID current initiationDispatcher
	 */ 
	public ConnexionClient( String distantHostName, int port, byte[] object_key,boolean thread,InitiationDispatcher ID )
	{
		// On ajoute l'object Key
		super( distantHostName, port, object_key,thread,ID );
		
	}
	/** 
	 * Constructeur pour le client. Etablie la connexion avec l'hote distant. 
	 * @param obj Identifiant de l'objet proxy 
	 * @param thread is a thread
	 * @param ID current initiationDispatcher
	 */ 
	public ConnexionClient(ObjectImpl obj,boolean thread,InitiationDispatcher ID )
	{
	    super(obj._get_protocol().distantHost().host(), obj._get_protocol().distantHost().port(),obj._get_protocol().object_key() ,thread,ID );
	    this.object=obj;
	}
	/** 
	 * Constructeur pour le client. Etablie la connexion avec l'hote distant. 
	 * @param distantHostName Nom de l'hote distant à contacter. 
	 * @param port Port à ouvrir sur l'hote distant à contacter. 
	 */ 
	public ConnexionClient( String distantHostName, int port ) 
	{ 
		super( distantHostName, port); 
		 
	} 
	/** 
	 *Envoie une requête de déconnexion et ferme la socket 
	 **/ 
    public void close() 
    { 
    	MessageHeader_1_0 message_header = new MessageHeader_1_0 (false,MessageHeader_1_0.CloseConnection,0);
		
		org.sorbet.CORBA.portable.OutputStream request_disconnect = new org.sorbet.CORBA.portable.OutputStream(false);
		request_disconnect.write_Message_Header(message_header);
		sendMessage(request_disconnect.get_donnees());
		try{
		_socket().close();
	}catch(java.io.IOException ex){ex.printStackTrace();}
    }
    byte thread_policy; 
    public ReplyHandler  get_ami_handler(){return ami_handler;}
    public void set_ami_handler(ReplyHandler _ami_handler){ami_handler=_ami_handler;}
} 
