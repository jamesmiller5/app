package test.unit;

import static org.junit.Assert.*;
import org.junit.*;
import java.util.Date;
import java.util.Calendar;
import org.neo4j.graphdb.Transaction;
/**
 * Unit test for {@link app.Session}.
 */
public class Session {

	@Before
	@After
	public void clearDb() {
		app.GraphDatabase.clearDb();
	}

	@Test
	public void checkExpiration_valid() {
		// Create new user
		String uemail = "user@example.com";
		String upass = "password";

		app.Email email = new app.Email(uemail);
		app.User user = new app.User();
		user.setPassword(upass);
		user.addEmail(email);

		app.Session s = app.Session.createFromLogin(uemail, upass);
		assertTrue(s.isValid());
	}

	@Test
	public void checkExpiration_noSuchUser() {
		// Create new user
		String uname = "user@example.com";
		String upass = "password";

		app.Session s = app.Session.createFromLogin(uname, upass);
		assertNull(s);
	}
}
