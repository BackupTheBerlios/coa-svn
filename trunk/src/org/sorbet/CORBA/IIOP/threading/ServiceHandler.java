package org.sorbet.CORBA.IIOP.threading;

import java.net.Socket;
import java.util.Vector;
import java.io.IOException;
/**
 * @author Millo Jean-Vivien
 * @version 2.5
 */
public abstract class ServiceHandler extends EventHandler
{
    public ServiceHandler(){}
    
    public ServiceHandler(InitiationDispatcher ID)
    {
        super(ID);
    }
    public ServiceHandler(InitiationDispatcher ID,Handle H)
    {
        super(ID,H);
    }
    public ServiceHandler(InitiationDispatcher ID,boolean T)
    {
        super(ID,T);
    }
    public ServiceHandler(InitiationDispatcher ID,Handle H,boolean T)
    {
        super(ID,H,T);
    }
    public void handleEvent(){}
}
