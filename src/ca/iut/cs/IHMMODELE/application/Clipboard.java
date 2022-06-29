package ca.iut.cs.IHMMODELE.application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ca.iut.cs.IHMMODELE.geom.Rectangle;
import ca.iut.cs.IHMMODELE.graph.Edge;
import ca.iut.cs.IHMMODELE.graph.Graph;
import ca.iut.cs.IHMMODELE.graph.GraphElement;
import ca.iut.cs.IHMMODELE.graph.Node;
import ca.iut.cs.IHMMODELE.graph.nodes.ChildNode;
import ca.iut.cs.IHMMODELE.graph.nodes.ParentNode;
import ca.iut.cs.IHMMODELE.gui.GraphPanel;

/**

 * 
 * Stores a graph subset for purpose of pasting. The clip-board does not
 * accept edges unless both end-points are also being copied.
 * 
 * The Clipboard is a singleton. This is necessary to allow copying elements
 * between diagrams of the same type.
 */
public final class Clipboard 
{
	private static final Clipboard INSTANCE = new Clipboard();
	
	private List<Node> aNodes = new ArrayList<Node>();
	private List<Edge> aEdges = new ArrayList<Edge>();

	/**
	 * Creates an empty clip-board.
	 */
	private Clipboard() 
	{}
	
	/**
	 * @return The Singleton instance of the Clipboard.
	 */
	public static Clipboard instance()
	{
		return INSTANCE;
	}
	
	/* For testing only */
	Collection<Node> getNodes()
	{
		return Collections.unmodifiableCollection(aNodes);
	}
	
	/* For testing only */
	Collection<Edge> getEdges()
	{
		return Collections.unmodifiableCollection(aEdges);
	}

	/**
	 * Clones the selection in pPanel and stores it in the clip-board.
	 * @param pSelection The elements to copy. Cannot be null.
	 */
	public void copy(SelectionList pSelection)
	{
		assert pSelection != null;
		aNodes.clear();
		aEdges.clear();
		
		// First copy the edges so we can assign their end-points when copying nodes.
		// Do not include dangling edges.
		for( GraphElement element : pSelection )
		{
			if( element instanceof Edge && pSelection.capturesEdge((Edge)element ))
			{	
				aEdges.add((Edge)((Edge) element).clone());
			}
		}
		
		// Clone the nodes and re-route their edges
		for( GraphElement element : pSelection )
		{
			if( element instanceof Node )
			{
				if( missingParent( (Node)element ))
				{
					continue;
				}
				Node cloned = ((Node) element).clone();
				aNodes.add(cloned);
				reassignEdges(aEdges, (Node)element, cloned);
			}
		}
		
		// Delete any edge whose parent is not in aNodes
		List<Edge> toDelete = new ArrayList<>();
		for( Edge edge : aEdges )
		{
			if( !recursivelyContains(edge.getStart()) || !recursivelyContains(edge.getEnd()))
			{
				toDelete.add(edge);
			}
		}
		for( Edge edge : toDelete )
		{
			aEdges.remove(edge);
		}
	}
	
	/**
	 * Copies the selection list in the panel (as done by the copy method) and removes all
	 * the nodes in the selection from the graph wrapped by this pPanel.
	 * 
	 * @param pPanel The graph containing the elements
	 */
	public void cut(GraphPanel pPanel)
	{
		assert pPanel != null;
		assert pPanel.getSelectionList() != null;
		copy(pPanel.getSelectionList());	
		pPanel.removeSelected();
	}
	
