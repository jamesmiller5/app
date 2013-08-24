package test.unit;

import static org.junit.Assert.assertEquals;
import org.junit.*;

/**
 * Unit test for {@link app.Hello}.
 */
public class Hello {
	app.Hello h;
	@Before
	public void setup() {
		h = new app.Hello();
	}

	@After
	public void teardown() {
		h = null;
	}

    @Test
    public void testPing() {
		assertEquals(h.ping(), "pong!");
    }
}
