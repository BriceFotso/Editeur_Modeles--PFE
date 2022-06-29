
package ca.iut.cs.IHMMODELE.graph.nodes;

import ca.iut.cs.IHMMODELE.graph.Node;

/**
 * Node that potentially has a parent node 
 * according to a diagram type-specific parent-child
 * relation. A child node is defined as a child that
 * cannot exist without its parent according to the rules
 * of UML. Child nodes are controlled by their parent nodes.
 * See ParentNode for details.
 * 
 
 */
public interface ChildNode extends Node
{
	/**
	 * @return True if this node type absolutely
	 * needs a parent to exist, and false if it can
	 * exist as a root node.
	 */
	boolean requiresParent();	
	
	/**
	 * @return The node that is the parent of this node.
	 */
	ParentNode getParent();
	
	/**
	 * Sets the parent of this node. This operation does 
	 * NOT set the child node's parent as this node.
	 * 
	 * @param pParentNode The node to set as parent of this node.
	 */
	void setParent(ParentNode pParentNode);
}
