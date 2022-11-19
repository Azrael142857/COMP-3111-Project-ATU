package ATU;

public class Team {
	private int k1Avg = 0;
	private int k2Avg = 0;
	private int energyAvg = 0; //mean of k1 and k2
	private final int groupNumber;
	private int num_members = 0;

	public Team(int group_number) {
		this.groupNumber = group_number;
	}
	
	public void calculateTeamInfo() {
		if (num_members == 0) {return;}
		k1Avg = k1Avg/num_members;
		k2Avg = k2Avg/num_members;
		energyAvg = (k1Avg+k2Avg)/2;
	}

	// Helper functions (set/get) to alter/access data
	public int getGroupNumber() {
		return groupNumber;
	}
	
	public int getEnergyAvg() {
		return energyAvg;
	}
	
	public int getk1Avg() { return k1Avg; }
	public void setk1Avg(int val) { k1Avg = val; }
	
	public int getk2Avg() { return k2Avg; }
	public void setk2Avg(int val) { k2Avg = val; }
	
	public int getNumMembers() { return num_members; }
	public void setNumMembers(int val) { num_members = val; }
	
}
