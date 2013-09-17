package test.unit;

import static org.junit.Assert.*;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Transaction;
import org.junit.*;

/**
 * Unit test for {@link app.Email}.
 */
public class Subject {
	@Before
	public void setup() {
		DatabaseTester.clearDb();
	}

	@After
	public void teardown() {
		app.GraphDatabase.shutdown();
	}

	/*
	@Test
	public void test_getSubjects() {
		app.Subject arr[] = app.Subject.getSubjects();
		try( Transaction tx = app.GraphDatabase.get().beginTx() ) {
			app.Subject sub2 = new app.Subject("Git");
			app.Subject sub1 = new app.Subject("Algorithms");
			app.Subject sub3 = new app.Subject("C++");

			arr = app.Subject.getSubjects();
		}
		assertEquals( 3, arr.length );
	}
	*/
}
