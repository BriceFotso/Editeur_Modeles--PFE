

package ca.iut.cs.IHMMODELE.graph;

import ca.iut.cs.IHMMODELE.views.edges.EdgeView;

/**
 * An edge in a graph.
 */
public interface Edge extends GraphElement
{
   	/**
     * Connect this edge to two nodes.
     * @param pStart the starting node
     * @param pEnd the ending node
     * @param pGraph the graph where the two connected nodes 
     * exists. Can be null.
   	 */
   void connect(Node pStart, Node pEnd, Graph pGraph);

   	/**
     * Gets the starting node.
     * @return the starting node
     */
   	Node getStart();

   	/**
     * Gets the ending node.
     * @return the ending node
   	 */
   	Node getEnd();
   	
   	/**
   	 * @return The graph that contains this edge.
   	 */
   	Graph getGraph();

   	/**
   	 * @return A clone of this edge, with shallow cloning
   	 * of the start and end nodes (i.e., the start and end 
   	 * nodes are not cloned).
   	 */
   	Edge clone();
   	
   	/**
   	 * @return The view for this edge.
   	 */
   	EdgeView view();
}

