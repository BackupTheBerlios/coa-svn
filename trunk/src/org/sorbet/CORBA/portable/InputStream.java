package org.sorbet.CORBA.portable; 
 
import org.sorbet.CORBA.GIOP.*; 
import org.sorbet.CORBA.IIOP.*; 
import org.sorbet.CORBA.IOP.*; 
import org.sorbet.CORBA.*; 
//import util.*; 
 
/** 
 * Cette classe permet la gestion des flux entrants encod�s au format CDR.<P> 
 * L'octet de bourrage utilis� est le 0. 
 * @author Groupe IOP,IIOP,GIOP 
 * @version 2.5
 */ 
 
public class InputStream { 
 
	boolean byte_order; // 0=Big Endian, 1=Little Endian 
	byte[] donnees; // Ce tableau contiendra le flux encod� en CDR 
	int position; // Cette variable sert � l'alignement. 
 
	//private NatifClassDef ncd = new NatifClassDef(); 
//	private long start; 
//	private long end; 
//	private long NbCycles; 
//	private final long mhz=2400; 
 
 
	/** 
         * Construit un flux entrant au format CDR � partir d'un tableau d'octets 
	 * @param donnees Donn�es � mettre dans ce flux 
         * @param byte_order Ordre des bits dans le flux. TRUE=Little Endian, FALSE=Big Endian 
	 */ 
	public InputStream (byte[] donnees,boolean byte_order) { 
		this.byte_order=byte_order; 
		this.donnees=donnees; 
		this.position=0; 
	} 
	 
	public void set_position(int pos){ 
		this.position = pos; 
	} 
 
	/** 
	 * Retourne les donn�es sous forme d'un tableau d'octets. 
         * @return Donn�es sous forme d'un tableau d'octets 
	 */ 
	public byte[] get_donnees() { 
		return donnees; 
	} 
	 
	/** 
	 * Retourne le nombre d'octets restants dans le flux. 
         * @return Nombre d'octets restant dans le flux 
	 */ 
	 public int available() { 
	 	return (donnees.length-position); 
	} 
 
	/** 
	* Lit un bool�en dans le flux de donn�es. 
        * @return Bool�en lu 
	*/ 
	public boolean read_boolean() { 
		position++; 
		if (donnees[position-1]==0) 
			return false; 
		else 
			return true; 
	} 
 
	/** 
	* Lit un octet dans le flux de donn�es. 
        * @return Octet lu 
	*/ 
	public byte read_octet() { 
		position++; 
		return donnees[position-1]; 
	} 
 
	/** 
	* Lit un short dans le flux de donn�es. 
        * @return <B>short</B> lu 
	*/  
	public short read_short() { 
		if (position%2!=0) { 
			position++; 
		} 
		position=position+2; 
		if (byte_order) { 
			return (short)( ((donnees[position-1]&0xFF)<<8) | donnees[position-2]&0xFF ); 
		} 
		else { 
			return (short)( ((donnees[position-2]&0xFF)<<8) | donnees[position-1]&0xFF ); 
		} 
	} 
	 
	/** 
	 * Lit un short dans le flux de donn�es et le retourne en entier. 
         * @return <B>short</B> lu sous forme d'un <B>int</B> 
	 */  
	public int read_short_to_integer() { 
		if (position%2!=0) { 
			position++; 
		} 
		position=position+2; 
		if (byte_order) { 
			return ( ((donnees[position-1]&0xFF)<<8) | donnees[position-2]&0xFF ); 
		} 
		else { 
			return ( ((donnees[position-2]&0xFF)<<8) | donnees[position-1]&0xFF ); 
		} 
	} 
 
	/** 
	 * Lit un entier de type long (int en JAVA) dans le flux de donn�es. 
         * @return <B>int</B> lu 
	 */ 
	public int read_long() { 
		if ((position%4)!=0) { 
			int i; 
			int tmp=position%4; 
			for (i=0;i<4-tmp;i++) { 
				position++; 
			} 
		} 
		position=position+4; 
		if (byte_order) { 
			return (int) ( ((donnees[position-1]&0xFF)<<24) | ((donnees[position-2]&0xFF)<<16) | ((donnees[position-3]&0xFF)<<8) | donnees[position-4]&0xFF); 
		} 
		else { 
			return (int) ( ((donnees[position-4]&0xFF)<<24) | ((donnees[position-3]&0xFF)<<16) | ((donnees[position-2]&0xFF)<<8) | donnees[position-1]&0xFF); 
		} 
	} 
 
