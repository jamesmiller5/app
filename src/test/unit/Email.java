package test.unit;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * Unit test for {@link app.Email}.
 */
public class Email {
	@After
	@Before
	public void clearDb() {
		app.GraphDatabase.clearDb();
	}

	@Test(expected=IllegalArgumentException.class)
	public void isValid_null_exception() {
		app.Email isNull = new app.Email((String)null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void isValid_blank_exception() {
		app.Email isBlank = new app.Email("");
	}

	@Test(expected=IllegalArgumentException.class)
	public void isValid_no_at_sign_exception() {
		app.Email isBlank = new app.Email("aazz");
	}

	//We expect this to throw an exception
	@Test(expected=IllegalArgumentException.class)
	public void isValid_only_at_sign_exception() {
		app.Email atOnly = new app.Email("@");
	}

	//We expect this to throw an exception
	@Test(expected=IllegalArgumentException.class)
	public void isValid_at_is_at_end() {
		app.Email atEnd = new app.Email("foo@");
	}

	//We expect this to throw an exception
	@Test(expected=IllegalArgumentException.class)
	public void isValid_at_is_at_front() {
		app.Email atFront = new app.Email("@example.com");
	}

	@Test
	public void checkBasicEmail() {
		app.Email email = new app.Email("foo@example.com");

		// should be starting from a fresh database so email should
		// be unique.
		assertNotNull( email.getClaimToken() );
	}

    @Test
    public void isValidAddress() {
		app.Email validEmail = new app.Email("foo@example.com");

		//should pass, are valid
		assertTrue( app.Email.isValidAddress(validEmail.getAddress()));
    }

	@Test
	public void checkNoDuplicate() {
		app.Email sameEmail1 = new app.Email("aa@aa");
		app.Email sameEmail2 = new app.Email("aa@aa");
		app.Email diffEmail  = new app.Email("bb@bb");

		assertEquals( sameEmail1.getClaimToken(), sameEmail2.getClaimToken() );
		assertFalse(  diffEmail.getClaimToken().equals(sameEmail1.getClaimToken()) );
		assertFalse(  diffEmail.getClaimToken().equals(sameEmail2.getClaimToken()) );

		assertTrue("duplicate email is different Node.", sameEmail1.getInternalNode().equals(sameEmail2.getInternalNode()));
		assertTrue("different email is the same Node.", diffEmail.getInternalNode().getId()!=sameEmail1.getInternalNode().getId());
	}
}
