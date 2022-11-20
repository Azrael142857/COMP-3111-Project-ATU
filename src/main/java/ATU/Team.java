package ATU;

/**
 * Team: store a team's info.
 * @author Yang Yuang
 */
public class Team {
	private int k1Avg = 0;
	private int k2Avg = 0;
	private int energyAvg = 0; //mean of k1 and k2
	private final int groupNumber;
	private int num_members = 0;

	/**
	 * Construct a new Team object with given group number.
	 * @param group_number the index number of the group
	 */
	public Team(int group_number) {
		this.groupNumber = group_number;
	}
	/**
	 * helper function to calculate final group information
	 */
	public void calculateTeamInfo() {
		if (num_members == 0) {return;}
		k1Avg = k1Avg/num_members;
		k2Avg = k2Avg/num_members;
		energyAvg = (k1Avg+k2Avg)/2;
	}

	/**
	 * Helper function to access group number
	 * @return an integer indicating group number
	 */
	public int getGroupNumber() {
		return groupNumber;
	}
	/**
	 * Helper function to access average K1 and K2 energy
	 * @return an integer indicating the the average value of energy of the team
	 */
	public int getEnergyAvg() {
		return energyAvg;
	}
	/**
	 * Helper function to access average K1 energy
	 * @return an integer indicating average K1 energy
	 */
	public int getk1Avg() { return k1Avg; }
	/**
	 * Helper function to modify average K1 energy
	 * @param val the value to set k1Avg to be
	 */
	public void setk1Avg(int val) { k1Avg = val; }
	/**
	 * Helper function to access average K2 energy
	 * @return an integer indicating average K2 energy
	 */
	public int getk2Avg() { return k2Avg; }
	/**
	 * Helper function to modify average K2 energy
	 * @param val the value to set k2Avg to be
	 */
	public void setk2Avg(int val) { k2Avg = val; }
	/**
	 * Helper function to access the number of members within the group
	 * @return an integer indicating number of members within the group
	 */
	public int getNumMembers() { return num_members; }
	/**
	 * Helper function to modify the number of members within a group
	 * @param val the value to set the number of members to be
	 */
	public void setNumMembers(int val) { num_members = val; }
	
}
