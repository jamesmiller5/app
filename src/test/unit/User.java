package test.unit;

import org.neo4j.graphdb.*;
import static org.junit.Assert.*;
import org.junit.*;

/**
 * Unit test for {@link app.User}.
 */
public class User {
	app.User u;

	@Before
	@After
	public void clearDb() {
		app.GraphDatabase.clearDb();
	}

	@Test
	public void testUserCreation() {
	}

	@Test
	public void testEmails() {
		GraphDatabaseService graphDb = app.GraphDatabase.get();
		try(Transaction tx = graphDb.beginTx()) {
			app.User user = new app.User();
			app.Email email;
			email= new app.Email("test@test");
			user.addEmail(email);
			email = new app.Email("2@2");
			user.addEmail(email);
			System.out.println("Testing emails");
			for(app.Email e : user.viewEmails()) {
				System.out.println( e.getInternalNode().hasLabel(app.LabelDef.EMAIL) );
				System.out.println( "Email found: "+e.getAddress() );
			}
			tx.success();
		}

	}
}
