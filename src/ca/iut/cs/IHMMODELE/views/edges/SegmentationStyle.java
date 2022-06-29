

package ca.iut.cs.IHMMODELE.views.edges;

import java.awt.geom.Point2D;

import ca.iut.cs.IHMMODELE.geom.Direction;
import ca.iut.cs.IHMMODELE.graph.Edge;
import ca.iut.cs.IHMMODELE.graph.Graph;
import ca.iut.cs.IHMMODELE.graph.Node;

/**
 * A strategy for drawing a segmented line between two nodes.
 * 
 
 *
 */
public interface SegmentationStyle
{
	/**
	 * The side of a rectangle.
	 * This seems to be redundant with Direction, but to 
	 * overload Direction to mean both a side and a direction is
	 * confusing.
	 */
	enum Side
	{WEST, NORTH, EAST, SOUTH;
		
		boolean isEastWest() 
		{ return this == WEST || this == EAST; }
		
		Direction getDirection()
		{
			switch(this)
			{
			case WEST:
				return Direction.WEST;
			case NORTH:
				return Direction.NORTH;
			case EAST:
				return Direction.EAST;
			case SOUTH:
				return Direction.SOUTH;
			default:
				return null;
			}
		}
		
		Side flip()
		{
			switch(this)
			{
			case WEST:
				return EAST;
			case NORTH:
				return SOUTH;
			case EAST:
				return WEST;
			case SOUTH:
				return NORTH;
			default:
				return null;
			}
		}
	}
	
	/**
	 * Determines if it is possible to use this segmentation style.
	 * @param pEdge The edge to draw
	 * @return true if it is possible to use the segmentation style.
	 */
	boolean isPossible(Edge pEdge);
	
	/**
     * Gets the points at which the line representing an
     * edge is bent according to this strategy.
     * @param pEdge the Edge for which a path is determine
     * @param pGraph the graph holding the edge. Can be null.
     * @return an array list of points at which to bend the
     * segmented line representing the edge. Never null.
	 */
	Point2D[] getPath(Edge pEdge, Graph pGraph);
	
	/**
	 * Returns which side of the node attached to
	 * an edge is attached to the edge.
	 * @param pEdge The edge to check.
	 * @param pNode The node to check.
	 * @return The side the edge leaves from.
	 * @pre pNode == pEdge.getStart() || pNode == pEdge.getEnd()
	 */
	Side getAttachedSide(Edge pEdge, Node pNode);
}
