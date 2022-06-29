

package ca.iut.cs.IHMMODELE.graph.nodes;

import ca.iut.cs.IHMMODELE.views.nodes.NodeView;
import ca.iut.cs.IHMMODELE.views.nodes.PointNodeView;

/**
 *  An invisible node that is used in the toolbar to draw an
 *  edge, and in notes to serve as an end point of the node
 *  connector.
 */
public class PointNode extends AbstractNode
{
	@Override
	protected NodeView generateView()
	{
		return new PointNodeView(this);
	}
}
