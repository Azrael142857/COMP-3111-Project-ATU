package ATU;

import java.util.Comparator;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ATUEngine {
	private ObservableList <Person> person_data = null;
	private ObservableList <Statistics> stat_data = null;
	int student_size;
	int team_size;
	private float K1_mean = 0;
	private float K2_mean = 0;
	
	// Constructor: get the input data
	public ATUEngine(ObservableList <Person> person_data, ObservableList <Statistics> stat_data) {
		this.person_data = person_data;
		this.stat_data = stat_data;

	}
	
	//calculate size, K1_mean, and K2_mean
	public void calculate_statistics() {
		student_size = person_data.size();
		team_size = student_size / 3; //integer division: floor
		for(Person person : person_data) {
			K1_mean += person.getIntegerK1energy();
			K2_mean += person.getIntegerK2energy();
		}
		K1_mean /= student_size;
		K2_mean /= student_size;
	}

	//simple sample ATU algorithm 
	public void team_up() {
		person_data.sort(Comparator.comparing( Person::getIntegerK1energy ).reversed() );
		int team_id = 1;
		for(Person person : person_data) {
			person.setGroupNumber(Integer.toString(team_id));
			team_id++;
			if(team_id>team_size) {
				if( Integer.valueOf(person.getK1energy())<K1_mean ) 
					display(1, "WARNNIG: Not enough students have K1_energy greater or equals to Average_K1_energy");
				break;
			}
		}
		
		person_data.sort(Comparator.comparing( Person::getIntegerK2energy ) );
		team_id = 1;
		for(Person person : person_data) {
			if( person.getGroupNumber()!="-1" ) continue;
			
			person.setGroupNumber(Integer.toString(team_id));
			team_id++;
			if(team_id>team_size) break;
		}
		
		team_id = 1;
		for(Person person : person_data) {
			if( person.getGroupNumber()!="-1" ) continue;
			
			person.setGroupNumber(Integer.toString(team_id));
			team_id++;
			if(team_id>team_size) team_id=1;
		}
	}
	
	//Start the Automatic Teaming Up
	public boolean launch() {
		
		//calculate size, K1_mean, and K2_mean
		calculate_statistics();
		
		//apply ATU algorithm 
		team_up();
		
		//sort the person_data array by group number
		person_data.sort(Comparator.comparing( Person::getIntegerGroupNumber ) );
		
		//Return True if the ATU Engine runs successfully
		display(2, "ATU Engine ran successfully!");
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
	
}
