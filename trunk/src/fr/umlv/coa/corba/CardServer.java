/* 
 * File    : CardServer.java
 * Created : 14 févr. 2005
 * 
 * =======================================
 * COA PROJECT ("http://coa.berlios.de")
 * =======================================
 *
 */

package fr.umlv.coa.corba;

import org.omg.CORBA.ORB;
import org.omg.CORBA.Policy;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.IdAssignmentPolicyValue;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import fr.umlv.coa.terminal.CardTerminalFactory;
import fr.umlv.coa.terminal.CardTerminalInterface;


/**
 * @author Ludo
 *
 */
public final class CardServer 
{
	/**
	 * The main entry
	 * 
	 * @param args the program arguments
	 * @throws Exception 
	 */
	public static void main (String [] args) throws Exception
	{
		ORB orb  = ORB.init (args, null);
		POA root = POAHelper.narrow (orb.resolve_initial_references ("RootPOA"));
		NamingContextExt naming = null;
		
		try
		{
			naming = NamingContextExtHelper.narrow (orb.resolve_initial_references ("NameService"));
		}
		catch (Exception e)
		{
			naming = null;
		}
		
		Policy[] policies = {root.create_id_assignment_policy (IdAssignmentPolicyValue.USER_ID)};
		
		POA child= root.create_POA ("COA", root.the_POAManager (), policies);

		root.the_POAManager ().activate ();
		
		CardTerminalInterface terminal = CardTerminalFactory.getGemplusTerminalInterface ();

		new CardManager (orb, child, naming, terminal);
		
		orb.run ();
	}
	
}
