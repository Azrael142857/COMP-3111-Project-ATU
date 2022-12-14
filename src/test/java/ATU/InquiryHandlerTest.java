package ATU;

import java.io.File;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class InquiryHandlerTest {
	InputHandler vaild_inputer = new InputHandler();
	//InputHandler null_inputer = null;

	@Before public void setUp() throws Exception {
		vaild_inputer.load_input(new File("src/test/resources/input_test_cases/valid/sample.csv"));
		vaild_inputer.generate_statistics();
	}
	
	@Test public void test_launch() {
		try {
			ATUEngine processor = new ATUEngine(vaild_inputer.getPersondata());
			InquiryHandler handler1 = new InquiryHandler(vaild_inputer.getPersondata(), "20004488");
			InquiryHandler handler2 = new InquiryHandler(vaild_inputer.getPersondata(), "20603796");
			InquiryHandler handler3 = new InquiryHandler(vaild_inputer.getPersondata(), "20000000");
			Thread thread = new Thread(new Runnable() {
				@Override public void run() {
					
					new JFXPanel();
					Platform.runLater(new Runnable() {
						@Override public void run() {
							assertTrue( processor.launch() );
							assertTrue( handler1.launch());
							assertTrue( handler1.launch());
							assertTrue( handler2.launch());
							assertFalse( handler3.launch());
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
