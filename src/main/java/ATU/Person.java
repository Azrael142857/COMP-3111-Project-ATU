package ATU;

import javafx.beans.property.SimpleStringProperty;

/**
 * Person: store a single student's private info.
 * All properties are stored with SimpleStringProperty.
 * Helper functions (set/get) expect String parameter/return-value.
 * @author SHU Tian
 */
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
	private final SimpleStringProperty groupNumber; // value of this property should be "N/A" before running ATU Engine

	/**
	 * Construct a new Person object with given attribute values.
	 * @param student_id the student's ID number
	 * @param student_name the student's name
	 * @param student_email the student's email
	 * @param k1_energy the student's K1 Energy value
	 * @param k2_energy the student's K2 Energy value
	 * @param k3_tick1 0/1 value, whether "Is Creative" is selected
	 * @param k3_tick2 0/1 value, whether "Willing more workloads" is selected
	 * @param my_preference 0/1 value, whether "Wanna be project leader" is selected
	 * @param concerns any student's comment
	 */
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

	/**
	 * Helper function to get student ID
	 * @return a string indicating student ID
	 */
	public String getStudentid() { return studentid.get(); }
	/**
	 * Helper function to set new student ID
	 * @param val a string of the new student ID
	 */
	public void setStudentid(String val) { studentid.set(val); }

	/**
	 * Helper function to get student name
	 * @return a string indicating student name
	 */
	public String getStudentname() { return studentname.get(); }
	/**
	 * Helper function to set new student name
	 * @param val a string of the new student name
	 */
	public void setStudentname(String val) { studentname.set(val); }

	/**
	 * Helper function to get student's email
	 * @return a string indicating student's email
	 */
	public String getStudentemail() { return studentemail.get(); }
	/**
	 * Helper function to set new email
	 * @param val a string of the new email
	 */
	public void setStudentemail(String val) { studentemail.set(val); }

	/**
	 * Helper function to get student's K1 Energy value
	 * @return a string indicating student's K1 Energy value
	 */
	public String getK1energy() { return k1energy.get(); }
	/**
	 * Helper function to get student's K1 Energy value by integer
	 * @return an integer indicating student's K1 Energy value
	 */
	public int getIntegerK1energy() { return Integer.valueOf( k1energy.get() ); }
	/**
	 * Helper function to set new K1 Energy value
	 * @param val a string of the new K1 Energy value
	 */
	public void setK1energy(String val) { k1energy.set(val); }

	/**
	 * Helper function to get student's K2 Energy value
	 * @return a string indicating student's K2 Energy value
	 */
	public String getK2energy() { return k2energy.get(); }
	/**
	 * Helper function to get student's K2 Energy value by integer
	 * @return an integer indicating student's K2 Energy value
	 */
	public int getIntegerK2energy() { return Integer.valueOf( k2energy.get() ); }
	/**
	 * Helper function to set new K2 Energy value
	 * @param val a string of the new K2 Energy value
	 */
	public void setK2energy(String val) { k2energy.set(val); }

	/**
	 * Helper function to get student's K3 Tick1 value
	 * @return a string indicating student's K3 Tick1 value
	 */
	public String getK3tick1() { return k3tick1.get(); }
	/**
	 * Helper function to set new K3 Tick1 value
	 * @param val a string of the new K3 Tick1 value
	 */
	public void setK3tick1(String val) { k3tick1.set(val); }

	/**
	 * Helper function to get student's K3 Tick2 value
	 * @return a string indicating student's K3 Tick2 value
	 */
	public String getK3tick2() { return k3tick2.get(); }
	/**
	 * Helper function to set new K3 Tick2 value
	 * @param val a string of the new K3 Tick2 value
	 */
	public void setK3tick2(String val) { k3tick2.set(val); }

	/**
	 * Helper function to get student's My Preference value
	 * @return a string indicating student's My Preference value
	 */
	public String getMypreference() { return mypreference.get(); }
	/**
	 * Helper function to set new My Preference value
	 * @param val a string of the new My Preference value
	 */
	public void setMypreference(String val) { mypreference.set(val); }

	/**
	 * Helper function to get student's concerns
	 * @return a string indicating student's concerns
	 */
	public String getConcerns() { return concerns.get(); }
	/**
	 * Helper function to set student's new concerns
	 * @param val a string of the student's new concerns
	 */
	public void setConcerns(String val) { concerns.set(val); }

	/**
	 * Helper function to get student's group number
	 * @return a string indicating student's group number.
	 * If no group is assigned yet, return "N/A".
	 */
	public String getGroupNumber() { return groupNumber.get(); }
	/**
	 * Helper function to get student's group number by integer
	 * @return an integer indicating student's group number
	 * If no group is assigned yet, return -1.
	 */
	public int getIntegerGroupNumber() {
		if (groupNumber.get().equals("N/A")) return -1;
		return Integer.valueOf(groupNumber.get());
	}
	/**
	 * Helper function to set student's group number
	 * @param val a string of the student's group number
	 */
	public void setGroupNumber(String val) { groupNumber.set(val); }
}