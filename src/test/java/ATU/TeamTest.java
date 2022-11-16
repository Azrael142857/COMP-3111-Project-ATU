package ATU;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class TeamTest {
	private Team team = null;

	@Before public void setUp() throws Exception {
		team = new Team(3);
	}

	@Test public void testTeam() {
		assertEquals(team.getNumMembers(), 0);
		team.setNumMembers(3);
		assertEquals(team.getNumMembers(), 3);
		
		team.setk1Avg(3);
		assertEquals(team.getk1Avg(), 3);
		team.setk2Avg(3);
		assertEquals(team.getk2Avg(), 3);
		
		assertEquals(team.getGroupNumber(), 3);
		team.calculateTeamInfo();
		assertEquals(team.getEnergyAvg(), 1);
		
		team.setNumMembers(0);
		team.calculateTeamInfo();
	}
}