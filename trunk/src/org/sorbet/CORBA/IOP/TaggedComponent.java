
package org.sorbet.CORBA.IOP; 
 
/** 
 * Cette classe permet la gestion des TaggedComponent<P> 
 * Classe issue de l'IDL fourni par l'omg.<P> 
 * @author Groupe IOP,IIOP,GIOP 
 * @version 2.5
 */ 
 
public class TaggedComponent 
{ 
	public final static int TAG_ORB_TYPE			= 0; 
	public final static int TAG_CODE_SETS			= 1; 
	public final static int TAG_POLICIES			= 2;    
	public final static int TAG_ALTERNATE_IIOP_ADDRESS	= 3; 
	public final static int TAG_ASSOCIATION_OPTIONS		= 13; 
	public final static int TAG_SEC_NAME			= 14; 
	public final static int TAG_SPKM_1_SEC_MECH		= 15; 
	public final static int TAG_SPKM_2_SEC_MECH		= 16; 
	public final static int TAG_KerberosV5_SEC_MECH		= 17; 
	public final static int TAG_CSI_ECMA_Secret_SEC_MECH	= 18; 
	public final static int TAG_CSI_ECMA_Hybrid_SEC_MECH	= 19; 
	public final static int TAG_SSL_SEC_TRANS		= 20; 
	public final static int TAG_CSI_ECMA_Public_SEC_MECH	= 21; 
	public final static int TAG_GENERIC_SEC_MECH		= 22; 
	public final static int TAG_FIREWALL_TRANS		= 23; 
	public final static int TAG_SCCP_CONTACT_INFO		= 24; 
	public final static int TAG_JAVA_CODEBASE		= 25; 
 
	public final static int TAG_TRANSACTION_POLICY		= 26; 
	public final static int TAG_MESSAGE_ROUTERS		= 30; 
	public final static int TAG_OTS_POLICY			= 31; 
	public final static int TAG_INV_POLICY			= 32; 
	public final static int TAG_INET_SEC_TRANS		= 123; 
 
	public final static int TAG_COMPLETE_OBJECT_KEY		= 5; 
	public final static int TAG_ENDPOINT_ID_POSITION	= 6; 
	public final static int TAG_LOCATION_POLICY		= 12; 
	public final static int TAG_DCE_STRING_BINDING		= 100; 
	public final static int TAG_DCE_BINDING_NAME		= 101; 
	public final static int TAG_DCE_NO_PIPES		= 102; 
	public final static int TAG_DCE_SEC_MECH		= 103; // Security Service 
 
 
	int	_tag; 
	byte[]	_component_data; 
	 
        /**  
         * Retourne le TAG de ce composant 
         * @return TAG de ce composant 
         */ 
	int tag() 
	{ 
		return _tag; 
	} 
	 
        /** 
         * Permet de modifier le TAG de ce composant 
         * @param tag TAG à utiliser 
         */ 
	void tag( int tag ) 
	{ 
		_tag = tag; 
	} 
	 
        /** 
         * Retourne les données de ce composant 
         * @return Données de ce composant 
         */ 
	byte[] component_data() 
	{ 
		return _component_data; 
	} 
	 
        /** 
         * Permet de modifier les données de ce composant 
         * @param component_data Données à utiliser 
         */ 
	void component_data( byte[] component_data ) 
	{ 
		_component_data = component_data; 
	} 
	 
        /**  
         * Constructeur 
         * @param tag TAG à utiliser pour ce composant 
         * @param component_data Données à utiliser pour ce composant 
         */ 
	public TaggedComponent( int tag, byte[] component_data ) 
	{ 
		_tag = tag; 
		_component_data = component_data; 
	} 
}
