package org.sorbet.CORBA;
/**
 * @author Equipe ORB&POA
 * @version 2.5
 */
public class InvalidName extends SystemException {

    public InvalidName(){}

    public InvalidName(String reason)
    {
		super(reason);

    }
}
