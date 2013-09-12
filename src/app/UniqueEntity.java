package app;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.index.*;
import java.util.Map;

public abstract class UniqueEntity extends Entity {
	private boolean wasCreated;

	public UniqueEntity(Node internalNode) {
		this(internalNode, false);
	}

	public UniqueEntity(String key, Object value, UniqueFactory<Node> factory) {
		try(Transaction tx = graphDb().beginTx()) {
			if( factory == null ) {
				throw new IllegalStateException("Factory not initialized.");
			}
			UniqueFactory.UniqueEntity<Node> result = factory.getOrCreateWithOutcome( key, value );
			result.entity().addLabel(LabelDef.ENTITY);
			initialize(result.entity());
			this.wasCreated = result.wasCreated();
			tx.success();
		}

	}

	private UniqueEntity(Node internalNode, boolean wasCreated) {
		initialize(internalNode);
		this.wasCreated = wasCreated;
	}

	public boolean wasCreated() {
		return wasCreated;
	}

	public static UniqueFactory<Node> createFactory(final String key, final String index) {
		try( Transaction tx = graphDb().beginTx() ) {
			UniqueFactory<Node> factory = new UniqueFactory.UniqueNodeFactory( graphDb(), index )
			{
				@Override
				protected void initialize( Node created, Map<String, Object> properties ) {
					created.setProperty( key, properties.get(key) );
				}
			};

			return factory;
		}
	}
}
