package test.unit;

import static org.junit.Assert.*;
import org.junit.*;
import java.util.Date;

/**
 * Unit test for {@link app.Session}.
 */
public class Session {
	app.Session s;
	Date testDate;
	
	@Before
	public void setup() {
		testDate = new Date();
	}

	@After
	public void teardown() {
		s = null;
	}
	
	@Test
	public void checkExpiration() {
		s = new app.Session(new app.User(new app.Email[] { new app.Email("fart@traf") }), testDate);
		assertTrue(s.isValid());
	}

}
