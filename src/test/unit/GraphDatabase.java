package test.unit;

import static org.junit.Assert.assertEquals;
import org.junit.*;

/**
 * Unit test for {@link app.GraphDatabase}.
 */
public class GraphDatabase {
	@Before
	public void setup() {
	}

	@After
	public void teardown() {
	}

    @Test
    public void testScratch() {
		//TODO: test that a scratch directory & blank database is created if none existed
		//TODO: test that the default database location openes a database if one did exist
		//TODO: test that any write & read operations succeed regardless if database existed or not, aka it works out of the box with no errors
    }
}
