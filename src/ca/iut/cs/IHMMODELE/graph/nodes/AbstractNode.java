
package ca.iut.cs.IHMMODELE.graph.nodes;

import ca.iut.cs.IHMMODELE.geom.Point;
import ca.iut.cs.IHMMODELE.graph.AbstractGraphElement;
import ca.iut.cs.IHMMODELE.graph.Node;
import ca.iut.cs.IHMMODELE.views.nodes.NodeView;

/**
 * Common elements for the Node hierarchy.
 * 
 *
 *
 */
public abstract class AbstractNode extends AbstractGraphElement implements Node
{
	private NodeView aView;
	private Point aPosition = new Point(0, 0);
	
	/**
	 * Calls an abstract delegate to generate the view for this node
	 * and positions the node at (0,0).
	 */
	protected AbstractNode()
	{
		aView = generateView();
	}
	
	@Override
	public void translate(int pDeltaX, int pDeltaY)
	{
		aPosition = new Point( aPosition.getX() + pDeltaX, aPosition.getY() + pDeltaY );
	}
	
	/**
	 * Generates a view for this node. Because of cloning, this cannot
	 * be done in the constructor, because when a node is cloned a new 
	 * wrapper view must be produced for the clone.
	 * 
	 * @return The view that wraps this node.
	 */
	protected abstract NodeView generateView();
	
	@Override
	public NodeView view()
	{
		return aView;
	}
	
	@Override
	public Point position()
	{
		return aPosition;
	}
	
	@Override
	public void moveTo(Point pPoint)
	{
		aPosition = pPoint;
	}

	@Override
	public AbstractNode clone()
	{
		AbstractNode clone = (AbstractNode) super.clone();
		clone.aView = clone.generateView();
		return clone;
	}
	
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + " " + view().getBounds();
	}
	
	@Override
	protected void buildProperties()
	{
		super.buildProperties();
		properties().addInvisible("x", () -> aPosition.getX(), pX -> aPosition.setX((int)pX)); 
		properties().addInvisible("y", () -> aPosition.getY(), pY -> aPosition.setY((int)pY));
	}
}
