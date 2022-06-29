
package ca.iut.cs.IHMMODELE.persistence;

import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import ca.iut.cs.IHMMODELE.FormEditor;
import ca.iut.cs.IHMMODELE.graph.Edge;
import ca.iut.cs.IHMMODELE.graph.Graph;
import ca.iut.cs.IHMMODELE.graph.Node;
import ca.iut.cs.IHMMODELE.graph.Properties;
import ca.iut.cs.IHMMODELE.graph.Property;
import ca.iut.cs.IHMMODELE.graph.nodes.ChildNode;
import ca.iut.cs.IHMMODELE.graph.nodes.ParentNode;

/**
 * Converts a graph to JSON notation. The notation includes:
 * * The JetUML version
 * * The graph type
 * * An array of node encodings
 * * An array of edge encodings
 * 
 * 
 *
 */
public final class JsonEncoder
{
	private JsonEncoder() {}
	
	/**
	 * @param pGraph The graph to serialize.
	 * @return A JSON object that encodes the graph.
	 */
	public static JSONObject encode(Graph pGraph)
	{
		assert pGraph != null;
		
		JSONObject object = new JSONObject();
		object.put("version", ResourceBundle.getBundle(FormEditor.class.getName() + "Version").getString("version.number"));
		object.put("diagram", pGraph.getClass().getSimpleName());
		SerializationContext context = new SerializationContext(pGraph);
		object.put("nodes", encodeNodes(context));
		object.put("edges", encodeEdges(context));
		return object;
	}
	
	private static JSONArray encodeNodes(SerializationContext pContext)
	{
		JSONArray nodes = new JSONArray();
		for( Node node : pContext ) 
		{
			nodes.put(encodeNode(node, pContext));
		}
		return nodes;
	}
	
	private static JSONObject encodeNode(Node pNode, SerializationContext pContext)
	{
		JSONObject object = toJSONObject(pNode.properties());
		object.put("id", pContext.getId(pNode));
		object.put("type", pNode.getClass().getSimpleName());
		if( pNode instanceof ParentNode )
		{
			object.put("children", encodeChildren(pNode, pContext));
		}
		return object;
	}
	
	private static JSONArray encodeChildren(Node pNode, SerializationContext pContext)
	{
		JSONArray children = new JSONArray();
		for( ChildNode child : ((ParentNode)pNode).getChildren())
		{
			children.put(pContext.getId(child));
		}
		return children;
	}
	
	private static JSONArray encodeEdges(AbstractContext pContext)
	{
		JSONArray edges = new JSONArray();
		for( Edge edge : pContext.getGraph().getEdges() ) 
		{
			JSONObject object = toJSONObject(edge.properties());
			object.put("type", edge.getClass().getSimpleName());
			object.put("start", pContext.getId(edge.getStart()));
			object.put("end", pContext.getId(edge.getEnd()));
			
			edges.put(object);
		}
		return edges;
	}
	
	private static JSONObject toJSONObject(Properties pProperties)
	{
		JSONObject object = new JSONObject();
		for( Property property : pProperties )
		{
			Object value = property.get();
			if( value instanceof String || value instanceof Enum )
			{
				object.put(property.getName(), value.toString());
			}
			else if( value instanceof Integer)
			{
				object.put(property.getName(), (int) value);
			}
			else if( value instanceof Boolean)
			{
				object.put(property.getName(), (boolean) value);
			}
		}
		return object;
	}
}
