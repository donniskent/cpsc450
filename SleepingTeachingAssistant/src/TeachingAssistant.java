import java.util.Random;
import java.util.concurrent.Semaphore;

public class TeachingAssistant extends Thread {
	private Semaphore chair;
	// take a chair, leave a chair

	// TA is open if a pass is availible
	private Semaphore open;
	
	// if a pass is taken, the Teacher needs to wake up because a student needs help
	private Semaphore awaken;
	
	private long timeAsleep;
	private long timeWorking;
	public int fullChairCounter;
	
	
	
	public TeachingAssistant(Semaphore chair, Semaphore open, Semaphore awaken, int fullChairCounter) {
		this.chair = chair;
		this.open = open;
		this.awaken = awaken;
		this.fullChairCounter = fullChairCounter; 
		
	}

	public void run() {
		System.out.println("TA is in Office.");
		while (true) {
			Long now = System.currentTimeMillis();
			Long stop = now + 1000;
			
			while (System.currentTimeMillis() < stop) {
				
				//always start by checking chairs. 
				if(chair.availablePermits() == 3) {
				
				try {

				//	System.out.println("TA is asleep");
					//acquire blocks until a student realeases
					Long pre = System.currentTimeMillis();
					awaken.acquire();
					Long post = System.currentTimeMillis();
					timeAsleep += (post-pre); 
				
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//sets the 
				
			//	System.out.println("awaken = " + awaken.availablePermits());

				
				//work with student who woke you up 
				Random r = new Random();
				int waitTime = r.nextInt(20);
				try {
					
					//System.out.println("working w student ");
					sleep(waitTime);
					open.release();
					timeWorking += waitTime;
				
				
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// student is kicked from office

				//System.out.println("Student released and open = " + open.availablePermits());

				// check Chairs
			//	System.out.println("chairs =" + chair.availablePermits());
				}
				
				
				
				// not sleeping, students in chairs. 
				while (chair.availablePermits() < 3 && System.currentTimeMillis() < stop) {
				//	System.out.println("Grabbing student from a chair");
					open.release();
					chair.release(); // student pulled from chair
					Random r = new Random();
					int waitTime = r.nextInt(20);

					
					try {

						
						sleep(waitTime);
						timeWorking+= waitTime;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// unblocks student thread in chair 1
				//	System.out.println("Release chair student + chair size is " + chair.availablePermits());

				}

			
				
				//System.out.println("end of loop open is + " + open.availablePermits());
				
				// after working, reset your availibilty and

			}

		//	System.out.println(open.availablePermits() + " Stats------------------ " + awaken.availablePermits()
		//			+ " ----------------------------------------------------------- " + chair.availablePermits());

			System.out.println("TA has went home for the evening");
			System.out.println("Number of times students arrived to full chairs: " + Classroom.counter);
			float totalTime = timeAsleep + timeWorking; 
			System.out.println(totalTime);
			float asleep = timeAsleep; 
			float work = timeWorking; 
			float averageSleep = asleep / totalTime; 
			float averageWork = work / totalTime;
			System.out.println("Sleep percentage: " + averageSleep * 100 + " %");
			System.out.println("Working percentage: " + averageWork * 100 + " %");

			
			
			Classroom.counter = 0; 
			timeAsleep = 0;
			timeWorking = 0;
			
			
			try {
				
				
				//this makes the student threads think that the teacher is awake so they dont try to wake him up 
				// otherwise, the student doesnt sit in the chair like they need to when they arrive when the TA is out of office. 
				awaken.release();
				sleep(2000);
				awaken.acquire();
				System.out.println("TA is in Office.");
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}