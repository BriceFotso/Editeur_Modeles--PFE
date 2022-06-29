
package ca.iut.cs.IHMMODELE.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Stack;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ca.iut.cs.IHMMODELE.application.Clipboard;
import ca.iut.cs.IHMMODELE.application.GraphModificationListener;
import ca.iut.cs.IHMMODELE.application.MoveTracker;
import ca.iut.cs.IHMMODELE.application.PropertyChangeTracker;
import ca.iut.cs.IHMMODELE.application.SelectionList;
import ca.iut.cs.IHMMODELE.application.UndoManager;
import ca.iut.cs.IHMMODELE.commands.AddEdgeCommand;
import ca.iut.cs.IHMMODELE.commands.AddNodeCommand;
import ca.iut.cs.IHMMODELE.commands.ChangePropertyCommand;
import ca.iut.cs.IHMMODELE.commands.CompoundCommand;
import ca.iut.cs.IHMMODELE.commands.DeleteNodeCommand;
import ca.iut.cs.IHMMODELE.commands.RemoveEdgeCommand;
import ca.iut.cs.IHMMODELE.geom.Conversions;
import ca.iut.cs.IHMMODELE.geom.Line;
import ca.iut.cs.IHMMODELE.geom.Point;
import ca.iut.cs.IHMMODELE.geom.Rectangle;
import ca.iut.cs.IHMMODELE.graph.Edge;
import ca.iut.cs.IHMMODELE.graph.Graph;
import ca.iut.cs.IHMMODELE.graph.GraphElement;
import ca.iut.cs.IHMMODELE.graph.Node;
import ca.iut.cs.IHMMODELE.graph.Property;
import ca.iut.cs.IHMMODELE.graph.nodes.ChildNode;
import ca.iut.cs.IHMMODELE.graph.nodes.ParentNode;
import ca.iut.cs.IHMMODELE.views.Grid;

/**
 * A panel to draw a graph.
 */
@SuppressWarnings("serial")
public class GraphPanel extends JPanel
{
	private enum DragMode 
	{ DRAG_NONE, DRAG_MOVE, DRAG_RUBBERBAND, DRAG_LASSO }
	
	private static final int CONNECT_THRESHOLD = 8;
	private static final Color GRABBER_COLOR = new Color(77, 115, 153);
	private static final Color GRABBER_FILL_COLOR = new Color(173, 193, 214);
	private static final Color GRABBER_FILL_COLOR_TRANSPARENT = new Color(173, 193, 214, 75);
	
	private Graph aGraph;
	private ToolBar aSideBar;
	private int aZoom;	
	private boolean aHideGrid;
	private boolean aModified;
	private SelectionList aSelectedElements = new SelectionList();
	private Point2D aLastMousePoint;
	private Point2D aMouseDownPoint;   
	private DragMode aDragMode;
	private UndoManager aUndoManager = new UndoManager();
	private final MoveTracker aMoveTracker = new MoveTracker();
	
	/**
	 * Constructs the panel, assigns the graph to it, and registers
	 * the panel as a listener for the graph.
	 * 
	 * @param pGraph The graph managed by this panel.
	 * @param pSideBar the Side Bar which contains all of the tools for nodes and edges.
	 */
	public GraphPanel(Graph pGraph, ToolBar pSideBar)
	{
		aGraph = pGraph;
		aGraph.setGraphModificationListener(new PanelGraphModificationListener());
		aZoom = 1;
		aSideBar = pSideBar;
		setBackground(Color.WHITE);
		addMouseListener(new GraphPanelMouseListener());
		addMouseMotionListener(new GraphPanelMouseMotionListener());
	}

	/**
	 * Copy the currently selected elements to the clip board.
	 */
	public void copy()
	{
		if( aSelectedElements.size() > 0 )
		{
			Clipboard.instance().copy(aSelectedElements);
		}
	}
	
	/**
	 * Pastes the content of the clip board into the graph managed by this panel.
	 */
	public void paste()
	{
		aSelectedElements = Clipboard.instance().paste(this);
	}
	
	/**
	 * Copy the currently selected elements to the clip board and removes them
	 * from the graph managed by this panel.
	 */
	public void cut()
	{
		if( aSelectedElements.size() > 0 )
		{
			Clipboard.instance().cut(this);
		}
	}
	
