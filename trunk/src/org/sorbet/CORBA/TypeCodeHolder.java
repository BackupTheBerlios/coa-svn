package org.sorbet.CORBA;

/**
 *classe implanté mais pas utilisee
*@author Millo Jean-Vivien
*@version 2.5
*/
public final class TypeCodeHolder implements org.omg.CORBA.portable.Streamable
{
    public org.omg.CORBA.TypeCode value;

    public TypeCodeHolder()
    {
    }

    public TypeCodeHolder( org.omg.CORBA.TypeCode initial )
    {
        value = initial;
    }

    public void _read( org.omg.CORBA.portable.InputStream is )
    {
        value = is.read_TypeCode();
    }

    public void _write( org.omg.CORBA.portable.OutputStream os )
    {
        os.write_TypeCode( value );
    }

    public org.omg.CORBA.TypeCode _type()
    {
        return org.omg.CORBA.ORB.init().get_primitive_tc (org.omg.CORBA.TCKind.tk_TypeCode );
    }
}
