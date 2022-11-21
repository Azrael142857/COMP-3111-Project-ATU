package ATU;

import java.io.File;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class ReportHandlerTest {
	InputHandler vaild_inputer = new InputHandler();
	//InputHandler null_inputer = null;

	@Before public void setUp() throws Exception {
		vaild_inputer.load_input(new File("src/test/resources/input_test_cases/valid/sample.csv"));
		vaild_inputer.generate_statistics();
	}
	
	@Test public void test_launch() {
		
		try {
			ATUEngine processor = new ATUEngine(vaild_inputer.getPersondata());
			ReportHandler handler = new ReportHandler(vaild_inputer.getPersondata());
				
			Thread thread = new Thread(new Runnable() {
				@Override public void run() {
						
					new JFXPanel();
					Platform.runLater(new Runnable() {
						@Override public void run() {
							assertTrue( processor.launch() );
							assertTrue( handler.launch());
						}
					});
				}
			});
			thread.start();
			Thread.sleep(3000);
				
			
			}
		catch (Exception e) {}
	}
	
}
