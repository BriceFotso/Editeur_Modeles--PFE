
package ca.iut.cs.IHMMODELE.graph.nodes;

import ca.iut.cs.IHMMODELE.views.nodes.NodeView;
import ca.iut.cs.IHMMODELE.views.nodes.ButtonNodeView;


/**
   A button node in a form diagram.
*/
public class ButtonNode extends NamedNodeBut
{
	@Override
	protected NodeView generateView()
	{
		return new ButtonNodeView(this);
	}
}
