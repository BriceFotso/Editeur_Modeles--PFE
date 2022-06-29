
package ca.iut.cs.IHMMODELE.commands;

import ca.iut.cs.IHMMODELE.graph.Edge;
import ca.iut.cs.IHMMODELE.graph.Graph;

/**
 * Represents the removal of an edge to the graph.
 * 
 * 
 */
public class RemoveEdgeCommand extends GraphElementRelatedCommand
{
	/**
	 * Creates the command.
	 * @param pGraph The target graph.
	 * @param pEdge The related edge.
	 */
	public RemoveEdgeCommand(Graph pGraph, Edge pEdge)
	{
		super(pGraph, pEdge);
	}
	
	/**
	 * Undoes the command and adds/deletes the edge.
	 */
	public void undo() 
	{
		assert aElement instanceof Edge;
		aGraph.insertEdge((Edge)aElement);
	}

	/**
	 * Performs the command and adds/deletes the edge.
	 */
	public void execute() 
	{
		assert aElement instanceof Edge;
		aGraph.removeEdge((Edge)aElement);
	}
}
