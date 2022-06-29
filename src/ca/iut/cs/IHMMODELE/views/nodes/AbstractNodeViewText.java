
package ca.iut.cs.IHMMODELE.views.nodes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

import ca.iut.cs.IHMMODELE.graph.Graph;
import ca.iut.cs.IHMMODELE.graph.Node;

/**
 * Basic services for drawing nodes.
 * 
 * 
 *
 */
public abstract class AbstractNodeViewText implements NodeView
{
	public static final int SHADOW_GAP = 4;
	private static final Color SHADOW_COLOR = Color.MAGENTA;
	
	private Node aNode;
	
	/**
	 * @param pNode The node to wrap.
	 */
	protected AbstractNodeViewText(Node pNode)
	{
		aNode = pNode;
	}
	
	/**
	 * @return The wrapped edge.
	 */
	protected Node node()
	{
		return aNode;
	}
	
	@Override
	public void draw(Graphics2D pGraphics2D)
	{
		Shape shape = getShape();
		Color oldColor = pGraphics2D.getColor();
		pGraphics2D.translate(SHADOW_GAP, SHADOW_GAP);      
		pGraphics2D.setColor(SHADOW_COLOR);
		pGraphics2D.fill(shape);
		pGraphics2D.translate(-SHADOW_GAP, -SHADOW_GAP);
		pGraphics2D.setColor(pGraphics2D.getBackground());
		pGraphics2D.fill(shape);      
		pGraphics2D.setColor(oldColor);
	}
	
	/**
     *  @return the shape to be used for computing the drop shadow
    */
	protected abstract Shape getShape();

	@Override
	public void layout(Graph pGraph)
	{}
}
