package org.sorbet.CORBA.portable;

import org.sorbet.CORBA.*;
/**
 *Suivant les Specification CORBA, la classe _applStub implante InvokeHandler
 *@author Millo Jean-Vivien
 *@version 2.5  
 **/
public interface InvokeHandler
{
public org.sorbet.CORBA.portable.OutputStream _invoke(String method, org.sorbet.CORBA.portable.InputStream input, ResponseHandler handler);
}
