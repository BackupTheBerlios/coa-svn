package org.sorbet.CORBA; 
import java.util.Hashtable; 
//import util.*; 
/** 
* Cette classe fournit la m�thode static init permettant le chargement de l'ORB 
* @author Equipe ORB&POA 
* @version 2.5
*/ 
 
public class ORBLoader {
 
	private static ORB orb = null; 
	private static Hashtable orb_list = new Hashtable(); 
	/** 
    *	Charge l'ORB 
    * 
    * 	@return la r�f�rence de l'ORB 
    * 
    *	@throws org.sorbet.CORBA.INITIALIZE si un ORB a deja �t� initialis� 
    */ 
    public static ORB init() 
	{ 
		if (orb == null) 
		{ 
			orb = new ORB(); 
                        orb_list.put("",orb); 
		} 
	return orb; 
		 
	} 
 
    /** 
    *	Charge l'ORB en passant des param�tres et l'identifiant de l'ORB. 
    * 	Les param�tres reconnus sont : 
    * 	-ORBid Value : l'identifiant de l'ORB 
    *	-ORBInitRef ID=value : une r�f�rence initiale 
    *  
    *	@param String[] args, String id 
    * 
    * 	@return la r�f�rence de l'ORB 
    * 
    *	@throws org.sorbet.CORBA.INITIALIZE si un ORB a deja �t� initialis� 
    */ 
    /*public static ORB init(String[] args,String id) 
	{ 
		if (orb==null) 
		{ 
            if (id == null) 
            { 
                id = "default"; 
            } 
			orb = new ORB(args,id); 
            orb_list.put(id,orb); 
			return orb; 
		} 
		if (id != null) 
        { 
            if (orb_list.containsKey(id)) 
				return (ORB)orb_list.get(id); 
		} 
        else 
		{ 
			orb = new ORB(args,id); 
            orb_list.put(id,orb); 
			return orb; 
		} 
	}*/ 
	public static ORB init(String[] args,java.util.Properties Prosp) 
	{return orb;} 
 
}
