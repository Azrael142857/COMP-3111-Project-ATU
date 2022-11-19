package ATU;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class GroupingInfoTest {
	private GroupingInfo info = null;

	@Before public void setUp() throws Exception {
		info = new GroupingInfo("3", "3", "Yuang", "206777", "one", "two", "three", "9");
	}

	@Test public void testGroupingInfo() {
		info.setK1Avg("4");
		assertEquals(info.getK1Avg(), "4");
		info.setK2Avg("4");
		assertEquals(info.getK2Avg(), "4");
		assertEquals(info.getIntegerK2Avg(), 4);
		info.setMyName("yuang");
		assertEquals(info.getMyName(), "yuang");
		info.setStudentid("88");
		assertEquals(info.getStudentID(), "88");
		info.setTeammate1("1");
		assertEquals(info.getTeammate1(), "1");
		info.setTeammate2("2");
		assertEquals(info.getTeammate2(), "2");
		info.setTeammate3("3");
		assertEquals(info.getTeammate3(), "3");
		info.setTeamNo("88");
		assertEquals(info.getTeamNo(), "88");
		assertEquals(info.getIntegerTeamNo(), 88);
	}
}