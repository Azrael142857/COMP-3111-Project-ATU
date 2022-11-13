package ATU;

import javafx.beans.property.SimpleStringProperty;

public class GroupingInfo {
	private final SimpleStringProperty studentID;
    private final SimpleStringProperty myName;
    private final SimpleStringProperty teamNo;
    private final SimpleStringProperty teammate1;
    private final SimpleStringProperty teammate2;
    private final SimpleStringProperty teammate3;
    private final SimpleStringProperty k1Avg;
    private final SimpleStringProperty k2Avg;
    
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
    
    public String getStudentID() { return studentID.get(); }
	public void setStudentid(String val) { studentID.set(val); }

	public String getMyName() { return myName.get(); }
	public void setMyName(String val) { myName.set(val); }
	
	public String getTeamNo() { return teamNo.get(); }
	public int getIntegerTeamNo() { return Integer.valueOf( teamNo.get() ); }
	public void setTeamNo(String val) { teamNo.set(val); }

	public String getTeammate1() { return teammate1.get(); }
	public void setTeammate1(String val) { teammate1.set(val); }
	
	public String getTeammate2() { return teammate2.get(); }
	public void setTeammate2(String val) { teammate2.set(val); }
	
	public String getTeammate3() { return teammate3.get(); }
	public void setTeammate3(String val) { teammate3.set(val); }

	public String getK2Avg() { return k2Avg.get(); }
	public int getIntegerK2Avg() { return Integer.valueOf( k2Avg.get() ); }
	public void setK2Avg(String val) { k2Avg.set(val); }

	public String getK1Avg() { return k1Avg.get(); }
	public void setK1Avg(String val) { k1Avg.set(val); }

}
