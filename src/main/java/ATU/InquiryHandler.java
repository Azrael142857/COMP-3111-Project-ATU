package ATU;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
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
	public InquiryHandler(ObservableList <Person> person_data, String key) {
		this.person_data = person_data;
		this.key = key;
	}
		
	//find the student data using the key provided
	public void find_person() {
		target = null;
		for(Person person : person_data) {
			if (person.getStudentid().equals(key) || person.getStudentname().equals(key) ) {
				target = person;
				break;
			}
		}
		if (target == null) {
			display(0, "ERROR: invalid student ID or name");
		}
	}
	
	//find all the team information to be included in data output
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
	
	//Start the Inquiry
	public boolean launch() {
		
		//find the person data that is being queried
		find_person();
		
		//find that person's team info
		if(target != null) {
			find_team_info();
			//display team info in a new window
			display_results();
		}	
		
		return true;
	}
	
	// Prompt window showing error message
	public void display(int type, String message) {
		if( type!=0 && type!=1 && type!=2 ) return; //0 for Error, 1 for Warning, 2 for notice
		
		Stage stage_error = new Stage();
		Scene scene_error = new Scene(new Group());
		if(type==0) stage_error.setTitle("Error Message");
		if(type==1) stage_error.setTitle("Warning Message");
		if(type==2) stage_error.setTitle("Notice");
		stage_error.setWidth(400);
		stage_error.setHeight(80);
		
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
	
	public void display_results() {
		
		table = new TableView<GroupingInfo>();
		if (stage_team == null) stage_team = new Stage();
		final Scene scene_team = new Scene(new Group());
		stage_team.setTitle("Inquiry results");
		stage_team.setWidth(800);
		stage_team.setHeight(500);
		stage_team.setResizable(false);
		
		table.setEditable(false);
		
		final Label label = new Label("Your Inquiry results");
        label.setFont(new Font("Arial", 20));
		
		TableColumn<GroupingInfo, String> userInputCol = new TableColumn<GroupingInfo, String>("User Input");
		TableColumn<GroupingInfo, String> studentIDCol = new TableColumn<GroupingInfo, String>("My Student ID");
        TableColumn<GroupingInfo, String> studentNameCol = new TableColumn<GroupingInfo, String>("My Student Name");
        userInputCol.getColumns().addAll(studentIDCol, studentNameCol);
		TableColumn<GroupingInfo, String> dataOutputCol = new TableColumn<GroupingInfo, String>("Data Output");
        TableColumn<GroupingInfo, String> teamNumCol = new TableColumn<GroupingInfo, String>("My Team No.");
        TableColumn<GroupingInfo, String> teammatesCol = new TableColumn<GroupingInfo, String>("My Teammates");
        TableColumn<GroupingInfo, String> teammates1Col = new TableColumn<GroupingInfo, String>("1");
        TableColumn<GroupingInfo, String> teammates2Col = new TableColumn<GroupingInfo, String>("2");
        TableColumn<GroupingInfo, String> teammates3Col = new TableColumn<GroupingInfo, String>("3");
        teammatesCol.getColumns().addAll(teammates1Col, teammates2Col, teammates3Col);
        TableColumn<GroupingInfo, String> teamEnergyCol = new TableColumn<GroupingInfo, String>("Our Team Energy");
        TableColumn<GroupingInfo, String> teamK1Col = new TableColumn<GroupingInfo, String>("K1 Average");
        TableColumn<GroupingInfo, String> teamK2Col = new TableColumn<GroupingInfo, String>("K2 Average");
        teamEnergyCol.getColumns().addAll(teamK1Col, teamK2Col);
        dataOutputCol.getColumns().addAll(teamNumCol, teammatesCol, teamEnergyCol);
       
        
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
        
        
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);
 
        ((Group) scene_team.getRoot()).getChildren().addAll(vbox);
		
		stage_team.setScene(scene_team);
		stage_team.show();
		
	}
	
	
	
}
