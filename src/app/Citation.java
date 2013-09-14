package app;

import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.GraphDatabaseService;
import java.util.Date;

public class Citation extends Entity {
	private final static String DESCRIPTION = "Description";
	private final static String RESOURCE = "Resource";
	private final static String DATE_CREATED = "DateCreated";

	public Citation(final String description, final String resource) {
		if( description == null
				|| description.length() < 0
				|| resource == null
				|| resource.length() < 0
				|| description.length() > 256
				|| resource.length() > 256 )
		{
			throw new IllegalArgumentException();
		}

		GraphDatabaseService graphDb = graphDb();
		try( Transaction tx = graphDb.beginTx() ) {
			Node node = graphDb.createNode( LabelDef.CITATION );
			node.setProperty( DESCRIPTION, description );
			node.setProperty( RESOURCE, resource );
			node.setProperty( DATE_CREATED, getTimeUTC() );
			tx.success();
			initialize(node);
		}
	}

	public String getDescription() {
		// No description parameter is an illegal state
		try( Transaction tx = graphDb().beginTx() ) {
			String description = (String) getInternalNode().getProperty(DESCRIPTION);
			tx.success();
			return description;
		}
	}

	public String getResource() {
		// No resouce parameter is an illegal state
		try( Transaction tx = graphDb().beginTx() ) {
			String resource = (String) getInternalNode().getProperty(RESOURCE);
			tx.success();
			return resource;
		}
	}
	public Date getDateCreated() {
		// No Date parameter is an illegal state
		try( Transaction tx = graphDb().beginTx() ) {
			long date = (long) getInternalNode().getProperty(DATE_CREATED);
			tx.success();
			return new Date(date);
		}
	}

	private long getTimeUTC() {
		return new Date().getTime();
	}
}
