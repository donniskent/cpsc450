import java.util.Random;
import java.util.concurrent.Semaphore;

public class Student extends Thread {
// chairs semaphore of size 3. Students acquire a semaphore to be in the chair. 
// semaphore to be with the TA 

	// open really toggles whether a student is with a teacher or not.
// 
	private Semaphore chair;
	
	private Semaphore open;
	
	private Semaphore awaken;
	
	private int prefix;
	
	
	

	public Student(Semaphore chair, Semaphore open, Semaphore awaken, int prefix) {
		this.chair = chair;
		this.open = open;
		this.awaken = awaken;
		this.prefix = prefix; 
		
	
	
	
	}

	public void run() {

		// awaken: TA should try to acquire an awaken flag, but is blocked until a
		// Student gives it up

		// what the thread does only in the beginning of the
		while (true) {

			// student works for a random amount of time.
			Random r = new Random();
			int waitTime = r.nextInt(20);
			try {
				//System.out.println(prefix + " student working alone ");
				sleep(waitTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// student sees if the TA is awake or not
			if (awaken.availablePermits() == 0 ) {
				// TA is sleeping
				//System.out.println(prefix + " waking up TA");
				// semaphore release wakes up the TA, which is waiting on an awaken flag
				awaken.release();

				try {
					// Student takes the open flag. When empty, the TA is with a student. This is
					// binary, so one student at a time.
					open.acquire();
					// once here, the student has been released by the TA. It can now continue to
					// work.

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			// TA is awake already, which means they are likely with a student.
			else {
				// see if there are any chair's available
				// tryAcquire doesn't lock the thread until a chair is released.
				// it instead returns a boolean representing the success of acquiring the flag.
				// if true, the flag is acquired. if false, no flags remain, and the thread
				// continues to run.
				if (chair.tryAcquire()) {
					// if the tryAcquire returns true, Student has sat in a chair and decremented
					// the semaphore by 1.
					//System.out.println(prefix + " Chair sat in");

					try {
						// The Student waits in the chair until the TA releases an availability.
						// at this point, the Student thread does no work and is blocked until the TA
						// releases it.
						open.acquire();
						// once here, the Student thread has spent time with the TA and can now go back
						// to work.
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				// the Chairs are full, as no flags in the chair semaphore remain
				else {

					// since the TA is busy, the student should go back to working.
					
					//increment counter 
					Classroom.counter++; 
					//System.out.println("TA is busy and no availible chairs. Back to work");

					// pass each student a shared int (as well as the TA) to count who gets turned
					// away.
					// will have to synchronize this. Make it 0 when time is up

				}

			}

			// if you cant, get a chair flag
			// if you cant, then just keep programming

			// acquire availible flag work for some amount of time,
			// then release

		}
	}

}
