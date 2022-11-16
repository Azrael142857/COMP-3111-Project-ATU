package ATU;

import javafx.beans.property.SimpleStringProperty;

// Person: store a single student's private info
// All properties are stored with SimpleStringProperty
// Helper functions (set/get) expect String parameter/return-value
public class Person {
	private final SimpleStringProperty studentid;
	private final SimpleStringProperty studentname;
	private final SimpleStringProperty studentemail;
	private final SimpleStringProperty k1energy;
	private final SimpleStringProperty k2energy;
	private final SimpleStringProperty k3tick1;
	private final SimpleStringProperty k3tick2;
	private final SimpleStringProperty mypreference;
	private final SimpleStringProperty concerns;
	private final SimpleStringProperty groupNumber; //value this property should be "-1" before running ATU Engine

	public Person(String student_id, String student_name, String student_email, 
				   String k1_energy, String k2_energy, String k3_tick1, 
				   String k3_tick2, String my_preference, String concerns) {
		this.studentid = new SimpleStringProperty(student_id);
		this.studentname = new SimpleStringProperty(student_name);
		this.studentemail = new SimpleStringProperty(student_email);
		this.k1energy = new SimpleStringProperty(k1_energy);
		this.k2energy = new SimpleStringProperty(k2_energy);
		this.k3tick1 = new SimpleStringProperty(k3_tick1);
		this.k3tick2 = new SimpleStringProperty(k3_tick2);
		this.mypreference = new SimpleStringProperty(my_preference);
		this.concerns = new SimpleStringProperty(concerns);
		this.groupNumber = new SimpleStringProperty("N/A");
	}

	// Helper functions (set/get) to alter/access data
	public String getStudentid() { return studentid.get(); }
	public void setStudentid(String val) { studentid.set(val); }

	public String getStudentname() { return studentname.get(); }
	public void setStudentname(String val) { studentname.set(val); }
	
	public String getStudentemail() { return studentemail.get(); }
	public void setStudentemail(String val) { studentemail.set(val); }

	public String getK1energy() { return k1energy.get(); }
	public int getIntegerK1energy() { return Integer.valueOf( k1energy.get() ); }
	public void setK1energy(String val) { k1energy.set(val); }

	public String getK2energy() { return k2energy.get(); }
	public int getIntegerK2energy() { return Integer.valueOf( k2energy.get() ); }
	public void setK2energy(String val) { k2energy.set(val); }

	public String getK3tick1() { return k3tick1.get(); }
	public void setK3tick1(String val) { k3tick1.set(val); }

	public String getK3tick2() { return k3tick2.get(); }
	public void setK3tick2(String val) { k3tick2.set(val); }

	public String getMypreference() { return mypreference.get(); }
	public void setMypreference(String val) { mypreference.set(val); }

	public String getConcerns() { return concerns.get(); }
	public void setConcerns(String val) { concerns.set(val); }
	
	public String getGroupNumber() { return groupNumber.get(); }
	public int getIntegerGroupNumber() {
		if (groupNumber.get().equals("N/A")) return -1;
		return Integer.valueOf(groupNumber.get());
	}
	public void setGroupNumber(String val) { groupNumber.set(val); }
}