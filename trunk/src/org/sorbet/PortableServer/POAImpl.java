package org.sorbet.PortableServer; 
/**Sorbet version 2.5*/ 
 
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.*; 
import org.sorbet.CORBA.Object; 
import org.sorbet.CORBA.portable.ObjectImpl; 
import org.sorbet.CORBA.SystemException; 
import org.sorbet.CORBA.ORB; 
import org.sorbet.CORBA.IIOP.Connexion;

import fr.umlv.coa.CardApplet;
import fr.umlv.coa.CardProxyHandler;
 
/** 
* Cette classe fournit toutes les opérations et structures relatives au POA 
* @author Equipe ORB&POA
* @version 2.5
*/ 
public class POAImpl implements POA, org.sorbet.CORBA.Object 
{ 
	private Hashtable idtoServant; 
     
    private int compteurCourant; 
    private String the_name = ""; 
    private POA the_parent = null; 
    private POAManager the_POAManager = null; 
    private ORB orb = null; 
    private boolean POA_runs = false; 
 
    /** 
     *Politique de thread sur ce POA 
     **/ 
    private byte thread_policy=POA.SINGLE_THREAD; 
 
/** 
* Le constructeur de l'ORB sans paramètre 
*/ 
    public POAImpl() 
    { 
        this.the_name = "RootPOA"; 
        this.the_parent = null; 
        this.the_POAManager = null; 
        idtoServant = new Hashtable(); 
         
        compteurCourant = 0; 
        POAHelper.setPOA(this); 
        this.POA_runs = true; 
    } 
/** 
* Le constructeur de l'ORB avec paramètre 
* @param orb ORB qui a lancé ce POA 
*/ 
     public POAImpl(ORB orb) 
    { 
        this.the_name = "RootPOA"; 
        this.the_parent = null; 
        this.the_POAManager = null; 
        idtoServant = new Hashtable(); 
        this.orb = orb; 
 
        compteurCourant = 0; 
        POAHelper.setPOA(this); 
        this.POA_runs = true; 
    } 
 
/** 
* méthode non-implémentée 
*/ 
    public POA create_POA(String adapter_name, POAManager a_POAManager, PolicyList policies) 
        throws AdapterAlreadyExists, InvalidPolicy 
        { 
            // Write your code here 
            return null; 
    } 
 
/** 
* méthode non-implémentée 
*/ 
    public POA find_POA(String adapter_name, boolean activate_it) throws AdapterNonExistent 
    { 
        return ((POA)this); 
    } 
 
/** 
* méthode non-implémentée 
*/ 
    public void destroy(boolean etherealize_objects, boolean wait_for_completion) 
    { 
        idtoServant = null; 
        this.POA_runs = false; 
    } 
/** 
* connecte un objet sur le bus CORBA 
* insert la référence de l"objet Servant dans la table ActiveMap 
* @param  p_servant le servant 
* @return l'oid généré 
*/ 
    public byte[] activate_object(Servant p_servant) throws ServantAlreadyActive, WrongPolicy 
    { 
		Servant tmp = p_servant;
		
		System.out.println ("ACTIVATE OBJECT");
		
		if (tmp instanceof CardApplet && ((CardApplet) tmp).isOnCard ())
		{
			System.out.println ("CARD APPLET");
			
			InvocationHandler handler = new CardProxyHandler (); 
			
			// Create Remote Proxy
			//tmp = (Servant) Proxy.newProxyInstance (tmp.getClass ().getClassLoader (), new Class [] {tmp.getClass ().getInterfaces () [0]}, handler);
		}

    	
    	
    	
    	byte[] oid = getCompteur(); 
    	tmp.set_thread_policy(get_thread_policy()); 
/*	Combine : on enregistre oid sous forme de string */ 
        idtoServant.put(new String(oid), tmp); 
 
        return oid; 
    } 
 
/** 
* méthode non-implémentée 
*/ 
    public void activate_object_with_id(ObjectId id, Servant p_servant) throws ServantAlreadyActive, 
        WrongPolicy 
        { 
            // test si la policy IdAssignment est positionnée à USER_ID 
             
/*          p_servant.set_thread_policy(get_thread_policy()); 
            if (valueAssignmentPolicy.SYSTEM_ID) 
            { 
                throw( WrongPolicy); 
            } 
            else 
            { 
                // test si l'id déja présent 
                if (!(OidtoServant.containsKey(id))) 
                { 
                    OidtoServant.put(id, p_servant); 
                    return oid; 
                } 
                else // --> générer exception 
                { 
                    throw( ServantAlreadyActive); // pas sur de ca 
                } 
            } 
*/ 
    } 
 
/** 
* méthode non-implémentée 
*/ 
    public void deactivate_object(ObjectId oid) throws  WrongPolicy 
    { 
        // ca n'arrive pas 
    } 
 
 
/** 
* méthode non-implémentée 
*/ 
    public Object create_reference() throws WrongPolicy 
    { 
/* 
 inutile ?? 
            //        org.sorbet.CORBA.Object monObjet = new org.sorbet.CORBA.Object(); 
            String ret = ""; 
            java.util.Calendar cal = java.util.Calendar.getInstance(); 
            String ts = cal.getTime().getTime().toString(); 
            //		String oid = getCompteur().toString() ; 
            // Write your code here 
            return ts + "/" + this.the_name + "/" + oid; 
*/ 
        return null; 
    } 
 
/** 
* méthode non-implémentée 
*/ 
    public Object create_reference_with_id(ObjectId oid) throws WrongPolicy 
    { 
/* inutile ?? 
            return create_reference(); 
*/ 
 
        return null; 
    } 
 
/** 
* méthode non-implémentée 
*/ 
    public byte[] servant_to_id(Servant p_servant) throws ServantNotActive, WrongPolicy 
    { 
        // on n'en voit pas l'utilité 
		return null; 
    } 
 
/** 
* méthode non-implémentée 
*/ 
    public org.sorbet.CORBA.Object servant_to_reference(Servant p_servant) throws ServantNotActive, WrongPolicy 
    { 
        // on n'en voit pas l'utilité 
        return null; 
    } 
/** 
* fournit l'objet-servant correspondant à une référence 
* @param reference la référence 
* @return l'objet-servant 
*/ 
    public Servant reference_to_servant(Object reference) throws WrongAdapter, WrongPolicy,ObjectNotActive 
        /* il n'y a qu'un seul adapter donc le servant que l'on cherche ne peut se trouver dans 
   dans un autre adapter ! */ 
 
