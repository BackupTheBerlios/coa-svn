package org.sorbet.CORBA.portable; 
 
 
import org.sorbet.CORBA.IIOP.*; 
import org.sorbet.CORBA.GIOP.*; 
import org.sorbet.CORBA.IOP.*; 
import java.io.*; 
/** 
 * Cette classe permet la gestion des flux sortant encodés au format CDR.<P> 
 * L'octet de bourrage utilisé est le 0. 
 * @author Groupe IOP,IIOP,GIOP 
 * @version 2.5
 */ 
 
public class OutputStream { 
	boolean byte_order; // 0=Big Endian, 1=Little Endian 
	byte[] donnees=new byte[0]; // Ce tableau contiendra le flux encodé en CDR 
	int position; // Cette variable sert à l'alignement. 
 
//private NatifClassDef ncd; 
//private long start; 
//private long end; 
//private long NbCycles; 
//private final long mhz=2400; 
 
	/** 
	 * Constructeur 
	 * @param byte_order Ordre des bits à utiliser. TRUE=Little Endian, FALSE=Big Endian 
	 */ 
	public OutputStream (boolean byte_order) { 
		this.byte_order=byte_order; 
		this.position=0; 
 
	} 
	 
	/** 
	 * Retourne les données sous forme d'un tableau d'octets. 
         * @return Données sous forme d'un tableau d'octet 
	 */ 
	public byte[] get_donnees() { 
		return donnees; 
		 
	} 
 
