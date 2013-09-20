package test.integrate;

import static org.junit.Assert.*;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.GraphDatabaseService;
import org.junit.*;

/**
 * Unit test for {@link app.User}.
 */
public class Registration {

	@After
	@Before
	public void setup() {
		app.GraphDatabase.clearDb();
	}

	@Test
	public void check_signup() {
		final String email_address = "test@integration.com";
		
		app.Email e = null;
		try {
			//check if email already exists
			e = new app.Email(app.Entity.findExistingNode(app.LabelDef.EMAIL, app.Email.EMAIL_KEY, email_address));
		}catch(IllegalStateException ise) {
			//email does not exist, therefore create a new one w/ claimtoken
			e = new app.Email(email_address);
		}

		assertEquals(e.getAddress(), email_address);
	}
	
	@Test
	public void check_multiple_signup() {
		final String email_address = "test@integration.com";
		app.ClaimToken ct = null;
		
		for(int i = 0; i < 10; i++) {
			app.Email e = null;
			try {
				//check if email already exists
				e = new app.Email(app.Entity.findExistingNode(app.LabelDef.EMAIL, app.Email.EMAIL_KEY, email_address));
			}catch(IllegalStateException ise) {
				//email does not exist, therefore create a new one w/ claimtoken
				e = new app.Email(email_address);
			}

			if(i > 0)
				assertEquals(e.getClaimToken().toString(), ct.toString());
				
			ct = e.getClaimToken();
		
			assertEquals(e.getAddress(), email_address);
		}
	}
	
	@Test
	public void check_taken_email() {
		app.Email e = new app.Email("test@integration.com");
		
		assertNull(e.getUser());
		
		GraphDatabaseService graphDB = app.GraphDatabase.get();
		try(Transaction tx = graphDB.beginTx()) {
			
			app.User u = new app.User();
			u.setPassword("password");
			u.addEmail(e);
		
			e.clearClaimToken();
		
			assertNotNull(e.getUser());
		
			app.Email f = new app.Email(app.Entity.findExistingNode(app.LabelDef.EMAIL, app.Email.EMAIL_KEY, "test@integration.com"));
		
			//make sure the email has been found
			assertNotNull(f.getUser());
			
			//confirm that both email objects are indeed the same, restricting users from using a single email for multiple accounts
			assertEquals(e.getAddress(), f.getAddress());
			assertNotEquals(e.getAddress() + "fart", f.getAddress() + "traf");
			assertEquals(e.getClaimToken(), f.getClaimToken());
			
			
			tx.success();
		}
	}
	
	@Test
	public void check_register() {
		
		String name = "whocares";
		String pass = "nobody";
		String verify = "nobody";
		
		assertTrue(pass.equals(verify));
		
		app.Email e = new app.Email("test@integration.com");
		assertNotNull(e.getClaimToken());
		String ct = e.getClaimToken().toString();
		assertNull(e.getUser());
		
		app.Email f = null;
		GraphDatabaseService graphDB = app.GraphDatabase.get();
		try(Transaction tx = graphDB.beginTx()) {
		
			f = new app.Email(app.Entity.findExistingNode(app.LabelDef.EMAIL, app.Email.CLAIM_TOKEN, ct));
			assertNotNull(f);
			
			tx.success();
		}
		
		assertTrue(ct.equals(f.getClaimToken().toString()));
		
		try(Transaction tx = graphDB.beginTx()) {
			app.User u = new app.User();
			u.setPassword(pass);
			u.addEmail(f);
		
			f.clearClaimToken();
		
			assertNotNull(f.getUser());
			assertNull(f.getClaimToken());
			assertEquals(u.getPassword(), pass);
		
			tx.success();
		
		}
		
	}
}