	/** 
	 * Lit une cha�ne de caract�res dans le flux de donn�es. 
         * @return Cha�ne lue 
	 */ 
	public String read_string() 
	{ 
		int taille=read_long(); 
		String tmp=new String(donnees,position,taille); 
        position=position+taille;	 
		return tmp; 
	} 
	 
	/** 
	 * Lit un tableau de bool�ens dans le flux de donn�es. 
	 * La taille du tableau doit �tre pr�cis�e en argument. 
         * @param taille Taille du tableau � lire 
         * @return Tableau de bool�ens lu 
	 */ 
	public boolean[] read_array_boolean(int taille) { 
		boolean[] tmp=new boolean[taille]; 
		java.lang.System.arraycopy(donnees,position,tmp,0,taille); 
		position=position+taille; 
		return tmp; 
	} 
 
	/** 
	 * Lit un tableau d'octets dans le flux de donn�es. 
	 * La taille du tableau doit �tre pr�cis�e en argument. 
         * @param taille Taille du tableau � lire 
         * @return Tableau d'octets lu 
	 */ 
	public byte[] read_array_octet(int taille) { 
		byte[] tmp=new byte[taille]; 
		java.lang.System.arraycopy(donnees,position,tmp,0,taille); 
		position=position+taille; 
		return tmp; 
	} 
 
	/** 
	 * Lit un tableau de short dans le flux de donn�es. 
	 * La taille du tableau doit �tre pr�cis�e en argument. 
         * @param taille Taille du tableau � lire 
         * @return Tableau de <B>short</B> lu 
	 */ 
	public short[] read_array_short(int taille) { 
		short[] tmp=new short[taille]; 
		for (int i=0;i<taille;i++) { 
			tmp[i]=read_short(); 
		} 
		return tmp; 
	} 
 
	/** 
	 * Lit un tableau de long dans le flux de donn�es. 
	 * La taille du tableau doit �tre pr�cis�e en argument. 
         * @param Taille du tableau � lire 
         * @return Tableau d'<B>int</B> lu 
	 */ 
	public int[] read_array_long(int taille) { 
		int[] tmp=new int[taille]; 
		for (int i=0;i<taille;i++) { 
			tmp[i]=read_long(); 
		} 
		return tmp; 
	} 
 
	/** 
	 * Lit une s�quence de bool�ens dans le flux de donn�es. 
         * @return Tableau de bool�ens lu 
	 */ 
	public boolean[] read_sequence_boolean() { 
                int taille=read_long(); 
                boolean[] tmp=new boolean[taille]; 
		java.lang.System.arraycopy(donnees,position,tmp,0,taille); 
		position=position+taille; 
		return tmp; 
	} 
 
	/** 
	 * Lit une s�quence d'octets dans le flux de donn�es 
         * @return Tableau d'octets lu 
	 */ 
	public byte[] read_sequence_octet() { 
        int taille=read_long(); 
		byte[] tmp=new byte[taille]; 
		java.lang.System.arraycopy(donnees,position,tmp,0,taille); 
		position=position+taille; 
//System.out.println(taille); 
		return tmp; 
	} 
 
	/** 
	 * Lit une s�quence de short dans le flux de donn�es 
         * @return Tableau de <B>short</B> lu 
	 */ 
	public short[] read_sequence_short() { 
        int taille=read_long(); 
		short[] tmp=new short[taille]; 
		for (int i=0;i<taille;i++) { 
			tmp[i]=read_short(); 
		} 
		return tmp; 
	} 
 
	/** 
	* Lit une s�quence de long dans le flux de donn�es 
        * @return Tableau d'<B>int</B> lu 
	*/ 
	public int[] read_sequence_long() { 
        int taille=read_long(); 
		int[] tmp=new int[taille]; 
		for (int i=0;i<taille;i++) { 
			tmp[i]=read_long(); 
		} 
		return tmp; 
	} 
 
	/** 
	 * Permet de modifier l'ordre des bits. Ceci peut �tre utile pour la lecture des ent�tes des messages o� 
	 * le byte_order n'est connu qu'apr�s la cr�ation de la org.sorbet.CORBA.portable.InputStream. La modification de cette valeur 
         * n'entra�ne aucune modification dans la structure du flux. 
         * @param byte_order Ordre des bits � utiliser 
	 */ 
 
