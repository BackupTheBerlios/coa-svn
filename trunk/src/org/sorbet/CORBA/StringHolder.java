package org.sorbet.CORBA; 
 
/** 
 * Cette classe definie un Holder pour le type de base String 
 * 
 * @author	Equipe Stub&Skeleton 
 * @version	2.5
 */ 
 
final public class StringHolder implements org.sorbet.CORBA.portable.Streamable 
{ 
	/** 
	 * La valeur contenue dans le Holder 
	 */ 
    public String value; 
    
	/** 
	 * Un constructeur par defaut 
	 */ 
    public StringHolder() 
	{ 
		value = null; 
	} 
    
	/** 
	 * Un constructeur avec une instanciation munie d'une valeur initiale 
	 */ 
    public StringHolder( String initial) 
    { 
		value = initial; 
    } 
	 
	public void _read( org.sorbet.CORBA.portable.InputStream is ) 
	{ 
		value = is.read_string(); 
	} 
	 
	public void _write( org.sorbet.CORBA.portable.OutputStream os ) 
	{ 
		os.write_string( value ); 
	} 
	 
 };
