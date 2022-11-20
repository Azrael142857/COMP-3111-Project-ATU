package ATU;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * UI container to create and set the scene for main UI
 * @author SHU Tian
 */
public class UIApplication extends Application {
	/**
	 * Override start method in JavaFX Application to create scene on main stage
	 */
	@Override public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/ControllerUI.fxml"));
		VBox root = (VBox) loader.load();
		Scene scene = new Scene(root);
		stage.setTitle("Automatic Teaming Up");
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Launch the main stage and run UI components
	 * @param arg arguments passed by compiler
	 */
	public static void run(String arg[]) {
		Application.launch(arg);
	}
}