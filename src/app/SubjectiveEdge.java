package app;

import org.neo4j.graphdb.Node;

public class SubjectiveEdge extends TrustEdge {
	public int score = 0;

	public SubjectiveEdge( final Node internalNode ) {
		super(internalNode);
	}
}
