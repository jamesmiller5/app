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
	Date futureDate;
	@Before
	public void setup() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.HOUR_OF_DAY, 1);
		futureDate = cal.getTime();
	}

	@After
	public void teardown() {
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
