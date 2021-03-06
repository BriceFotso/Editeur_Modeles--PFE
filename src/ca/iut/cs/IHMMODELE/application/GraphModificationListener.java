
package ca.iut.cs.IHMMODELE.application;

import ca.iut.cs.IHMMODELE.graph.Edge;
import ca.iut.cs.IHMMODELE.graph.Graph;
import ca.iut.cs.IHMMODELE.graph.Node;
import ca.iut.cs.IHMMODELE.graph.Property;

/**
 * Specifies a list of callback methods for any object
 * interested in modifications to a graph. Maps to the 
 * Observer interface in the Observer design pattern.
 * 
 *  - Initial code
 *  Observer inteface refactoring.
 *
 */
public interface GraphModificationListener
{
	/**
	 * Called whenever a node is added to a graph.
	 * @param pGraph The target graph.
	 * @param pNode The node added.
	 */
	void nodeAdded(Graph pGraph, Node pNode);
	
	/**
	 * Called whenever a node is removed from a graph.
	 * @param pGraph The target graph.
	 * @param pNode The node removed.
	 */
	void nodeRemoved(Graph pGraph, Node pNode);

	/**
	 * Called whenever an edge is added to a graph.
	 * @param pGraph The target graph.
	 * @param pEdge The edge added
	 */
	void edgeAdded(Graph pGraph, Edge pEdge);

	/**
	 * Called whenever an edge is removed from a graph.
	 * @param pGraph The target graph.
	 * @param pEdge The edge removed
	 */
	void edgeRemoved(Graph pGraph, Edge pEdge);

	/**
	 * Indicates that the graph is about to be modified
	 * through multiple related operations.
	 */
	void startingCompoundOperation();
	
	/**
	 * Indicates that a compound operation has been
	 * completed. 
	 */
	void finishingCompoundOperation();
	
	/**
	 * Notifies implementers that a property of a graph element has
	 * a new value.
	 * 
	 * @param pProperty The changed property.
	 * @param pOldValue The value of the property before the change.
	 */
	void propertyChanged(Property pProperty, Object pOldValue);
}