	public void set_byte_order(boolean byte_order) { 
		this.byte_order=byte_order; 
	} 
 
	/** 
	 * Extrait une org.sorbet.CORBA.portable.InputStream � partir de la position actuelle dans cette org.sorbet.CORBA.portable.InputStream. 
         * @return Flux lu 
	 */ 
	public org.sorbet.CORBA.portable.InputStream read_CDRInputStream() { 
		org.sorbet.CORBA.portable.InputStream flux; 
		byte[] buffer_tmp=new byte[donnees.length-position]; 
		java.lang.System.arraycopy(donnees,position,buffer_tmp,0,donnees.length-position); 
		flux=new org.sorbet.CORBA.portable.InputStream(buffer_tmp,byte_order); 
		return flux; 
	} 
 
	/** 
	 * Lit une ent�te de message GIOP dans le flux. Seule les ent�tes dont la version du 
         * protocole est 1.0 sont accept�es.<P> 
         * L'ordre des bits dans le flux est automatiquement fix� en fonction de celui 
         * qui est pr�cis� dans l'ent�te. 
         * @return Ent�te lue 
         * @throws NonCORBAMessageException Message incoh�rent 
         * @throws InvalidVersionNumberException Version diff�rente de 1.0 
         * @throws InvalidMessageTypeException Type de message non valide 
         * @throws InvalidMessageSizeException Taille de message anormale - Non impl�ment�e 
	 */ 
 
	public MessageHeader_1_0 read_Message_Header() 
		throws	NonCORBAMessageException, InvalidVersionNumberException, 
				InvalidMessageTypeException, InvalidMessageSizeException 
	{ 
		/* Pour v�rification, on peut lire le MAGIC et la Version */ 
		 
		//ncd = new NatifClassDef(); 
	//	start = ncd.getCycles(); 
		 
		byte[] tmp=read_array_octet(4); 
		 
 
		if ( (tmp[0] != 'G')||(tmp[1] != 'I')||(tmp[2] != 'O')||(tmp[3] != 'P') ) { 
			throw (new NonCORBAMessageException()); 
		} 
		 
 
		org.sorbet.CORBA.GIOP.Version version=new org.sorbet.CORBA.GIOP.Version(read_octet(),read_octet()); 
                 
		if ( (version.major() != 1 )||(version.minor() != 0 ) ) { 
			throw (new InvalidVersionNumberException()); 
		} 
 
        	boolean byte_order=read_boolean(); 
 
 
		this.set_byte_order(byte_order); 
 
 
                byte message_type=read_octet(); 
 
        if ( (message_type<MessageHeader_1_0.Request) || (message_type>MessageHeader_1_0.MessageError) ) { 
          	throw (new InvalidMessageTypeException()); 
    	} 
 
 
        int taille=read_long(); 
 
 
		MessageHeader_1_0 message=new MessageHeader_1_0(byte_order,message_type,taille); 
		 
		return message; 
	} 
 
        /** 
	 * Lit une ent�te de requ�te GIOP 1.0 dans le flux.<P> 
         * @return Ent�te de requ�te lue 
         */ 
	public RequestHeader_1_0 read_RequestHeader_1_0()  
	{ 
                int taille_context=read_long(); 
                ServiceContext[] service_context=new ServiceContext[taille_context]; 
                for (int i=0;i<taille_context;i++)  
                { 
                        service_context[i]=new ServiceContext(read_long(),read_sequence_octet()); 
                } 
                 
        RequestHeader_1_0 requete=new RequestHeader_1_0(service_context,read_long(),read_boolean(),read_sequence_octet(),read_string(),read_sequence_octet()); 
 
		return requete; 
	} 
	 
        /** 
         * Lit un tableau de Contextes dans le flux 
         * @return Tableau de Contextes lu 
         */ 
	public ServiceContext[] read_service_context() { 
		int taille_context=read_long(); 
        ServiceContext[] service_context=new ServiceContext[taille_context]; 
        //System.out.println(taille_context); 
        for (int i=0;i<taille_context;i++) { 
			System.out.println( "read service context :" + i); 
        	service_context[i]=new ServiceContext(read_long(),read_sequence_octet()); 
        } 
        return service_context; 
	} 
	 
