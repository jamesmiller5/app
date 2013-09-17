package app;

import org.neo4j.graphdb.RelationshipType;

public enum RelType implements RelationshipType
{
	FROM,
	TO,
	FOR,
	OWNS,
	REASON,
	PORTFOLIO
}
