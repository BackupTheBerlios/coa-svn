package org.sorbet.CORBA.IIOP;

import org.sorbet.CORBA.*;
import org.sorbet.CORBA.IOP.*;
import org.sorbet.CORBA.IIOP.threading.*;
import org.sorbet.CORBA.GIOP.*;
import org.sorbet.PortableServer.*;
import java.net.*;
import java.io.*;
 
/** 
 * Cette classe abstraite permet la gestion des connexions réseaux<P> 
 * @author Millo Jean-Vivien 
 * @version 2.5
 */ 
 
public abstract class Connexion extends ServiceHandler
{ 
    /** 
     * Point d'ecoute pour l'hote distant. 
     * @see ListenPoint 
     */ 
    private ListenPoint _distantHost = null; 
     /** 
      * L'Object Key associé à la connexion 
      */ 
     protected byte[] object_key = null;
    
    /**
     *Politique de Thread
     **/
    protected byte ThreadPolicy;
     
    /** 
     * Flux associé à la connexion en entrée ( Sans le CDR ).
     */
    private InputStream _inputStream = null;

	/** 
	 * Flux associé à la connexion en sortie ( Sans le CDR ).
	 */
	private OutputStream _outputStream = null;

	/** 
	 * Flux streamé associé à la connexion en entrée ( Sans le CDR ). 
	 */ 
	private InputStreamReader _inputStreamReader = null; 
	 
	/** 
	 * Flux streamé associé à la connexion en sortie ( Sans le CDR ). 
	 */ 
	private OutputStreamWriter _outputStreamWriter = null; 
 
	/** 
	 * Flux avancé associé à la connexion en entrée ( Sans le CDR ). 
	 */ 
	private BufferedReader _stringReader = null; 
 
	/** 
	 * Flux avancé associé à la connexion en sortie ( Sans le CDR ). 
	 */ 
	private BufferedWriter _stringWriter = null; 
	 
	/** 
	 * Permet d'identifier une requête de la suivante et surtout 
	 *de s'assure que la réponse recu est celle de la question posé 
	 **/ 
	protected int request_id=10; 

	/** 
	 * Retourne le point d'écoute 
	 * @return Point d'écoute 
	 */ 
	public ListenPoint distantHost() { 
		return _distantHost; 
	} 
	 
	/** 
         * Retourne le Socket de connexion 
         * @return Socket de connexion 
         */ 
	public Socket socket() {
            return getHandle().getSocket();
	}
        public Socket _socket() {
            return getHandle().getSocket();
	}
	
	/** 
	 * Retourne l'Identifiant de l'objet associé à la connexion 
         * @return Identifiant de l'objet 
	 */ 
	public byte[] object_key() { 
		return this.object_key; 
	} 
	 
	/**
	 * Réalise la connexion avec l'ORB distant.
	 */
    public void connect()
	{
		try
		{
			if (getHandle()==null)
                            setPeerHandle( new Handle( new ListenPoint(_distantHost.host(), _distantHost.port()) ));
		}
		catch( Exception ex )
		{
			System.err.println( "Erreur Connexion.connect():" + ex.getMessage() );
			ex.printStackTrace();
		}
		
	}

        /** 
         * Finit la connexion avec l'ORB distant. 
         */ 
	public void disconnect() 
	{ 
		if(getthread())	 
			Stop(); 
		else if(initiationDispatcher!=null) 
			initiationDispatcher.eventInfoManager.unregisterHandler(this); 
	} 
 
        /** 
         * Met le flux à jour dans la classe. 
         */ 
	private void setStreams() 
	{ 
		setStreams( _socket() ); 
	} 
 
        /** 
         * Met le flux à jour dans la classe. 
         * @param _socket Socket sur lequel doivent être mis à jour les flux 
         */ 
	private void setStreams( Socket _socket ) 
	{ 
		try 
		{ 
			_inputStream = _socket().getInputStream(); 
			_outputStream = _socket().getOutputStream(); 
 
			 
 
			_inputStreamReader = new InputStreamReader( _inputStream ); 
			_outputStreamWriter = new OutputStreamWriter( _outputStream ); 
 
			_stringReader = new BufferedReader( _inputStreamReader ); 
			_stringWriter = new BufferedWriter( _outputStreamWriter ); 
			 
		} 
		catch( Exception ex ) 
		{ 
			System.err.println( "Error Connexion.setStreams():" + ex.getMessage() ); 
			ex.printStackTrace(); 
		} 
	} 
 
