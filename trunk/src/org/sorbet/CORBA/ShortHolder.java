package org.sorbet.CORBA; 
 
/** 
 * Cette classe definie un Holder pour le type de base Short 
 * 
 * @author	Equipe Stub&Skeleton 
 * @version	2.5
 */ 
 
final public class ShortHolder implements org.sorbet.CORBA.portable.Streamable 
{ 
	/** 
	 * La valeur contenue dans le Holder 
	 */ 
    public short value; 
    
	/** 
	 * Un constructeur par defaut 
	 */ 
    public ShortHolder() 
	{ 
		value = 0; 
	} 
    
	/** 
	 * Un constructeur avec une instanciation munie d'une valeur initiale 
	 */ 
    public ShortHolder( short initial) 
    { 
		value = initial; 
    } 
	 
	public void _read( org.sorbet.CORBA.portable.InputStream is ) 
	{ 
		value = is.read_short(); 
	} 
	 
	public void _write( org.sorbet.CORBA.portable.OutputStream os ) 
	{ 
		os.write_short( value ); 
	} 
	 
 };
