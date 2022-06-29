
package ca.iut.cs.IHMMODELE.graph.nodes;

import ca.iut.cs.IHMMODELE.views.nodes.NodeView;
import ca.iut.cs.IHMMODELE.views.nodes.TextNodeView;

/**
   A state node in a state diagram.
*/
public class TextNode extends NamedNodeText
{
	@Override
	protected NodeView generateView()
	{
		return new TextNodeView(this);
	}
}
