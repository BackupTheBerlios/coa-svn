package org.sorbet.CORBA; 
 
import java.util.Hashtable;

import org.sorbet.CORBA.GIOP.RequestHeader_1_0;
import org.sorbet.CORBA.IIOP.Connexion;
import org.sorbet.CORBA.IIOP.ConnexionFactory;
import org.sorbet.CORBA.IIOP.Serveur;
import org.sorbet.CORBA.IIOP.threading.InitiationDispatcher;
import org.sorbet.CORBA.IOP.IOR;
import org.sorbet.CORBA.IOP.ServiceContext;
import org.sorbet.CORBA.IOP.TaggedProfile;
import org.sorbet.PortableServer.POA;
/**
* Cette classe fournit toutes les opérations et structures relatives à l'ORB
* @author Equipe ORB&POA&Millo Jean-vivien
* @version 2.5
*/
public class ORB extends ORBLoader
{

    private boolean monitor;
    private String ORBid = null;
    private Hashtable initRef = new Hashtable();
    private boolean orb_destroyed = false;
    private boolean orb_runs = false;
    private int port = 2048;
    private String host = null;

    private Serveur server = null;
    
    private String _op =null;
    private org.sorbet.CORBA.portable.InputStream _input=null;
    private int request_id;
    /**
     * Reference vers l'initiationDispatcher
     */
    private InitiationDispatcher ID;

    
    /**
     * Le constructeur de l'ORB
     *
     **/
    public ORB() 
    { 
        // affectation des références initiales 
	 
	ORBid = ""; 
	ID=new InitiationDispatcher(); 
	    ID.init();
	try 
	    { 
	        this.host = java.net.InetAddress.getLocalHost().getHostName();
		 
	    } 
	catch(java.net.UnknownHostException e){e.printStackTrace();}

    
	org.sorbet.PortableServer.POA rootPOA = new org.sorbet.PortableServer.POAImpl( this ); 
    initRef.put("RootPOA",rootPOA); 
	 
    } 
 
/** 
* Le constructeur de l'ORB avec des paramètres 
* 
* @param args la liste des arguments 
* @param id l'identifiant de l'ORB 
*/ 
	// args du type -ORBsuffixValue 
    public ORB(String[] args,String id) 
    { 
		Parser parser = new Parser(); 
	ID=new InitiationDispatcher(); 
 
        // si id est passé en parametres, on l'affecte 
        if (id != null) 
		    ORBid = id; 
        // sinon on le cherche dans args 
        else 
        { 
			ORBid = parser.get_ORBid(args); 
            if (ORBid == null) 
				ORBid = ""; 
        } 
 
		try 
        { 
			java.net.InetAddress host = java.net.InetAddress.getLocalHost(); 
        } 
		catch(java.net.UnknownHostException e) 
        { 
            e.printStackTrace(); 
        } 
 
		// affectation des références initiales 
		org.sorbet.PortableServer.POA rootPOA = new org.sorbet.PortableServer.POAImpl( this ); 
        initRef.put("RootPOA",rootPOA); 
 
        // affectation des references initiales personnalisées 
		Hashtable temp = parser.get_initial_references(args); 
 
        //Copier les valeurs de t ds initRef (+ verification ) 
        if (temp.containsKey("RootPOA")) 
            initRef.put("RootPOA",temp.get("RootPOA")); 
	} 
 
/** 
* Retourne l'identifiant de l'ORB 
* 
* @return L'identifiant de l'ORB 
*/ 
    public String id(){ 
        return ORBid; 
    } 
/** 
* Retourne l'initiationDispatcher de l'ORB 
* 
* @return L'initiationDispatcher de l'ORB 
*/ 
    public InitiationDispatcher initiationDispatcher(){ 
        return ID; 
    }
    public void initiationDispatcher(InitiationDispatcher _initiationDispatcher){ID=_initiationDispatcher;}
 
/** 
* Retourne un tableau contenant les références initiales 
* 
* @return la liste des références initiales 
*/ 
    public String[] list_initial_services(){ 
		String[] initRefArray = new String[initRef.size()]; 
        int i = 0; 
		for (java.util.Enumeration e = initRef.keys() ; e.hasMoreElements() ;) { 
	         initRefArray[i] = e.nextElement().toString(); 
             i++; 
	    } 
        return initRefArray; 
    } 
 
/** 
* Retourne une références initiales 
* 
* @param Id L'identifiant de la référence 
* @return La référence initiale 
*/ 
    public org.sorbet.CORBA.Object resolve_initial_references(String Id) 
    throws InvalidName 
    { 
        if (initRef.containsKey(Id)) 
            return (org.sorbet.CORBA.Object)initRef.get(Id); 
        else 
            throw new InvalidName(); 
    } 
 
/** 
* Convertit un objet en String 
* 
* @param o L'objet CORBA 
* @return La chaine de l'objet transformé 
*/ 
    public String object_to_string(org.sorbet.CORBA.Object o) 
    { 
         
        byte[] objectKey = o.getConnexion().object_key(); 
 
        // Creation de l'IOR 
         
        IOR ior = new IOR(2); 
        //utilisable que quand on a qu'un seul POA (rootPOA) 
        //sinon il faut extraire le bon POA du parametre 
        byte[] threadpolicy={((POA)this.resolve_initial_references("RootPOA")).get_thread_policy()}; 
        ior.set_second_Profile(TaggedProfile.TAG_THREAD_POLICY,threadpolicy); 
         
        org.sorbet.CORBA.portable.OutputStream cdrOutputStream = new org.sorbet.CORBA.portable.OutputStream(false); 
        cdrOutputStream.write_IOR(ior, objectKey,threadpolicy); 
        String stringified_ior = cdrOutputStream.get_donnees_hexa(); 
        return stringified_ior;	 
         
    } 
 
/** 
* Transforme une String(IOR) en objet CORBA 
* 
* @param stringIOR la chaine IOR 
* @return L'objet CORBA 
*/ 
    public org.sorbet.CORBA.Object string_to_object(String stringIOR) 
    { 
	Connexion connexion = null; 
        try 
        { 
	    connexion = ConnexionFactory.createConnexionClient(stringIOR); 
        } catch (Exception e) { e.printStackTrace(); } 
         
        //System.out.println("connexion="+connexion); 
         
        org.sorbet.CORBA.portable.ObjectImpl obj = new org.sorbet.CORBA.portable.ObjectImpl(connexion);//,ID); 
	 
        return obj;  
    } 
 
/** 
* Démarre l'ORB. Cette opération est bloquante, le serveur se met en attente de requêtes 
* 
*/ 
    public void run() 
	{ 
	    if(orb_runs)
		return;  
	    orb_runs = true; 
	    
	    // on démarre le serveur IIOP 
	    //pour le moment, on a qu'un seul POA et donc qu'une seule politique
	    //quand on aura plusieurs POA, il faudra lancer plusieurs serveur. Attetion, ils se partagent la socket.
	    Serveur server = new org.sorbet.CORBA.IIOP.Serveur(port,0,ID,((POA)this.resolve_initial_references("RootPOA")).get_thread_policy(),this); 
	    server.run(); 
	} 
/** 
* Utilisée par GIOP pour transmettre une requete à l'ORB. 
* Effectue le traitement de la requête 
* 
* @param input Le flux d'entrée passé par GIOP à l'ORB 
* @return Le flux de sortie passé de l'ORB à GIOP 
*/ 
	// On recoit une requete 
 
