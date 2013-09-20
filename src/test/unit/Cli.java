package test.unit;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * Unit test for {@link app.User}.
 */
public class Cli {
	app.Cli c;

	@Before
	public void setup() {
		app.GraphDatabase.clearDb();
		c = new app.Cli();
	}

	@Test
	public void testSignup() {
		String email_address = "email@domain.com";
		c.signup(email_address);
	}
}
