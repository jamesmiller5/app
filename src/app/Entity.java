package app;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.index.*;

public abstract class Entity {
	protected Node internalNode;

	protected void initialize(Node internalNode) {
		this.internalNode = internalNode;
	}

	public Relationship createRelationshipTo(Entity other, RelType type) {
		return RelationshipFactory.get().getOrCreateRelationship(this.internalNode, other.internalNode, type);
	}

	public static GraphDatabaseService graphDb() {
		return GraphDatabase.get();
	}

	public static <T> T getSingle(ResourceIterable<T> iterable) {
		try (ResourceIterator<T> iterator = iterable.iterator()) {
			if( !iterator.hasNext() ) throw new IllegalStateException();
			return iterator.next();
		}
	}

	public Node getInternalNode() {
		return internalNode;
	}

	public static Node findExistingNode(Label label, String key, Object value) {
		try(Transaction tx = graphDb().beginTx()) {
			ResourceIterable<Node> nodes = graphDb().findNodesByLabelAndProperty( label, key, value );
			Node node = getSingle( nodes );
			tx.success();
			return node;
		}
	}
}
