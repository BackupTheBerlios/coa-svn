package org.sorbet.PortableServer;

import java.util.HashMap;
import org.sorbet.CORBA.Object;
import org.sorbet.CORBA.portable.ObjectImpl;
import org.sorbet.CORBA.SystemException;

/** This exception is thrown when ServantAlreadyActive occurred 
 * @author	Equipe Stub&Skeleton 
 * @version	2.5
 */
public class ServantAlreadyActive extends SystemException
{
    /** Constructs an Exception without a message. */
    public ServantAlreadyActive()
    {
        super();
    }

    /**
     * Constructs an Exception with a detailed message.
     * @param Message The message associated with the exception.
     */
    public ServantAlreadyActive(String message)
    {
        super(message);
    }
}


