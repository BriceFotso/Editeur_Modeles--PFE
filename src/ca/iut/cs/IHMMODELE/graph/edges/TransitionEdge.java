

package ca.iut.cs.IHMMODELE.graph.edges;

import ca.iut.cs.IHMMODELE.views.edges.EdgeView;
import ca.iut.cs.IHMMODELE.views.edges.TransitionEdgeView;

/**
 *  A curved edge for a state transition in a state diagram. The
 *  edge has two natures, either a self-edge, or a inter-node 
 *  edge.
 *  
 *  
 */
public class TransitionEdge extends SingleLabelEdge
{
	@Override
	protected EdgeView generateView()
	{
		return new TransitionEdgeView(this);
	}
}
