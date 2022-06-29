package ca.iut.cs.IHMMODELE.commands;

import ca.iut.cs.IHMMODELE.graph.Graph;
import ca.iut.cs.IHMMODELE.graph.Node;

/**
 * Represents the addition of a node to the graph.
 * 
 * 
 */
public class AddNodeCommand extends GraphElementRelatedCommand
{
	/**
	 * Creates the command.
	 * @param pGraph The graph the node was added to.
	 * @param pNode The node added.
	 */
	public AddNodeCommand(Graph pGraph, Node pNode)
	{
		super(pGraph, pNode);
	}
	
	/** 
	 * @see ca.mcgill.cs.jetuml.commands.Command#undo()
	 */
	public void undo() 
	{
		assert aElement instanceof Node;
		aGraph.removeNode((Node)aElement);
		aGraph.requestLayout();
	}

	/**
	 * Performs the command and adds/deletes the node.
	 */
	public void execute() 
	{ 
		assert aElement instanceof Node;
		aGraph.insertNode((Node)aElement);
		aGraph.requestLayout();
	}
}