	private boolean recursivelyContains(Node pNode)
	{
		for( Node node : aNodes )
		{
			if( node == pNode )
			{
				return true;
			}
			else if( node instanceof ParentNode )
			{
				if( recursivelyContains( pNode, ((ParentNode)node).getChildren()) )
				{
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean recursivelyContains(Node pNode, List<ChildNode> pNodes)
	{
		for( Node node : pNodes )
		{
			if( node == pNode )
			{
				return true;
			}
			else if( node instanceof ParentNode )
			{
				if(  recursivelyContains( pNode, ((ParentNode)node).getChildren()) )
				{
					return true;
				}
			}
		}
		return false;
	}
	
	private void reassignEdges(List<Edge> pEdges, Node pOld, Node pNew)
	{
		for( Edge edge : pEdges )
		{
			if( edge.getStart() == pOld )
			{
				edge.connect(pNew, edge.getEnd(), edge.getGraph());
			}
			if( edge.getEnd() == pOld)
			{
				edge.connect(edge.getStart(), pNew, edge.getGraph());
			}
		}
		if( pOld instanceof ParentNode )
		{
			List<ChildNode> oldChildren = ((ParentNode) pOld).getChildren();
			List<ChildNode> newChildren = ((ParentNode) pNew).getChildren();
			for( int i = 0; i < oldChildren.size(); i++)
			{
				reassignEdges(pEdges, oldChildren.get(i), newChildren.get(i));
			}
		}
	}
	
	/*
	 * Returns true of pNode needs a parent that isn't in 
	 * the clipboard.
	 */
	private boolean missingParent(Node pNode)
	{
		return pNode instanceof ChildNode && ((ChildNode)pNode).requiresParent() && !aNodes.contains(((ChildNode)pNode).getParent()) ;
	}
	
	/**
	 * Pastes the current selection into the pGraphPanel.
	 * @param pPanel The current Graph to paste contents to.
	 * @return The elements to paste as a selectionList.
	 */
	// CSOFF: Fix in later release
	public SelectionList paste(GraphPanel pPanel)
	{
		if( !validPaste(pPanel.getGraph()))
		{
			return new SelectionList();
		}
		
		pPanel.startCompoundGraphOperation();
		List<Edge> clonedEdges = new ArrayList<>();
		for( Edge edge : aEdges )
		{
			clonedEdges.add((Edge) edge.clone());
		}
		
		List<Node> clonedRootNodes = new ArrayList<>();
		Rectangle bounds = null;

		for( Node node : aNodes )
		{
			Node cloned = node.clone();
			clonedRootNodes.add(cloned);
			reassignEdges(clonedEdges, node, cloned);
			bounds = updateBounds(bounds, node);

		}
		
		removeDanglingReferencesToParents(clonedRootNodes);
		
		for( Node node : clonedRootNodes )
		{
			pPanel.getGraph().insertNode(node);
		}
		for( Edge edge : clonedEdges )
		{
			// Verify that the nodes were correctly added.
			// It is possible that some nodes could not be 
			// pasted (e.g., children nodes without their parent)
			// so some edges might no longer be relevant.
			if( pPanel.getGraph().contains( edge.getStart() ) && pPanel.getGraph().contains(edge.getEnd()))
			{
				pPanel.getGraph().insertEdge(edge);
			}
		}
		
		// Reposition the graph
		for( Edge edge : clonedEdges )
		{
			bounds = updateBounds(bounds, edge);
		}
		for( Node node : clonedRootNodes )
		{
			node.translate(-bounds.getX(), -bounds.getY());
		}
		// End graph repositioning
		pPanel.finishCompoundGraphOperation();
		
		SelectionList selectionList  = new SelectionList();
		for( Edge edge : clonedEdges )
		{
			selectionList.add(edge);
		}
		for( Node node : clonedRootNodes )
		{
			selectionList.add(node);
		}
		return selectionList;
	} // CSON:
	
	// Goes through pNodes and removes the reference to the parent
	// of any node who does not have a parent in the pNodes list
	private static void removeDanglingReferencesToParents(List<Node> pNodes)
	{
		for( Node node : pNodes )
		{
			if( node instanceof ChildNode && ((ChildNode)node).getParent() != null )
			{
				if( !pNodes.contains(((ChildNode)node).getParent()))
				{
					((ChildNode)node).getParent().removeChild((ChildNode)node);
				}
			}
		}
	}
	
	private static Rectangle updateBounds(Rectangle pBounds, GraphElement pElement)
	{
		Rectangle bounds = pBounds;
		if( bounds == null )
		{
			bounds = getBounds(pElement);
		}
		else
		{
			bounds = bounds.add( getBounds(pElement));
		}
		return bounds;
	}
	
	private static Rectangle getBounds(GraphElement pElement)
	{
		if( pElement instanceof Node )
		{
			return ((Node)pElement).view().getBounds();
		}
		else if( pElement instanceof Edge )
		{
			return ((Edge)pElement).view().getBounds();
		}
		else
		{
			assert false;
			return null;
		}
	}
	
	/*
	 * Returns true only of all the nodes and edges in the selection 
	 * are compatible with the target graph type.
	 */
	private boolean validPaste(Graph pGraph)
	{
		for( Edge edge : aEdges )
		{
			if( !validEdgeFor(edge, pGraph ))
			{
				return false;
			}
		}
		for( Node node : aNodes )
		{
			if( !validNodeFor(node, pGraph ))
			{
				return false;
			}
		}
		return true;
	}
	
	private static boolean validNodeFor( Node pNode, Graph pGraph )
	{
		for( Node node : pGraph.getNodePrototypes() )
		{
			if( pNode.getClass() == node.getClass() )
			{
				return true;
			}
		}
		return false;
	}
	
	private static boolean validEdgeFor( Edge pEdge, Graph pGraph )
	{
		for( Edge edge : pGraph.getEdgePrototypes() )
		{
			if( pEdge.getClass() == edge.getClass() )
			{
				return true;
			}
		}
		return false;
	}
}





