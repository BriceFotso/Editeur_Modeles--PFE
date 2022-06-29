
package ca.iut.cs.IHMMODELE.views.edges;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

import ca.iut.cs.IHMMODELE.geom.Direction;
import ca.iut.cs.IHMMODELE.geom.Line;
import ca.iut.cs.IHMMODELE.geom.Point;
import ca.iut.cs.IHMMODELE.geom.Rectangle;
import ca.iut.cs.IHMMODELE.graph.Edge;
import ca.iut.cs.IHMMODELE.views.ArrowHead;

/**
 * An S- or C-shaped edge with an arrowhead.
 */
public class ObjectReferenceEdgeView extends AbstractEdgeView
{
	private static final int ENDSIZE = 10;
	
	/**
	 * @param pEdge the edge to wrap.
	 */
	public ObjectReferenceEdgeView(Edge pEdge)
	{
		super(pEdge);
	}
	
	@Override
	protected Shape getShape()
	{
		Line line = getConnectionPoints();

		double y1 = line.getY1();
		double y2 = line.getY2();
		double xmid = (line.getX1() + line.getX2()) / 2;
		double ymid = (line.getY1() + line.getY2()) / 2;
		GeneralPath path = new GeneralPath();
		if(isSShaped())
		{
			double x1 = line.getX1() + ENDSIZE;
			double x2 = line.getX2() - ENDSIZE;
         
			path.moveTo((float)line.getX1(), (float)y1);
			path.lineTo((float)x1, (float)y1);
			path.quadTo((float)((x1 + xmid) / 2), (float)y1, (float)xmid, (float)ymid);
			path.quadTo((float)((x2 + xmid) / 2), (float)y2, (float)x2, (float)y2);
			path.lineTo((float)line.getX2(), (float)y2);
		}
		else // reverse C shaped
		{
			double x1 = Math.max(line.getX1(), line.getX2()) + ENDSIZE;
			double x2 = x1 + ENDSIZE;
			path.moveTo((float)line.getX1(), (float)y1);
			path.lineTo((float)x1, (float)y1);
			path.quadTo((float)x2, (float)y1, (float)x2, (float)ymid);
			path.quadTo((float)x2, (float)y2, (float)x1, (float)y2);
			path.lineTo((float)line.getX2(), (float)y2);
		}
		return path;
	}
	
	/**
     * 	Tests whether the node should be S- or C-shaped.
     * 	@return true if the node should be S-shaped
	 */
	private boolean isSShaped()
	{
		Rectangle b = edge().getEnd().view().getBounds();
		Point p = edge().getStart().view().getConnectionPoint(Direction.EAST);
		return b.getX() >= p.getX() + 2 * ENDSIZE;
	}

	@Override
	public void draw(Graphics2D pGraphics2D)
	{
		pGraphics2D.draw(getShape());
		Line line = getConnectionPoints();
		double x1;
		double x2 = line.getX2();
		double y = line.getY2();
		if (isSShaped())
		{
			x1 = x2 - ENDSIZE;
		}
		else
		{
			x1 = x2 + ENDSIZE;
		}
		ArrowHead.BLACK_TRIANGLE.view().draw(pGraphics2D, new Point2D.Double(x1, y), new Point2D.Double(x2, y));      
	}

	@Override
	public Line getConnectionPoints()
	{
		Point point = edge().getStart().view().getConnectionPoint(Direction.EAST);
		if (isSShaped())
		{
			return new Line(point, edge().getEnd().view().getConnectionPoint(Direction.WEST));
		}
		else
		{
			return new Line(point, edge().getEnd().view().getConnectionPoint(Direction.EAST));
		}
	}

}
