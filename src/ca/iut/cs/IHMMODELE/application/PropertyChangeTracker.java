
package ca.iut.cs.IHMMODELE.application;

import java.util.HashMap;

import ca.iut.cs.IHMMODELE.commands.ChangePropertyCommand;
import ca.iut.cs.IHMMODELE.commands.CompoundCommand;
import ca.iut.cs.IHMMODELE.graph.GraphElement;
import ca.iut.cs.IHMMODELE.graph.Properties;
import ca.iut.cs.IHMMODELE.graph.Property;

/**
 * Tracks modification to the properties of a GraphElement.
 * Should be discarded after a call to stopTracking().
 * 
 *
 */
public class PropertyChangeTracker 
{
	private HashMap<String, Object> aOldValues = new HashMap<>();
	private Properties aProperties;
	
	/**
	 * Creates a new tracker for pEdited.
	 *  
	 * @param pEdited The element to track.
	 * @pre pEdited != null;
	 */
	public PropertyChangeTracker(GraphElement pEdited)
	{
		assert pEdited != null;
		aProperties = pEdited.properties();
	}

	/**
	 * Makes a snapshot of the properties values of the tracked element.
	 * 
	 */
	public void startTracking()
	{
		for( Property property : aProperties )
		{
			aOldValues.put(property.getName(), property.get());
		}
	}
	
	/**
	 * Creates and returns a CompoundCommand that represents any change
	 * in properties detected between the time startTracking
	 * and stopTracking were called.
	 * 
	 * @return A CompoundCommand describing the property changes.
	 */
	public CompoundCommand stopTracking()
	{
		CompoundCommand command = new CompoundCommand();
		for( Property property : aProperties )
		{
			if( !aOldValues.get(property.getName()).equals(property.get()))
			{
				command.add(new ChangePropertyCommand(property, aOldValues.get(property.getName()), property.get()));
			}
		}
		return command;
	}
}
