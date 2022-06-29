
package ca.iut.cs.IHMMODELE.graph.edges;

import ca.iut.cs.IHMMODELE.graph.AbstractGraphElement;
import ca.iut.cs.IHMMODELE.graph.Edge;
import ca.iut.cs.IHMMODELE.graph.Graph;
import ca.iut.cs.IHMMODELE.graph.Node;
import ca.iut.cs.IHMMODELE.views.edges.EdgeView;

/**
 * Abstract edge in the new hierarchy.
 * 
 * 
 */
public abstract class AbstractEdge extends AbstractGraphElement implements Edge
{
	protected EdgeView aView;
	private Node aStart;
	private Node aEnd;
	private Graph aGraph;
	
	/**
	 * Calls an abstract delegate to generate the view for this edge.
	 */
	protected AbstractEdge()
	{
		aView = generateView();
	}
	
	@Override
	public void connect(Node pStart, Node pEnd, Graph pGraph)
	{
		assert pStart != null && pEnd != null;
		aStart = pStart;
		aEnd = pEnd;
		aGraph = pGraph;		
	}

	@Override
	public Node getStart()
	{
		return aStart;
	}

	@Override
	public Node getEnd()
	{
		return aEnd;
	}

	@Override
	public Graph getGraph()
	{
		return aGraph;
	}

	/**
	 * Generates a view for this edge. Because of cloning, this cannot
	 * be done in the constructor, because when an edge is clone a new 
	 * wrapper view must be produced for the clone.
	 * 
	 * @return The view that wraps this edge.
	 */
	protected abstract EdgeView generateView();
	
	@Override
	public AbstractEdge clone()
	{
		AbstractEdge clone = (AbstractEdge) super.clone();
		clone.aView = clone.generateView();
		return clone;
	}
	
	@Override
	public EdgeView view()
	{
		return aView;
	}
	
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + " " + getStart() + " -> " + getEnd();
	}
}
