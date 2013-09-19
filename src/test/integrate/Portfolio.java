package test.integrate;

import static org.junit.Assert.*;
import org.junit.*;
import org.neo4j.graphdb.Transaction;

/**
 * Unit test for {@link app.Portfolio}.
 */
public class Portfolio {

	@Before
	@After
	public void clearDb() {
		app.GraphDatabase.clearDb();
	}

	@Test
	public void check_addToPortfolio() {
		assertTrue(true);
	}
}
