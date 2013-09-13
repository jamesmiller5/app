package app;

import org.neo4j.graphdb.Label;

enum LabelDef implements Label {
	ENTITY,
	USER,
	EMAIL,
	CITATION,
	TRUSTEDGE,
	UNIQUE,
	CREATED
}
