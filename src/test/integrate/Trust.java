package test.integrate;

import static org.junit.Assert.*;
import org.junit.*;
import org.neo4j.graphdb.Transaction;
import asg.cliche.Command;
import asg.cliche.ShellFactory;
import asg.cliche.InputConverter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.Iterator;
import org.neo4j.kernel.Traversal;
import org.neo4j.graphdb.traversal.*;
import org.neo4j.graphdb.*;

import app.*;

/**
 * Unit test for {@link app.viewTrustNetwork}.
 */
public class Trust {

	@Before
	@After
	public void clearDb() {
		app.GraphDatabase.clearDb();
	}

	@Test
	public void check_can_trust_user() {
		GraphDatabaseService gdb=app.GraphDatabase.get();

		try(Transaction tx=gdb.beginTx()) {
			User n, j;

			j=new User();
			j.addEmail(new Email("j@example.com"));

			n=new User();
			n.addEmail(new Email("n@example.com"));

			TrustEdge te = new TrustEdge( j, n, new Subject("testing") );

			Entity from = te.from();
			Entity to = te.to();

			assertNotNull( te.from() );
			assertTrue( from instanceof User );
			assertNotNull( te.to() );
			assertTrue( to instanceof User );
			assertTrue(((User)from).viewEmails().iterator().next().getAddress().equals("j@example.com"));
			assertTrue(((User)  to).viewEmails().iterator().next().getAddress().equals("n@example.com"));
			Subject s = te.subject();
			assertNotNull( s );
			assertEquals( s.getName(), "testing" );
			assertNotEquals( s.getName() + "stuff", "testing" );
			assertFalse( te.reasons().iterator().hasNext() );

			Citation c_source = new Citation( "reason" , "CS408 project excellence");
			te.addCitation( c_source );

			Iterator<Citation> hasOne = te.reasons().iterator();
			assertTrue( hasOne.hasNext() );
			Citation one = hasOne.next();
			assertNotNull( one );
			assertEquals( c_source.toString(), one.toString() );
			assertNotEquals( c_source.toString() + "stuff", one.toString() + "notffust" );

			tx.success();
		}
	}

	@Test
	public void check_can_trust_email() {
		GraphDatabaseService gdb=app.GraphDatabase.get();

		try(Transaction tx=gdb.beginTx()) {
			User j;
			Email n;

			j=new User();
			j.addEmail(new Email("j@example.com"));

			n=new Email("n@example.com");

			TrustEdge te = new TrustEdge( j, n, new Subject("testing") );

			Entity from = te.from();
			Entity to = te.to();

			assertNotNull( te.from() );
			assertTrue( from instanceof User );
			assertNotNull( te.to() );
			assertTrue( to instanceof Email );
			assertTrue(((User)from).viewEmails().iterator().next().getAddress().equals("j@example.com"));
			assertTrue(((Email)  to).getAddress().equals("n@example.com"));
			Subject s = te.subject();
			assertNotNull( s );
			assertEquals( s.getName(), "testing" );
			assertNotEquals( s.getName() + "stuff", "testing" );
			assertFalse( te.reasons().iterator().hasNext() );

			Citation c_source = new Citation( "reason" , "CS408 project excellence");
			te.addCitation( c_source );

			Iterator<Citation> hasOne = te.reasons().iterator();
			assertTrue( hasOne.hasNext() );
			Citation one = hasOne.next();
			assertNotNull( one );
			assertEquals( c_source.toString(), one.toString() );
			assertNotEquals( c_source.toString() + "stuff", one.toString() + "notffust" );

			tx.success();
		}
	}

	@Test
	public void check_can_untrust_email() {
		User j;
		Email n;
		TrustEdge te;

		try(Transaction tx= app.GraphDatabase.get().beginTx()) {
			j=new User();
			j.addEmail(new Email("j@example.com"));
			n=new Email("n@example.com");

			te = new TrustEdge( j, n, new Subject("testing") );
			assertFalse( te.reasons().iterator().hasNext() );

			Citation c_source = new Citation( "reason" , "CS408 project excellence");
			te.addCitation( c_source );

			Iterator<Citation> hasOne = te.reasons().iterator();
			assertTrue( hasOne.hasNext() );
			assertNotNull( hasOne.next() );
			assertFalse( hasOne.hasNext() );

			//test removal
			te.delete();

			for(Path pos:Traversal.description().breadthFirst().evaluator(Evaluators.fromDepth(1))
					.relationships(RelType.TO,Direction.OUTGOING).traverse(j.getInternalNode())){
				//should error, no traversal should happen
				assertTrue( false );
			}

			tx.success();
		}
	}
}