	/** 
	* Retourne les données sous forme hexadécimale dans une chaîne de caractères  
        * @return Chaîne de caractères contenant les données sous forme hexadécimale 
	*/ 
	public String get_donnees_hexa() { 
		 
		String chaine=new String("IOR:"); 
		short dontemp; 
		char[] codes={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'}; 
		for (int i=0;i<donnees.length;i++) { 
			if (donnees[i]<0) 
				dontemp = (short)(donnees[i]+256); 
			else  
				dontemp = donnees[i];	 
			 
			chaine+=codes[(dontemp/16)]; 
			chaine+=codes[(dontemp%16)]; 
 
		} 
		return chaine;		 
	} 
 
	/** 
	 * Ajoute un booléen à la fin du flux 
         * @param booleen Booléen à ajouter dans le flux 
	 */ 
	public void write_boolean(boolean booleen) { 
		byte[] buffer_tmp=new byte[donnees.length+1]; 
		java.lang.System.arraycopy(donnees,0,buffer_tmp,0,donnees.length); 
		donnees=buffer_tmp; 
		if (booleen)  
			donnees[position]=(byte)1; 
		else 
			donnees[position]=(byte)0; 
		position++; 
	} 
 
	/** 
	 * Ajoute un octet à la fin du flux 
         * @param octet Octet à ajouter dans le flux 
	 */ 
	public void write_octet(byte octet) { 
      
		byte[] buffer_tmp=new byte[donnees.length+1]; 
		java.lang.System.arraycopy(donnees,0,buffer_tmp,0,donnees.length); 
		donnees=buffer_tmp; 
		donnees[position]=octet; 
		position++; 
 
	} 
 
	/** 
	 * Ajoute un entier de type short à la fin du flux 
         * @param short_var <B>short</B> à ajouter dans le flux 
	 */  
	public void write_short(short short_var) { 
	 
		byte[] buffer_tmp; 
		if (position%2!=0) 
			buffer_tmp=new byte[donnees.length+3]; 
		else 
			buffer_tmp=new byte[donnees.length+2]; 
		java.lang.System.arraycopy(donnees,0,buffer_tmp,0,donnees.length); 
		donnees=buffer_tmp; 
		if (position%2!=0) { 
			donnees[position]=0; 
			position++; 
		} 
		if (byte_order) { 
			donnees[position]=(byte)(short_var); 
			donnees[position+1]=(byte)(short_var >>> 8); 
		} 
		else { 
			donnees[position+1]=(byte)(short_var); 
			donnees[position]=(byte)(short_var >>> 8); 
 
		} 
		position=position+2; 
	} 
 
	/** 
	 * Ajoute un entier de type long (int en JAVA) à la fin du flux 
         * @param long_var <B>int</B> à ajouter dans le flux 
	 */ 
	public void write_long(int long_var) { 
       
		byte[] buffer_tmp; 
		if (position%4!=0) 
			buffer_tmp=new byte[position+8-position%4]; 
		else 
			buffer_tmp=new byte[position+4]; 
		java.lang.System.arraycopy(donnees,0,buffer_tmp,0,position); 
		donnees=buffer_tmp; 
		if ((position%4)!=0) { 
			int i; 
			int tmp=position%4; 
			for (i=0;i<4-tmp;i++) { 
				donnees[position]=0; 
				position++; 
			} 
		} 
		if (byte_order) { 
			donnees[position+3]=(byte)(long_var >>> 24); 
			donnees[position+2]=(byte)(long_var >>> 16); 
			donnees[position+1]=(byte)(long_var >>> 8); 
			donnees[position]=(byte)(long_var); 
		} 
		else { 
			donnees[position]=(byte)(long_var >>> 24); 
			donnees[position+1]=(byte)(long_var >>> 16); 
			donnees[position+2]=(byte)(long_var >>> 8); 
			donnees[position+3]=(byte)(long_var); 
		} 
		position=position+4; 
	} 
 
	/** 
	 * Ajoute une chaîne de caractères à la fin du flux 
         * @param string_var Chaîne de caractère à ajouter dans le flux 
	 */ 
	public void write_string(String string_var) 
	{ 
 
		write_long(string_var.length()); 
		byte[] buffer_tmp=new byte[position+string_var.length()]; 
		java.lang.System.arraycopy(donnees,0,buffer_tmp,0,position); 
		donnees=buffer_tmp; 
		int i; 
		for (i=0;i<string_var.length();i++)  
		{ 
			donnees[position]=(byte)string_var.charAt(i); 
			position++; 
		} 
	} 
	 
	/** 
	* Ajoute un tableau de booléens à la fin du flux.<P> 
        * Attention : En utilisant cette méthode, la taille du tableau n'est pas  
        * inscrite dans le flux. De ce fait, lors de la lecture dans une org.sorbet.CORBA.portable.InputStream, 
        * il faudra utiliser la méthode read_array_boolean(<B>int</B>) en indiquant la taille 
        * du tableau à relire. Si la taille du tableau lu n'est pas identique à la taille 
        * du tableau écrit, le flux sera corrompu. 
        * @param array_boolean Tableau de booléens à ajouter dans le flux 
	*/ 
	public void write_array_boolean(boolean[] array_boolean) { 
		/*byte[] buffer_tmp=new byte[donnees.length+4+array_boolean.length]; 
		java.lang.System.arraycopy(donnees,0,buffer_tmp,0,donnees.length); 
		donnees=buffer_tmp;*/ 
		int i; 
		for (i=0;i<array_boolean.length;i++) { 
			write_boolean(array_boolean[i]); 
		} 
	} 
 
	/** 
	* Ajoute un tableau d'octets à la fin du flux.<P> 
        * Attention : En utilisant cette méthode, la taille du tableau n'est pas  
        * inscrite dans le flux. De ce fait, lors de la lecture dans une org.sorbet.CORBA.portable.InputStream, 
        * il faudra utiliser la méthode read_array_octet(<B>int</B>) en indiquant la taille 
        * du tableau à relire. Si la taille du tableau lu n'est pas identique à la taille 
        * du tableau écrit, le flux sera corrompu. 
        * @param array_octet Tableau d'octets à ajouter dans le flux 
	*/ 
	public void write_array_octet(byte[] array_octet) { 
		/*byte[] buffer_tmp=new byte[donnees.length+4+array_octet.length]; 
		java.lang.System.arraycopy(donnees,0,buffer_tmp,0,donnees.length); 
		donnees=buffer_tmp;*/ 
		int i; 
		for (i=0;i<array_octet.length;i++) { 
			write_octet(array_octet[i]); 
		} 
	} 
 
	/** 
	* Ajoute un tableau de <B>short</B> à la fin du flux.<P> 
        * Attention : En utilisant cette méthode, la taille du tableau n'est pas  
        * inscrite dans le flux. De ce fait, lors de la lecture dans une org.sorbet.CORBA.portable.InputStream, 
        * il faudra utiliser la méthode read_array_short(<B>int</B>) en indiquant la taille 
        * du tableau à relire. Si la taille du tableau lu n'est pas identique à la taille 
        * du tableau écrit, le flux sera corrompu. 
        * @param array_short Tableau de <B>short</B> à ajouter dans le flux 
	*/ 
	public void write_array_short(short[] array_short) { 
		/*byte[] buffer_tmp=new byte[donnees.length+4+(array_short.length)*2]; 
		java.lang.System.arraycopy(donnees,0,buffer_tmp,0,donnees.length); 
		donnees=buffer_tmp;*/ 
		int i; 
		for (i=0;i<array_short.length;i++) { 
			write_short(array_short[i]); 
		} 
	} 
 
	/** 
	* Ajoute un tableau d'</B>int</B> à la fin du flux.<P> 
        * Attention : En utilisant cette méthode, la taille du tableau n'est pas  
        * inscrite dans le flux. De ce fait, lors de la lecture dans une org.sorbet.CORBA.portable.InputStream, 
        * il faudra utiliser la méthode read_array_long(<B>int</B>) en indiquant la taille 
        * du tableau à relire. Si la taille du tableau lu n'est pas identique à la taille 
        * du tableau écrit, le flux sera corrompu. 
        * @param array_long Tableau d'<B>int</B> à ajouter dans le flux 
	*/ 
	public void write_array_long(int[] array_long) { 
		/*byte[] buffer_tmp=new byte[donnees.length+4+(array_long.length)*4]; 
		java.lang.System.arraycopy(donnees,0,buffer_tmp,0,donnees.length); 
		donnees=buffer_tmp;*/ 
		int i; 
		for (i=0;i<array_long.length;i++) { 
			write_long(array_long[i]); 
		} 
	} 
 
        /** 
	* Ajoute une séquence de booléens à la fin du flux 
        * @param array_boolean Tableau de booléens à ajouter dans le flux 
	*/ 
	public void write_sequence_boolean(boolean[] array_boolean) { 
 
		int i; 
                write_long(array_boolean.length); 
		for (i=0;i<array_boolean.length;i++) { 
			write_boolean(array_boolean[i]); 
		} 
	} 
 
	/** 
	* Ajoute une séquence d'octets à la fin du flux 
        * @param array_octet Tableau d'octets à ajouter dans le flux 
	*/ 
	public void write_sequence_octet(byte[] array_octet) { 
		 
		int i; 
                write_long(array_octet.length); 
		for (i=0;i<array_octet.length;i++) { 
			write_octet(array_octet[i]); 
		} 
	} 
 
	/** 
	* Ajoute une séquence de <B>short</B> à la fin du flux 
        * @param array_short Tableau de <B>short</B> à ajouter dans le flux 
	*/ 
	public void write_sequence_short(short[] array_short) { 
		int i; 
                write_long(array_short.length); 
		for (i=0;i<array_short.length;i++) { 
			write_short(array_short[i]); 
		} 
	} 
 
	/** 
	* Ajoute une séquence d'<B>int</B> à la fin du flux 
        @param array_long Tableau d'<B>int</B> à ajouter dans le flux 
	*/ 
	public void write_sequence_long(int[] array_long) { 
		int i; 
                write_long(array_long.length); 
		for (i=0;i<array_long.length;i++) { 
			write_long(array_long[i]); 
		} 
	} 
 
	/** 
	* Concatène une org.sorbet.CORBA.portable.OutputStream à la org.sorbet.CORBA.portable.OutputStream actuelle. La org.sorbet.CORBA.portable.OutputStream 
        * est concaténée sans modification de l'alignement. 
        * @param flux org.sorbet.CORBA.portable.OutputStream à ajouter dans le flux 
	*/ 
	public void write_CDROutputStream(org.sorbet.CORBA.portable.OutputStream flux) { 
		write_array_octet(flux.get_donnees()); 
	} 
 
	/** 
	* Ajoute l'entête GIOP 1.0 dans le flux 
        * @param message Entête à ajouter dans le flux 
	*/ 
	public void write_Message_Header(MessageHeader_1_0 message) { 
		write_array_octet(message.magic()); 
		write_octet(message.GIOP_version().major()); 
		write_octet(message.GIOP_version().minor()); 
		write_boolean(message.byte_order()); 
		write_octet(message.message_type()); 
		write_long(message.message_size()); 
	} 
 
	/** 
	* Ecrit l'entête d'une requête GIOP 1.0 dans le flux 
        * @param header Entête à ajouter dans le flux 
	*/ 
	public void write_Header(RequestHeader_1_0 header) { 
		int i; 
 
		ServiceContext[] service_context=header.service_context(); 
        write_long(service_context.length); 
		for (i=0;i<service_context.length;i++) { 
			write_long(service_context[i].context_id()); 
			write_sequence_octet(service_context[i].context_data()); 
		} 
		write_long(header.request_id()); 
		write_boolean(header.response_expected()); 
        write_sequence_octet(header.object_key()); 
		write_string(header.operation()); 
		write_sequence_octet(header.requesting_principal()); 
	} 
 
	/** 
	* Ecrit l'entête d'une réponse GIOP 1.0 dans le flux 
        * @param header Entête à ajouter dans le flux 
	*/ 
	public void write_Header(ReplyHeader_1_0 header) { 
		int i; 
		ServiceContext[] service_context=header.service_context(); 
                write_long(service_context.length); 
		for (i=0;i<service_context.length;i++) { 
			System.out.println( "write service context :" + i); 
			write_long(service_context[i].context_id()); 
			write_sequence_octet(service_context[i].context_data()); 
		} 
		write_long(header.request_id()); 
		write_long(header.reply_status()); 
	} 
 
        /** 
	* Ecrit l'entête d'une requête d'annulation GIOP dans le flux 
        * @param header Entête à ajouter dans le flux 
	*/ 
    public void write_Header(CancelRequestHeader header) { 
		write_long(header.request_id()); 
    } 
 
	/** 
	* Ecrit l'entête d'une requête de localisation GIOP 1.0 dans le flux 
        * @param header Entête à ajouter dans le flux 
	*/ 
	public void write_Header(LocateRequestHeader_1_0 header) { 
		write_long(header.request_id()); 
		write_sequence_octet(header.object_key()); 
	} 
 
	/** 
	* Ecrit l'entête d'une réponse de localisation GIOP 1.0 dans le flux 
        * @param header Entête à ajouter dans le flux 
	*/ 
	public void write_Header(LocateReplyHeader_1_0 header) { 
		write_long(header.request_id()); 
		write_long(header.locate_status()); 
	} 
 
        /** 
	* Ecrit le corps d'une exception dans le flux 
        * @param exception Corps d'exception à ajouter dans le flux 
	*/ 
    public void write_Exception_Body(SystemExceptionReplyBody exception) { 
        write_string(exception.exception_id()); 
        write_long(exception.minor_code_value()); 
        write_long(exception.completion_status()); 
    } 
         
        /** 
        * Ecrit une IOR dans le flux.<P> 
        * L'IOR doit avoir été construite à partir du constructeur par défaut IOR(). 
        * Elle ne peut donc pas contenir plusieurs Profile. 
        * @param ior IOR à écrire 
        * @param object_key Identifiant d'objet à mettre dans l'IOR 
        */ 
    public void write_IOR(IOR ior,byte[] object_key,byte[] ThreadPolicy) { 
      	org.sorbet.CORBA.portable.OutputStream profile_body_flux=new org.sorbet.CORBA.portable.OutputStream(byte_order); 
        	// Le port est mis en 2048 pour l'instant 
       	try { 
       		ProfileBody_1_0 profile_body=new ProfileBody_1_0( (java.net.InetAddress.getLocalHost()).getHostAddress(),2048,"".getBytes()); 
       		write_boolean(byte_order); 
       		write_string(ior.type_id()); 
       		write_long(ior.profiles().length); 
    		write_long(TaggedProfile.TAG_INTERNET_IOP); 
       		profile_body_flux.write_boolean(this.byte_order); 
       		profile_body_flux.write_octet(profile_body.iiop_version().major()); 
      		profile_body_flux.write_octet(profile_body.iiop_version().minor()); 
       		profile_body_flux.write_string(profile_body.host()); 
       		profile_body_flux.write_short((short)((profile_body.port())&0xFFFF)); 
       		profile_body_flux.write_sequence_octet(object_key); 
       		write_sequence_octet(profile_body_flux.get_donnees()); 
       		//ecriture dans l'ior de la politique de thread 
       		org.sorbet.CORBA.portable.OutputStream profile_body_flux2=new org.sorbet.CORBA.portable.OutputStream(byte_order); 
       		write_long(ior.profiles()[1].tag()); 
       		profile_body_flux2.write_octet(ior.profiles()[1].profile_data()[0]); 
       		write_sequence_octet(profile_body_flux2.get_donnees()); 
       	}  
       	catch (java.net.UnknownHostException e) { 
        		e.printStackTrace(); 
        		System.out.println(e.getMessage()); 
        } 
    } 
	 
        /** 
         * Ajoute un objet CORBA dans le flux.<P> 
         * Cette opération consiste en l'écriture de l'IOR corresponsant à cet objet 
         * dans le flux. 
         * @param objet Objet CORBA à ajouter 
         */ 
	public void write_Object(org.sorbet.CORBA.Object objet) { 
		org.sorbet.CORBA.portable.ObjectImpl obj=(org.sorbet.CORBA.portable.ObjectImpl) objet; 
		Connexion connexion=obj.protocol; 
		IOR ior=new IOR(); 
		org.sorbet.CORBA.portable.OutputStream profile_body_flux=new org.sorbet.CORBA.portable.OutputStream(byte_order); 
	//	try { 
    		ProfileBody_1_0 profile_body=new ProfileBody_1_0( connexion.distantHost().host(),connexion.distantHost().port(),connexion.object_key() ); 
    		write_boolean(byte_order); 
    		write_string(ior.type_id()); 
    		write_long(ior.profiles().length); 
			write_long(TaggedProfile.TAG_INTERNET_IOP); 
    		profile_body_flux.write_boolean(this.byte_order); 
    		profile_body_flux.write_octet(profile_body.iiop_version().major()); 
    		profile_body_flux.write_octet(profile_body.iiop_version().minor()); 
    		profile_body_flux.write_string(profile_body.host()); 
    		profile_body_flux.write_short((short)((profile_body.port())&0xFFFF)); 
    		profile_body_flux.write_sequence_octet(profile_body.object_key()); 
    		write_sequence_octet(profile_body_flux.get_donnees()); 
    	/*} catch (java.net.UnknownHostException e) { 
    		e.printStackTrace(); 
    		System.out.println(e.getMessage()); 
    	}*/ 
	} 
	public org.sorbet.CORBA.portable.InputStream create_input_stream () 
	{return null;} 
} 
