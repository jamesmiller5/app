package app;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.index.*;
import java.util.Map;

public class UniqueNodeFactory {
	final protected String key;
	final protected String index;

	private GraphDatabaseService graphDbRef = null;
	private UniqueFactory<Node> factory = null;

	public UniqueNodeFactory(final String key, final String index) {
		this.key = key;
		this.index = index;
	}

	public Node getOrCreate(Object value) {
		GraphDatabaseService graphDb = GraphDatabase.get();
		try(Transaction tx = graphDb.beginTx()) {
			Node internalNode = getFactory(graphDb).getOrCreate( key, value );
			tx.success();
			return internalNode;
		}
	}
	private UniqueFactory<Node> getFactory(GraphDatabaseService graphDb) {
		if( graphDbRef != graphDb ) {
			factory = createFactory( graphDb );
			graphDbRef = graphDb;
		}
		return factory;
	}
	private UniqueFactory<Node> createFactory(GraphDatabaseService graphDb) {
		try( Transaction tx = graphDb.beginTx() ) {
			UniqueFactory<Node> factory = new UniqueFactory.UniqueNodeFactory( graphDb, index )
			{
				@Override
				protected void initialize( Node created, Map<String, Object> properties ) {
					created.setProperty( key, properties.get(key) );
					//created.addLabel( LabelDef.UNIQUE );
				}
			};
			tx.success();
			return factory;
		}
	}
}
