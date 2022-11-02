package ATU;

import java.io.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;

import com.opencsv.CSVReader;

public class Input {
	private TableView <Person> person_table = new TableView <Person> ();
	private TableView <Statistics> stat_table = new TableView <Statistics> ();
	private ObservableList <Person> person_data = FXCollections.observableArrayList();
	private ObservableList <Statistics> stat_data = FXCollections.observableArrayList();
	
	// Read CSV file, return false if the file is invalid
	private boolean load_input(File file) {
		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader(file));
			String[] line = reader.readNext();
			while ((line = reader.readNext()) != null)
				person_data.add(new Person(line[0], line[1], line[2], 
										   line[3], line[4], line[5], 
										   line[6], line[7], line[8]));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	// Validate data, return false if the data is invalid
	private boolean validate_data() {
		try {
			boolean[] mrk = new boolean[1000000];	// Record existing IDs
			for (Person student : person_data) {
				String id = student.getStudentid();
				if (id.length() != 8) return false;
				int num_id = Integer.valueOf(id);
				if (num_id < 20000001) return false;
				if (num_id > 20999999) return false;
				// If this id is taken, return false
				if (mrk[num_id-20000000] == true) return false;
				mrk[num_id-20000000] = true;	// Mark this id as taken

				String name = student.getStudentname();
				if (name.length() > 40) return false;

				String email = student.getStudentemail();
				if (email.length() > 50) return false;

				int k1 = Integer.valueOf(student.getK1energy());
				if (k1 < 0 || k1 > 100) return false;

				int k2 = Integer.valueOf(student.getK2energy());
				if (k2 < 0 || k2 > 100) return false;

				int k3tick1 = Integer.valueOf(student.getK3tick1());
				if (k3tick1 != 0 && k3tick1 != 1) return false;

				int k3tick2 = Integer.valueOf(student.getK3tick2());
				if (k3tick2 != 0 && k3tick2 != 1) return false;

				int mypreference = Integer.valueOf(student.getMypreference());
				if (mypreference != 0 && mypreference != 1) return false;
				
				String concerns = student.getConcerns();
				if (concerns.length() > 200) return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// Calculate statistics
	private void generate_statistics() {
		int total_num = 0;
		int k1sum = 0, k1min = 100, k1max = 0;
		int k2sum = 0, k2min = 100, k2max = 0;
		int k3tick1_cnt = 0, k3tick2_cnt = 0;
		int mypreference_cnt = 0;
		for (Person student : person_data) {
			// Transform integer properties
			int k1 = Integer.valueOf(student.getK1energy());
			int k2 = Integer.valueOf(student.getK2energy());
			int k3tick1 = Integer.valueOf(student.getK3tick1());
			int k3tick2 = Integer.valueOf(student.getK3tick2());
			int mypreference = Integer.valueOf(student.getMypreference());
			// Update statistics
			total_num++;
			k1sum += k1;
			if (k1 < k1min) k1min = k1;
			if (k1 > k1max) k1max = k1;
			k2sum += k2;
			if (k2 < k2min) k2min = k2;
			if (k2 > k2max) k2max = k2;
			if (k3tick1 == 1) k3tick1_cnt++;
			if (k3tick2 == 1) k3tick2_cnt++;
			if (mypreference == 1) mypreference_cnt++;
		}
		// Group k1 & k2 statistics as string tuples
		String k1stat = "("+1.0*k1sum/total_num+", "+k1min+", "+k1max+")";
		String k2stat = "("+1.0*k2sum/total_num+", "+k2min+", "+k2max+")";
		// Add statistics to list
		stat_data.add(new Statistics("Total Number of Students", Integer.toString(total_num)));
		stat_data.add(new Statistics("K1_Energy(Average, Min, Max)", k1stat));
		stat_data.add(new Statistics("K2_Energy(Average, Min, Max)", k2stat));
		stat_data.add(new Statistics("K3_Tick1 = 1", Integer.toString(k3tick1_cnt)));
		stat_data.add(new Statistics("K3_Tick2 = 1", Integer.toString(k3tick2_cnt)));
		stat_data.add(new Statistics("My_Preference = 1", Integer.toString(mypreference_cnt)));
	}

	// Display tables of student info and statistics
	private void display_results() {
		// Create table for student info
		Stage stage_person = new Stage();
		Scene scene_person = new Scene(new Group());
		stage_person.setTitle("Table of students' personal data");
		stage_person.setWidth(1140);
		stage_person.setHeight(480);
		stage_person.setResizable(false);

		final Label label_person = new Label("Person");
		label_person.setFont(new Font("Arial", 20));
		person_table.setEditable(false);

		// Create student info table columns
		TableColumn <Person, String> person_index_column = new TableColumn <Person, String> ("Row_Index");
		person_index_column.setMinWidth(80);
		person_index_column.setStyle("-fx-alignment: CENTER;");
		person_index_column.setCellFactory(new RowIndexCellFactory <Person, String> ());

		TableColumn <Person, String> studentid_column = new TableColumn <Person, String> ("Student_ID");
		studentid_column.setMinWidth(80);
		studentid_column.setStyle("-fx-alignment: CENTER;");
		studentid_column.setCellValueFactory(new PropertyValueFactory <Person, String> ("studentid"));

		TableColumn <Person, String> studentname_column = new TableColumn <Person, String> ("Student_Name");
		studentname_column.setMinWidth(200);
		studentname_column.setCellValueFactory(new PropertyValueFactory <Person, String> ("studentname"));

		TableColumn <Person, String> studentemail_column = new TableColumn <Person, String> ("Student_Email");
		studentemail_column.setMinWidth(220);
		studentemail_column.setCellValueFactory(new PropertyValueFactory <Person, String> ("studentemail"));
		
		TableColumn <Person, String> k1energy_column = new TableColumn <Person, String> ("K1_Energy");
		k1energy_column.setMinWidth(70);
		k1energy_column.setStyle("-fx-alignment: CENTER-RIGHT;");
		k1energy_column.setCellValueFactory(new PropertyValueFactory <Person, String> ("k1energy"));

		TableColumn <Person, String> k2energy_column = new TableColumn <Person, String> ("k2_Energy");
		k2energy_column.setMinWidth(70);
		k2energy_column.setStyle("-fx-alignment: CENTER-RIGHT;");
		k2energy_column.setCellValueFactory(new PropertyValueFactory <Person, String> ("k2energy"));

		TableColumn <Person, String> k3tick1_column = new TableColumn <Person, String> ("K3_Tick1");
		k3tick1_column.setMinWidth(60);
		k3tick1_column.setStyle("-fx-alignment: CENTER-RIGHT;");
		k3tick1_column.setCellValueFactory(new PropertyValueFactory <Person, String> ("k3tick1"));

		TableColumn <Person, String> k3tick2_column = new TableColumn <Person, String> ("K3_Tick2");
		k3tick2_column.setMinWidth(60);
		k3tick2_column.setStyle("-fx-alignment: CENTER-RIGHT;");
		k3tick2_column.setCellValueFactory(new PropertyValueFactory <Person, String> ("k3tick2"));

		TableColumn <Person, String> mypreference_column = new TableColumn <Person, String> ("My_Preference");
		mypreference_column.setMinWidth(100);
		mypreference_column.setStyle("-fx-alignment: CENTER-RIGHT;");
		mypreference_column.setCellValueFactory(new PropertyValueFactory <Person, String> ("mypreference"));

		TableColumn <Person, String> concerns_column = new TableColumn <Person, String> ("Concerns");
		concerns_column.setMinWidth(100);
		concerns_column.setCellValueFactory(new PropertyValueFactory <Person, String> ("concerns"));

		// Merge all columns into the table
		person_table.setItems(person_data);
		person_table.getColumns().addAll(person_index_column, studentid_column, 
										 studentname_column, studentemail_column, 
										 k1energy_column, k2energy_column,
										 k3tick1_column, k3tick2_column, 
										 mypreference_column, concerns_column);
		person_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		// Set scene for the table
		final VBox vbox_person = new VBox();
		vbox_person.setSpacing(5);
		vbox_person.setPadding(new Insets(10, 0, 0, 10));
		vbox_person.getChildren().addAll(label_person, person_table);
		((Group)scene_person.getRoot()).getChildren().addAll(vbox_person);
		stage_person.setScene(scene_person);
		stage_person.show();
		
		// Create table for statistics
		Stage stage_stat = new Stage();
		Scene scene_stat = new Scene(new Group());
		stage_stat.setTitle("Table of statistics data");
		stage_stat.setWidth(450);
		stage_stat.setHeight(500);
		stage_stat.setResizable(false);

		final Label label_stat = new Label("Statistics");
		label_stat.setFont(new Font("Arial", 20));
		stat_table.setEditable(false);

		// Create statistics table columns
		TableColumn <Statistics, String> stat_index_column = new TableColumn <Statistics, String> ("Row_Index");
		stat_index_column.setMinWidth(80);
		stat_index_column.setStyle("-fx-alignment: CENTER;");
		stat_index_column.setCellFactory(new RowIndexCellFactory <Statistics, String> ());
		
		TableColumn <Statistics, String> entry_column = new TableColumn <Statistics, String> ("Entry");
		entry_column.setMinWidth(200);
		entry_column.setCellValueFactory(new PropertyValueFactory <Statistics, String> ("entry"));

		TableColumn <Statistics, String> value_column = new TableColumn <Statistics, String> ("Value");
		value_column.setMinWidth(130);
		value_column.setStyle("-fx-alignment: CENTER-RIGHT;");
		value_column.setCellValueFactory(new PropertyValueFactory <Statistics, String> ("value"));

		// Merge all columns into the table
		stat_table.setItems(stat_data);
		stat_table.getColumns().addAll(stat_index_column, entry_column, value_column);
		stat_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		// Set scene for the table
		final VBox vbox_stat = new VBox();
		vbox_stat.setSpacing(5);
		vbox_stat.setPadding(new Insets(10, 0, 0, 10));
		vbox_stat.getChildren().addAll(label_stat, stat_table);
		((Group)scene_stat.getRoot()).getChildren().addAll(vbox_stat);
		stage_stat.setScene(scene_stat);
		stage_stat.show();
	}

	// Prompt window showing error message
	private void display_error(int type) {
		Stage stage_error = new Stage();
		Scene scene_error = new Scene(new Group());
		stage_error.setTitle("Error Message");
		stage_error.setWidth(400);
		stage_error.setHeight(80);
		
		final Label label_error = new Label();
		if (type == 0)
			label_error.setText("ERROR: NO File Loaded! Please Try Again.");
		if (type == 1)
			label_error.setText("ERROR: Invalid CSV File! Please Try Again.");
		if (type == 2)
			label_error.setText("ERROR: Invalid Student Info! Please Try Again.");
		label_error.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		
		final VBox vbox_error = new VBox();
		vbox_error.setSpacing(5);
		vbox_error.setPadding(new Insets(10, 0, 0, 10));
		vbox_error.getChildren().addAll(label_error);
		
		((Group)scene_error.getRoot()).getChildren().addAll(vbox_error);
		
		stage_error.setScene(scene_error);
		stage_error.show();
	}
	
	// Read CSV and generate statistics, return false if file/info is invalid
	public boolean launch() {
		// JavaFX FileChooser for showing file dialog
		final FileChooser fc = new FileChooser();
		fc.setTitle("Browse Student Info CSV...");
		String current_dir = System.getProperty("user.dir");
		fc.setInitialDirectory(new File(current_dir));
		fc.getExtensionFilters().addAll(new ExtensionFilter("CSV Files", "*.csv"));
		File file = fc.showOpenDialog(null);
		if (file != null) {		// Ensures file exists
			if (load_input(file)) {		// Ensures valid CSV file
				if (validate_data()) {		// Ensures valid student info
					generate_statistics();
					display_results();
					return true;
				} else display_error(2);
			} else display_error(1);
		} else display_error(0);
		return false;
	}
	
	// Return students' info and statistics
	ObservableList <Person> getPersondata() { return person_data; }
	ObservableList <Statistics> getStatdata() { return stat_data; }

	// Helper class for creating row index
	public class RowIndexCellFactory <S, T> implements Callback <TableColumn <S, T>, TableCell <S, T>> {
		@Override public TableCell <S, T> call(TableColumn <S, T> param) {
			TableCell <S, T> ret = new TableCell <S, T> () {
				@Override protected void updateItem(T item, boolean empty) {
					super.updateItem(item, empty);
					if (!empty)
						setText(Integer.toString(this.getTableRow().getIndex()+1));
					else setText("");
	            }
	        };
	        return ret;
	    }
	}
}
