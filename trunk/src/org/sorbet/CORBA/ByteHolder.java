package org.sorbet.CORBA; 
 
/** 
 * Cette classe definie un Holder pour le type de base Byte 
 * 
 * @author	Equipe Stub&Skeleton 
 * @version	2.5
 */ 
 
final public class ByteHolder implements org.sorbet.CORBA.portable.Streamable 
{ 
	/** 
	 * La valeur contenue dans le Holder 
	 */ 
    public byte value; 
    
	/** 
	 * Un constructeur par defaut 
	 */ 
    public ByteHolder() 
	{ 
		value = 0; 
	} 
    
	/** 
	 * Un constructeur avec une instanciation munie d'une valeur initiale 
	 */ 
    public ByteHolder( byte initial) 
    { 
		value = initial; 
    } 
	 
	public void _read( org.sorbet.CORBA.portable.InputStream is ) 
	{ 
		value = is.read_octet(); 
	} 
	 
	public void _write(org.sorbet.CORBA.portable.OutputStream  os ) 
	{ 
		os.write_octet( value ); 
	} 
	 
 };
