package ca.iut.cs.IHMMODELE.commands;

import ca.iut.cs.IHMMODELE.graph.Graph;
import ca.iut.cs.IHMMODELE.graph.Node;

/**
 * Represents the removal of a node from the graph.
 * 
 * 
 */
public class DeleteNodeCommand extends GraphElementRelatedCommand
{
	/**
	 * Creates the command.
	 * @param pGraph The graph the node was removed from.
	 * @param pNode The node removed.
	 */
	public DeleteNodeCommand(Graph pGraph, Node pNode)
	{
		super( pGraph, pNode );
	}
	
	/**
	 * Undoes the command and adds/deletes the node.
	 */
	public void undo() 
	{
		aGraph.insertNode((Node)aElement);
	}

	/**
	 * Performs the command and adds/deletes the node.
	 */
	public void execute() 
	{
		aGraph.removeNode((Node)aElement);
	}
}