        /** 
         * Lit un tableau de TaggedProfile dans le flux 
         * @return Tableau de TaggedProfile lu 
         */ 
	public TaggedProfile[] read_tagged_profile() { 
		int taille_profile=read_long(); 
        TaggedProfile[] tagged_profiles=new TaggedProfile[taille_profile]; 
         for (int i=0;i<taille_profile;i++) { 
        	tagged_profiles[i]=new TaggedProfile(read_long(),read_sequence_octet()); 
           } 
        return tagged_profiles; 
	} 
	 
        /** 
         * Lit une ent�te de r�ponse GIOP 1.0 dans le flux.<P> 
         * @return Ent�te de r�ponse lue 
         */ 
        public ReplyHeader_1_0 read_ReplyHeader_1_0() { 
            ServiceContext[] service_context=read_service_context(); 
            ReplyHeader_1_0 reponse=new ReplyHeader_1_0(service_context,read_long(),read_long()); 
            return reponse; 
        } 
 
        /** 
         * Lit une ent�te d'annulation GIOP dans le flux.<P> 
         * @return Ent�te d'annulation lue 
         */ 
        public CancelRequestHeader read_CancelRequestHeader() { 
            CancelRequestHeader annulation=new CancelRequestHeader(read_long()); 
            return annulation; 
        } 
 
        /** 
         * Lit une ent�te de requ�te de localisation dans le flux.<P> 
         * @return Ent�te de requ�te de localisation lue 
         */ 
        public LocateRequestHeader_1_0 read_LocateRequestHeader_1_0() { 
            LocateRequestHeader_1_0 requete=new LocateRequestHeader_1_0(read_long(),read_sequence_octet()); 
            return requete; 
        } 
 
        /** 
         * Lit une ent�te de r�ponse de localisation dans le flux.<P> 
         * @return Ent�te de r�ponse de localisation lue 
         */ 
        public LocateReplyHeader_1_0 read_LocateReplyHeader_1_0() { 
            LocateReplyHeader_1_0 reponse=new LocateReplyHeader_1_0(read_long(),read_long()); 
            return reponse; 
        } 
 
        /** 
         * Lit un corps d'exception dans le flux.<P> 
         * @return Corps d'exception lu 
         */ 
        public SystemExceptionReplyBody read_Exception_Body() { 
            SystemExceptionReplyBody exception=new SystemExceptionReplyBody(read_string(),read_long(),read_long()); 
            return exception; 
        } 
     
        /** 
         * Lit un corps de Profile IIOP 1.0 dans le flux.<P> 
         * @return Corps de Profile lu 
         */ 
        public ProfileBody_1_0 read_ProfileBody_1_0() { 
            org.sorbet.CORBA.IIOP.Version version=new org.sorbet.CORBA.IIOP.Version(read_octet(),read_octet()); 
            String host=read_string(); 
            int port=read_short_to_integer(); 
            byte[] object_key=read_sequence_octet(); 
            ProfileBody_1_0 profile=new ProfileBody_1_0(host,port,object_key); 
            return profile; 
        } 
     
        /** 
         * Lit un object CORBA dans le flux.<P> 
         * Le flux contient l'IOR de l'objet CORBA. Cette IOR est lue puis on cr�e un  
         * org.sorbet.CORBA.portable.ObjectImpl que l'on retourne 
         * @return Objet CORBA lu dans le flux 
         */ 
        public org.sorbet.CORBA.Object read_Object() { 
    	    this.byte_order=read_boolean(); 
    	    read_string(); 
    	    read_long(); 
    	    read_long(); 
    	    org.sorbet.CORBA.portable.InputStream profile_flux=read_CDRInputStream(); 
    	    profile_flux.set_byte_order(profile_flux.read_boolean()); 
    	    profile_flux.read_octet(); 
    	    profile_flux.read_octet(); 
    	    Connexion connexion=ConnexionFactory.createConnexionClient(profile_flux.read_string(),profile_flux.read_short(),profile_flux.read_sequence_octet()); 
    	    return (new org.sorbet.CORBA.portable.ObjectImpl(connexion)); 
        } 
        public org.sorbet.CORBA.Object read_Object(java.lang.Class clas) 
        {return null;} 
}
