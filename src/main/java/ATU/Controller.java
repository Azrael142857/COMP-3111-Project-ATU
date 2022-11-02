package ATU;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class Controller {
	@FXML private Button button_input;
	@FXML private Button button_process;
	@FXML private Button button_inquiry;
	@FXML private Button button_report;
	@FXML private TextField textField;
	
	private boolean flag = false;	// flag: 0/1, whether data is successfully loaded
	
	// Lists for student info and statistics
	private ObservableList <Person> person_data = FXCollections.observableArrayList();
	private ObservableList <Statistics> stat_data = FXCollections.observableArrayList();
	
	@FXML void inputPressed(ActionEvent event) {
		Input input_handler = new Input();
		if (input_handler.launch()) {
			person_data = input_handler.getPersondata();
			stat_data = input_handler.getStatdata();
			flag = true;
		}
    }
    
	@FXML void processPressed(ActionEvent event) {
		for (Person person : person_data)
			System.out.println(person.getStudentname());
		for (Statistics stat : stat_data)
			System.out.println(stat.getValue());
	}
	
	@FXML void inquiryPressed(ActionEvent event) {
		String key = textField.getText();
	}
	
	@FXML void reportPressed(ActionEvent event) {
	}
}