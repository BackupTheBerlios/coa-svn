package org.sorbet.CORBA.IIOP;

import java.lang.*;
/*
 *Cette exception se déclanche quand le message recu est innatendu
 * @author Groupe IOP,IIOP,GIOP
 * @version 2.5
 **/
public class UnexpectedMessageTypeException extends org.sorbet.CORBA.SystemException
{
	public UnexpectedMessageTypeException( String e )
	{
		super(e);
	}

	public UnexpectedMessageTypeException()
	{
		super( "Message recu inatendu" );
	}
}
