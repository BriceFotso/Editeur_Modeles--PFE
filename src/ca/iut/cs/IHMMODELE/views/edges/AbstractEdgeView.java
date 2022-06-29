
package ca.iut.cs.IHMMODELE.views.edges;

import java.awt.BasicStroke;
import java.awt.Shape;

import ca.iut.cs.IHMMODELE.geom.Conversions;
import ca.iut.cs.IHMMODELE.geom.Direction;
import ca.iut.cs.IHMMODELE.geom.Line;
import ca.iut.cs.IHMMODELE.geom.Point;
import ca.iut.cs.IHMMODELE.geom.Rectangle;
import ca.iut.cs.IHMMODELE.graph.Edge;

/**
 * Provides shared services for rendering an edge.
 * 
 * 
 *
 */
public abstract class AbstractEdgeView implements EdgeView
{
	private static final int MAX_DISTANCE = 3;
	private static final int DEGREES_180 = 180;
	
	private Edge aEdge;
	
	/**
	 * @param pEdge The edge to wrap.
	 */
	protected AbstractEdgeView(Edge pEdge)
	{
		aEdge = pEdge;
	}
	
	/**
	 * @return The shape.
	 */
	protected abstract Shape getShape();
	
	/**
	 * @return The wrapped edge.
	 */
	protected Edge edge()
	{
		return aEdge;
	}
	
	@Override
	public boolean contains(Point pPoint)
	{
		Line conn = getConnectionPoints();
		if(pPoint.distance(conn.getPoint1()) <= MAX_DISTANCE || pPoint.distance(conn.getPoint2()) <= MAX_DISTANCE)
		{
			return false;
		}

		Shape fatPath = new BasicStroke((float)(2 * MAX_DISTANCE)).createStrokedShape(getShape());
		return fatPath.contains(Conversions.toPoint2D(pPoint));
	}
	
	@Override
	public Rectangle getBounds()
	{
		return Conversions.toRectangle(getShape().getBounds()); 
	}
	
	/** 
	 * The default behavior implemented by this method
	 * is to find the connection point that each start/end
	 * node provides for a direction that is oriented
	 * following a straight line connecting the center
	 * of the rectangular bounds for each node.
	 */
	@Override
	public Line getConnectionPoints()
	{
		Rectangle startBounds = edge().getStart().view().getBounds();
		Rectangle endBounds = edge().getEnd().view().getBounds();
		Point startCenter = startBounds.getCenter();
		Point endCenter = endBounds.getCenter();
		Direction toEnd = new Direction(startCenter, endCenter);
		return new Line(edge().getStart().view().getConnectionPoint(toEnd), 
				edge().getEnd().view().getConnectionPoint(toEnd.turn(DEGREES_180)));
	}
	
	/**
	 * Wrap the string in an html container and 
	 * escape the angle brackets.
	 * @param pRawLabel The initial string.
	 * @pre pRawLabel != null;
	 * @return The string prepared for rendering as HTML
	 */
	protected static String toHtml(String pRawLabel)
	{
		assert pRawLabel != null;
		StringBuilder lReturn = new StringBuilder();
		lReturn.append("<html>");
		lReturn.append(pRawLabel.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
		lReturn.append("</html>");
		return lReturn.toString();
	}
}