	/**
	 * Edits the properties of the selected graph element.
	 */
	public void editSelected()
	{
		GraphElement edited = aSelectedElements.getLastSelected();
		if( edited == null )
		{
			return;
		}
		PropertyChangeTracker tracker = new PropertyChangeTracker(edited);
		tracker.startTracking();
		PropertySheet sheet = new PropertySheet(edited, new PropertySheet.PropertyChangeListener()
		{
			@Override
			public void propertyChanged()
			{
				aGraph.requestLayout();
				repaint();
			}
		});
		if(sheet.isEmpty())
		{
			return;
		}
		String[] options = {"OK"};
		JOptionPane.showOptionDialog(this, sheet, 
				ResourceBundle.getBundle("ca.iut.cs.IHMMODELE.gui.EditorStrings").getString("dialog.properties"),
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
		CompoundCommand command = tracker.stopTracking();
		if(command.size() > 0)
		{
			aUndoManager.add(command);
		}
		setModified(true);
	}

	/**
	 * Removes the selected graph elements.
	 */
	public void removeSelected()
	{
		aUndoManager.startTracking();
		Stack<Node> nodes = new Stack<Node>();
		for( GraphElement element : aSelectedElements )
		{
			if(element instanceof Node)
			{
				aGraph.removeAllEdgesConnectedTo((Node)element);
				nodes.add((Node) element);
			}
			else if(element instanceof Edge)
			{
				aGraph.removeEdge((Edge) element);
			}
		}
		while(!nodes.empty())
		{
			aGraph.removeNode(nodes.pop());
		}
		aUndoManager.endTracking();
		if(aSelectedElements.size() > 0)
		{
			setModified(true);
		}
		repaint();
	}
	
	/**
	 * Indicate to the GraphPanel that is should 
	 * consider all following operations on the graph
	 * to be part of a single conceptual one.
	 */
	public void startCompoundGraphOperation()
	{
		aUndoManager.startTracking();
	}
	
	/**
	 * Indicate to the GraphPanel that is should 
	 * stop considering all following operations on the graph
	 * to be part of a single conceptual one.
	 */
	public void finishCompoundGraphOperation()
	{
		aUndoManager.endTracking();
	}
	
	/**
	 * Resets the layout of the graph if there was a change made.
	 */
	public void layoutGraph()
	{
		aGraph.requestLayout();
	}
	
	/**
	 * @return the graph in this panel.
	 */
	public Graph getGraph()
	{
		return aGraph;
	}
	
	/**
	 * Collects all coming calls into single undo - redo command.
	 */
	public void startCompoundListening() 
	{
		aUndoManager.startTracking();
	}
	
	/**
	 * Ends collecting all coming calls into single undo - redo command.
	 */
	public void endCompoundListening() 
	{
		aUndoManager.endTracking();
	}
	
	/**
	 * Undoes the most recent command.
	 * If the UndoManager performs a command, the method 
	 * it calls will repaint on its own
	 */
	public void undo()
	{
		aUndoManager.undoCommand();
		revalidate();
		repaint();
	}
	
	/**
	 * Removes the last undone action and performs it.
	 * If the UndoManager performs a command, the method 
	 * it calls will repaint on its own
	 */
	public void redo()
	{
		aUndoManager.redoCommand();
		revalidate();
		repaint();
	}
	
	/**
	 * Clears the selection list and adds all the root nodes and edges to 
	 * it. Makes the selection tool the active tool.
	 */
	public void selectAll()
	{
		aSelectedElements.clearSelection();
		for( Node node : aGraph.getRootNodes() )
		{
			aSelectedElements.add(node);
		}
		for( Edge edge : aGraph.getEdges() )
		{
			aSelectedElements.add(edge);
		}
		aSideBar.setToolToBeSelect();
		repaint();
	}

	@Override
	public void paintComponent(Graphics pGraphics)
	{
		super.paintComponent(pGraphics);
		Graphics2D g2 = (Graphics2D) pGraphics;
		g2.scale(aZoom, aZoom);
		Rectangle2D bounds = getBounds();
		Rectangle graphBounds = aGraph.getBounds();
		if(!aHideGrid) 
		{
			Grid.draw(g2, new Rectangle2D.Double(0, 0, Math.max(bounds.getMaxX() / aZoom, graphBounds.getMaxX()), 
				   Math.max(bounds.getMaxY() / aZoom, graphBounds.getMaxY())));
		}
		aGraph.draw(g2);

		Set<GraphElement> toBeRemoved = new HashSet<>();
		for(GraphElement selected : aSelectedElements)
		{
			if(!aGraph.contains(selected)) 
			{
				toBeRemoved.add(selected);
			}
			else if(selected instanceof Node)
			{
				Rectangle grabberBounds = ((Node) selected).view().getBounds();
				drawGrabber(g2, grabberBounds.getX(), grabberBounds.getY());
				drawGrabber(g2, grabberBounds.getX(), grabberBounds.getMaxY());
				drawGrabber(g2, grabberBounds.getMaxX(), grabberBounds.getY());
				drawGrabber(g2, grabberBounds.getMaxX(), grabberBounds.getMaxY());
			}
			else if(selected instanceof Edge)
			{
				Line line = ((Edge) selected).view().getConnectionPoints();
				drawGrabber(g2, line.getX1(), line.getY1());
				drawGrabber(g2, line.getX2(), line.getY2());
			}
		}

		for( GraphElement element : toBeRemoved )
		{
			aSelectedElements.remove(element);
		}                 
      
		if(aDragMode == DragMode.DRAG_RUBBERBAND)
		{
			Color oldColor = g2.getColor();
			g2.setColor(GRABBER_COLOR);
			g2.draw(new Line2D.Double(aMouseDownPoint, aLastMousePoint));
			g2.setColor(oldColor);
		}      
		else if(aDragMode == DragMode.DRAG_LASSO)
		{
			Color oldColor = g2.getColor();
			g2.setColor(GRABBER_COLOR);
			double x1 = aMouseDownPoint.getX();
			double y1 = aMouseDownPoint.getY();
			double x2 = aLastMousePoint.getX();
			double y2 = aLastMousePoint.getY();
			Rectangle2D.Double lasso = new Rectangle2D.Double(Math.min(x1, x2), 
					Math.min(y1, y2), Math.abs(x1 - x2) , Math.abs(y1 - y2));
			g2.draw(lasso);
			g2.setColor(GRABBER_FILL_COLOR_TRANSPARENT);
			g2.fill(lasso);
			g2.setColor(oldColor);
		}      
	}

	/**
	 * Draws a single "grabber", a filled square.
	 * @param pGraphics2D the graphics context
	 * @param pX the x coordinate of the center of the grabber
	 * @param pY the y coordinate of the center of the grabber
	 */
	public static void drawGrabber(Graphics2D pGraphics2D, double pX, double pY)
	{
		final int size = 6;
		Color oldColor = pGraphics2D.getColor();
		pGraphics2D.setColor(GRABBER_COLOR);
		pGraphics2D.drawRect((int)(pX - size / 2), (int)(pY - size / 2), size, size);
		pGraphics2D.setColor(GRABBER_FILL_COLOR);
		pGraphics2D.fillRect((int)(pX - size / 2)+1, (int)(pY - size / 2)+1, size-1, size-1);
		pGraphics2D.setColor(oldColor);
	}

	@Override
	public Dimension getPreferredSize()
	{
		Rectangle bounds = aGraph.getBounds();
		return new Dimension(aZoom * bounds.getMaxX(), aZoom * bounds.getMaxY());
	}

	/**
	 * Changes the zoom of this panel. The zoom is 1 by default and is multiplied
	 * by sqrt(2) for each positive stem or divided by sqrt(2) for each negative step.
	 * @param pSteps the number of steps by which to change the zoom. A positive
	 * value zooms in, a negative value zooms out.
	 */
	public void changeZoom(int pSteps)
	{
      final double factor = Math.sqrt(2);
      for(int i = 1; i <= pSteps; i++)
      {
    	  aZoom *= factor;
      }
      for(int i = 1; i <= -pSteps; i++)
      {
    	  aZoom /= factor;
      }
      revalidate();
      repaint();
	}

	/**
	 * Checks whether this graph has been modified since it was last saved.
	 * @return true if the graph has been modified
	 */
	public boolean isModified()
	{	
		return aModified;
	}

	/**
	 * Sets or resets the modified flag for this graph.
	 * @param pModified true to indicate that the graph has been modified
	 */
	public void setModified(boolean pModified)
	{
		aModified = pModified;

		GraphFrame graphFrame = getFrame();
		if(graphFrame != null)
		{
			graphFrame.setTitle(aModified);
		}
	}
	
	/* 
	 * Obtains the parent frame of this panel through the component hierarchy.
	 */
	private GraphFrame getFrame()
	{
		Component parent = this;
		do
		{
			parent = parent.getParent();
		}
		while(parent != null && !(parent instanceof GraphFrame));
		return (GraphFrame) parent;
	}
   
	/**
	 * Sets the value of the hideGrid property.
	 * @param pHideGrid true if the grid is being hidden
	 */
	public void setHideGrid(boolean pHideGrid)
	{
		aHideGrid = pHideGrid;
		repaint();
	}

	/**
	 * Gets the value of the hideGrid property.
	 * @return true if the grid is being hidden
	 */
	public boolean getHideGrid()
	{
		return aHideGrid;
	}
	
	/**
	 * @return the currently SelectedElements from the GraphPanel.
	 */
	public SelectionList getSelectionList()
	{
		return aSelectedElements;
	}
	
	/**
	 * @param pSelectionList the new SelectedElements for the GraphPanel.
	 */
	public void setSelectionList(SelectionList pSelectionList)
	{
		aSelectedElements = pSelectionList;
	}
	
	/**
	 * @param pNode the currently selected Node
	 * @return whether or not there is a problem with switching to the selection tool.
	 */
//	public boolean switchToSelectException(Node pNode)
//	{
////		if(pNode instanceof PackageNode)
////		{
////			return true;
////		}
////		return false;
//	}
//	
	private class GraphPanelMouseListener extends MouseAdapter
	{	
		/**
		 * Also adds the inner edges of parent nodes to the selection list.
		 * @param pElement
		 */
		private void setSelection(GraphElement pElement)
		{
			aSelectedElements.set(pElement);
			for( Edge edge : aGraph.getEdges() )
			{
				if( hasSelectedParent(edge.getStart()) && hasSelectedParent(edge.getEnd()))
				{
					aSelectedElements.add(edge);
				}
			}
			aSelectedElements.add(pElement); // Necessary to make a parent node the last node selected so it can be edited.
		}
		
		/**
		 * Also adds the inner edges of parent nodes to the selection list.
		 * @param pElement
		 */
		private void addToSelection(GraphElement pElement)
		{
			aSelectedElements.add(pElement);
			for( Edge edge : aGraph.getEdges() )
			{
				if( hasSelectedParent(edge.getStart()) && hasSelectedParent(edge.getEnd()))
				{
					aSelectedElements.add(edge);
				}
			}
			aSelectedElements.add(pElement); // Necessary to make a parent node the last node selected so it can be edited.
		}
		
		/**
		 * @param pNode a Node to check.
		 * @return True if pNode or any of its parent is selected
		 */
		private boolean hasSelectedParent(Node pNode)
		{
			if( pNode == null )
			{
				return false;
			}
			else if( aSelectedElements.contains(pNode) )
			{
				return true;
			}
			else if( pNode instanceof ChildNode )
			{
				return hasSelectedParent( ((ChildNode)pNode).getParent() );
			}
			else
			{
				return false;
			}
		}
		
		private boolean isCtrl(MouseEvent pEvent)
		{
			return (pEvent.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0;
		}
		
		private Point getMousePoint(MouseEvent pEvent)
		{
			return new Point((int)(pEvent.getX() / aZoom), (int)(pEvent.getY() / aZoom));
		}
		
		/*
		 * Will return null if nothing is selected.
		 */
		private GraphElement getSelectedElement(MouseEvent pEvent)
		{
			Point mousePoint = getMousePoint(pEvent);
			GraphElement element = aGraph.findEdge(mousePoint);
			if( element == null )
			{
				element = aGraph.findNode(new Point(mousePoint.getX(), mousePoint.getY())); 
			}
			return element;
		}
		
		private void handleSelection(MouseEvent pEvent)
		{
			GraphElement element = getSelectedElement(pEvent);
			if(element != null) // Something is selected
			{
				if( isCtrl(pEvent) )
				{
					if(!aSelectedElements.contains(element))
					{
						addToSelection(element);
					}
					else
					{
						aSelectedElements.remove(element);
					}
				}
				else if( !aSelectedElements.contains(element))
				{
					// The test is necessary to ensure we don't undo multiple selections
					setSelection(element);
				}
				aDragMode = DragMode.DRAG_MOVE;
				aMoveTracker.startTrackingMove(aSelectedElements);
			}
			else // Nothing is selected
			{
				if(!isCtrl(pEvent)) 
				{
					aSelectedElements.clearSelection();
				}
				aDragMode = DragMode.DRAG_LASSO;
			}
		}
		
		private void handleDoubleClick(MouseEvent pEvent)
		{
			GraphElement element = getSelectedElement(pEvent);
			if( element != null )
			{
				setSelection(element);
				editSelected();
			}
			else
			{
				Point point = getMousePoint(pEvent);
				final Point mousePoint = new Point(point.getX(), point.getY()); 
				aSideBar.showPopup(GraphPanel.this, mousePoint);
			}
		}
		
		private void handleNodeCreation(MouseEvent pEvent)
		{
			Node newNode = ((Node)aSideBar.getSelectedTool()).clone();
			Point point = getMousePoint(pEvent);
			boolean added = aGraph.addNode(newNode, new Point(point.getX(), point.getY())); 
			if(added)
			{
				setModified(true);
				setSelection(newNode);
			}
			else // Special behavior, if we can't add a node, we select any element at the point
			{
				handleSelection(pEvent);
			}
		}
		
		private void handleEdgeStart(MouseEvent pEvent)
		{
			GraphElement element = getSelectedElement(pEvent);
			if(element != null && element instanceof Node ) 
			{
				aDragMode = DragMode.DRAG_RUBBERBAND;
			}
		}
		
		/*
	     * Implements a convenience feature. Normally returns 
	     * aSideBar.getSelectedTool(), except if the mouse points
	     * to an existing node, in which case defaults to select
	     * mode because it's likely the user wanted to select the node
	     * and forgot to switch tool. The only exception is when adding
	     * children nodes, where the parent node obviously has to be selected.
		 */
		private GraphElement getTool(MouseEvent pEvent)
		{
			GraphElement tool = aSideBar.getSelectedTool();
			GraphElement selected = getSelectedElement(pEvent);
			
			if(tool !=null && tool instanceof Node)
			{
				if( selected != null && selected instanceof Node )
				{
					if(!(tool instanceof ChildNode && selected instanceof ParentNode ))
					{
						aSideBar.setToolToBeSelect();
						tool = null;
					}
				}
			}	
			return tool;
		}
		
		@Override
		public void mousePressed(MouseEvent pEvent)
		{
			GraphElement tool = getTool(pEvent);

			if(pEvent.getClickCount() > 1 || (pEvent.getModifiers() & InputEvent.BUTTON1_MASK) == 0) // double/right click
			{  
				handleDoubleClick(pEvent);
			}
			else if(tool == null)
			{
				handleSelection(pEvent);
			}
			else if(tool instanceof Node)
			{
				handleNodeCreation(pEvent);
			}
			else if(tool instanceof Edge)
			{
				handleEdgeStart(pEvent);
			}
			Point point = getMousePoint(pEvent);
			aLastMousePoint = new Point2D.Double(point.getX(), point.getY()); // TODO move to geom.point
			aMouseDownPoint = aLastMousePoint;
			repaint();
		}

		@Override
		public void mouseReleased(MouseEvent pEvent)
		{
			Point2D mousePoint = new Point2D.Double(pEvent.getX() / aZoom, pEvent.getY() / aZoom);
			Object tool = aSideBar.getSelectedTool();
			if(aDragMode == DragMode.DRAG_RUBBERBAND)
			{
				Edge prototype = (Edge) tool;
				Edge newEdge = (Edge) prototype.clone();
				if(mousePoint.distance(aMouseDownPoint) > CONNECT_THRESHOLD && 
						aGraph.addEdge(newEdge, Conversions.toPoint(aMouseDownPoint), 
								Conversions.toPoint(mousePoint)))
				{
					setModified(true);
					setSelection(newEdge);
				}
			}
			else if(aDragMode == DragMode.DRAG_MOVE)
			{
				aGraph.requestLayout();
				setModified(true);
				CompoundCommand command = aMoveTracker.endTrackingMove(aGraph);
				if( command.size() > 0 )
				{
					aUndoManager.add(command);
				}
			}
			aDragMode = DragMode.DRAG_NONE;
			revalidate();
			repaint();
		}
	}
	
	private class GraphPanelMouseMotionListener extends MouseMotionAdapter
	{
		@Override
		public void mouseDragged(MouseEvent pEvent)
		{
			Point2D mousePoint = new Point2D.Double(pEvent.getX() / aZoom, pEvent.getY() / aZoom);
			boolean isCtrl = (pEvent.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0; 

			if(aDragMode == DragMode.DRAG_MOVE && aSelectedElements.getLastNode()!=null)
			{               
				Node lastNode = aSelectedElements.getLastNode();
				Rectangle bounds = lastNode.view().getBounds();
				int dx = (int)(mousePoint.getX() - aLastMousePoint.getX());
				int dy = (int)(mousePoint.getY() - aLastMousePoint.getY());
                   
				// we don't want to drag nodes into negative coordinates
				// particularly with multiple selection, we might never be 
				// able to get them back.
				for( GraphElement selected : aSelectedElements )
				{
					if(selected instanceof Node)
					{
						Node n = (Node) selected;
						bounds = bounds.add(n.view().getBounds());
					}
				}
				dx = Math.max(dx, -bounds.getX());
				dy = Math.max(dy, -bounds.getY());
            
				for( GraphElement selected : aSelectedElements )
				{
					if(selected instanceof ChildNode)
					{
						ChildNode n = (ChildNode) selected;
						if (!aSelectedElements.parentContained(n)) // parents are responsible for translating their children
						{
							n.translate(dx, dy); 
						}	
					}
					else if(selected instanceof Node)
					{
						Node n = (Node) selected;
						n.translate(dx, dy); 
					}
				}
			}
			else if(aDragMode == DragMode.DRAG_LASSO)
			{
				double x1 = aMouseDownPoint.getX();
				double y1 = aMouseDownPoint.getY();
				double x2 = mousePoint.getX();
				double y2 = mousePoint.getY();
				Rectangle lasso = new Rectangle((int)Math.min(x1, x2), (int)Math.min(y1, y2), (int)Math.abs(x1 - x2) , (int)Math.abs(y1 - y2));
				for( Node node : aGraph.getRootNodes() )
				{
					selectNode(isCtrl, node, lasso);
				}
				//Edges need to be added too when highlighted, but only if both their endpoints have been highlighted.
				for (Edge edge: aGraph.getEdges())
				{
					if(!isCtrl && !lasso.contains(edge.view().getBounds()))
					{
						aSelectedElements.remove(edge);
					}
					else if(lasso.contains(edge.view().getBounds()))
					{
						if(aSelectedElements.transitivelyContains(edge.getStart()) && aSelectedElements.transitivelyContains(edge.getEnd()))
						{
							aSelectedElements.add(edge);
						}
					}
				}
			}
			aLastMousePoint = mousePoint;
			repaint();
		}
		
		private void selectNode( boolean pCtrl, Node pNode, Rectangle pLasso )
		{
			if(!pCtrl && !pLasso.contains(pNode.view().getBounds())) 
			{
				aSelectedElements.remove(pNode);
			}
			else if(pLasso.contains(pNode.view().getBounds())) 
			{
				aSelectedElements.add(pNode);
			}
			if( pNode instanceof ParentNode )
			{
				for( ChildNode child : ((ParentNode) pNode).getChildren() )
				{
					selectNode(pCtrl, child, pLasso);
				}
			}
		}
	}
	
	private class PanelGraphModificationListener implements GraphModificationListener
	{
		@Override
		public void startingCompoundOperation() 
		{
			aUndoManager.startTracking();
		}
		
		@Override
		public void finishingCompoundOperation()
		{
			aUndoManager.endTracking();
		}
		
		@Override
		public void nodeAdded(Graph pGraph, Node pNode)
		{
			aUndoManager.add(new AddNodeCommand(pGraph, pNode));
		}
		
		@Override
		public void nodeRemoved(Graph pGraph, Node pNode)
		{
			aUndoManager.add(new DeleteNodeCommand(pGraph, pNode));
		}
		
		@Override
		public void edgeAdded(Graph pGraph, Edge pEdge)
		{
			aUndoManager.add(new AddEdgeCommand(pGraph, pEdge));
		}
		
		@Override
		public void edgeRemoved(Graph pGraph, Edge pEdge)
		{
			aUndoManager.add(new RemoveEdgeCommand(pGraph, pEdge));
		}

		@Override
		public void propertyChanged(Property pProperty, Object pOldValue)
		{
			aUndoManager.add(new ChangePropertyCommand(pProperty, pOldValue, pProperty.get()));
		}
	}
}
