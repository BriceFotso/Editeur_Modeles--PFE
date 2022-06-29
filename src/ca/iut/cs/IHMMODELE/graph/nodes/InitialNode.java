

package ca.iut.cs.IHMMODELE.graph.nodes;

import ca.iut.cs.IHMMODELE.views.nodes.CircularStateNodeView;
import ca.iut.cs.IHMMODELE.views.nodes.NodeView;

/**
 * An initial in a state diagram.
 */
public class InitialNode extends AbstractNode
{
	@Override
	protected NodeView generateView()
	{
		return new CircularStateNodeView(this, false);
	}
}

