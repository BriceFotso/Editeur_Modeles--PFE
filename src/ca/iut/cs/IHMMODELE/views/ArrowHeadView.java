

package ca.iut.cs.IHMMODELE.views;

import static ca.iut.cs.IHMMODELE.views.ArrowHead.BLACK_DIAMOND;
import static ca.iut.cs.IHMMODELE.views.ArrowHead.BLACK_TRIANGLE;
import static ca.iut.cs.IHMMODELE.views.ArrowHead.DIAMOND;
import static ca.iut.cs.IHMMODELE.views.ArrowHead.NONE;
import static ca.iut.cs.IHMMODELE.views.ArrowHead.TRIANGLE;
import static ca.iut.cs.IHMMODELE.views.ArrowHead.V;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

/**
 * Defines how to draw arrow heads.
 */
public final class ArrowHeadView
{
	private static final double ARROW_ANGLE = Math.PI / 6; 
	private static final double ARROW_LENGTH = 10;
	
	private final ArrowHead aArrowHead;
	
	/**
	 * Creates a new view for pArrowHead.
	 * 
	 * @param pArrowHead The arrowhead to wrap.
	 */
	public ArrowHeadView(ArrowHead pArrowHead)
	{
		aArrowHead = pArrowHead;
	}
	
	/**
	 * Draws the arrowhead.
	 * @param pGraphics2D the graphics context
	 * @param pPoint1 a point on the axis of the arrow head
	 * @param pEnd the end point of the arrow head
	 */
	public void draw(Graphics2D pGraphics2D, Point2D pPoint1, Point2D pEnd)
	{
		GeneralPath path = getPath(pPoint1, pEnd);
		Color oldColor = pGraphics2D.getColor();
		if(aArrowHead == ArrowHead.BLACK_DIAMOND || aArrowHead == BLACK_TRIANGLE) 
		{
			pGraphics2D.setColor(Color.BLACK);
		}
		else 
		{
			pGraphics2D.setColor(Color.WHITE);
		}
		pGraphics2D.fill(path);
		pGraphics2D.setColor(oldColor);
		pGraphics2D.draw(path);
	}
	
   	/**
     *  Gets the path of the arrowhead.
     * @param pPoint1 a point on the axis of the arrow head
     * @param pEnd the end point of the arrow head
     * @return the path
     */
   	public GeneralPath getPath(Point2D pPoint1, Point2D pEnd)
   	{
   		GeneralPath path = new GeneralPath();
   		if(aArrowHead == NONE) 
   		{
   			return path;
   		}
   		
   		double dx = pEnd.getX() - pPoint1.getX();
   		double dy = pEnd.getY() - pPoint1.getY();
   		double angle = Math.atan2(dy, dx);
   		double x1 = pEnd.getX() - ARROW_LENGTH * Math.cos(angle + ARROW_ANGLE);
   		double y1 = pEnd.getY() - ARROW_LENGTH * Math.sin(angle + ARROW_ANGLE);
   		double x2 = pEnd.getX() - ARROW_LENGTH * Math.cos(angle - ARROW_ANGLE);
   		double y2 = pEnd.getY() - ARROW_LENGTH * Math.sin(angle - ARROW_ANGLE);

   		path.moveTo((float)pEnd.getX(), (float)pEnd.getY());
   		path.lineTo((float)x1, (float)y1);
   		if(aArrowHead == V)
   		{
   			path.moveTo((float)x2, (float)y2);
   			path.lineTo((float)pEnd.getX(), (float)pEnd.getY());
   		}
   		else if(aArrowHead == TRIANGLE || aArrowHead == BLACK_TRIANGLE)
   		{
   			path.lineTo((float)x2, (float)y2);
   			path.closePath();                  
   		}
   		else if(aArrowHead == DIAMOND || aArrowHead == BLACK_DIAMOND)
   		{
   			double x3 = x2 - ARROW_LENGTH * Math.cos(angle + ARROW_ANGLE);
   			double y3 = y2 - ARROW_LENGTH * Math.sin(angle + ARROW_ANGLE);
   			path.lineTo((float)x3, (float)y3);
   			path.lineTo((float)x2, (float)y2);
   			path.closePath();         
   		}      
   		return path;
   	}
}
