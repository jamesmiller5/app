package test.unit;

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
	}
}
