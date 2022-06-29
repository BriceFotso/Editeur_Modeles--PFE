
package ca.iut.cs.IHMMODELE.commands;

import ca.iut.cs.IHMMODELE.graph.Graph;
import ca.iut.cs.IHMMODELE.graph.GraphElement;

/**
 * A command that involves a single graph element.
 * 
 * 
 */
abstract class GraphElementRelatedCommand implements Command
{
	protected GraphElement aElement;
	protected Graph aGraph;
	
	/**
	 * Creates the command.
	 * @param pGraph The target graph.
	 * @param pElement The related element
	 */
	protected GraphElementRelatedCommand(Graph pGraph, GraphElement pElement)
	{
		aGraph = pGraph;
		aElement = pElement;
	}
}
