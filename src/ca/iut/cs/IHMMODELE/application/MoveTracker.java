
package ca.iut.cs.IHMMODELE.application;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import ca.iut.cs.IHMMODELE.commands.CompoundCommand;
import ca.iut.cs.IHMMODELE.commands.MoveCommand;
import ca.iut.cs.IHMMODELE.geom.Conversions;
import ca.iut.cs.IHMMODELE.graph.Graph;
import ca.iut.cs.IHMMODELE.graph.GraphElement;
import ca.iut.cs.IHMMODELE.graph.Node;

/**
 * Tracks the movement of a set of selected graph elements.
 * 
 *
 */
public class MoveTracker 
{
	private List<Node> aTrackedNodes = new ArrayList<>();
	private List<Rectangle2D> aOriginalBounds = new ArrayList<>();

	/**
	 * Records the elements in pSelectedElements and their position at the 
	 * time where the method is called.
	 * 
	 * @param pSelectedElements The elements that are being moved. Not null.
	 */
	public void startTrackingMove(SelectionList pSelectedElements)
	{
		assert pSelectedElements != null;
		
		aTrackedNodes.clear();
		aOriginalBounds.clear();
		
		for(GraphElement element : pSelectedElements)
		{
			assert element != null;
			if(element instanceof Node)
			{
				aTrackedNodes.add((Node) element);
				aOriginalBounds.add(Conversions.toRectangle2D(((Node)element).view().getBounds()));
			}
		}
	}

	/**
	 * Creates and returns a CompoundCommand that represents the movement
	 * of all tracked nodes between the time where startTrackingMove was 
	 * called and the time endTrackingMove was called.
	 * 
	 * @param pGraph The Graph containing the selected elements.
	 * @return A CompoundCommand describing the move.
	 */
	public CompoundCommand endTrackingMove(Graph pGraph)
	{
		CompoundCommand command = new CompoundCommand();
		Rectangle2D[] selectionBounds2 = new Rectangle2D[aOriginalBounds.size()];
		int i = 0;
		for(Node node : aTrackedNodes)
		{
			selectionBounds2[i] = Conversions.toRectangle2D(node.view().getBounds());
			i++;
		}
		for(i = 0; i < aOriginalBounds.size(); i++)
		{
			int dY = (int)(selectionBounds2[i].getY() - aOriginalBounds.get(i).getY());
			int dX = (int)(selectionBounds2[i].getX() - aOriginalBounds.get(i).getX());
			if(dX != 0 || dY != 0)
			{
				command.add(new MoveCommand(pGraph, aTrackedNodes.get(i), dX, dY));
			}
		}
		return command;
	}
}
