package test.unit;

import static org.junit.Assert.*;
import org.junit.*;
import java.util.Date;

/**
 * Unit test for {@link app.Citation}.
 */
public class Citation {
	app.Citation c;
	
	@Before
	public void setup() {
	}

	@After
	public void teardown() {
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void excessiveDescription() {
		String s = "";
		for(int i = 0; i < 257; i++) {
			s.concat("a");
		}
		
		c = new app.Citation(s, "test");
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void excessiveResource() {
		String r = "";
		for(int i = 0; i < 257; i++) {
			r.concat("a");
		}
		
		c = new app.Citation(r, "test");
	}

}
