package ATU;

import javafx.beans.property.SimpleStringProperty;

/**
 * GroupingInfo: stores the grouping information needed when a students inquires
 * @author Yang Yuang
 */
public class GroupingInfo {
	private final SimpleStringProperty studentID;
    private final SimpleStringProperty myName;
    private final SimpleStringProperty teamNo;
    private final SimpleStringProperty teammate1;
    private final SimpleStringProperty teammate2;
    private final SimpleStringProperty teammate3;
    private final SimpleStringProperty k1Avg;
    private final SimpleStringProperty k2Avg;
    
    /**
     * the constructor for Grouping Info
     * @param student_id the student ID of the student making the inquiry
     * @param my_name the name of the student making the inquiry
     * @param team_no the team number of the student's team
     * @param teammate1 the name of the student's first team mate
     * @param teammate2	the name of the student's second team mate
     * @param teammate3 the name of the student's first team mate (if exists)
     * @param k1Avg	the average K1 energy of the student's team
     * @param k2Avg the average K2 energy of the student's team
     */
    public GroupingInfo(String student_id, String my_name, String team_no, 
			   String teammate1, String teammate2, String teammate3, 
			   String k1Avg, String k2Avg) {
	this.studentID = new SimpleStringProperty(student_id);
	this.myName = new SimpleStringProperty(my_name);
	this.teamNo = new SimpleStringProperty(team_no);
	this.teammate1 = new SimpleStringProperty(teammate1);
	this.teammate2 = new SimpleStringProperty(teammate2);
	this.teammate3 = new SimpleStringProperty(teammate3);
	this.k1Avg = new SimpleStringProperty(k1Avg);
	this.k2Avg = new SimpleStringProperty(k2Avg);
    }
    /**
     * helper function to access the student's ID
     * @return string that indicates the student's ID
     */
    public String getStudentID() { return studentID.get(); }
    /**
     * helper function to set the student's ID
     * @param val a string to set the student's ID to be
     */
	public void setStudentid(String val) { studentID.set(val); }
	/**
     * helper function to access the student's name
     * @return string that indicates the student's name
     */
	public String getMyName() { return myName.get(); }
	/**
     * helper function to set the student's name
     * @param val a string to set the student's name to be
     */
	public void setMyName(String val) { myName.set(val); }
	
	/**
     * helper function to access the team number
     * @return string that indicates the team number
     */
	public String getTeamNo() { return teamNo.get(); }
	/**
     * helper function to access the team number in integer form
     * @return integer that indicates the team number
     */
	public int getIntegerTeamNo() { return Integer.valueOf( teamNo.get() ); }
	/**
     * helper function to set the team number
     * @param string that indicates the team number
     */
	public void setTeamNo(String val) { teamNo.set(val); }

	/**
	 * helper function to get the name of first team mate
	 * @return string that indicate the first team mate's name
	 */
	public String getTeammate1() { return teammate1.get(); }
	/**
	 * helper function to set the 1st team mate's name
	 * @param val a string that indicate the first team mate's name to be
	 */
	public void setTeammate1(String val) { teammate1.set(val); }
	
	/**
	 * helper function to get the name of second team mate
	 * @return string that indicate the second team mate's name
	 */
	public String getTeammate2() { return teammate2.get(); }
	/**
	 * helper function to set the second team mate's name
	 * @param val a string that indicate the second team mate's name to be
	 */
	public void setTeammate2(String val) { teammate2.set(val); }
	
	/**
	 * helper function to get the name of third team mate
	 * @return string that indicate the third team mate's name
	 */
	public String getTeammate3() { return teammate3.get(); }
	/**
	 * helper function to set the third team mate's name
	 * @param val a string that indicate the third team mate's name to be
	 */
	public void setTeammate3(String val) { teammate3.set(val); }

	/**
	 * helper function the get the average K2 energy in the team
	 * @return a string that indicates the average K2 energy in the team
	 */
	public String getK2Avg() { return k2Avg.get(); }
	/**
	 * helper function the get the average K2 energy in the team as an integer
	 * @return an integer that indicates the average K2 energy in the team
	 */
	public int getIntegerK2Avg() { return Integer.valueOf( k2Avg.get() ); }
	/**
	 * helper function to set the average K2 energy
	 * @param val a string that indicates the average K2 energy
	 */
	public void setK2Avg(String val) { k2Avg.set(val); }

	/**
	 * helper function the get the average K1 energy in the team
	 * @return a string that indicates the average K1 energy in the team
	 */
	public String getK1Avg() { return k1Avg.get(); }
	/**
	 * helper function to set the average K1 energy
	 * @param val a string that indicates the average K1 energy
	 */
	public void setK1Avg(String val) { k1Avg.set(val); }

}
