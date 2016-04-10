package sort;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Megan Tran
 * CMSC350 
 * Project #3 Sorting Algorithms
 * February 16th, 2015
 * 
 * New additions: 
 * A method called insertionSort that performs the InsertionSort algorithm
 * A method called quickSort that performs the QuickSort algorithm (recursive)
 * A method called swap used in the quickSort method.
 * A method called printArray that prints an entire array. Used for testing.
 */

public class RelayPlayer implements Comparable<RelayPlayer>{
	static Random randomNum = new Random();
	//These array lists will store the different variables of RelayPlayer
	static ArrayList <String> firstnames = new ArrayList <String>();    
	static ArrayList <String>  lastnames = new ArrayList <String>();   
	static ArrayList <String>  schools = new ArrayList <String>(); 
	static ArrayList<Integer> heights = new ArrayList <Integer>();
	static ArrayList<Double> relays = new ArrayList<Double>();
	
	static SORTMETHOD sort = SORTMETHOD.LAST; 
	static int counterID = 1; 
	int playerID  = 0; 
	String firstName, lastName, school;      
	int heightcm = 0; 
	double successfulRelay  = 0; 
	
	//Enum types for the different sorts
	enum SORTMETHOD {
		FIRST, LAST, HEIGHT, SCHOOL, RELAY
	}
	
	//default constructor with no parameters
	public RelayPlayer () {
		playerID = counterID++;
	}
	
	//constructor
	public RelayPlayer(String s){
		this(new Scanner(s));
	}
	
	//scanner constructor
	public RelayPlayer(Scanner scanner) {
		playerID = counterID++;       
		firstName = scanner.next();       
		lastName = scanner.next(); 
		school = scanner.next();
		heightcm = scanner.nextInt();  
		successfulRelay = scanner.nextDouble(); 
	}
	
	//Compare method
	public int compareTo(RelayPlayer x) {
		switch (sort) {       
		case LAST   : return this.lastName.compareTo (x.lastName); 
		case FIRST  : return this.firstName.compareTo (x.firstName); 
		case SCHOOL  : return this.school.compareTo (x.school); 
		case HEIGHT: return this.heightcm - x.heightcm;        
		case RELAY    : return (this.successfulRelay > x.successfulRelay)? 1 : -1;     
		}
		return 0;
	}
		
	//reading in the file to put the file information into different arraylists
	static {       
		try {          
			Scanner scan = new Scanner (new File("strideplayers.txt"));
			while(scan.hasNext()){
				String s = scan.nextLine();
				String[] elements = s.split("\\s+");
				int h = Integer.parseInt(elements[3]);
				double r = Double.parseDouble(elements[4]);
				firstnames.add(elements[0]);
				lastnames.add(elements[1]);
				schools.add(elements[2]);
				heights.add(h);
				relays.add(r);
			}
			scan.close();
		}        
		catch (FileNotFoundException e) {          
			System.out.println (e);       
		}  
	}
	

	//method that makes a random N amount of relay players with the information in
	//the array lists.
	public static RelayPlayer [] makeRandom (int n) { 
		RelayPlayer [] player = new RelayPlayer [n];       
		for (int i = 0; i < player.length; i++) {         
			player[i] = new RelayPlayer();         
			player[i].firstName = firstnames.get (randomNum.nextInt(firstnames.size())); 
			player[i].lastName = lastnames.get (randomNum.nextInt(firstnames.size())); 
			player[i].school = schools.get (randomNum.nextInt(firstnames.size())); 
			player[i].heightcm = heights.get (randomNum.nextInt(firstnames.size())); 
			player[i].successfulRelay = relays.get (randomNum.nextInt(firstnames.size())); 
			     
			}      
		return player;    
		}
	
	/*Insertion Sort Method
	 * For each iteration, the method will take one element of the unsorted array at one 
	 * time, and place it at an index where there is no lesser values.
	 * It returns a fully sorted RelayPlayer array
	 */
	public static RelayPlayer[] insertionSort(RelayPlayer[] player){
		for(int i = 1; i < player.length; i++){
			//second element of the unsorted array
			RelayPlayer rp = player[i];
			//index of the first element of the unsorted array
			int j = i-1;
			while ((j > -1)&& (player[j].compareTo(rp)>0)){
				//if the previous element is larger than rp, push rp back
				player[j+1] = player[j];
				j--;
			}
			//place rp at the index when there are no lesser values.
			player[j+1] = rp;
			System.out.println(printArray(player));
		}
		return player;
	}
	
	//This method swaps the values at the first and second index in the T array
	//store firstIndex value in temp, copy value of the second index into the first index, 
	//then store the value of temp into the second index
	private static void swap(RelayPlayer array [], int firstIndex, int secondIndex) {
		RelayPlayer temp = array[firstIndex]; 
		array[firstIndex] = array[secondIndex]; 
		array[secondIndex] = temp;               
	}
	
	/*Quick Sort method
	 * a “pivot” element is selected, the method will sort all the elements that are less than the 
	 * pivot to the left, and all elements “more” than the pivot to the right. 
	 * Recursively calls itself on the arrays to the left and right side of the pivot element
	 * until the array is fully sorted. 
	 */
	public static RelayPlayer[] quickSort(RelayPlayer player[], int front, int end) {
		//put the parameters into variable
		int i = front, j = end;
		//create the element pivot - in the middle of the array
		RelayPlayer pivot = player[front + (end - front) / 2];
		//now, we must find an element larger than 
		while (i <= j) {
			//if the current player is less than pivot, go to next
			while (player[i].compareTo(pivot) < 0){
				i++;
			}
			//if the current player is less more pivot, go backwards
			while (player[j].compareTo(pivot) > 0){
				j--;
			}
			if (i <= j){
				//swap the elements of i and j
				swap(player, i, j);
				System.out.println(printArray(player));
				i++;
				j--;
			}
		}
		//now, recursively call the method until it is fully sorted
		if(front < j){
			quickSort(player, front, j);
		}
		if(i < end){
			quickSort(player, i, end);
		}
		
		return player;
	}

	
	//print a RelayPlayer array. Used for testing.
	public static String printArray(RelayPlayer player[]){
		StringBuilder s = new StringBuilder();
		s.append("-------");
		for (int index = 0; index < player.length; index++) {
			s.append("\n");
			s.append(player[index]);
		}
		s.append("\n");
		s.append("-------");
		return s.toString();
	}



	//toString method that formats the relay player information neatly
	public String toString () {  
		return String.format("%2d %15s, %8s: %15s, %10d cm, %10.2f", 
				playerID, lastName, firstName, school, heightcm, successfulRelay) 
				+"% successful relays.";   
		}
	
	public static void main(String[] args) {
		   
		
		System.out.println("Creating 10 random players"); 
		RelayPlayer[] x = makeRandom (10);
		
		//print out random players
		for (RelayPlayer player: x){      
			System.out.println(player);  
		}
		System.out.println(); 
		
		//using quick sort to sort the players in order
		System.out.println("*********Using quick sort to sort the players by last name************");
		System.out.println(); 
		System.out.println(printArray(quickSort(x, 0, x.length-1)));
		
		//using insertion sort to sort the players in order by first name
		System.out.println();
		System.out.println("*********Using insertion sort to sort the players by first name*********");
		System.out.println(); 
		sort = SORTMETHOD.FIRST; 
		System.out.println(printArray(insertionSort(x)));

	}

	
}
