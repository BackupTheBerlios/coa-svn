package org.sorbet.CORBA; 
 
/** 
* Le parser de l'ObjectKey 
* @author Equipe ORB&POA 
* @version 2.5
*/ 
public class ObjectKeyParser 
{ 
	private byte[] ObjectKey = null; 
	private byte[] oid; 
    private String poa_name; 
 
/** 
* Le constructeur du parser 
* @param ObjKey L'objectKey 
*/ 
	public ObjectKeyParser(byte[] ObjKey) 
	{ 
        this.ObjectKey = ObjectKey; 
		String objectKey = new String(ObjKey); 
 
		int Index; 
		String res; 
 
        // getoid 
   		Index = objectKey.indexOf('/'); 
		res = objectKey.substring(0,Index); 
		oid = res.getBytes(); 
 
        //getpoa 
		Index = objectKey.indexOf('/'); 
		res = objectKey.substring(Index+1); 
		poa_name = res; 
 
	} 
 
/** 
* Le constructeur du parser 
* @param poa_name Le nom du POA 
* @param oid l'OID 
*/ 
    public ObjectKeyParser( String poa_name, byte[] oid ) 
    { 
		this.poa_name = poa_name; 
        this.oid = oid; 
		ObjectKey = (oid+"/"+poa_name).getBytes() ; 
    } 
 
/** 
* Renvoie l'OID 
* @return L'OID 
*/ 
	public byte[] getOID() 
	{ 
		return oid; 
	} 
 
/** 
* Renvoir le nom du POA 
* @return le POA 
*/ 
	public String getPOA() 
	{ 
		return poa_name; 
	} 
 
/** 
* Renvoir l'ObjectKey 
* @return l'ObjectKey 
*/ 
    public byte[] getObjectKey() 
    { 
		return ObjectKey; 
    } 
} 
	
