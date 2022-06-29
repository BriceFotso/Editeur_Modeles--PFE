
package ca.iut.cs.IHMMODELE.persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONException;
import org.json.JSONObject;

import ca.iut.cs.IHMMODELE.graph.Graph;

/**
 * Services for saving and loading Graph objects (i.e., UML diagrams).
 * 
 * 
 */
public final class PersistenceService
{
	private PersistenceService() {}
	
	/**
     * Saves the current graph in a file. 
     * 
     * @param pGraph The graph to save
     * @param pFile The file to save
     * @throws IOException If there is a problem writing to pFile.
     * @pre pGraph != null.
     * @pre pFile != null.
     */
	public static void save(Graph pGraph, File pFile) throws IOException
	{
		assert pGraph != null && pFile != null;
		try( PrintWriter out = new PrintWriter(new FileWriter(pFile)))
		{
			out.println(JsonEncoder.encode(pGraph).toString());
		}
	}
	
	/**
	 * Reads a graph from a file.
	 * 
	 * @param pFile The file to read the graph from.
	 * @return The graph that is read in
	 * @throws IOException if the graph cannot be read.
	 * @throws DeserializationException if there is a problem decoding the file.
	 * @pre pFile != null
	 */
	public static Graph read(File pFile) throws IOException, DeserializationException
	{
		assert pFile != null;
		try( BufferedReader in = new BufferedReader(new FileReader(pFile)))
		{
			Graph graph = JsonDecoder.decode(new JSONObject(in.readLine()));
			return graph;
		}
		catch( JSONException e )
		{
			throw new DeserializationException("Cannot decode the file", e);
		}
	}
}
