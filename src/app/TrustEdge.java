package app;

import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.GraphDatabaseService;

public class TrustEdge extends Entity {
	private static final String SUBJECT = "subject";

	public TrustEdge( final Node internalNode ) {
		super.initialize(internalNode);
	}

	public TrustEdge( final User from, final User to, final Subject subject ) {

	}
}
