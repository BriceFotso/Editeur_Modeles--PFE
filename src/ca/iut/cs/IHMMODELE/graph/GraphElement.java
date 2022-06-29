
package ca.iut.cs.IHMMODELE.graph;

/**
 * A type that allows us to treat nodes and edges uniformly.
 * 
 *
 */
public interface GraphElement extends Cloneable
{
	/**
	 * @return A set of properties that define this object.
	 */
	Properties properties();
}
