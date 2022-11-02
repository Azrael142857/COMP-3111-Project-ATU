package ATU;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UIApplication extends Application {
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

	public static void run(String arg[]) {
		Application.launch(arg);
	}
}