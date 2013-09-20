package test.integrate;

import static org.junit.Assert.*;
import org.junit.*;
import org.neo4j.graphdb.Transaction;
import java.util.Iterator;
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
		try(Transaction tx = app.GraphDatabase.get().beginTx()) {
			String uemail = "user@example.com";
			String upass = "password";
			app.Email email = new app.Email(uemail);
			app.User user = new app.User();
			user.setPassword(upass);
			user.addEmail(email);
			String description = "description";
			String resource = "resource";
			app.Citation c = new app.Citation(description, resource);
			user.addToPortfolio(c);

			Iterator<app.Citation> iterator = user.viewPortfolio().iterator();
			StringBuilder output = new StringBuilder();
			assertTrue(iterator.hasNext());
			while (iterator.hasNext()) {
				app.Citation c1 = iterator.next();
				//output.append(c1.toString());
				assertFalse(c1.toString().equals(c.toString()+"gobbeldygook"));
				assertTrue(c1.toString().equals(c.toString()));
			}

			//System.out.println(output);
			//assertTrue(false);
		tx.success();
		}
	}

}
