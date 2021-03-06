

package ca.iut.cs.IHMMODELE.geom;

/**
 * This class describes a direction in the 2D plane. 
 * A direction is a vector of length 1 with an angle between 0 
 * (inclusive) and 360 degrees (exclusive). There is also
 * a degenerate direction of length 0. Note that this class
 * currently does not support hashing with hashCode and 
 * equality testing with equals().
 */
public class Direction
{
	public static final Direction NORTH = new Direction(0, -1);
	public static final Direction SOUTH = new Direction(0, 1);
	public static final Direction EAST = new Direction(1, 0);
	public static final Direction WEST = new Direction(-1, 0);
	
	private double aX;
	private double aY;   
	
	/**
     * Constructs a direction (normalized to length 1).
     * @param pX the x-value of the direction
     * @param pY the corresponding y-value of the direction
	 */
	public Direction(double pX, double pY)
	{
		aX = pX;
		aY = pY;
		double length = Math.sqrt(aX * aX + aY * aY);
		if(length == 0) 
		{
			return;
		}
		aX = aX / length;
		aY = aY / length;
	}

	/**
     * Constructs a direction between two points.
     * @param pPoint1 the starting point
     * @param pPoint2 the ending point
	 */
	public Direction(Point pPoint1, Point pPoint2) 
	{
		this(pPoint2.getX() - pPoint1.getX(), pPoint2.getY() - pPoint1.getY());
	}

	/**
     * Turns this direction by an angle.
     * @param pAngle the angle in degrees
     * @return The new, rotated direction.
	 */
	public Direction turn(double pAngle)
	{
		double a = Math.toRadians(pAngle);
		return new Direction(aX * Math.cos(a) - aY * Math.sin(a), aX * Math.sin(a) + aY * Math.cos(a));
	}

	/**
     * Gets the x-component of this direction.
     * @return the x-component (between -1 and 1)
	 */
	public double getX()
	{
		return aX;
	}

	/**
     * Gets the y-component of this direction.
     * @return the y-component (between -1 and 1)
	 */
	public double getY()
	{
		return aY;
	}
}
