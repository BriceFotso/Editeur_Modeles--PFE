
package ca.iut.cs.IHMMODELE.views.nodes;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import ca.iut.cs.IHMMODELE.geom.Rectangle;
import ca.iut.cs.IHMMODELE.graph.Graph;
import ca.iut.cs.IHMMODELE.graph.nodes.LabelNode;
import ca.iut.cs.IHMMODELE.views.Grid;
import ca.iut.cs.IHMMODELE.views.StringViewer;

/**
 * An object to render a StateNode.
 * 
 * 
 *
 */
public class LabelNodeView extends RectangleBoundedNodeView
{
	private static final int DEFAULT_WIDTH = 80;
	private static final int DEFAULT_HEIGHT = 60;
	private static final int ARC_SIZE = 20;
	private static final StringViewer NAME_VIEWER = new StringViewer(StringViewer.Align.CENTER, false, false);
	
	/**
	 * @param pNode The node to wrap.
	 */
	public LabelNodeView(LabelNode pNode)
	{
		super(pNode, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	private String id()
	{
		return ((LabelNode)node()).getId();
	}
	
	@Override
	public void draw(Graphics2D pGraphics2D)
	{
		super.draw(pGraphics2D);
		pGraphics2D.draw(getShape());
		NAME_VIEWER.draw(id(), pGraphics2D, getBounds());
	}
	
	@Override
	protected Shape getShape()
	{       
		return new RoundRectangle2D.Double(getBounds().getX(), getBounds().getY(), 
				getBounds().getWidth(), getBounds().getHeight(), ARC_SIZE, ARC_SIZE);
	}
	
	@Override	
	public void layout(Graph pGraph)
	{
		Rectangle bounds = NAME_VIEWER.getBounds(id());
		bounds = new Rectangle(getBounds().getX(), getBounds().getY(), 
				Math.max(bounds.getWidth(), DEFAULT_WIDTH), Math.max(bounds.getHeight(), DEFAULT_HEIGHT));
		setBounds(Grid.snapped(bounds));
	}
}
