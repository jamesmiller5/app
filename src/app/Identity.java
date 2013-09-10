package app;

import org.neo4j.graphdb.RelationshipType;

//used to link email identity

public interface Identity {
	enum RelTypes implements RelationshipType {
		IDENTIFIES
	}

	public Email getEmail();
}
