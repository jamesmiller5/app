package app;

import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

public class Hello {

	private static enum RelTypes implements RelationshipType {
		KNOWS
	}

	public static void main(String[] args) {
		System.out.println("Hello World\n");

		//reset database to blank
		GraphDatabase.clearDb();

		//simple database test
		try (Transaction tx = GraphDatabase.get().beginTx()){
			// do stuff inside the transaction
			Node firstNode, secondNode;
			Relationship relationship;

			firstNode = GraphDatabase.get().createNode();
			firstNode.setProperty( "message", "Hello, " );
			secondNode = GraphDatabase.get().createNode();
			secondNode.setProperty( "message", "World!" );

			relationship = firstNode.createRelationshipTo( secondNode, RelTypes.KNOWS );
			relationship.setProperty( "message", "brave Neo4j " );

			System.out.print( firstNode.getProperty( "message" ) );
			System.out.print( relationship.getProperty( "message" ) );
			System.out.println( secondNode.getProperty( "message" ) );

			tx.success();
		}
	}

	public String ping() {
		return "pong!";
	}
}
