

package ca.iut.cs.IHMMODELE.graph.nodes;

/**
   A node with a name.
*/
public abstract class NamedNode extends AbstractNode
{
	private String aName = "";

	/**
     * Sets the name property value.
     * @param pName the new state name
	 */
	public void setName(String pName)
	{
		aName = pName;
	}

	/**
     * Gets the name property value.
     * @return the state name
	 */
	public String getName()
	{
		return aName;
	}
	
	@Override
	protected void buildProperties()
	{
		super.buildProperties();
		properties().add("name", () -> aName, pName -> aName = (String)pName);
	}
}
