
package ca.iut.cs.IHMMODELE.graph.nodes;

import ca.iut.cs.IHMMODELE.views.nodes.NodeView;
import ca.iut.cs.IHMMODELE.views.nodes.LabelNodeView;

/**
   A label node in a form diagram.
*/
public class LabelNode extends NamedNodeLab
{
	@Override
	protected NodeView generateView()
	{
		return new LabelNodeView(this);
	}
}
