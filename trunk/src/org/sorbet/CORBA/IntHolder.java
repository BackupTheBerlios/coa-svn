package org.sorbet.CORBA; 
 
/** 
 * Cette classe definie un Holder pour le type de base Int 
 * 
 * @author	Equipe Stub&Skeleton 
 * @version	2.5
 */ 
final public class IntHolder implements org.sorbet.CORBA.portable.Streamable 
{ 
	/** 
	 * La valeur contenue dans le Holder 
	 */ 
    public int value; 
    
	/** 
	 * Un constructeur par defaut 
	 */ 
    public IntHolder() 
	{ 
		value = 0; 
	} 
    
	/** 
	 * Un constructeur avec une instanciation munie d'une valeur initiale 
	 */ 
    public IntHolder( int initial) 
    { 
		value = initial; 
    } 
	 
	public void _read( org.sorbet.CORBA.portable.InputStream is ) 
	{ 
		value = is.read_long(); 
	} 
	 
	public void _write( org.sorbet.CORBA.portable.OutputStream os ) 
	{ 
		os.write_long( value ); 
	} 
	 
 };
