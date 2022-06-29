
package ca.iut.cs.IHMMODELE.views.nodes;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import ca.iut.cs.IHMMODELE.geom.Direction;
import ca.iut.cs.IHMMODELE.geom.Point;
import ca.iut.cs.IHMMODELE.geom.Rectangle;
import ca.iut.cs.IHMMODELE.graph.Graph;
import ca.iut.cs.IHMMODELE.graph.Node;
import ca.iut.cs.IHMMODELE.views.Grid;

/**
 * An object to render a CircularStateNode.
 * 
 * 
 *
 */
public class CircularStateNodeView extends AbstractNodeView
{
	private static final int DIAMETER = 20;
	private static final int DEFAULT_GAP = 3;   
	private final boolean aFinal;
	
	/**
	 * @param pNode The node to wrap.
	 * @param pFinal true if this is a final node, false if it's an initial node.
	 */
	public CircularStateNodeView(Node pNode, boolean pFinal)
	{
		super(pNode);
		aFinal = pFinal;
	}
	
	@Override
	public void draw(Graphics2D pGraphics2D)
	{
		super.draw(pGraphics2D);
		Ellipse2D circle = new Ellipse2D.Double(node().position().getX(), node().position().getY(), 
				DIAMETER, DIAMETER);
      
      	if(aFinal)
      	{
      		Ellipse2D inside = new Ellipse2D.Double( node().position().getX() + DEFAULT_GAP, 
      				node().position().getY() + DEFAULT_GAP, DIAMETER - 2 * DEFAULT_GAP, DIAMETER - 2 * DEFAULT_GAP);
      		pGraphics2D.fill(inside);
      		pGraphics2D.draw(circle);
      	}
		else
		{
			pGraphics2D.fill(circle);
		}      
	}
	
	@Override
	public Point getConnectionPoint(Direction pDirection)
	{
		Rectangle bounds = getBounds();
		double a = bounds.getWidth() / 2;
		double b = bounds.getHeight() / 2;
		double x = pDirection.getX();
		double y = pDirection.getY();
		double cx = bounds.getCenter().getX();
		double cy = bounds.getCenter().getY();
      
		if(a != 0 && b != 0 && !(x == 0 && y == 0))
		{
			double t = Math.sqrt((x * x) / (a * a) + (y * y) / (b * b));
			return new Point(cx + x / t, cy + y / t);
		}
		else
		{
			return new Point(cx, cy);
		}
	}   	 
	
	@Override
	public Shape getShape()
	{
		return new Ellipse2D.Double(getBounds().getX(), getBounds().getY(), DIAMETER - 1, DIAMETER - 1);
	}

	@Override
	public Rectangle getBounds()
	{
		return new Rectangle(node().position().getX(), node().position().getY(), DIAMETER, DIAMETER);
	}
	
	@Override
	public void layout(Graph pGraph)
	{
		node().moveTo(Grid.snapped(getBounds()).getOrigin());
	}

	@Override
	public boolean contains(Point pPoint)
	{
		return getBounds().contains(pPoint);
	}
}
