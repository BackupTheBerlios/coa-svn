package org.sorbet.CORBA.IIOP.threading; 
 
import java.net.Socket; 
import java.net.ServerSocket; 
import java.net.InetAddress; 
import java.net.SocketException; 
import java.io.IOException; 
import java.io.ObjectOutputStream; 
import java.io.ObjectInputStream; 
import org.sorbet.CORBA.IIOP.*; 
/** 
* Defines a Handle. 
* @author Millo Jean-Vivien from Alexandre Delarue
* @version 2.5
*/ 
public class Handle 
{ 
	/**Socket vers l'hôte distant*/ 
	private Socket socket = null; 
	/**ServerSocket d'ecoute de demande de connexion*/ 
	private ServerSocket serverSocket = null; 
	/**Socket special pour l'objet ConnexionAcceptor. pour entendre les messages de deconnexion*/
	private Socket Specialsocket = null;
	/**Permet de savoir si le Handle est en cours d'utilisation*/ 
	private boolean isInUse = false; 
	/**Permet de savoir si le Handle est en cours de fermeture*/ 
	private boolean isDying = false; 
	/** 
	* Creates a new Handle 
	* @param addr The Address associated with this new Handle. 
	*/ 
	public Handle(ListenPoint addr) throws IOException 
	{ 
        try { this.socket = new Socket(/*InetAddress.getByName(*/addr.host()/*)*/, addr.port()); } 
		catch(IOException ioe) {throw ioe;} 
	} 
	/** 
	* Creates a new Handle 
	* @param port The port (on local machine) associated with this new Handle. 
	*/ 
	public Handle(int port) 
	{ 
		try { this.serverSocket = new ServerSocket(port); } 
		catch(IOException ioe) { System.out.println(ioe.getMessage()); } 
	} 
	/** 
	* Creates a new Handle. 
	* @param s The socket corresponding to this new Handle. 
	*/ 
	public Handle(Socket s){this.socket = s;} 
	/** 
	* Wait for an event to occur on this Handle. 
	* @param timeout The maximum time to wait before considering that no event occurs on this Handle. 
	* @param id The Initiation Dispatcher that is handling the events. 
	*/ 
	public boolean waitForEvent(int timeout, InitiationDispatcher id) 
	{ 
		//System.out.println("*entrée waitForEvent : Handle"); 
		if (serverSocket == null) 
		// Socket based Handle 
		{
		    //System.out.println("socket");
			try
			{
				socket.setSoTimeout(timeout); 
				if (socket.getInputStream().available() > 0) 
                 	return true; 
               	else 
                	return false; 
			} 
			catch(SocketException se) { System.out.println("*"+se.getMessage()); return false; } 
			catch(IOException ioe) { System.out.println("*"+ioe.getMessage()); return false; } 
		} 
		else 
		// ServerSocket based Handle
		{
                    try
			{
                            if(Specialsocket != null)
                                {
                                    Specialsocket.setSoTimeout(timeout);
                                    if ((av=Specialsocket.getInputStream().available()) > 0)
                                        return true;
                                }		
                            serverSocket.setSoTimeout(timeout);
                            Socket s = serverSocket.accept(); 
                            socket=s; 
                            return true; 
			} 
                    catch(SocketException se) {return false; } 
                    catch(IOException ioe) { return false; } 
                             
		} 
	} 
    /** 
     * To read some data from this Handle. 
     * @param b The byte array used to store the read data. 
     * @throws IOException 
     */ 
	public void read(byte[] b) throws IOException 
	{ 
		if (socket != null) 
		{ 
			try { socket.getInputStream().read(b); } 
			catch(IOException ioe) { throw ioe; } 
		} 
		else b = null; 
	}  
    /** 
     * To read some data from this Special socket. 
     * @param b The byte array used to store the read data. 
     * @throws IOException 
     */ 
	public void readSpecial(byte[] b) throws IOException 
	{ 
		if (Specialsocket != null) 
		{ 
			try { Specialsocket.getInputStream().read(b); } 
			catch(IOException ioe) { throw ioe; } 
		} 
		else b = null; 
	} 
	/** 
	* To read some data from this Handle. 
	* @param o The object used to store the read data. 
	* @throws IOException 
	*/ 
	public Object read() throws IOException 
	{ 
		if (socket != null) 
		{ 
			try 
			{ 
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream()); 
				return ois.readObject(); 
			} 
			catch(IOException ioe) { throw ioe; } 
			catch(ClassNotFoundException cnfe) { throw new IOException(cnfe.getMessage()); } 
		} 
		else return null; 
	} 
	/** 
	* To write some data to this Handle. 
	* @param b The byte array to send to this Handle. 
	* @throws IOException 
	*/ 
	public void write(byte[] b) throws IOException 
	{ 
		if (socket != null) 
		{ 
		try { socket.getOutputStream().write(b); } 
		catch(IOException ioe) { throw ioe; } 
		} 
	} 
	/** 
	* To write some data to this Handle. 
	* @param o The object to send to this Handle. 
	* @throws IOException 
	*/ 
	public void write(Object o) throws IOException 
	{ 
		if (socket != null)  
		{ 
			try 
			{ 
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream()); 
				oos.writeObject(o); 
			} 
			catch(IOException ioe) {throw ioe; } 
		} 
	} 
	/** 
	* Used to let the removeHandler method know that the InitiationDispatcher is using this Handle, 
	* so that we can prevent it from being killed while used. 
	* Call setInUse(false) to release the handle after use so that it can die if needed. 
	* @param b True to set this Handle to isInUse, false to release this Handle. 
	*/ 
	public void setInUse(boolean b){this.isInUse = b;} 
	/** 
	* Begin the process of removing the handle. No Initiation Dispatcher will be allowed to listen anymore on this handle. 
	*/ 
	public void kill() 
	{ 
		this.isDying = true;		 
	} 
	public boolean isDying(){return isDying;} 
	/** 
	* Wait for the InintiationDispatcher to end listening on the Handle if the kill method was called while the Handle was in use. 
	*/ 
	public void waitForDeath(){while (this.isInUse);} 
	/** 
	 * Cloture les sockets avant de mourir 
	 **/ 
	public void close() throws IOException 
	{ 
		if (socket != null) 
		{ 
			try{this.socket.close();} 
			catch(IOException ioe) { throw ioe; } 
			//System.out.println("Socket fermé"); 
		} 
		if (serverSocket != null) 
		{ 
			try{this.serverSocket.close();} 
			catch(IOException ioe) { throw ioe; } 
			//System.out.println("serverSocket fermé"); 
		} 
	} 
	/** 
	 *@return La Socket 
	 **/ 
	public Socket getSocket(){return socket;} 
	public void setSocket(Socket s){socket=s;} 
	public Socket getSpecialSocket(){return Specialsocket;} 
	public void setSpecialSocket(Socket s){Specialsocket=s;} 
	public ServerSocket getServeurSocket(){return serverSocket;} 
	public void setServerSocket(ServerSocket ss){serverSocket=ss;}
    int av=-1;
    public int getAvailable(){return av;}
}
