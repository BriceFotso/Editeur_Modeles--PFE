

package ca.iut.cs.IHMMODELE.graph.edges;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import ca.iut.cs.IHMMODELE.geom.Rectangle;
import ca.iut.cs.IHMMODELE.graph.Edge;
import ca.iut.cs.IHMMODELE.graph.Graph;
import ca.iut.cs.IHMMODELE.graph.Node;
import ca.iut.cs.IHMMODELE.graph.nodes.PointNode;
import ca.iut.cs.IHMMODELE.views.ArrowHead;
import ca.iut.cs.IHMMODELE.views.LineStyle;
import ca.iut.cs.IHMMODELE.views.edges.EdgeView;
import ca.iut.cs.IHMMODELE.views.edges.SegmentationStyle;
import ca.iut.cs.IHMMODELE.views.edges.SegmentedEdgeView;

/**
 *  An edge that joins two call nodes.
 */
public class ReturnEdge extends SingleLabelEdge
{
	@Override
	protected EdgeView generateView()
	{
		return new SegmentedEdgeView(this, createSegmentationStyle(), () -> LineStyle.DOTTED,
				() -> ArrowHead.NONE, ()->ArrowHead.V, ()->"", ()->getMiddleLabel(), ()->"");
	}
	
	private SegmentationStyle createSegmentationStyle()
	{
		return new SegmentationStyle()
		{
			@Override
			public boolean isPossible(Edge pEdge)
			{
				assert false; // Should not be called.
				return false;
			}

			@Override
			public Point2D[] getPath(Edge pEdge, Graph pGraph)
			{
				return getPoints(pEdge);
			}

			@Override
			public Side getAttachedSide(Edge pEdge, Node pNode)
			{
				assert false; // Should not be called
				return null;
			}
		};
	}
	
	private static Point2D[] getPoints(Edge pEdge)
	{
		ArrayList<Point2D> lReturn = new ArrayList<>();
		Rectangle start = pEdge.getStart().view().getBounds();
		Rectangle end = pEdge.getEnd().view().getBounds();
		if(pEdge.getEnd() instanceof PointNode) // show nicely in tool bar
		{
			lReturn.add(new Point2D.Double(end.getX(), end.getY()));
			lReturn.add(new Point2D.Double(start.getMaxX(), end.getY()));
		}      
		else if(start.getCenter().getX() < end.getCenter().getX())
		{
			lReturn.add(new Point2D.Double(start.getMaxX(), start.getMaxY()));
			lReturn.add(new Point2D.Double(end.getX(), start.getMaxY()));
		}
		else
		{
			lReturn.add(new Point2D.Double(start.getX(), start.getMaxY()));
			lReturn.add(new Point2D.Double(end.getMaxX(), start.getMaxY()));
		}
		return lReturn.toArray(new Point2D[lReturn.size()]);
	}
}
