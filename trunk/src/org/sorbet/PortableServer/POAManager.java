package org.sorbet.PortableServer;

import org.sorbet.CORBA.SystemException;
/**
* @author  PortableServerTeam
* @version 2.5
*/
interface POAManager {
    void activate() throws AdapterInactive ;

    static class AdapterInactive extends SystemException
    {
    }
}
