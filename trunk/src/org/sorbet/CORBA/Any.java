package org.sorbet.CORBA;

import org.sorbet.CORBA.*;
/**
 *Classe vide
 *@author Millo Jean-Vivien
 *@version 2.5
 **/
public class Any
{
    org.sorbet.CORBA.Object s;
    
    public org.sorbet.CORBA.portable.OutputStream create_output_stream (){return null;}
    public org.sorbet.CORBA.portable.InputStream create_input_stream (){return null;}
    public void type(TypeCode TC){}
    public void read_value(org.sorbet.CORBA.portable.InputStream os,TypeCode TC){}
    public void insert_Object(org.sorbet.CORBA.Object s){this.s=s;}
    public org.sorbet.CORBA.Object extract_Object(){return s;}
}
