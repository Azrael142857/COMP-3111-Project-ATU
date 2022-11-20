package ATU;

import java.util.Comparator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.stage.Stage;

/**
* The ReportHandle class handles the call to produce a report
* 
*
* @author  Yang Yuang
* @version 1.0
* @since   2022-11-20
*/

public class ReportHandler {
	private ObservableList <Person> person_data = null;
	private ObservableList <Team> team_data = null;
	private Stage stage_report = null;
	int num_teams;
	
	/**
	 * Constructor, get person data list
	 * @param person_data the list of all student data
	 */
	public ReportHandler(ObservableList <Person> person_data) {
		if (person_data != null) {
			this.person_data = person_data;
		}
	}
	
	/**
	 * construct all the teams and their information
	 */
	public void CalculateTeamsInfo() {
		this.team_data = FXCollections.observableArrayList();
		int team_id = 0;
		Team this_team = null;
		person_data.sort(Comparator.comparing( Person::getIntegerGroupNumber ) );
		
		for (Person person : person_data) {
			if (person.getIntegerGroupNumber() > team_id) {
				team_id +=1;
				if (this_team != null) {
					team_data.add(this_team);
				}
				this_team = new Team(team_id);
			}
			this_team.setNumMembers(this_team.getNumMembers() +1);
			this_team.setk1Avg(this_team.getk1Avg()+ person.getIntegerK1energy());
			this_team.setk2Avg(this_team.getk2Avg()+ person.getIntegerK2energy());
		}
		num_teams = team_id;
		if (this_team != null) {
			team_data.add(this_team);
		}
		
		for (Team team : team_data) {
			team.calculateTeamInfo();
		}
	}
	
	/**
	 * generate and display report on each team's average energy
	 */
	public void DisplayReport() {
		if (stage_report == null) {stage_report = new Stage();}
		stage_report.hide();
		
		NumberAxis xAxis = new NumberAxis(1, num_teams, 2); 
	    xAxis.setLabel("Team"); 
	         
	    NumberAxis yAxis = new NumberAxis(0, 100, 10); 
	    yAxis.setLabel("Team's Average Energy %"); 
	        
	    //Creating the line chart 
		LineChart<Number, Number> linechart = new LineChart<Number, Number>(xAxis, yAxis); 
	       
	    //Prepare XYChart.Series objects by setting data 
	    XYChart.Series<Number, Number> series1 = new Series<Number, Number>(); 
	    series1.setName("K1"); 
	    XYChart.Series<Number, Number> series2 = new Series<Number, Number>(); 
	    series2.setName("K2"); 
	    XYChart.Series<Number, Number> series3 = new Series<Number, Number>(); 
	    series3.setName("K1+K2"); 
	    for (Team team : team_data) {
	    	series1.getData().add(new XYChart.Data<Number, Number>(team.getGroupNumber(),team.getk1Avg()));
	    	series2.getData().add(new XYChart.Data<Number, Number>(team.getGroupNumber(),team.getk2Avg()));
	    	series3.getData().add(new XYChart.Data<Number, Number>(team.getGroupNumber(),team.getEnergyAvg()));
	    }
	            
	    //Setting the data to Line chart    
	    linechart.getData().add(series1); 
	    linechart.getData().add(series2);  
	    linechart.getData().add(series3);  
	        
	    //Creating a Group object  
	    Group root = new Group(linechart); 
	         
	    //Creating a scene object 
	    Scene scene = new Scene(root, 500, 400);  
	      
	    stage_report.setResizable(false);
	    
	    //Setting title to the Stage 
	    stage_report.setTitle("Team Average"); 
	         
	    //Adding scene to the stage 
	    stage_report.setScene(scene);
		   
	    //Displaying the contents of the stage 
	    stage_report.show();
	}
	
	/**
	 * Helper function to hide report stage
	 */
	public void hideReport() {
		if (stage_report != null)
			stage_report.hide();
	}
	/**
	 * launch the report handler
	 * @return a boolean, always true when the run is successful
	 */
	public boolean launch() {
		CalculateTeamsInfo();
		DisplayReport();
		return true;
	}
}
