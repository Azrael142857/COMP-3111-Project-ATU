package ATU;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Main controller for JavaFX UI components
 * @author SHU Tian
 */
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

	/**
	 * Set initial states of UI components
	 */
	@FXML public void initialize() {
		button_process.setDisable(true);
		button_inquiry.setDisable(true);
		button_report.setDisable(true);
		textField.setDisable(true);
	}

	/**
	 * When "Load" button is pressed, initiate InputHandler to read file
	 * @param event the ButtonPress event occurred
	 */
	@FXML public void inputPressed(ActionEvent event) {
		// Disable other buttons and windows
		button_process.setDisable(true);
		button_inquiry.setDisable(true);
		button_report.setDisable(true);
		textField.setDisable(true);
		if (report_handler != null) report_handler.hideReport();

		if (input_handler == null)
			input_handler = new InputHandler();
		if (input_handler.launch(null))
			button_process.setDisable(false);
    }

	/**
	 * When "Engine" button is pressed, initiate ATUEngine to process
	 * @param event the ButtonPress event occurred
	 */
	@FXML public void processPressed(ActionEvent event) {
		button_process.setDisable(true);
		//check if CSV data has imported
		if (input_handler==null) new ATUEngine (null, null).display(0, "Input data before running ATU Engine!");
		if (report_handler != null) report_handler.hideReport();
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

	/**
	 * When "Inquiry" button is pressed, initiate InquiryHandler to handle query
	 * @param event the ButtonPress event occurred
	 */
	@FXML public void inquiryPressed(ActionEvent event) {
		String key = textField.getText();
		//check if CSV data has imported
		if(input_handler==null) new InquiryHandler (null, null).display("Input data before making inquiries!");
		inquiry_handler = new InquiryHandler(input_handler.getPersondata(), key);
		inquiry_handler.launch();
		textField.clear();
	}
	
	/**
	 * When "Report" button is pressed, initiate ReportHandler to generate report
	 * @param event the ButtonPress event occurred
	 */
	@FXML void reportPressed(ActionEvent event) {
		if (report_handler != null) report_handler.hideReport();
		report_handler = new ReportHandler(input_handler.getPersondata());
		report_handler.launch();
	}
}