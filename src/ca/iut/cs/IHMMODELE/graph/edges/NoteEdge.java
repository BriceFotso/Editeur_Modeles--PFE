

package ca.iut.cs.IHMMODELE.graph.edges;

import ca.iut.cs.IHMMODELE.views.edges.EdgeView;
import ca.iut.cs.IHMMODELE.views.edges.NoteEdgeView;

/**
 *  A dotted line that connects a note to its attachment.
 */
public class NoteEdge extends AbstractEdge
{
	@Override
	protected EdgeView generateView()
	{
		return new NoteEdgeView(this);
	}
}
