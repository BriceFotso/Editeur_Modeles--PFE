
package ca.iut.cs.IHMMODELE.graph.edges;

/**
 * An edge with three labels.
 * 
 * 
 */
public abstract class ThreeLabelEdge extends SingleLabelEdge
{
	private String aStartLabel = "";
	private String aEndLabel = "";
	
	/**
	 * @param pLabel The new start label.
	 */
	public void setStartLabel(String pLabel)
	{
		aStartLabel = pLabel;
	}
	
	/**
	 * @param pLabel The new end label.
	 */
	public void setEndLabel(String pLabel)
	{
		aEndLabel = pLabel;
	}
	
	/**
	 * @return The start label.
	 */
	public String getStartLabel()
	{
		return aStartLabel;
	}
	
	/**
	 * @return The middle label.
	 */
	public String getEndLabel()
	{
		return aEndLabel;
	}
	
	@Override
	protected void buildProperties()
	{
		super.buildProperties();
		properties().addAt("startLabel", ()-> aStartLabel, pLabel -> aStartLabel = (String) pLabel, 0);
		properties().add("endLabel", ()-> aEndLabel, pLabel -> aEndLabel = (String) pLabel);
	}
}
