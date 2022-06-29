

package ca.iut.cs.IHMMODELE.graph.nodes;


public abstract class NamedNodeLab extends AbstractNode
{
	private String aValue = "";
	private String aId = "";

	//setters
	
	public void setValue(String pValue)
	{
		aValue = pValue;
	}

	public void setId(String pId)
	{
		aId = pId;
	}

	//getter
	
	public String getValue()
	{
		return aValue;
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
		properties().add("value", () -> aValue, pValue -> aValue = (String)pValue);
	}
}