        /** 
         * Libération des flux. 
         */ 
	private void freeStreams() 
	{ 
		try 
		{ 
			_stringWriter.flush(); 
 
			_stringReader.close(); 
			_stringWriter.close(); 
		} 
		catch( Exception ex ) 
		{ 
			System.err.println( "Error Connexion.freeStreams():" + ex.getMessage() ); 
			ex.printStackTrace(); 
		} 
	} 
 
        /** 
         * Obtention du buffer sur le flux en entréé. 
         * @return Données lues sur le Socket 
         */ 
	public byte[] getMessage() 
	{ 
	 
		try 
		{ 
			// Non securise a modifier 
			while( _inputStream.available() <= 0 ); 
			byte[] message = new byte[_inputStream.available()]; 
			_inputStream.read(message); 
//	System.out.println("message : "+message); 
//                        System.out.println( "[Received] " + (new String(message)) ); 
 
			return message; 
		} 
		catch( Exception ex ) 
		{ 
			System.err.println( "Error Connexion.getMessage():" + ex.getMessage() ); 
			ex.printStackTrace(); 
		} 
		return null; 
	} 
 
        /** 
         * Lecture d'une chaîne de caractère sur le flux en entrée 
         * @return Chaîne lue 
         */ 
	public String getStringMessage() 
	{ 
		try 
		{ 
			String message = _stringReader.readLine(); 
 
			System.out.println( "[Received] " + message ); 
 
			return message; 
		} 
		catch( Exception ex ) 
		{ 
			System.err.println( "Error Connexion.getStringMessage() :" + ex.getMessage() ); 
			ex.printStackTrace(); 
		} 
		return "";
	}

        /**
         * Permet d'effectuer une requetes à partir du skeleton.
         * @param operation Nom de la méthode à invoquer sur l'objet
         * @param os Flux CDR contenant les arguments
         * @param response_expected Indique si une réponse est attendue
         * @return Flux CDR contenant les valeurs de retour de la méthode
         * @throws UnexpectedMessageTypeException Le message reçu du serveur n'est pas de type Reply
         * @throws InvalidRequestIdException L'Identifiant de la requête dans la réponse ne correspond pas à celui de la requête
         * @throws NonCORBAMessageException Message reçu incohérent
         * @throws InvalidVersionNumberException Version GIOP incorrecte. Seule la version 1.0 est acceptée.
         * @throws InvalidMessageSizeException Taille du message invalide - Non Implémentée
         * @throws UnknownHostException Impossible de se connecter à l'hôte
         */
	public org.sorbet.CORBA.portable.InputStream send( String operation, org.sorbet.CORBA.portable.OutputStream os, boolean responseExpected) throws UnexpectedMessageTypeException,InvalidRequestIdException,NonCORBAMessageException, InvalidVersionNumberException, InvalidMessageTypeException, InvalidMessageSizeException, UnknownHostException 
	{		 
		// Génération d'un requesting_principal bidon car déprécié 
		byte[] requesting_principal={0};
		
                try{
                    if(ThreadPolicy==POA.SINGLE_THREAD&&!(_socket().isConnected()))
                        _socket().connect(new InetSocketAddress(distantHost().host(),distantHost().port()));
                }catch(IOException e){e.printStackTrace();}	
                
		// Génération du Request Header 
		// Le service sert a rien pour le moment 
		ServiceContext[] service_context=new ServiceContext[0]; 
		//example a conserver 
		//service_context[0]=new ServiceContext(1,(new String(""+qos.sem_invocation)).getBytes()); 
 
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
		if (responseExpected==false)  
			return null; 
 
		 
//	org.sorbet.CORBA.portable.InputStream reponse_2=null; 
 
		byte[] buffer_reponse= new byte[12]; 
		buffer_reponse = getMessage(); 
		 
		org.sorbet.CORBA.portable.InputStream reponse = new org.sorbet.CORBA.portable.InputStream(buffer_reponse,false); 
		MessageHeader_1_0 message_header2 = reponse.read_Message_Header(); 
		if (message_header2.message_type() != MessageHeader_1_0.Reply) { 
			throw (new UnexpectedMessageTypeException()); 
		} 
 
		ReplyHeader_1_0 reponse_2_entete=reponse.read_ReplyHeader_1_0(); 
		int request_id_reponse=reponse_2_entete.request_id(); 
		if (request_id_reponse!=request_id) { 
			throw new InvalidRequestIdException(); 
		}		 

		request_id++;

		return reponse;   
		 
	} 
 
