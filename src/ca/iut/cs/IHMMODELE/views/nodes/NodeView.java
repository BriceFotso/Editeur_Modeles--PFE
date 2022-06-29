
package ca.iut.cs.IHMMODELE.views.nodes;

import java.awt.Graphics2D;

import ca.iut.cs.IHMMODELE.geom.Direction;
import ca.iut.cs.IHMMODELE.geom.Point;
import ca.iut.cs.IHMMODELE.geom.Rectangle;
import ca.iut.cs.IHMMODELE.graph.Graph;

/**
 * Services to query the visual properties of a node.
 * 
 * 
 */
public interface NodeView
{
	/**
     * Gets the smallest rectangle that bounds this node.
     * The bounding rectangle contains all labels.
     * @return the bounding rectangle
   	 */
	Rectangle getBounds();
	
	/**
     *  Draw the node.
     * @param pGraphics2D the graphics context
     */
	void draw(Graphics2D pGraphics2D);

	/**
     * Tests whether the node contains a point.
     * @param pPoint the point to test
     * @return true if this node contains aPoint
     */
	boolean contains(Point pPoint);

	/**
     * Get the best connection point to connect this node 
     * with another node. This should be a point on the boundary
     * of the shape of this node.
     * @param pDirection the direction from the center 
     * of the bounding rectangle towards the boundary 
     * @return the recommended connection point
	 */
	Point getConnectionPoint(Direction pDirection);

	/**
     * Lays out the node and its children.
     * @param pGraph the ambient graph
	 */
	void layout(Graph pGraph);
}
