package org.sorbet.PortableServer;

import org.sorbet.CORBA.Object;
import org.sorbet.CORBA.portable.*;
/**
 *Definie les fonctions du POA
 *@author PortableServerTeam
 *@version 2.5
 */
public interface POA 
{ 
    void set_thread_policy(byte policy) throws Invalide_Thread_Policy_Exception; 
 
    byte get_thread_policy(); 
     
    POA create_POA(String adapter_name, POAManager a_POAManager, PolicyList policies) throws InvalidPolicy,AdapterAlreadyExists; 
 
    POA find_POA(String adapter_name, boolean activate_it) throws AdapterNonExistent; 
 
    void destroy(boolean etherealize_objects, boolean wait_for_completion); 
 
    byte[] activate_object(Servant p_servant) throws WrongPolicy,ServantAlreadyActive; 
 
    void activate_object_with_id(ObjectId id, Servant p_servant) throws WrongPolicy,ServantAlreadyActive; 
 
    void deactivate_object(ObjectId oid) throws WrongPolicy; 
 
    Object create_reference() throws WrongPolicy; 
 
    Object create_reference_with_id(ObjectId oid) throws WrongPolicy; 
 
    byte[] servant_to_id(Servant p_servant) throws WrongPolicy,ServantNotActive; 
 
    Object servant_to_reference(Servant p_servant) throws WrongPolicy,ServantNotActive; 
 
    Servant reference_to_servant(Object reference) throws WrongPolicy,WrongAdapter,ObjectNotActive; 
 
    byte[] reference_to_id(Object reference) throws WrongPolicy; 
 
    Servant id_to_servant(byte[] oid) throws WrongPolicy; 
 
    Object id_to_reference(byte[] oid) throws WrongPolicy; 
 
    String the_name = ""; 
 
    byte thread_policy=POA.SINGLE_THREAD; 
    POA the_parent = null; 
 
/*    POAManager the_POAManager = null;*/ 
     
    public final static byte NO_POLICY=0; 
    public final static byte SINGLE_THREAD=1; 
    public final static byte THREAD_PER_REQUEST=2; 
    public final static byte THREAD_PER_SERVANT=3; 
    public final static byte THREAD_PER_PROXY=4; 
     
}
