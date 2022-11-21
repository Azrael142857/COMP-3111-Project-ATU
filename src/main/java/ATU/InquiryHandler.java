package ATU;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
* 
* The InquiryHandler class handles inquiries from students, 
* and it takes studentID or student name as a key, 
* and outputs his/her grouping information
* 
*
* @author  Yang Yuang
* @version 1.0
* @since   2022-11-20
*/
public class InquiryHandler {
	private ObservableList <Person> person_data = null;
	private ObservableList <GroupingInfo> grouping_data = null;
	private Stage stage_team = null;
	int student_size;
	int team_size;
	String key = null;
	Person target = null;
	TableView<GroupingInfo> table;
	
	// Constructor: get the input data
	/**
	* This is the constructor for InquiryHandler
	* @param person_data This is the list of all student data
	* @param key This is the key for the inquiry, it can either be a name or student ID
	*/
	public InquiryHandler(ObservableList <Person> person_data, String key) {
		this.person_data = person_data;
		this.key = key;
	}
		
	//find the student data using the key provided
	/**
	 * This method is used to find the single person entry with the key provided, inside the all the student data
	 */
	public void find_person() {
		target = null;
		for(Person person : person_data) {
			if (person.getStudentid().equals(key) || person.getStudentname().equals(key) ) {
				target = person;
				break;
			}
		}
		if (target == null) {
			display("ERROR: invalid student ID or name");
		}
	}
	
	/**
	 * find all the team information to be included in data output
	 */
	public void find_team_info() {
		String team_number = target.getGroupNumber();
		List<String> members_names = new ArrayList<String>();
		int k1 = target.getIntegerK1energy();
		int k2 = target.getIntegerK2energy();
		int number_members = 1;
		
		grouping_data = FXCollections.observableArrayList();
		
		//find the team members
		for (Person student : person_data) {
			if (student.getGroupNumber().equals(team_number) && student != target) {
				members_names.add(student.getStudentname());
				k1 += student.getIntegerK1energy();
				k2 += student.getIntegerK2energy();
				number_members +=1;
			}	
		}
		
		//compute avg k1 and k2 for the team
		k1 = k1/number_members;
		k2 = k2/number_members;
		
		// Add statistics to groupingInfo object
		if (number_members == 3) {
			grouping_data.add(new GroupingInfo(target.getStudentid(), target.getStudentname(), team_number, members_names.get(0), members_names.get(1), "-", Integer.toString(k1), Integer.toString(k2)));
		}
		else {
			grouping_data.add(new GroupingInfo(target.getStudentid(), target.getStudentname(), team_number, members_names.get(0), members_names.get(1), members_names.get(2), Integer.toString(k1), Integer.toString(k2)));
			
		}
	}
	
	/**Start the Inquiry
	 * 
	 * @return a boolean, if can find the person and its team info, return true. Otherwise return false.
	 */
	public boolean launch() {
		
		//find the person data that is being queried
		find_person();
		
		//find that person's team info
		if(target != null) {
			find_team_info();
			//display team info in a new window
			display_results();
			return true;
		}	
		
		return false;
	}
	

	/**
	 * Prompt window showing error message
	 * @param message the message to be shown on the error window
	 */
	public void display(String message) {		
		Stage stage_error = new Stage();
		Scene scene_error = new Scene(new Group());
		stage_error.setTitle("Error Message");
		stage_error.setWidth(400);
		stage_error.setHeight(80);
		stage_error.setResizable(false);
		
		final Label label_error = new Label();
		label_error.setText(message);
		label_error.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		
		final VBox vbox_error = new VBox();
		vbox_error.setSpacing(5);
		vbox_error.setPadding(new Insets(10, 0, 0, 10));
		vbox_error.getChildren().addAll(label_error);
		
		((Group)scene_error.getRoot()).getChildren().addAll(vbox_error);
		
		stage_error.setScene(scene_error);
		stage_error.show();
	}
	

	/**
	 * Display the grouping results for the inquiry
	 */
	@SuppressWarnings("unchecked")

