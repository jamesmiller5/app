package test.unit;

import static org.junit.Assert.assertEquals;
import org.junit.*;

/**
 * Unit test for {@link app.Email}.
 */
public class Email {
	@Before
	public void setup() {
	}

	@After
	public void teardown() {
	}

	//We expect this to throw an exception
	@Test(expected=IllegalArgumentException.class)
	public void isValid_null_exception() {
		app.Email isNull = new app.Email(null);
	}

	//We expect this to throw an exception
	@Test(expected=IllegalArgumentException.class)
	public void isValid_blank_exception() {
		app.Email isBlank = new app.Email("");
	}

	@Test
	public void validUnclaimed() {
		app.Email email = new app.Email("ab@ba");

		assertEquals( email.isValid(), true );
		assertEquals( email.isClaimed, false );
		assertEquals( email.getEmail(), email );
	}

    @Test
    public void isValid() {
		app.Email isOK = new app.Email("foo@example.com");

		//should pass, are valid
		assertEquals( isOK.isValid(), true );
    }
}
