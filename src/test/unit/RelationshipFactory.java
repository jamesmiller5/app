package test.unit;

import static org.junit.Assert.assertEquals;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.junit.*;
import java.util.Iterator;

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
		Node start, end;
		Relationship relationship;
		try(Transaction tx = graphDb.beginTx()) {
			start = graphDb.createNode(DynamicLabel.label("Start"));
			end = graphDb.createNode(DynamicLabel.label("End"));
			relationship = app.RelationshipFactory.get()
				.getOrCreateRelationship( start, end , app.RelType.OWNS );
			assertEquals( start, relationship.getStartNode() );
			assertEquals( end, relationship.getEndNode() );
			assertEquals( app.RelType.OWNS.name(), relationship.getType().name());

			int count = 0;
			for(Relationship r : start.getRelationships(Direction.OUTGOING)) {
				count++;
			}
			tx.success();
			assertEquals( count, 1 );
		}
	}

	@Test
	public void duplicatesNotCreated() {
		GraphDatabaseService graphDb = app.GraphDatabase.get();
		Node start, end;
		Relationship relationship;
		try(Transaction tx = graphDb.beginTx()) {
			start = graphDb.createNode(DynamicLabel.label("Start"));
			end = graphDb.createNode(DynamicLabel.label("End"));
			tx.success();
		}
		for(int i=0; i<5; i++) {
			try(Transaction tx = graphDb.beginTx()) {
				relationship = app.RelationshipFactory.get()
					.getOrCreateRelationship( start, end , app.RelType.OWNS );
				relationship = app.RelationshipFactory.get()
					.getOrCreateRelationship( start, end, app.RelType.OWNS );

				int count = 0;
				for(Relationship r : start.getRelationships(Direction.OUTGOING)) {
					count++;
				}
				tx.success();
				assertEquals( count, 1 );
			}
		}
	}

	@Test
	public void distinctRelationships() {
		GraphDatabaseService graphDb = app.GraphDatabase.get();
		Node start, end;
		Relationship relationship;
		try(Transaction tx = graphDb.beginTx()) {
			start = graphDb.createNode(DynamicLabel.label("Start"));
			end = graphDb.createNode(DynamicLabel.label("End"));

			relationship = app.RelationshipFactory.get()
				.getOrCreateRelationship( start, end , app.RelType.OWNS );
			relationship = app.RelationshipFactory.get()
				.getOrCreateRelationship( start, end , app.RelType.REASON );
			relationship = app.RelationshipFactory.get()
				.getOrCreateRelationship( end, start, app.RelType.OWNS );
			relationship = app.RelationshipFactory.get()
				.getOrCreateRelationship( end, start, app.RelType.REASON );

			int count = 0;
			for(Relationship r : start.getRelationships(Direction.BOTH)) {
				count++;
			}
			tx.success();
			assertEquals( count, 4 );
		}
	}
}