	public void display_results() {
		
		table = new TableView<GroupingInfo>();
		if (stage_team == null) stage_team = new Stage();
		final Scene scene_team = new Scene(new Group());
		stage_team.setTitle("Inquiry results");
		stage_team.setWidth(1010);
		stage_team.setHeight(210);
		stage_team.setResizable(false);
		
		table.setEditable(false);
		
		final Label label = new Label("Your Inquiry Results");
        label.setFont(new Font("Arial", 20));
		
		TableColumn<GroupingInfo, String> userInputCol = new TableColumn<GroupingInfo, String>("User Input");
		TableColumn<GroupingInfo, String> studentIDCol = new TableColumn<GroupingInfo, String>("Student ID");
        TableColumn<GroupingInfo, String> studentNameCol = new TableColumn<GroupingInfo, String>("Student Name");
        userInputCol.getColumns().addAll(studentIDCol, studentNameCol);
        studentNameCol.setMinWidth(160);
        studentNameCol.setStyle("-fx-alignment: CENTER;");
        studentIDCol.setMinWidth(80);
        studentIDCol.setStyle("-fx-alignment: CENTER;");
        userInputCol.setSortable(false);
        studentIDCol.setSortable(false);
        studentNameCol.setSortable(false);

		TableColumn<GroupingInfo, String> dataOutputCol = new TableColumn<GroupingInfo, String>("Data Output");
        TableColumn<GroupingInfo, String> teamNumCol = new TableColumn<GroupingInfo, String>("Team No.");
        TableColumn<GroupingInfo, String> teammatesCol = new TableColumn<GroupingInfo, String>("Teammates");
        TableColumn<GroupingInfo, String> teammates1Col = new TableColumn<GroupingInfo, String>("1");
        TableColumn<GroupingInfo, String> teammates2Col = new TableColumn<GroupingInfo, String>("2");
        TableColumn<GroupingInfo, String> teammates3Col = new TableColumn<GroupingInfo, String>("3");
        teammatesCol.getColumns().addAll(teammates1Col, teammates2Col, teammates3Col);
        teamNumCol.setMinWidth(90);
        teamNumCol.setStyle("-fx-alignment: CENTER;");
        teammates1Col.setMinWidth(160);
        teammates1Col.setStyle("-fx-alignment: CENTER;");
        teammates2Col.setMinWidth(160);
        teammates2Col.setStyle("-fx-alignment: CENTER;");
        teammates3Col.setMinWidth(160);
        teammates3Col.setStyle("-fx-alignment: CENTER;");
        dataOutputCol.setSortable(false);
        teamNumCol.setSortable(false);
        teammatesCol.setSortable(false);
        teammates1Col.setSortable(false);
        teammates2Col.setSortable(false);
        teammates3Col.setSortable(false);

        TableColumn<GroupingInfo, String> teamEnergyCol = new TableColumn<GroupingInfo, String>("Our Team Energy");
        TableColumn<GroupingInfo, String> teamK1Col = new TableColumn<GroupingInfo, String>("K1 Average");
        TableColumn<GroupingInfo, String> teamK2Col = new TableColumn<GroupingInfo, String>("K2 Average");
        teamEnergyCol.getColumns().addAll(teamK1Col, teamK2Col);
        dataOutputCol.getColumns().addAll(teamNumCol, teammatesCol, teamEnergyCol);
        teamK1Col.setMinWidth(80);
        teamK1Col.setStyle("-fx-alignment: CENTER;");
        teamK2Col.setMinWidth(80);
        teamK2Col.setStyle("-fx-alignment: CENTER;");
        teamEnergyCol.setSortable(false);
        teamK1Col.setSortable(false);
        teamK2Col.setSortable(false);

        studentIDCol.setCellValueFactory(new PropertyValueFactory<GroupingInfo,String>("studentID"));
        studentNameCol.setCellValueFactory(new PropertyValueFactory<GroupingInfo,String>("myName"));
        teamNumCol.setCellValueFactory(new PropertyValueFactory<GroupingInfo,String>("teamNo"));
        teammates1Col.setCellValueFactory(new PropertyValueFactory<GroupingInfo,String>("teammate1"));
        teammates2Col.setCellValueFactory(new PropertyValueFactory<GroupingInfo,String>("teammate2"));
        teammates3Col.setCellValueFactory(new PropertyValueFactory<GroupingInfo,String>("teammate3"));
        teamK1Col.setCellValueFactory(new PropertyValueFactory<GroupingInfo,String>("k1Avg"));
        teamK2Col.setCellValueFactory(new PropertyValueFactory<GroupingInfo,String>("k2Avg"));
		
        table.setItems(grouping_data);
        table.getColumns().addAll(userInputCol, dataOutputCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setFixedCellSize(25);
        table.prefHeightProperty().bind(Bindings.size(table.getItems()).multiply(table.getFixedCellSize()).add(30*3));
        
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);
 
        ((Group) scene_team.getRoot()).getChildren().addAll(vbox);
		
		stage_team.setScene(scene_team);
		stage_team.show();
		
	}
	
	
	
}
