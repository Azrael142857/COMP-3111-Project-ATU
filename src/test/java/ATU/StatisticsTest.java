package ATU;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class StatisticsTest {
	private Statistics stat = null;

	@Before public void setUp() throws Exception {
		stat = new Statistics("Total Number of Students", "100");
	}

	@Test public void testStatistics() {
		// Test Entry Helper
		assertEquals(stat.getEntry(), "Total Number of Students");
		stat.setEntry("test");
		assertNotEquals(stat.getEntry(), "Total Number of Students");
		assertEquals(stat.getEntry(), "test");

		// Test Value Helper
		assertEquals(stat.getValue(), "100");
		stat.setValue("50");
		assertNotEquals(stat.getValue(), "100");
		assertEquals(stat.getValue(), "50");
	}
}
