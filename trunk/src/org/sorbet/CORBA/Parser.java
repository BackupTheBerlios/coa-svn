package org.sorbet.CORBA; 
 
import java.util.Hashtable; 
import java.lang.String; 
 
/** 
* Cette classe fournit un parser des arguments passés à l'ORB 
* @author Equipe ORB&POA 
* @version 2.5
*/ 
 
class Parser 
{ 
	 
	public Parser(){ 
		} 
	 
	// Pour récupérer les références initiales passées en arguments.	 
	public Hashtable get_initial_references(String[] args) 
	{ 
		Hashtable initRef = new Hashtable(); 
		for(int i=0;i<args.length;i++) 
	        { 
			if (args[i].startsWith("-ORBInitRef")) 
			{ 
                int index = args[i].indexOf('='); 
				if (index != -1) 
				{ 
					String RefId = args[i].substring(12,index); 
                    RefId = RefId.trim(); 
					String Refvalue = args[i].substring(index+1); 
                    Refvalue = Refvalue.trim(); 
					initRef.put(RefId,Refvalue); 
				} 
                	} 
	        } 
		return initRef; 
	} 
 
    public String get_ORBid(String[] args) 
    { 
         
        String ORBid = null; 
        for(int i=0;i<args.length;i++) 
            { 
                if (args[i].startsWith("-ORBid")) 
                    { 
                        int index = args[i].indexOf('='); 
                        if (index != -1) 
                            { 
                                ORBid = args[i].substring(index); 
                                ORBid = ORBid.trim(); 
                            } 
                    } 
            } 
        return ORBid; 
    } 
     
}
