package org.sorbet.CORBA.IOP; 
 
/** 
 * Cette classe permet la gestion des ServiceContext<P> 
 * Classe issue de l'IDL fourni par l'sorbet.<P> 
 * @author Groupe IOP,IIOP,GIOP 
 * @version 2.5 
 */ 
 
public class ServiceContext 
{ 
	public final static int TransactionService		= 0; 
	public final static int CodeSets			= 1; 
	public final static int ChainBypassCheck		= 2; 
	public final static int ChainBypassInfo			= 3; 
	public final static int LogicalThreadId			= 4; 
	public final static int BI_DIR_IIOP			= 5; 
	public final static int SendingContextRunTime		= 6; 
	public final static int INVOCATION_POLICIES		= 7; 
	public final static int FORWARDED_IDENTITY		= 8; 
	public final static int UnknownExceptionInfo		= 9; 
	public final static int RTCorbaPriority			= 10; 
	public final static int RTCorbaPriorityRange		= 11; 
	public final static int ExceptionDetailMessage		= 14; 
	public final static int SecurityAttributeService	= 15; 
 
	int	_context_id; 
	byte[]	_context_data; 
	 
        /** 
         * Retourne l'Identifiant du contexte 
         * @return Identifiant du contexte 
         */ 
	public int context_id() 
	{ 
		return _context_id; 
	} 
	 
        /** 
         * Permet de modifier l'identifiant du contexte 
         * @param context_id Identifiant du contexte à utiliser 
         */ 
	public void context_id( int context_id ) 
	{ 
		_context_id = context_id; 
	} 
	 
        /** 
         * Retourne les données du contexte 
         * @return Données du contexte 
         */ 
	public byte[] context_data() 
	{ 
		return _context_data; 
	} 
	 
        /** 
         * Permet de modifier les données du contexte 
         * @param context_data Données du contexte à utiliser 
         */ 
	public void context_data( byte[] context_data ) 
	{ 
		_context_data = context_data; 
	} 
	 
        /** 
         * Constructeur 
         * @param context_id Identifiant du contexte à utiliser 
         * @param context_date Données du contexte à utiliser 
         */ 
	public ServiceContext( int context_id, byte[] context_data ) 
	{ 
		_context_id = context_id; 
		_context_data = context_data; 
	} 
}
