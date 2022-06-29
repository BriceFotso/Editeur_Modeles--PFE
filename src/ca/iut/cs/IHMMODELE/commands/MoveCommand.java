package ca.iut.cs.IHMMODELE.commands;

import ca.iut.cs.IHMMODELE.graph.Graph;
import ca.iut.cs.IHMMODELE.graph.Node;

/**
 * Stores the moving of a node.
 * @author EJBQ
 */
public class MoveCommand implements Command
{
	private Node aNode;
	private Graph aGraph;
	private int aDX;
	private int aDY;
	
	/**
	 * Creates the command.
	 * @param pGraph The panel being moved on
	 * @param pNode The node being moved
	 * @param pDX The amount moved horizontally
	 * @param pDY The amount moved vertically
	 */
	public MoveCommand(Graph pGraph, Node pNode, int pDX, int pDY)
	{
		aGraph = pGraph;
		aNode = pNode;
		aDX = pDX;
		aDY = pDY;
	}
	
	/**
	 * Undoes the command and moves the node back where it came from.
	 */
	public void undo() 
	{
		aNode.translate(-aDX, -aDY);
		aGraph.requestLayout();
	}

	/**
	 * Performs the command and moves the node.
	 */
	public void execute() 
	{
		aNode.translate(aDX, aDY);
		aGraph.requestLayout();
	}

}
