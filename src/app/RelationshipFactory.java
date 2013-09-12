package app;

import java.util.HashMap;
import java.util.Map;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.index.*;

public class RelationshipFactory {
	private static RelationshipFactory factory = new RelationshipFactory();

	public static RelationshipFactory get() {
		return factory;
	}

	// MERGE IS NOT THREAD SAFE! -- MERGE only supports Node's
	// .created should be local state, not in graph;
	// however, could not find examples of this
	private String getOrCreateQuery =
		"Start s=node({from}), e=node({to})\n"+
		"CREATE UNIQUE s-[r:%s]->e\n"+
		"RETURN r\n"+
		"";
		/*
		"MERGE s-[r:%s]-e\n"+
		"ON CREATE r\n"+
			"SET r.wasCreated = TRUE\n"+
		"ON MATCH r\n" +
			"SET r.wasCreated = FALSE\n"+
		"RETURN r\n";
		*/

	// Creates edge or returns existing one if the edge already exists
	public Relationship getOrCreateRelationship(Node from, Node to, RelationshipType type) {
		Relationship r = null;
		try(Transaction tx = GraphDatabase.get().beginTx()) {
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("from", from);
			params.put("to", to);
			// TODO: How to use query parameters with relationship type? Shouldn't need
			// to do string formatting.
			String query = String.format(getOrCreateQuery,type.name());
			ExecutionResult result = GraphDatabase.execute(query, params);
			r = result.<Relationship>columnAs("r").next();
			tx.success();
		}
		return r;
	}
}
