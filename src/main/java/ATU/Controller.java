package ATU;

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
	
	private InputHandler input_handler = null;
	
	@FXML public void initialize() {
		button_process.setDisable(true);
		button_inquiry.setDisable(true);
		button_report.setDisable(true);
		textField.setDisable(true);
	}
	
	@FXML public void inputPressed(ActionEvent event) {
		button_process.setDisable(true);
		button_inquiry.setDisable(true);
		button_report.setDisable(true);
		textField.setDisable(true);
		if (input_handler == null)
			input_handler = new InputHandler();
		if (input_handler.launch(null))
			button_process.setDisable(false);
    }
    
	@FXML public void processPressed(ActionEvent event) {
		for (Person person : input_handler.getPersondata())
			System.out.println(person.getStudentname());
		for (Statistics stat : input_handler.getStatdata())
			System.out.println(stat.getValue());
		button_inquiry.setDisable(false);
		button_report.setDisable(false);
		textField.setDisable(false);
	}
	
	@FXML public void inquiryPressed(ActionEvent event) {
		String key = textField.getText();
	}
	
	@FXML void reportPressed(ActionEvent event) {
	}
}