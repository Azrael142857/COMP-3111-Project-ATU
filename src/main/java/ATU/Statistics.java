package ATU;

import javafx.beans.property.SimpleStringProperty;

/**
 * Statistics: store a single statistics' entry and value.
 * Entry and value are stored with SimpleStringProperty.
 * Helper functions (set/get) expect String parameter/return-value.
 * @author SHU Tian
 *
 */
public class Statistics {
	private final SimpleStringProperty entry;
	private final SimpleStringProperty value;

	/**
	 * Construct a new Statistics object with given attribute values.
	 * @param fName the name of the entry
	 * @param lName the value of the entry
	 */
	public Statistics(String fName, String lName) {
		this.entry = new SimpleStringProperty(fName);
		this.value = new SimpleStringProperty(lName);
	}

	/**
	 * Helper function to get entry name
	 * @return a string indicating the entry name
	 */
	public String getEntry() { return entry.get(); }
	/**
	 * Helper function to set new entry name
	 * @param val a string of the new entry name
	 */
	public void setEntry(String val) { entry.set(val); }

	/**
	 * Helper function to get entry value
	 * @return a string indicating the entry value
	 */
	public String getValue() { return value.get(); }
	/**
	 * Helper function to set new entry value
	 * @param val a string of the new entry value
	 */
	public void setValue(String val) { value.set(val); }
}