package org.sorbet.PortableServer; 
/**Sorbet version 2.5*/ 
/**
 * @author PortableServerTeam 
 * @version 2.5
 */
public class ObjectId 
{ 
	private Integer value ; 
 
    public ObjectId(int value) 
    { 
        this.value = new Integer(value) ; 
    } 
	public void setValue(Integer v) 
    { 
		this.value = v ; 
    } 
    public void setValue(int v) 
    { 
		this.value = new Integer(v); 
    } 
    public Integer getValue() 
    { 
        return this.value ; 
    } 
}
