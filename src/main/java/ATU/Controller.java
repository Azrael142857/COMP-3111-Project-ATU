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
	private ATUEngine process_handler = null;
	private InquiryHandler inquiry_handler = null;
	private ReportHandler report_handler = null;
	
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
		
		//check if CSV data has imported
		if(input_handler==null) new ATUEngine (null, null).display(0, "Input data before running ATU Engine!");
			
		process_handler = new ATUEngine(input_handler.getPersondata(), input_handler.getStatdata());
		if( process_handler.launch() ) {
			button_inquiry.setDisable(false);
			button_report.setDisable(false);
			textField.setDisable(false);
		}
		
		System.out.println("-------------");
		for (Person person : input_handler.getPersondata())
			System.out.println(person.getStudentid()+" -- "+person.getGroupNumber());
		System.out.println("-------------");
	}
	
	@FXML public void inquiryPressed(ActionEvent event) {
		String key = textField.getText();
		//check if CSV data has imported
		if(input_handler==null) new InquiryHandler (null, null).display(0, "Input data before making inquiries!");
					
		inquiry_handler = new InquiryHandler(input_handler.getPersondata(), key);
		inquiry_handler.launch();
		
	}
	
	
	@FXML void reportPressed(ActionEvent event) {					
		report_handler = new ReportHandler(input_handler.getPersondata());
		report_handler.CalculateTeamsInfo();
		report_handler.DisplayReport();
		
		
		
	}
}