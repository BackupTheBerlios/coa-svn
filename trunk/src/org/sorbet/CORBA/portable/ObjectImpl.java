package org.sorbet.CORBA.portable;

/**
 * Cette classe implante ObjectImpl qui correspond au code proprietaire
 * pour l'implantation d'un objet CORBA
 *
 * @author	Millo Jean-Vivien
 * @version	2.5
 *
 **/
import org.sorbet.CORBA.IIOP.threading.*;
import org.sorbet.CORBA.IIOP.*;
import org.sorbet.CORBA.*;
import org.sorbet.CORBA.portable.*;
import org.sorbet.PortableServer.*;
import java.util.Vector;
import org.jacorb.orb.*;

public class ObjectImpl implements org.sorbet.CORBA.Object
{
    private static Vector ListProxy;
    /**
     * Reference sur le protocole de communication
     * + OID / POA
     */
    public org.sorbet.CORBA.IIOP.Connexion protocol;
    /**
     *Paramètre de la prochaine operaion a réaliser dans cas synchrone
     **/
    private String operation=null; 
    /**
     *Paramètre de la prochaine operaion a réaliser dans cas synchrone
     **/
     
    private boolean responseExpected;
    /**
     *Liste des objets Delegate afin de pouvoir acceder à la tache client
     * utilisé en asynchrone thread par proxy seulement
     **/
    
    private Vector listRequest=new Vector();
    /**
     * Liste les handles qui sont en attente de reponse
     * pour ne pas leur couper les vivres à l'appel de la fonction _close
     **/
    private Vector listHandler=new Vector();
    /**
     * @return la liste des handler de reponse
     **/
    public Vector getListHandler(){return listHandler;}
    /**id de l'objet*/
    String[] id={"IDL:ObjectImpl 1.0"};
    /**@return la tache cliente contenue dans l'un des objets Delegate stocké dans listRequest */
    public taskClient _getTaskClient()
    {
	if(listRequest.get(0)!=null)
	    return ((Delegate)listRequest.get(0)).getTS();
	else return null;
    }
     
    public ObjectImpl(){} 
     
    /** 
     * @param connexion contient l'object key 
     */ 
    public ObjectImpl(org.sorbet.CORBA.IIOP.Connexion connexion) 
    { 
	//System.out.println("connexionObjImpl="+protocol); 
	this.protocol=connexion; 
    } 
    /** 
     * @param connexion contient l'object key 
     * @param ID reference vers l'initiation dispatcher du client
     */ 
    public ObjectImpl(org.sorbet.CORBA.IIOP.Connexion connexion,InitiationDispatcher ID) 
    { 
//connector=new Connector(ID,connexion.distantHost().host(),connexion.distantHost().port(),connexion); 
	 
	//System.out.println("connexionObjImpl="+protocol); 
	this.protocol=connexion; 
    } 
     
    // Retourne le Repository ID 
    public String[] _ids(){ 
	return id;    
    } 
    /**@param id nouvelle id de l'objet*/
    public void setId(String[] id){this.id=id;} 
    /** 
     * Création d'une requête 
     * @param Operation	= le nom de la méthode 
     * @param responseExpected True si une réponse est attendue 
     * @return un Outputstream pour marshalliser les paramètres 
     */ 
     
    public org.sorbet.CORBA.portable.OutputStream _request(String operation, boolean responseExpected)
    {
	org.sorbet.CORBA.portable.OutputStream os = new org.sorbet.CORBA.portable.OutputStream(false);
	this.operation = operation;
	this.responseExpected = responseExpected;
        if(ListProxy==null)
            ListProxy=new Vector();
        if(!(ListProxy.contains(((java.lang.Object)this))))
           ListProxy.add(this);
	return os;
    }

	/**
	 * Invocation d'une méthode distante
	 *
	 * @param os = les paramètres marshallisés à destination de l'objet distant 
	 * @return les données marshallisées retournées par l'objet distant 
	 */

