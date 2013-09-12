package test.unit;

import static org.junit.Assert.assertEquals;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.junit.*;

/**
 * Unit test for {@link app.RelationshipFactory}
 */
public class RelationshipFactory {
	@Before
	public void setup() {
		DatabaseTester.clearDb();
	}

	@After
	public void teardown() {
		app.GraphDatabase.shutdown();
	}

	@Test
	public void simpleRelationship() {

		GraphDatabaseService graphDb = app.GraphDatabase.get();
		try(Transaction tx = graphDb.beginTx()) {
			Node start = graphDb.createNode(DynamicLabel.label("Start"));
			Node end = graphDb.createNode(DynamicLabel.label("End"));
			Relationship relationship = app.RelationshipFactory.get()
				.getOrCreateRelationship( start, end , app.RelType.OWNS );
		}
	}

	@Test
	public void selfReferential() {

	}
}
