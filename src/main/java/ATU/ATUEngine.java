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


/**
* This class contains methods for manipulating an Person type ObservableList. 
* This class teams up the Person type objects in ObservableList 
* by setting the groupNumber attribute through Person::setGroupNumber method.
* <p>
* The team up process follows the following priorities:<br/>
*  (1) Each team has at least one member with Person::k1energy greater or equals to the average K1_energy over the entire ObservableList.<br/>
*  (2) The sum of variance of average Person::k1energy and Person::k2energy among groups should be close to the possible minimum value.<br/>
*  (3) The distribution of "1" in Person::k3tick1 and Person::k3tick2 should be even.<br/>
*  (4) The distribution of "1" in Person::myPefernce should be even.
* 
* @author 		ZHANG Juntao
* @see        	Person
* @see		  	ObservableList
*/
public class ATUEngine {
	private ObservableList <Person> person_data = null;
	private int student_size;
	private int team_size;
	private float K1_mean = 0;
	private float K2_mean = 0;
	
	/**
	 * Construct ATUEngine object and pass the ObservableList it needs to manipulate.
	 *
	 * @param person_data  	an Person type ObservableList that needs to be manipulate.
	 */
	public ATUEngine(ObservableList <Person> person_data) {
		this.person_data = person_data;
	}
	
	/**
	 * The interface for starting the Automatic Teaming Up process.
	 * Person objects in the ObservableList will be manipulated upon calling this method.
	 *
	 * @return True if the manipulation is successful, else false.
	 */
	public boolean launch() {
		//set all person::groupNumber to "N/A"
		for(Person person : person_data) person.setGroupNumber("N/A");
		
		//calculate size, K1_mean, and K2_mean
		calculate_statistics();
		
		//apply ATU algorithm 
		autoTeamUp();
		adjust();
		
		//sort the person_data array by group number
		person_data.sort(Comparator.comparing( Person::getIntegerGroupNumber ) );
		
		//Return True if the ATU Engine runs successfully
		display(2, "ATU Engine ran successfully!");
		return true;
	}

	/**
	 * Prompt window showing error, warning, or notice message.
	 * 
	 * @param type 		Message type. 0 for Error, 1 for Warning, 2 for notice
	 * @param message 	A string that describes the message to be shown in the prompt window.
	 */
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

