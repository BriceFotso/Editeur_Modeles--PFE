

package ca.iut.cs.IHMMODELE.graph.nodes;

import ca.iut.cs.IHMMODELE.views.nodes.NodeView;
import ca.iut.cs.IHMMODELE.views.nodes.NoteNodeView;

/**
 *  A note node in a UML diagram. The name of the node
 *  is the text of the note.
 */
public class NoteNode extends NamedNode
{
	@Override
	protected NodeView generateView()
	{
		return new NoteNodeView(this);
	}
}
