

package ca.iut.cs.IHMMODELE.views;

import java.awt.BasicStroke;
import java.awt.Stroke;

/**
 *   Defines line styles of various shapes.
 */
public enum LineStyle
{
	SOLID, DOTTED;
	
	private static final Stroke[] STROKES = new Stroke[] {
			new BasicStroke(),
			new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10, new float[] { 3, 3 }, 0)
	};
	
	/**
	 * @return The stroke with which to draw this line style.
	 */
	public Stroke getStroke()
	{
		return STROKES[ordinal()];
	}
}
