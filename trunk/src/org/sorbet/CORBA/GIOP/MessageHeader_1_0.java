package org.sorbet.CORBA.GIOP; 
 
/** 
 * Cette classe permet la gestion des entêtes des messages GIOP définies dans 
 * la version 1.0 du protocole GIOP.<P> 
 * Classe issue de l'IDL fourni par l'sorbet.<P> 
 * @author Groupe IOP,IIOP,GIOP 
 * @version 2.5 
 */ 
 
public class MessageHeader_1_0 { 
        /** 
         * Message de type "Request" 
         */ 
	public final static byte Request=(byte)0; 
        /** 
         * Message de type "Reply" 
         */ 
	public final static byte Reply=(byte)1; 
        /** 
         * Message de type "CancelRequest" 
         */ 
	public final static byte CancelRequest=(byte)2; 
        /** 
         * Message de type "LocateRequest" 
         */ 
	public final static byte LocateRequest=(byte)3; 
        /** 
         * Message de type "LocateReply" 
         */ 
	public final static byte LocateReply=(byte)4; 
        /** 
         * Message de type "CloseConnection" 
         */ 
	public final static byte CloseConnection=(byte)5; 
        /** 
         * Message de type "MessageError" 
         */ 
	public final static byte MessageError=(byte)6; 
         
	 
	 
	byte[] 	_magic=new byte[4]; 
	Version _GIOP_version; 
	boolean _byte_order; 
	byte	_message_type; 
	int	_message_size; 
	 
        /**  
         * Construit l'entête GIOP 
         * @param byte_order Ordre des bits dans le message. TRUE=Little Endian, FALSE=Big Endian 
         * @param message_type Type du message GIOP 
         * @param message_size Taille du message (sans compter cette entête) 
         */ 
	public MessageHeader_1_0(boolean byte_order,byte message_type,int message_size) { 
		this._magic[0]='G'; 
		this._magic[1]='I'; 
		this._magic[2]='O'; 
		this._magic[3]='P'; 
		this._GIOP_version=new Version((byte)1,(byte)0); 
		this._byte_order=byte_order; 
		this._message_type=message_type; 
		this._message_size=message_size; 
	} 
	 
        /** 
         * Retourne le champ "magic" de l'entête. La valeur retourner doit toujours 
         * être 'GIOP' 
         * @return Champ "magic" de l'entête 
         */ 
	public byte[] magic() { 
		return this._magic; 
	} 
 
        /** 
         * Permet de modifier la version du protocole GIOP utilisé. Cette méthode ne 
         * doit normalement pas être utilisée 
         * @param major Numéro de version majeure 
         * @param minor Numéro de version mineure 
         */ 
	public void GIOP_version(byte major,byte minor) { 
		this._GIOP_version.major(major); 
		this._GIOP_version.major(minor); 
	} 
	 
        /** 
         * Retourne la version du protocole GIOP utilisé. Cette valeur doit toujours 
         * être "1.0" 
         * @return Version du protocole GIOP utilisée 
         */ 
	public Version GIOP_version() { 
		return this._GIOP_version; 
	} 
	 
        /** 
         * Permet de modifier l'ordre des bits. Ceci n'affecte que le champ byte_order 
         * de l'entête, le message ne sera pas modifié en conséquence. 
         * @param byte_order Ordre des bits à utiliser 
         */ 
	public void byte_order(boolean byte_order) { 
		this._byte_order=byte_order; 
	} 
	 
        /** 
         * Retourne l'ordre des bits utilisé dans le message 
         * @return Ordre des bits utilisé dans le message 
         */ 
	public boolean byte_order() { 
		return this._byte_order; 
	} 
	 
	 
        /** 
         * Permet de modifier le type du message GIOP 
         * @param message_type Type du message GIOP 
         */ 
	public void message_type(byte message_type) { 
		this._message_type=message_type; 
	} 
	 
        /** 
         * Retourne le type du message GIOP 
         * @return Type du message GIOP 
         */ 
	public byte message_type() { 
		return _message_type; 
	} 
	 
        /** 
         * Permet de modifier la taille du message. Cette méthode doit normalement 
         * être utilisée une fois que le reste du message est construit et aligné dans 
         * un flux CDR. 
         * @param message_size Taille du message 
         */ 
	public void message_size(int message_size) { 
		this._message_size=message_size; 
	} 
	 
        /** 
         * Retourne la taille du message 
         * @return Taille du message 
         */ 
	public int message_size() { 
		return this._message_size; 
	} 
         
        /* Pour Tests */ 
	/** 
         * Affiche les informations concernant le message 
         * @return Informations sous forme d'une String 
         */ 
	public String toString() { 
		String desc=(char)_magic[0]+"-"+(char)_magic[1]+"-"+(char)_magic[2]+"-"+(char)_magic[3]+"\n"; 
		desc=desc+"Version:"+_GIOP_version.major()+"."+_GIOP_version.minor()+"\n"; 
		desc=desc+"Byte Order:"+_byte_order+"\n"; 
		desc=desc+"Type:"+_message_type+"\n"; 
		desc=desc+"Taille:"+_message_size+"\n"; 
		return desc; 
	} 
}
