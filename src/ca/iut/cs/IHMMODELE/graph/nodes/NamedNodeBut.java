

package ca.iut.cs.IHMMODELE.graph.nodes;

/**
   A node with a name.
*/
public abstract class NamedNodeBut extends AbstractNode
{
	private String aName = "";
	private String aValue = "";
	private String aType = "";
	private String aId = "";

	//setters
	public void setName(String pName)
	{
		aName = pName;
	}
	public void setValue(String pValue)
	{
		aValue = pValue;
	}
	public void setType(String pType)
	{
		aType = pType;
	}
	public void setId(String pId)
	{
		aId = pId;
	}

	//getter
	public String getName()
	{
		return aName;
	}
	public String getValue()
	{
		return aValue;
	}
	public String getType()
	{
		return aType;
	}
	public String getId()
	{
		return aId;
	}
	
	
	@Override
	protected void buildProperties()
	{
		super.buildProperties();
		properties().add("id", () -> aId, pId -> aId = (String)pId);
		properties().add("name", () -> aName, pName -> aName = (String)pName);
		properties().add("value", () -> aValue, pValue -> aValue = (String)pValue);
		properties().add("type", () -> aType, pType -> aType = (String)pType);
	}
}
