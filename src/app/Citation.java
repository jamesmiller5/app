package app;

import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.helpers.collection.IterableWrapper;
import org.neo4j.graphdb.Path;
import java.util.Date;

public class Citation extends Entity {
	private final static String DESCRIPTION = "Description";
	private final static String RESOURCE = "Resource";
	private final static String DATE_CREATED = "DateCreated";

	public Citation(final String description, String resource) {
		if( resource.toLowerCase().indexOf("reddit")>=0 || resource.toLowerCase().indexOf("wikipedia")>=0 ){
			resource = "[Citation Needed]";
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

	public Citation(Node internalNode) {
		initialize(internalNode);
	}

	public Citation(Token id) {
		initialize(nodeByID(id));
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
			return new Date(date-(3600*1000));
		}
	}

	private long getTimeUTC() {
		return new Date().getTime();
	}

	public static class PathIterableWrapper extends IterableWrapper<Citation, Path> {
		public PathIterableWrapper(Iterable<Path> iterable) {
			super(iterable);
		}

		@Override
		protected Citation underlyingObjectToObject( Path path ) {
			return new Citation( path.endNode() );
		}
	}

	public String toString() {
		return "{" + getDescription() + ":" + getResource() + ":" + getDateCreated()+ ":" + getId()+ "}";
	}
}
