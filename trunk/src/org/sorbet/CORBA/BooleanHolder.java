package org.sorbet.CORBA; 
 
/** 
 * Cette classe definie un Holder pour le type de base Boolean 
 * 
 * @author	Equipe Stub&Skeleton 
 * @version	2.5
 */ 
 
final public class BooleanHolder implements org.sorbet.CORBA.portable.Streamable 
{ 
	/** 
	 * La valeur contenue dans le Holder 
	 */ 
 
    public boolean value; 
    
	/** 
	 * Un constructeur par defaut 
	 */ 
 
    public BooleanHolder() 
	{ 
		value = false; 
	} 
    
	/** 
	 * Un constructeur avec une instanciation munie d'une valeur initiale 
	 */ 
 
    public BooleanHolder( boolean initial) 
    { 
		value = initial; 
    } 
	 
	public void _read( org.sorbet.CORBA.portable.InputStream is ) 
	{ 
		value = is.read_boolean(); 
	} 
	 
	public void _write( org.sorbet.CORBA.portable.OutputStream os ) 
	{ 
		os.write_boolean( value ); 
	}
 };