    { 
            Servant servantTemporaire = id_to_servant(reference_to_id(reference)); 
            if (servantTemporaire == null) throw new org.sorbet.PortableServer.ObjectNotActive(); 
            return servantTemporaire; 
  	} 
 
/** 
* fournit l'oid correspondant à une référence 
* @param reference la référence 
* @return l'oid 
*/ 
    public byte[] reference_to_id(Object reference) throws WrongPolicy 
    { 
        org.sorbet.CORBA.ObjectKeyParser okp = new org.sorbet.CORBA.ObjectKeyParser(reference.getConnexion().object_key()); 
		return okp.getOID(); 
    } 
 
/** 
* fournit le servant correspondant à un oid 
* @param oid oid 
* @return Servant 
*/ 
    public Servant id_to_servant(byte[] oid) throws  WrongPolicy 
    { 
		Servant temp=(Servant)idtoServant.get(new String(oid)); 
		if(temp==null) 
			throw  new WrongPolicy("Le Servant n'existe pa sur le serveur"); 
		return temp; 
    } 
 
/** 
* fournit l'objet-servant correspondant à un oid 
* @param oid oid 
* @return l'objet-servant 
*/ 
    public org.sorbet.CORBA.Object id_to_reference(byte[] oid) throws WrongPolicy 
    { 
		//construction d'un org.sorbet.corba.object 
//        byte[] objectkey = (oid.toString()+"/"+this.the_name).getBytes(); 
        byte[] temp = ("/"+this.the_name).getBytes(); 
        byte[] objectkey = new byte[oid.length+temp.length]; 
		java.lang.System.arraycopy(oid,0,objectkey,0,oid.length); 
		java.lang.System.arraycopy(temp,0,objectkey,oid.length,temp.length); 
/* 
        org.sorbet.CORBA.IIOP.Connexion connexion = org.sorbet.CORBA.IIOP.ConnexionFactory.createConnexionClient( orb.getHost(),orb.getPort(),objectkey); 
		org.sorbet.CORBA.Object obj = new org.sorbet.CORBA.portable.ObjectImpl ( connexion ); 
        return obj; 
*/ 
        org.sorbet.CORBA.IIOP.Connexion connexion = org.sorbet.CORBA.IIOP.ConnexionFactory.createConnexionClient( orb.getHost(),orb.getPort(),objectkey); 
		org.sorbet.CORBA.Object obj = new org.sorbet.CORBA.portable.ObjectImpl ( connexion ); 
        return obj; 
 
    } 
 
    private byte[] getCompteur() 
    { 
        compteurCourant++; 
		String str = new String(); 
        str = str.valueOf(compteurCourant); 
        return str.getBytes(); 
    } 
// pour corba.object 
	public String _ids(){ 
        // Write your code here 
        return null; 
    } 
	 public Connexion getConnexion(){ 
        // Write your code here 
        return null; 
    } 
    public boolean _is_a(java.lang.String S) 
    {return true;} 
 
    public void set_thread_policy(byte policy) throws org.sorbet.CORBA.portable.Invalide_Thread_Policy_Exception 
    { 
        if(policy<1||policy>4) 
            throw new org.sorbet.CORBA.portable.Invalide_Thread_Policy_Exception("le politique de gestion de thread n'est pas valide"); 
        else 
            thread_policy=policy; 
    } 
 
    public byte get_thread_policy(){return thread_policy;} 
} //fin de la classe POAImpl
