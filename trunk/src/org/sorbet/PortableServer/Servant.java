package org.sorbet.PortableServer;

/**
 * classe mère de tous les skeletons  
 * @author	Equipe Stub&Skeleton 
 * @version	2.5
 */
public abstract class Servant
{ 
 
	public org.sorbet.CORBA.ORB _orb; 
	 
	/** 
	 * Constructeur 
	 */ 
	public void Delegate( org.sorbet.CORBA.ORB orb ) 
	{ 
		_orb = orb; 
	} 
 
	/** 
	 * Retourne le POA (RootPOA) 
	 */ 
	public POA default_POA( Servant self ){ 
 
        try 
        { 
            return org.sorbet.PortableServer.POAHelper.narrow( _orb.resolve_initial_references("RootPOA") ); 
        } 
        catch (org.sorbet.CORBA.InvalidName e) 
        { 
            e.printStackTrace(); 
            return null; 
        } 
	} 
    public abstract org.sorbet.CORBA.portable.OutputStream _invoke(String operation, org.sorbet.CORBA.portable.InputStream input,org.sorbet.CORBA.portable.ResponseHandler $rh); 
 
	public org.sorbet.CORBA.Object _this_object(org.sorbet.CORBA.ORB orb) 
	{ 
		return (org.sorbet.CORBA.Object)this; 
	} 
	public org.sorbet.CORBA.Object _this_object() 
	{ 
		return (org.sorbet.CORBA.Object)this; 
	} 
    public void set_thread_policy(byte p) 
    { 
        thread_policy=p; 
    } 
    public byte get_thread_policy() 
    { 
        return thread_policy; 
    } 
    byte thread_policy; 
}
