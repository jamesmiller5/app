package test.unit;

import static org.junit.Assert.assertEquals;
import org.junit.*;

public class DatabaseTester {
	private static boolean isClean = false;

	public static void clearDb() {
		if( !isClean ) {
			app.GraphDatabase.clearDb();
			isClean = true;
		}
	}

	public static void markDirty() {
		isClean = false;
	}

	@Test
	public void noop() {
		//blank just to haave this utility class
	}
}
