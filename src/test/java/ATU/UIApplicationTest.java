package ATU;

import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

import org.junit.Test;
import static org.junit.Assert.*;

public class UIApplicationTest {
	@Test public void testBasicFlow() {
		try {
			Thread thread = new Thread(new Runnable() {
				@Override public void run() {
					new JFXPanel();
					Platform.runLater(new Runnable() {
						@Override public void run() {
							try {
								new UIApplication().start(new Stage());
							} catch (Exception e) {
								//e.printStackTrace();
							}
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
