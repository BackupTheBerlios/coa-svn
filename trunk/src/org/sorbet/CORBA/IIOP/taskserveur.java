package org.sorbet.CORBA.IIOP; 
 
import org.sorbet.CORBA.*; 
import org.sorbet.CORBA.GIOP.*; 
import org.sorbet.PortableServer.*; 
import org.sorbet.CORBA.IOP.*; 
/** 
 * C'est lui qui execute effectivement le code du servant ecrit par l'utilisateur
 * @author Millo Jean-Vivien
 * @version 2.5
 **/ 
public class taskserveur extends Thread 
{ 
	private Servant _servant=null; 
    private ConnexionServeur _cs = null; 
	private String _op = null; 
	private int _request_id; 
	private org.sorbet.CORBA.portable.InputStream _input = null; 
	private boolean inUse=false; 
 
    public taskserveur(Servant servant) 
    { 
        _servant = servant; 
    } 
	/** 
	 *constructeur 
	 *@param servant reference vers le servant donc on va executer la fonction 
	 *@param reference vers l'objet connexion serveur qui l'appel  
	 *@param opération a réaliser 
	 *@param Data 
	 *@param identifiant de la requête 
	 **/ 
	public taskserveur(Servant servant,ConnexionServeur cs, String op,org.sorbet.CORBA.portable.InputStream input,int request_id) 
	{ 
		_servant = servant; 
        _cs = cs; 
		_op = op; 
		_input = input; 
		_request_id=request_id; 
	} 
	/** 
	 *complète le constructeur sans paramètre 
	 *@param cs vers l'objet connexion serveur qui l'appel  
	 *@param op operation a realiser 
	 *@param input
	 *@param request_id Id de la requete 
	 **/ 
	public void parametrer(ConnexionServeur cs, String op,org.sorbet.CORBA.portable.InputStream input,int request_id) 
	{ 
		_cs = cs; 
		_op = op; 
		_input = input; 
		_request_id=request_id; 
	} 
     
    boolean alive=true; 
    public void alive(boolean a){alive=a;} 
	/** 
	 *Fonction réalisant l'éxecution de la requête du client 
	 **/ 
	public synchronized void run()
	{
		while(alive)
		{
		    inUse=true;
		    
		    org.sorbet.CORBA.portable.ResponseHandlerImpl RH=new org.sorbet.CORBA.portable.ResponseHandlerImpl();
			org.sorbet.CORBA.portable.OutputStream result = _servant._invoke(_op,_input,RH);
	                
			_input.set_position(0);
	                
			MessageHeader_1_0 header_reponse=new MessageHeader_1_0(false,MessageHeader_1_0.Reply,0);
			org.sorbet.CORBA.portable.OutputStream flux_final=new org.sorbet.CORBA.portable.OutputStream(false);
	            
		    ReplyHeader_1_0 reponse_header_reponse=new ReplyHeader_1_0(new ServiceContext[0],_request_id,ReplyHeader_1_0.NO_EXCEPTION);
		    flux_final.write_Message_Header(header_reponse);
		    flux_final.write_Header(reponse_header_reponse);
		    flux_final.write_CDROutputStream(result);
			
			_cs.sendMessage(flux_final.get_donnees());
			   
			inUse=false;
			_cs.Stop();
			if(_cs.ThreadPolicy==org.sorbet.PortableServer.POA.THREAD_PER_SERVANT)
				try{wait();}catch(InterruptedException e){e.printStackTrace();}
			else alive=false;
		}
	} 
	/** 
	 *permet de ligerer ou de prendre le monopole de la classe 
	 **/ 
	public boolean inUse(){return inUse;} 
	boolean start=false; 
	public synchronized boolean isStart(){return start;} 
	public synchronized void Notify(){notify();} 
}