    /** 
     * Envoye un buffer à l'ORB distant. 
     * @param message Buffer à envoyer 
     */ 
	public void sendMessage( byte[] message ) 
	{ 
		try 
		{ 
			_outputStream.write( message, 0, message.length ); 
			_outputStream.flush(); 
			//System.out.println( "[Sending] " + (new String(message)) ); 
		} 
		catch( Exception ex ) 
		{ 
			System.err.println( "Error Connexion.sendMessage():" + ex.getMessage() ); 
			ex.printStackTrace(); 
		} 
	} 
 
        /** 
         * Envoi d'un message sous forme d'un tableau de caractères 
         * @param message Message à envoyer 
         */ 
	public void sendMessage( char[] message ) 
	{ 
		try 
		{ 
			_outputStreamWriter.write( message, 0, message.length ); 
			_outputStreamWriter.flush(); 
		} 
		catch( Exception ex ) 
		{ 
			System.err.println( "Error Connexion.sendMessage():" + ex.getMessage() ); 
			ex.printStackTrace(); 
		} 
	} 
 
        /** 
         * Envoi d'une chaîne de caractères 
         * @param message Chaîne de caractères à envoyer 
         */ 
	public void sendStringMessage( String message ) 
	{ 
		try 
		{ 
			_stringWriter.write( message ); 
			_stringWriter.flush(); 
		} 
		catch( Exception ex ) 
		{ 
			System.err.println( "Error Connexion.sendStringMessage:" + ex.getMessage() ); 
			ex.printStackTrace(); 
		} 
	} 
	/** 
	 *Cette fonction a du être surcharger pour initialiser les Streams en plus 
	 *si le Handle était null avant l'appel 
	 **/ 
	 
	public void setPeerHandle(Handle H)
	{
		if(peerH==null)
		{	
			peerH=H;
			initSocket();	
		}else 
		{
		    peerH=H;
		    if(peerH!=null)
			initSocket();
		}
	}
	public void initSocket()
	{
		setStreams(_socket());
	}
    
     /** 
     * Constructeur 
     * @param distantHostName Nom de l'hôte distant 
     * @param port Numéro du port à utiliser sur l'hôte distant 
     */ 
    protected Connexion( String distantHostName, int port ) 
    {            
     	_distantHost = new ListenPoint( distantHostName, port ); 
    } 
     
    /** 
     * Constructeur 
     * @param distantHostName Nom de l'hôte distant 
     * @param port Numéro du port à utiliser sur l'hôte distant 
     * @param object_key Identifiant de l'objet à contacter 
     */ 
    protected Connexion( String distantHostName, int port, byte[] object_key ) 
    {
        _distantHost = new ListenPoint( distantHostName, port ); 
        this.object_key=object_key;
        initiationDispatcher=ORBLoader.init().initiationDispatcher();
    } 

    /** 
     * Constructeur 
     * @param distantHostName Nom de l'hôte distant 
     * @param port Numéro du port à utiliser sur l'hôte distant 
     * @param object_key Identifiant de l'objet à contacter 
     * @param thread is a thread
     * @param ID current InitiationDispatcher
     
     */ 
    protected Connexion( String distantHostName, int port, byte[] object_key,boolean thread,InitiationDispatcher ID )
    {
        super(ID,thread);
	_distantHost = new ListenPoint( distantHostName, port );
        this.object_key=object_key;
    } 
     
    /** 
     * Constructeur 
     * @param Handle à utiliser pour la connexion 
     * @param distantHostName Nom de l'hôte à contacter 
     * @param port Numéro du port à utiliser sur l'hôte distant 
     */ 
    protected Connexion(Handle H, String distantHostName, int port ) 
    { 
         
        _distantHost = new ListenPoint( distantHostName, port ); 
        this.setPeerHandle(H);
    } 
     
    protected Connexion(InitiationDispatcher ID,Handle H , String distantHostName, int port ) 
    { 
        super(ID,H); 
        _distantHost = new ListenPoint( distantHostName, port ); 
        } 
    protected Connexion(InitiationDispatcher ID,Handle H) 
    { 
        super(ID,H); 
      } 
    protected Connexion(InitiationDispatcher ID,Handle H,boolean T) 
    { 
        super(ID,H,T); 
     } 
    /** 
     *Permet de finir la connexion 
  	 **/ 
    public void close(){}

    public void set_thread_policy(byte p)
    {
        ThreadPolicy=p;
    }
    public byte get_thread_policy()
    {
        return ThreadPolicy;
    }
}