	/**
	 * Clustering the remaining Points to the 2nd and 3rd cluster, 
	 * so that the 3rd Cluster has low intra-cluster L2 distances (i.e., the 3rd cluster have Points close to the mean).
	 * <br/>Centroid Clustering is applied to the remaining Points.
	 * @param studentid_to_Cluster	A hashMap maps student_id to the Cluster he/she belongs to.
	 */
	public void clusterRest(HashMap<String, Integer> studentid_to_Cluster) {
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
	
	/**
	 * Greedily assign Person in designated Cluster to each groups so that the resulted sum of K1, K2 energy is close to target_mean.<br/>
	 * Group further from original_mean will be assigned first.
	 * 
	 * @param groupList 			Array of Points indicate the Group's (K1, K2) position,
	 * @param cluster				Cluster number of the designated Cluster.
	 * @param original_mean			Average (K1, K2) point of Groups before the assignment of Person.
	 * @param target_mean			Average (K1, K2) point of Groups after the assignment of Person.
	 * @param studentid_to_Cluster	A hashMap maps student_id to the Cluster he/she belongs to.
	 */
	public void greedyAssign(ArrayList<Point> groupList, int cluster, Point original_mean, Point target_mean, HashMap<String, Integer> studentid_to_Cluster) {
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

	/**
	 * The caller function of a sequence of functions to manipulate ATUEgine::person_data.
	 * After this, all Person in ATUEngine::person_data should be assigned to a group.<br/>
	 * This method gives a preliminary grouping result.
	 */
	public void autoTeamUp() {
		
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

	}
	
	/**
	 * Swap 2 person from their groups 
	 * if after swapping, the change of the sum of K1, K2 variance is under the specific loss_tolerance.
	 * @param p1				Person 1.
	 * @param p2				Person 2.
	 * @param loss_tolerance	The specific tolerance.
	 * @param groupList			Array of points indicating current groups's (K1, K2).
	 * @return					True if swap is performed, else false.
	 */
	public boolean tryAndSwap(Person p1, Person p2, float loss_tolerance, ArrayList<Point> groupList) {
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
	
	/**
	 * Adjust the team assignment so that:<br/>
	 * When (1) Each team has at least one member with Person::k1energy greater or equals to the average K1_energy over the entire ObservableList.<br/>
	 *  (2) The sum of variance of average Person::k1energy and Person::k2energy among groups should be close to the possible minimum value.<br/>
	 *  (3) The distribution of "1" in Person::k3tick1 and Person::k3tick2 should be even.<br/>
	 *  (4) The distribution of "1" in Person::myPefernce should be even.<br/>
	 *  This method gives a final grouping result.
	 */
	public void adjust() {
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

	/**
	 * calculate size, K1_mean, and K2_mean
	 */
	private void calculate_statistics() {
		student_size = person_data.size();
		team_size = student_size / 3; //integer division: floor
		for(Person person : person_data) {
			K1_mean += person.getIntegerK1energy();
			K2_mean += person.getIntegerK2energy();
		}
		K1_mean /= student_size;
		K2_mean /= student_size;
	}

	/**
	 * After all Points are divided into 3 clusters (i.e., all student_id exist in studentid_to_Cluster's key set)
	 * <br/>Calculated the mean Point of the 3 Clusters. 
	 * @param cluster1_mean 		mean Point of the 1st Cluster to be calculated.
	 * @param cluster2_mean 		mean Point of the 2nd Cluster to be calculated.
	 * @param studentid_to_Cluster 	A hashMap maps student_id to the Cluster he/she belongs to.
	 */
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

	/**
	 * A private class that represents a Point on a Cartesian coordinate. 
	 * It has public properties to directly access and manipulate.<br/>
	 * Point::K1, Point::K2 are 1st and 2nd coordinate of the point.<br/>
	 * Point::id, Point::dist are properties to store information.
	 * 
	 * @author ZHANG Juntao
	 * 
	 */
	private class Point {
		public String id;
		public float K1, K2;
		public float dist;
		
		/**
		 * Construct a Point.
		 * 
		 * @param K1	Value of 1st coordinate
		 * @param K2	Value of 2nd coordinate
		 * @param id	Value of additional String information
		 */
		public Point( float K1, float K2, String id) {
			this.K1 = K1;
			this.K2 = K2;
			this.id = id;
		}
		
		/**
		 * Calculate the L2 distance to another Point object.
		 * @param 	anotherPoint		Another Point object to calculate L2 distance.
		 * @return	The L2 distance	between the two points.
		 */
		public float getL2Distance(Point anotherPoint) {
			return (K1-anotherPoint.K1)*(K1-anotherPoint.K1) + (K2-anotherPoint.K2)*(K2-anotherPoint.K2);
		}
		
		public float getDist() {return dist;}
		public void setDist(float dist) {this.dist = dist;}
	}

	/**
	 * A private class that represents a Cluster of Point objects. 
	 * It has public properties to directly access and manipulate.<br/>
	 * Cluster::pointList An Observable List of Point Object in the Cluster.<br/>
	 * Cluster::K1_mean, Cluster::K2_mean are 1st and 2nd coordinate of the mean point.<br/>
	 * Cluster::size is number of Point object in the Cluster.
	 * 
	 * @author 	ZHANG Juntao
	 * @see 	Point
	 */
	private class Cluster {
		public ArrayList<Point> pointList;
		public float K1_mean, K2_mean;
		public int size;
		
		/**
		 * @param person	A person in the Cluster.
		 */
		public Cluster(Person person) {
			pointList = new ArrayList<Point>();
			size = 0;
			
			addPoint( person.getIntegerK1energy(), person.getIntegerK2energy(), person.getStudentid() );
		}
		
		/**
		 * Add a Point to the Cluster
		 * @param pK1 value of the 1st coordinate of the adding Point.
		 * @param pK2 value of the 1st coordinate of the adding Point.
		 * @param id additional information describing the point.
		 */
		public void addPoint(float pK1, float pK2, String id) {
			pointList.add( new Point(pK1, pK2, id) );
			K1_mean = K1_mean*size/(size+1) + pK1/(size+1);
			K2_mean = K2_mean*size/(size+1) + pK2/(size+1);
			size++;
		}
		
		/**
		 * Calculate the L2 distance of the centriod(mean) to the centroid of another cluster.
		 * @param 	anotherCluster	Another Point object to calculate L2 distance.
		 * @return 	The L2 distance	between the centroids of two Clusters.
		 */
		public float getCenterDistance(Cluster anotherCluster) {
			return (K1_mean-anotherCluster.K1_mean)*(K1_mean-anotherCluster.K1_mean)+
					(K2_mean-anotherCluster.K2_mean)*(K2_mean-anotherCluster.K2_mean);
		}
		
		/**
		 * Adding all the point form anotherCluster to this object.
		 * @param anotherCluster The other Cluster to merge.
		 */
		public void merge(Cluster anotherCluster) {
			for(Point point : anotherCluster.pointList) 
				addPoint(point.K1, point.K2, point.id);
			
		}
	}
	
}
