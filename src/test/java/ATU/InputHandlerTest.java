package ATU;

import java.io.File;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class InputHandlerTest {
	private File[] valid = null;
	private File[] invalid_csv = null;
	private File[] invalid_data = null;

	@Before public void setUp() throws Exception {
		valid = (new File("src/test/resources/input_test_cases/valid")).listFiles();
		invalid_csv = (new File("src/test/resources/input_test_cases/invalid_csv")).listFiles();
		invalid_data = (new File("src/test/resources/input_test_cases/invalid_data")).listFiles();
	}

	@Test public void testLoadInput() {
		InputHandler input_handler = new InputHandler();
		for (File file : valid)
			assertTrue(input_handler.load_input(file));
		for (File file : invalid_csv)
			assertFalse(input_handler.load_input(file));
		for (File file : invalid_data)
			assertTrue(input_handler.load_input(file));
	}
	
	@Test public void testValidInput() {
		InputHandler input_handler = new InputHandler();
		for (File file : valid) {
			input_handler.load_input(file);
			assertTrue(input_handler.validate_data());
		}
		for (File file : invalid_data) {
			input_handler.load_input(file);
			assertFalse(input_handler.validate_data());
		}
	}
	
	@Test public void testLaunch() {
		try {
			InputHandler input_handler = new InputHandler();
			assertFalse(input_handler.launch(null));
			Thread thread = new Thread(new Runnable() {
				@Override public void run() {
					new JFXPanel();
					Platform.runLater(new Runnable() {
						@Override public void run() {
							input_handler.display_error(0);
							input_handler.display_error(-1);
							assertTrue(input_handler.launch(valid[0]));
							assertFalse(input_handler.launch(invalid_csv[0]));
							assertFalse(input_handler.launch(invalid_data[0]));
							assertTrue(input_handler.launch(valid[1]));
						}
					});
				}
			});
			thread.start();
			Thread.sleep(3000);
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
}
