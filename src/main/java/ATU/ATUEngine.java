package ATU;

import java.util.Comparator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Iterator;

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
	
	//Clustering the 2nd and 3rd class
	//Class Point are used only in clusterRest() function
	//L2 distance is used
	//Centroid Clustering is used  
	private class Point {
		public String id;
		public float K1, K2;
		private float dist;
		
		public Point( float K1, float K2, String id) {
			this.K1 = K1;
			this.K2 = K2;
			this.id = id;
		}
		public Point( int K1, int K2, String id) {
			this.K1 = K1;
			this.K2 = K2;
			this.id = id;
		}
		
		public float getL2Distance(Point anotherPoint) {
			return (K1-anotherPoint.K1)*(K1-anotherPoint.K1) + (K2-anotherPoint.K2)*(K2-anotherPoint.K2);
		}
		
		public float getDist() {return dist;}
		public void setDist(float dist) {this.dist = dist;}
	}
	
	private class Cluster {
		public ArrayList<Point> pointList;
		public float K1_mean, K2_mean;
		public int size;
		
		public Cluster(Person person) {
			pointList = new ArrayList<Point>();
			size = 0;
			
			addPoint( person.getIntegerK1energy(), person.getIntegerK2energy(), person.getStudentid() );
		}
		
		public void addPoint(float pK1, float pK2, String id) {
			pointList.add( new Point(pK1, pK2, id) );
			K1_mean = K1_mean*size/(size+1) + pK1/(size+1);
			K2_mean = K2_mean*size/(size+1) + pK2/(size+1);
			size++;
		}
		
		public float getCenterDistance(Cluster anotherCluster) {
			return (K1_mean-anotherCluster.K1_mean)*(K1_mean-anotherCluster.K1_mean)+
					(K2_mean-anotherCluster.K2_mean)*(K2_mean-anotherCluster.K2_mean);
		}
		
		public void merge(Cluster anotherCluster) {
			for(Point point : anotherCluster.pointList) 
				addPoint(point.K1, point.K2, point.id);
			
		}
	}
	
	private void clusterRest(HashMap<String, Integer> studentid_to_Cluster) {
		ArrayList <Cluster> clusterList = new ArrayList <Cluster>();
		
		//create cluster for each point
		for( Person member : person_data ) 
			if( member.getGroupNumber().equals("N/A") ) 
				clusterList.add( new Cluster(member) );
		
		//merge cluster: Centriod Clustering
		Cluster smallVarianceCluster = null;
		while( clusterList.size()>1 ) {
			int mergeCluster1=0, mergeCluster2=1;
			float minimum_dist = 1000000; //>2* 100^2
			
			for(int ind1=0; ind1<clusterList.size(); ind1++) 
				for(int ind2=ind1+1; ind2<clusterList.size(); ind2++) {
					float dist = clusterList.get(ind1).getCenterDistance( clusterList.get(ind2) );
					if( dist<minimum_dist ) {
						mergeCluster1 = ind1; mergeCluster2 = ind2;
						minimum_dist = dist;
					}	
				}
			//merge Clusters
			//System.out.println("HERE");
			//System.out.println(mergeCluster1);
			//System.out.println(mergeCluster2);
			//System.out.println("-------------------");
			//for(int ind1=0; ind1<clusterList.size(); ind1++) 
			//	System.out.println("Cluster "+ Integer.toString(ind1)+
			//			"  size="+ Integer.toString(clusterList.get(ind1).size)+ 
			//			"  mean is ("+ Float.toString(clusterList.get(ind1).K1_mean)+ ", "+ Float.toString(clusterList.get(ind1).K2_mean));
			
			clusterList.get(mergeCluster1).merge( clusterList.get(mergeCluster2) );
			clusterList.remove(mergeCluster2);
			
			//if enough points have merged
			if( clusterList.get(mergeCluster1).size >= team_size ) {
				smallVarianceCluster = clusterList.get(mergeCluster1);
				break;
			}
		}		
		
		for(int i=0; i<team_size; i++) //set 3rd Cluster
			studentid_to_Cluster.put( smallVarianceCluster.pointList.get(i).id, 3 );
		
		for(Person person : person_data) //set 2nd Cluster
			if( !studentid_to_Cluster.containsKey(person.getStudentid()) ) 
				studentid_to_Cluster.put( person.getStudentid(), 2 );
	}
	
	private void initializeClusterMean(Point cluster1_mean, Point cluster2_mean, HashMap<String, Integer> studentid_to_Cluster) {
		int cluster1_size=0, cluster2_size=0;
		for(Person person : person_data) {
			String id = person.getStudentid();
			int clusterBelong = studentid_to_Cluster.get(id);
			int pK1 = person.getIntegerK1energy(), pK2 = person.getIntegerK2energy();
			
			if(clusterBelong==1) {
				cluster1_mean.K1 = cluster1_mean.K1*cluster1_size/(cluster1_size+1) + pK1/(cluster1_size+1);
				cluster1_mean.K2 = cluster1_mean.K2*cluster1_size/(cluster1_size+1) + pK2/(cluster1_size+1);
				cluster1_size++;
			} 
			else if(clusterBelong==2) {
				cluster2_mean.K1 = cluster2_mean.K1*cluster2_size/(cluster2_size+1) + pK1/(cluster2_size+1);
				cluster2_mean.K2 = cluster2_mean.K2*cluster2_size/(cluster2_size+1) + pK2/(cluster2_size+1);
				cluster2_size++;
			} 
		}
		cluster2_mean.K1 += cluster1_mean.K1;
		cluster2_mean.K2 += cluster1_mean.K2;
	}
	
	private void greedyAssign(ArrayList<Point> groupList, int cluster, Point original_mean, Point target_mean, HashMap<String, Integer> studentid_to_Cluster) {
		for(Point group : groupList) 
			group.setDist( group.getL2Distance(original_mean) );
		
		groupList.sort(Comparator.comparing( Point::getDist ).reversed() );
		
		for(Point group : groupList) {
			Person assignPerson = null;
			float minimum_difference = 1000000;
			
			for(Person person : person_data) {
				
				if( studentid_to_Cluster.get(person.getStudentid()) != cluster ) continue; //skip incorrect cluster
				if( !person.getGroupNumber().equals("N/A")) continue; //skip if is assigned
				
				group.K1 += person.getIntegerK1energy();
				group.K2 += person.getIntegerK2energy();
				float difference = group.getL2Distance(target_mean);
				if( difference<minimum_difference ) {
					minimum_difference = difference;
					assignPerson = person;
				}
				group.K1 -= person.getIntegerK1energy();
				group.K2 -= person.getIntegerK2energy();
			}
			//assign the one that makes the least variances
			assignPerson.setGroupNumber( group.id );
			group.K1 += assignPerson.getIntegerK1energy();
			group.K2 += assignPerson.getIntegerK2energy();
		}
	}

		//ATU implementation
	private void autoTeamUp() {
		
		//choose 1st member
		person_data.sort(Comparator.comparing( Person::getIntegerK1energy ).reversed() );
		boolean warningFlag = false;
		int currentGroupNum = 1;
		for(Person member : person_data) { //first round: consider myPreference
			if(currentGroupNum > team_size) break;
			if(member.getIntegerK1energy() < K1_mean) warningFlag=true;	
			
			member.setGroupNumber( Integer.toString(currentGroupNum) );
			currentGroupNum++;
		}
		if(warningFlag) 
			display(1, "WARNNIG: Not enough students have K1_energy greater or equals to Average_K1_energy");
		
		//Divide the student into 3 clusters. Each of the 3 group member should come from 3 clusters (exept the 4th one)
		//1st Cluster has already created
		//grouping the rest student into 2 clusters s.t. one has minimum sum of intra l2 difference in K1-K2 Euclidean plane
		HashMap<String, Integer> studentid_to_Cluster = new HashMap<String, Integer>();
		for(Person member : person_data) 
			if( !member.getGroupNumber().equals("N/A") ) {
				studentid_to_Cluster.put(member.getStudentid(), 1);
			}
		clusterRest(studentid_to_Cluster);
		
		//Calculate Cluster mean
		Point cluster1_mean=new Point(0,0,""), cluster2_mean=new Point(0,0,"");
		Point cluster3_mean=new Point(3*K1_mean, 3*K2_mean, "");
		//Point cluster4_mean=new Point(4*K1_mean, 4*K2_mean, "");
		initializeClusterMean(cluster1_mean, cluster2_mean, studentid_to_Cluster);
		
		//Create Group Point
		ArrayList<Point> groupList = new ArrayList<Point>();
		for(Person member : person_data) 
			if( !member.getGroupNumber().equals("N/A") ) 
				groupList.add( new Point(member.getIntegerK1energy(), member.getIntegerK2energy(), member.getGroupNumber()) );
		
		//Greedy Assign 2nd, 3rd member
		greedyAssign(groupList, 2, cluster1_mean, cluster2_mean, studentid_to_Cluster);
		greedyAssign(groupList, 3, cluster2_mean, cluster3_mean, studentid_to_Cluster);
		
		//assign person that are left
		String i="1";
		for(Person person : person_data) 
			if(person.getGroupNumber().equals("N/A")) {
				person.setGroupNumber(i);
				i="2";
			}
		//greedyAssign(groupList, 2, cluster3_mean, cluster4_mean, studentid_to_Cluster);
	
		
		//Iterator it = studentid_to_Cluster.entrySet().iterator(); 
		//while(it.hasNext()) {
		//	Map.Entry me = (Map.Entry)it.next();
		//       System.out.println("Key is: "+me.getKey() + 
		//      " & " + 
		//      " value is: "+me.getValue());
		//}
	}
	
	private boolean tryAndSwap(Person p1, Person p2, float loss_tolerance, ArrayList<Point> groupList) {
		int groupNumber1 = p1.getIntegerGroupNumber();
		int groupNumber2 = p2.getIntegerGroupNumber();
		if( groupNumber1 == groupNumber2 ) return false;
		
		Point group1 = groupList.get(groupNumber1), group2 = groupList.get(groupNumber2);
		
		float previousLoss = (K1_mean-group1.K1)*(K1_mean-group1.K1)+(K2_mean-group1.K2)*(K2_mean-group1.K2)+
				(K1_mean-group2.K1)*(K1_mean-group2.K1)+(K2_mean-group2.K2)*(K2_mean-group2.K2);
		
		float token1_K1 = group1.K1 + (p2.getIntegerK1energy()-p1.getIntegerK1energy())/( Integer.valueOf(group1.id) );
		float token1_K2 = group1.K2 + (p2.getIntegerK2energy()-p1.getIntegerK2energy())/( Integer.valueOf(group1.id) );
		float token2_K1 = group2.K1 - (p2.getIntegerK1energy()-p1.getIntegerK1energy())/( Integer.valueOf(group2.id) );
		float token2_K2 = group2.K2 - (p2.getIntegerK2energy()-p1.getIntegerK2energy())/( Integer.valueOf(group2.id) );
		
		float afterLoss = (K1_mean-token1_K1)*(K1_mean-token1_K1)+(K2_mean-token1_K2)*(K2_mean-token1_K2)+
				(K1_mean-token2_K1)*(K1_mean-token2_K1)+(K2_mean-token2_K2)*(K2_mean-token2_K2);
		
		if( afterLoss-previousLoss < loss_tolerance ) { //swap member p1 and p2
			
			int K1_member_change = 0;	//check if 1st priority violated: one person has K1>=average
			if(p1.getIntegerK1energy()>=K1_mean && p2.getIntegerK1energy()<K1_mean) 
				K1_member_change = -1;
			if(p1.getIntegerK1energy()<K1_mean && p2.getIntegerK1energy()>=K1_mean) 
				K1_member_change = 1;
			
			if( group1.getDist() + K1_member_change < 1-0.001 ) return false; //group1 violated
			if( group2.getDist() - K1_member_change < 1-0.001 ) return false; //group2 violated
				
			p1.setGroupNumber(Integer.toString(groupNumber2));
			p2.setGroupNumber(Integer.toString(groupNumber1));
			groupList.get(groupNumber1).K1 = token1_K1;
			groupList.get(groupNumber1).K2 = token1_K2;
			groupList.get(groupNumber2).K1 = token2_K1;
			groupList.get(groupNumber2).K2 = token2_K2;
			
			return true;
		}
		else return false;	
	}
	
	private void adjust() {
		person_data.sort(Comparator.comparing( Person::getIntegerGroupNumber ) );
		
		ArrayList <Point> groupList = new ArrayList<Point>();
		groupList.add(new Point(0,0,"")); //add a dummy
		
		int currentGroup = 1, counter=0, ind=0;
		float K1=0, K2=0;
		for(Person person : person_data) {
			ind++;
			if( person.getIntegerGroupNumber()!=currentGroup || ind==person_data.size() ) {
				groupList.add( new Point(K1/counter, K2/counter, Integer.toString(counter)) ); //Not id!
				
				currentGroup++;
				counter=0; K1=0; K2=0;
			}
			K1 += person.getIntegerK1energy();
			K2 += person.getIntegerK2energy();
			counter++;
		}
		
		for(Person person : person_data) //count K1 members of each group (K1>=K1_mean), count by Point.dist
			if( person.getIntegerK1energy() >= K1_mean ) {
				float val = groupList.get(person.getIntegerGroupNumber()).getDist();
				groupList.get(person.getIntegerGroupNumber()).setDist( val+1 );
			}
		

		
		//lower variance
		int iterate_times = 25;
		for(int i=0; i<iterate_times; i++)
			for(Person p1 : person_data) 
				for(Person p2 : person_data)
					tryAndSwap(p1, p2, 0, groupList);
				
		//Spread out K1/K2_Tick
		float tick_toleance = 10;
		ArrayList<Integer> groupCounter = new ArrayList<Integer>();
		for(int i=0; i<=team_size; i++)
			groupCounter.add(0);
		int groupNumber;
		for(Person person : person_data) 
			if( person.getK3tick1().equals("1") || person.getK3tick2().equals("1") ) {
				groupNumber = person.getIntegerGroupNumber();
				groupCounter.set(groupNumber, groupCounter.get(groupNumber)+1);
			}	
		
		for(Person p1 : person_data) 
			for(Person p2 : person_data) {
				int group1 = p1.getIntegerGroupNumber();
				int group2 = p2.getIntegerGroupNumber();
				if( groupCounter.get(group1) > groupCounter.get(group2)+1 )
					if( tryAndSwap(p1, p2, tick_toleance, groupList) ) {
						groupCounter.set(group1, groupCounter.get(group1)-1);
						groupCounter.set(group2, groupCounter.get(group2)+1);
						//System.out.println(Integer.toString(group1)+"<--1->"+Integer.toString(group2));
					}
			}
		
		//Spread out K1/K2_Tick
		float myPreference_toleance = 10;
		groupCounter.clear();
		for(int i=0; i<=team_size; i++)
			groupCounter.add(0);
		for(Person person : person_data) 
			if( person.getMypreference().equals("1") ) {
				groupNumber = person.getIntegerGroupNumber();
				groupCounter.set(groupNumber, groupCounter.get(groupNumber)+1);
			}	
		
		for(Person p1 : person_data) 
			for(Person p2 : person_data) {
				int group1 = p1.getIntegerGroupNumber();
				int group2 = p2.getIntegerGroupNumber();
				if( groupCounter.get(group1) > groupCounter.get(group2)+1 )
					if( tryAndSwap(p1, p2, myPreference_toleance, groupList) ) {
						groupCounter.set(group1, groupCounter.get(group1)-1);
						groupCounter.set(group2, groupCounter.get(group2)+1);
						//System.out.println(Integer.toString(group1)+"<--2->"+Integer.toString(group2));
					}
			}
		
	}
	
	//Start the Automatic Teaming Up
	public boolean launch() {
		
		//calculate size, K1_mean, and K2_mean
		calculate_statistics();
		
		//apply ATU algorithm 
		autoTeamUp();
		//team_up();
		adjust();
		
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
			if( !person.getGroupNumber().equals("N/A") ) continue;
			
			person.setGroupNumber(Integer.toString(team_id));
			team_id++;
			if(team_id>team_size) break;
		}
		
		team_id = 1;
		for(Person person : person_data) {
			if( !person.getGroupNumber().equals("N/A") ) continue;
			
			person.setGroupNumber(Integer.toString(team_id));
			team_id++;
			if(team_id>team_size) team_id=1;
		}
	}
}