	public synchronized org.sorbet.CORBA.portable.InputStream _invoke(org.sorbet.CORBA.portable.OutputStream os) throws org.sorbet.CORBA.portable.ApplicationException,org.sorbet.CORBA.portable.RemarshalException 
	{
	   	if ( operation==null) 
		{ 
			//pour faire plaisir au stub 
			throw new org.sorbet.CORBA.portable.ApplicationException(); 
		} 
		if ( operation==null) 
		{ 
			//pour faire plaisir au stub 
			throw new org.sorbet.CORBA.portable.RemarshalException(); 
		} 
	    try  
            { 
	            byte Thread_Policy=((ConnexionClient)protocol).get_thread_policy();
                    if(Thread_Policy<1||Thread_Policy>4)
                        throw new Invalide_Thread_Policy_Exception("la politique de gestion de thread n'est pas valide");
                    if(Thread_Policy==POA.SINGLE_THREAD)
                        {
                            return ((ConnexionClient)protocol).send(operation,os,responseExpected,Thread_Policy);
                        }
                    else if(Thread_Policy==POA.THREAD_PER_REQUEST)
                        {
			    ConnexionClient temp=(ConnexionClient)ConnexionFactory.createConnexionClient(protocol.distantHost().host(), protocol.distantHost().port(), protocol.object_key(),true,null );
			    temp.setPeerHandle(new Handle(temp.distantHost()));
                            return temp.send(operation,os,responseExpected,Thread_Policy);
                        }else
			{
			    ConnexionClient temp=(ConnexionClient)ConnexionFactory.createConnexionClient(protocol.distantHost().host(), protocol.distantHost().port(), protocol.object_key(),false,null );
			    temp.setPeerHandle(new Handle(temp.distantHost()));
                            return temp.send(operation,os,responseExpected,Thread_Policy);
			}
            }
	    catch (Exception e)
	    {
                System.err.println(e.getMessage());
                e.printStackTrace();
                return null;
            }
        //System.out.println("sortie invoke"); 
	}
    /**@return l'objet connexion faisant le liens avec le serveur*/
        public org.sorbet.CORBA.IIOP.Connexion getConnexion() {return protocol;}
    /**test si l'objet est un @param id id */
    public boolean _is_a (String id) 
	{ 
	    //c'est de la triche mais sinon on est pas compatible IDLJ 
	    return _ids()[0].equals("IDL:ObjectImpl 1.0"); 
	} 
    
    public void _releaseReply (org.sorbet.CORBA.portable.InputStream $in) 
 	{} 
    /*
     * crée un objet Delegate qui lui même crée une connexion vers le serveur
     **@return l'objet Delegate
     **/
    public org.sorbet.CORBA.portable.Delegate _get_delegate()
	{
	Delegate temp=new org.sorbet.CORBA.portable.Delegate(protocol);
	listRequest.add(temp);
	return temp;
	}
    /**
     *Affect notre objet connexion a partir de celui de Delegate
     *@param delegate objet delegate
     **/
    public void _set_delegate(org.sorbet.CORBA.portable.Delegate delegate)
	{
	    protocol=delegate.getConnexion();
	    //transmission des carac temps reel souhaite (periode echeance)
	    //recup du port de connexion pour les requetes
	    
	    protocol.setPeerHandle(new Handle(protocol._socket()));
	    if(!(((ConnexionClient)protocol).get_thread_policy()==POA.SINGLE_THREAD))
	    {
            try{
                protocol.distantHost().port(((Integer)protocol.getHandle().read()).intValue());
            }catch(java.io.IOException e){e.printStackTrace();}
	    }
	}
    /**
     *Permet de fermer une connexion entre un proxy et le serveur
     * cette fonction attend que les reponses a destination de ce proxy arrivent. 
     **/
    public void _close()
    {
	while(!(listHandler.isEmpty()))
	{Thread.yield();}
        try
        {
            ((Delegate)listRequest.get(0)).getTS().kill();
            ((Delegate)listRequest.get(0)).getTS().Notify();
    	}
        catch(NullPointerException e){}
        catch(ArrayIndexOutOfBoundsException ee){}
        ((ConnexionClient)protocol).close();
        if((ListProxy.contains(((java.lang.Object)this))))
           ListProxy.remove(this);
        if(ListProxy.isEmpty())
            org.sorbet.CORBA.ORBLoader.init().initiationDispatcher().kill();
    }
    public org.sorbet.CORBA.portable.ServantObject _servant_preinvoke(String Operation, java.lang.Class _class )
	{
	    //il faut crer un objet servantObject contenant le bon servant
	    return null;
	}
    public void _servant_postinvoke(org.sorbet.CORBA.portable.ServantObject _so){}
    /**
     *@return si ne servant est local toujours false
     **/
    public boolean _is_local (){return false;}
    /**
     *@return la prochaine operation de la prochaine requete
     **/
    public String _get_operation(){return operation;}
    /**
     *@return la prochaine reponseExpected de la prochaine requete
     **/
    public boolean _get_responseExpected(){return responseExpected;}
    /**
     *@return la connexion de l'objet
     **/
    public Connexion _get_protocol(){return protocol;}
    /**
     *@return la politique de Thread associé au servant connecté a ce proxy
     **/
    public byte _get_thread_policy(){return protocol.get_thread_policy();}
}
