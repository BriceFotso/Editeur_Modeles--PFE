
package ca.iut.cs.IHMMODELE.graph;

import ca.iut.cs.IHMMODELE.geom.Point;
import ca.iut.cs.IHMMODELE.views.nodes.NodeView;

/**
  * A node in a graph.
  */
public interface Node extends GraphElement
{
	/**
	 * @return The position of this node. Usually corresponds to the top left corner 
	 * of its bounding box.
	 */
	Point position();

	/**
	 * Move the position of the node to pPoint.
	 * 
	 * @param pPoint The new position of the node.
	 */
	void moveTo(Point pPoint);

	/**
     * Translates the node by a given amount.
     * @param pDeltaX the amount to translate in the x-direction
     * @param pDeltaY the amount to translate in the y-direction
	 */
	void translate(int pDeltaX, int pDeltaY);

	/**
	 * @return A clone of the node.
	 */
	Node clone();
	
	/**
	 * @return The view for this node. TODO remove default
	 */
	NodeView view();
}
