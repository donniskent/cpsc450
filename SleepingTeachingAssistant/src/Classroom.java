import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;



//what data is shared betweeen students and the TA? 
//1. the state of the chairs, therefore sitting and leaving need to be synchronized, and should alert other threads. 
//2. The TA is shared between Students, so the TA's state should be synchronized
// so , must synchronize the gets and sets of the chairs states (sitting and standing) 
// must also synchronize the gets and sets of the TA's state (awake, busy, asleep) 


public class Classroom {
	public static int counter = 0; 
	public static void main(String[] args) {
		Classroom classroom = new Classroom();
		classroom.go();

	}

	
	public void go() {
		// 1. Create the teacher thread
		// 2. Prompt the user for the number of students that need tutoring
		// 3. Use an arraylist to create new students, based on user input 
		// 4. will have a running queue to represent open seats, this will be shared between all the students and the TA 
		// 5. Students will work for a random time, then see if any seats are availbile. 
		// 6. If availible, pick a seat, and wait for the TA to pick you as a current student 
		// 7. IF full, go back to work 
		// 7. Need a mechanism to keep track of the students who are rejected from getting a seat 
		// 8. chairs can be its own class, that acts as queue
		
		
		// chairs need to be synchronized because the teacher and many students will be working with it. 
		// the chairs are the shared data 
		// chairs will be an implementation of a queue
		// chairs will notify students when one is open 
		// chairs shouldnt have to notify the teacher 
		// teacherss  
		
		
		
		
		// 3 total chairs.
		Semaphore chairSemaphore = new Semaphore(3,true);
		//TA is open 
		Semaphore open = new Semaphore(0, true); 
		//Wake up the TA
		Semaphore awaken = new Semaphore(0);
		
		
		
		TeachingAssistant TA = new TeachingAssistant(chairSemaphore,open,awaken, 0); 
		System.out.println("How many students will be in the tutor center? ");
		Scanner scan = new Scanner(System.in);
		int numStudents = 0; 
		
		while(true) {
		try {
		numStudents = scan.nextInt();
		
		break; 
		}
		catch(Exception e) {
			System.out.println("Please enter a valid integer. "); 
			scan.next();
			
		}
		
		
		}
		scan.close();
		//create student threads 
		TA.start();
		
		ArrayList<Student> students = new ArrayList<Student>();
		for(int i = 0; i < numStudents; i++) {
			
			students.add(new Student(chairSemaphore,open, awaken, i)); 
			students.get(i).start();
			
		}
		
		
		
		
		
		
		
		// once students and teacher are initialized, start them 
		
		
		
		
		
		
	}
	
}
