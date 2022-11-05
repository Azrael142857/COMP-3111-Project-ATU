package ATU;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.io.File;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

public class InputTest {
	File[] valid = null;
	File[] invalid_csv = null;
	File[] invalid_data = null;

	@Before public void setUp() throws Exception {
		valid = (new File("src/test/resources/input_test_cases/valid")).listFiles();
		invalid_csv = (new File("src/test/resources/input_test_cases/invalid_csv")).listFiles();
		invalid_data = (new File("src/test/resources/input_test_cases/invalid_data")).listFiles();
	}

	@Test public void testLoadInput() {
		Input input_handler = new Input();
		if (valid != null)
			for (File file : valid)
				assertTrue(input_handler.load_input(file));
		if (invalid_csv != null)
			for (File file : invalid_csv)
				assertFalse(input_handler.load_input(file));
		if (invalid_data != null)
			for (File file : invalid_data)
				assertTrue(input_handler.load_input(file));
	}
	
	@Test public void testValidInput() {
		Input input_handler = new Input();
		if (valid != null)
			for (File file : valid) {
				input_handler.load_input(file);
				assertTrue(input_handler.validate_data());
			}
		if (invalid_data != null)
			for (File file : invalid_data) {
				input_handler.load_input(file);
				assertFalse(input_handler.validate_data());
			}
	}
	
	@Test public void testBasicFlow() {
		try {
			Input input_handler = new Input();
			input_handler.launch();
			Thread thread = new Thread(new Runnable() {
				@Override public void run() {
					new JFXPanel();
					Platform.runLater(new Runnable() {
						@Override public void run() {
							assertTrue(input_handler.launch(new File("StuPI.CSV")));
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