    public org.sorbet.CORBA.portable.OutputStream postRequest(org.sorbet.CORBA.portable.InputStream input) 
    { 
        if (orb_runs) 
            { 
		RequestHeader_1_0 request = input.read_RequestHeader_1_0(); 
                ServiceContext serviceContext[] = request.service_context(); 
	        int request_id = request.request_id(); 
	        boolean response_expected = request.response_expected(); 
	    	byte[] ObjKey = request.object_key(); 
	        String op = request.operation(); 
	        byte[] requesting_principal = request.requesting_principal(); 
                ObjectKeyParser okp= new ObjectKeyParser(ObjKey); 
                byte[] OID = okp.getOID(); 
                String poa_name = okp.getPOA(); 
	        // trouver le poa et recuperer le servant 
                org.sorbet.CORBA.Object rootPOA = null; 
                org.sorbet.PortableServer.POA poa = null; 
                org.sorbet.PortableServer.Servant servant = null; 
                 
	        try 
	        { 
	        	rootPOA = this.resolve_initial_references("RootPOA"); 
	        } 
	        catch(org.sorbet.CORBA.InvalidName e){e.printStackTrace();} 
	        try 
	        { 
		        poa = ((org.sorbet.PortableServer.POA)rootPOA).find_POA(poa_name,true); 
	       		servant = poa.id_to_servant(OID); 
	        } 
	        catch(Exception e){e.printStackTrace();} 
	 
	        org.sorbet.CORBA.portable.ResponseHandlerImpl RH=new org.sorbet.CORBA.portable.ResponseHandlerImpl(); 
			org.sorbet.CORBA.portable.OutputStream output = servant._invoke(op,input.read_CDRInputStream(),RH); 
	        return output; 
        } 
        else 
            return null; 
	} 
 
 
/** 
* Retourne le port de la requête 
* 
* @return Le port 
*/ 
    public int getPort() 
    { 
        return port; 
    } 
 
/** 
* Retourne le nom de la machine host 
* 
* @return le nom de la machine 
*/ 
    public String getHost() 
    { 
        return host; 
    } 
 
/** 
* Utilisée par POA.activate_object() pour créer un corba.object 
* Créer une connexion en utilisant le port, l'host et l'objectKey 
* 
* @param poa_name le nom du POA 
* @param iod L'OID 
* @return La connexion 
*/ 
// utilisé par poa.activate_object pour créer un corba.object 
    public Connexion createConnexion( String poa_name, byte[] oid ) 
    { 
        byte[] objectkey = (new ObjectKeyParser(poa_name,oid)).getObjectKey(); 
		return ConnexionFactory.createConnexionClient( this.getHost(), this.getPort(), objectkey ); 
    } 
 
/** 
* Stop l'ORB : détruit le POA et rend les méthodes de traitement des requêtes inactives 
* 
* @param wait_for_completion Indique si l'on attend la fin du traitement des requêtes en cours 
*/ 
    public void shutdown(boolean wait_for_completion) 
    { 
		 
        org.sorbet.CORBA.Object rootPOA = null; 
        try 
        { 
        	rootPOA = this.resolve_initial_references("RootPOA"); 
        } 
        catch(org.sorbet.CORBA.InvalidName e){e.printStackTrace();} 
 
        ((org.sorbet.PortableServer.POA)rootPOA).destroy(true,wait_for_completion); 
        if(ID!=null) 
        	ID.kill(); 
    }
    /**@return l'id de la requete*/
    public int request_id(){return request_id;} 
    /**
     * trouve un servant à partir des information contenu dans l'input stream issu d'une requete du client
     * @param input flux d'entree
     *@return servant demande dans le flux d'entree
     **/
    public org.sorbet.PortableServer.Servant _servant(org.sorbet.CORBA.portable.InputStream input) 
	{ 
	    RequestHeader_1_0 request = input.read_RequestHeader_1_0(); 
	    
	    boolean response_expected = request.response_expected(); 
	    byte[] ObjKey = request.object_key(); 
	    String op = request.operation(); 
	    request_id=request.request_id(); 
	    byte[] requesting_principal = request.requesting_principal(); 
	    
	    ObjectKeyParser okp= new ObjectKeyParser(ObjKey); 
	    byte[] OID = okp.getOID(); 
	    String poa_name = okp.getPOA(); 
	    
	    // trouver le poa et recuperer le servant 
	    org.sorbet.CORBA.Object rootPOA = null; 
	    org.sorbet.PortableServer.POA poa = null; 
	    org.sorbet.PortableServer.Servant servant = null; 
	    
	    try 
	    { 
		rootPOA = this.resolve_initial_references("RootPOA"); 
	    } 
	    catch(org.sorbet.CORBA.InvalidName e){e.printStackTrace();} 
	    try 
	    { 
		poa = ((org.sorbet.PortableServer.POA)rootPOA).find_POA(poa_name,true); 
		servant = poa.id_to_servant(OID); 
	    } 
	    catch(Exception e){e.printStackTrace();} 
	    
	    _op=op; 
	    _input=input; 
	    // transmettre la requete au servant 
	    return servant; 
	} 
    /** 
     *@return l'operation de la prochaine requete
     **/	 
    public String operation(){ 
		return _op; 
    } 
    /**
     *@return le buffer d'entree
     **/
    public org.sorbet.CORBA.portable.InputStream input() { 
        return _input; 
    } 
    /**
     * toutes ces methodes sont vides et ne sont utile que à la compatibilité IDLJ
     **/
	public TypeCode create_abstract_interface_tc(String id, String name){throw new non_implante_Exception( "La fonction ou le service semandé n'est pas implanté" );} 
	public TypeCode create_alias_tc(String id, String name,TypeCode original_type){throw new non_implante_Exception( "La fonction ou le service semandé n'est pas implanté" );} 
	public TypeCode create_array_tc(int length, TypeCode element_type){throw new non_implante_Exception( "La fonction ou le service semandé n'est pas implanté" );} 
	public TypeCode create_enum_tc(String id, String name, String[] members){throw new non_implante_Exception( "La fonction ou le service semandé n'est pas implanté" ); } 
	public TypeCode create_exception_tc(String id, String name, StructMember[] members){throw new non_implante_Exception( "La fonction ou le service semandé n'est pas implanté" );} 
	public TypeCode create_fixed_tc(short digits, short scale){throw new non_implante_Exception( "La fonction ou le service semandé n'est pas implanté" );} 
	public TypeCode create_interface_tc(String id, String name){throw new non_implante_Exception( "La fonction ou le service semandé n'est pas implanté" );} 
	public TypeCode create_native_tc(String id, String name){throw new non_implante_Exception( "La fonction ou le service semandé n'est pas implanté" );} 
	public TypeCode create_recursive_sequence_tc(int bound, int offset){throw new non_implante_Exception( "La fonction ou le service semandé n'est pas implanté" );} 
	public TypeCode create_recursive_tc(String id){throw new non_implante_Exception( "La fonction ou le service semandé n'est pas implanté" ); } 
	public TypeCode create_sequence_tc(int bound, TypeCode element_type){throw new non_implante_Exception( "La fonction ou le service semandé n'est pas implanté" );} 
	public TypeCode create_string_tc(int bound){throw new non_implante_Exception( "La fonction ou le service semandé n'est pas implanté" );} 
	public TypeCode create_struct_tc(String id, String name, StructMember[] members){throw new non_implante_Exception( "La fonction ou le service semandé n'est pas implanté" );} 
	public TypeCode create_union_tc(String id, String name, TypeCode discriminator_type, UnionMember[] members){throw new non_implante_Exception( "La fonction ou le service semandé n'est pas implanté" ); } 
	public TypeCode create_value_box_tc(String id, String name, TypeCode boxed_type){throw new non_implante_Exception( "La fonction ou le service semandé n'est pas implanté" );} 
	public TypeCode create_value_tc(String id, String name, short type_modifier,TypeCode concrete_base, ValueMember[] members){throw new non_implante_Exception( "La fonction ou le service semandé n'est pas implanté" ); } 
	public TypeCode create_wstring_tc(int bound){throw new non_implante_Exception( "La fonction ou le service semandé n'est pas implanté" );}  
 
}
