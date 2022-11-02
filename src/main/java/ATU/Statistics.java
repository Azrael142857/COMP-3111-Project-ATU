package ATU;

import javafx.beans.property.SimpleStringProperty;

//Statistics: store a single statistics' entry and value
//Entry and value are stored with SimpleStringProperty
//Helper functions (set/get) expect String parameter/return-value
public class Statistics {
	private final SimpleStringProperty entry;
	private final SimpleStringProperty value;

	public Statistics(String fName, String lName) {
		this.entry = new SimpleStringProperty(fName);
		this.value = new SimpleStringProperty(lName);
	}

	// Helper functions (set/get) to alter/access data
	public String getEntry() { return entry.get(); }
	public void setEntry(String val) { entry.set(val); }

	public String getValue() { return value.get(); }
	public void setValue(String val) { value.set(val); }
}