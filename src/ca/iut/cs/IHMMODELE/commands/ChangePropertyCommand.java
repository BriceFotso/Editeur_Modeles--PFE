
package ca.iut.cs.IHMMODELE.commands;

import ca.iut.cs.IHMMODELE.graph.Property;

/**
 * Represents a change to the property of a GraphElement.
 * 
 * 
 */
public class ChangePropertyCommand implements Command
{
	private Property aProperty;
	private Object aOldValue; 
	private Object aNewValue;
	
	/**
	 * Create a new command.
	 * 
	 * @param pProperty The changed property.
	 * @param pOldValue The former value for the property.
	 * @param pNewValue The value the property should have after executing the command
	 * @pre pProperty != null && pOldValue != null && pNewValue != null
	 */
	public ChangePropertyCommand( Property pProperty, Object pOldValue, Object pNewValue)
	{
		assert pProperty != null && pOldValue != null && pNewValue != null;
		aProperty = pProperty;
		aOldValue = pOldValue;
		aNewValue = pNewValue;
	}
	
	@Override
	public void execute()
	{
		set(aNewValue);
	}

	@Override
	public void undo()
	{
		set(aOldValue);		
	}
	
	private void set(Object pValue)
	{
		if( pValue instanceof Enum )
		{
			aProperty.set(pValue.toString());		
		}
		else
		{
			aProperty.set(pValue);		
		}
	}
}