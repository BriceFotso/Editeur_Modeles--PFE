

package ca.iut.cs.IHMMODELE.diagrams;

import java.util.ResourceBundle;

import ca.iut.cs.IHMMODELE.geom.Point;
import ca.iut.cs.IHMMODELE.graph.Edge;
import ca.iut.cs.IHMMODELE.graph.Graph;
import ca.iut.cs.IHMMODELE.graph.Node;
import ca.iut.cs.IHMMODELE.graph.edges.NoteEdge;
import ca.iut.cs.IHMMODELE.graph.edges.TransitionEdge;
import ca.iut.cs.IHMMODELE.graph.nodes.ButtonNode;
import ca.iut.cs.IHMMODELE.graph.nodes.EndNode;
import ca.iut.cs.IHMMODELE.graph.nodes.InitialNode;
import ca.iut.cs.IHMMODELE.graph.nodes.NoteNode;
import ca.iut.cs.IHMMODELE.graph.nodes.TextNode;
import ca.iut.cs.IHMMODELE.graph.nodes.LabelNode;


public class FormDiagramGraph extends Graph
{
	private static final Node[] NODE_PROTOTYPES = new Node[]{new InitialNode(),new EndNode(), new LabelNode(),new ButtonNode(), new TextNode()};
	private static final Edge[] EDGE_PROTOTYPES = new Edge[]{new TransitionEdge()};
	
	@Override
	public Node[] getNodePrototypes()
	{
		return NODE_PROTOTYPES;
	}

	@Override
	public Edge[] getEdgePrototypes()
	{
		return EDGE_PROTOTYPES;
	}
	
	@Override
	public String getFileExtension() 
	{
		return ResourceBundle.getBundle("ca.iut.cs.IHMMODELE.FormEditorStrings").getString("Form.extension");
	}

	@Override
	public String getDescription() 
	{
		return ResourceBundle.getBundle("ca.iut.cs.IHMMODELE.FormEditorStrings").getString("Form.name");
	}
	
	// CSOFF:
	@Override
	public boolean canConnect(Edge pEdge, Node pNode1, Node pNode2, Point pPoint2)
	{
		if( pNode2 == null )
		{
			return false;
		}
		if( numberOfSimilarEdges(pNode1, pNode2) > 1 )
		{
			return false;
		}
		if((pNode2 instanceof NoteNode || pNode1 instanceof NoteNode) && !(pEdge instanceof NoteEdge))
		{
			return false;
		}
		if( pEdge instanceof NoteEdge && !(pNode1 instanceof NoteNode || pNode2 instanceof NoteNode))
		{
			return false;
		}
		if(pNode1 != null)
		{
			if(pNode1 instanceof EndNode)
			{
				if(!(pEdge instanceof NoteEdge))
				{
					return false;
				}
			}
		}
		if(pNode2 instanceof InitialNode)
		{
			if(!(pEdge instanceof NoteEdge))
			{
				return false;
			}
		}
		return true;
	} // CSON:
	
	private int numberOfSimilarEdges(Node pNode1, Node pNode2)
	{
		int lReturn = 0;
		for( Edge edge : getEdges() )
		{
			if( edge.getStart() == pNode1 && edge.getEnd() == pNode2 )
			{
				lReturn++;
			}
		}
		return lReturn;
	}
}





