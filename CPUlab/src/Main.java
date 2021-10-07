import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Main main = new Main();
		main.go();
	}

	public void go() {

		// read from file, in a loop, make all the processes, setting all the necessary
		// stats
		// have a cpu loop, where the end of the loop is 1 cycle.
		// based on which ever algo is being used, choose a process, run it for allotted
		// time or whatever,

		// read from file

		
		//fifo();
		roundRobin();
		
		
		//RoundRobin(processList);
		
	}

	public ArrayList<Process> createProcesses() {
		ArrayList<Process> processes = new ArrayList<Process>();
		try {
			File myFile = new File("processes.txt");
			Scanner fileScan = new Scanner(myFile);
			int numProcesses = fileScan.nextInt();
			int arrival;
			int run;
			int cpu;
			int io;

			for (int i = 0; i < numProcesses; i++) {
				arrival = fileScan.nextInt();
				run = fileScan.nextInt();
				cpu = fileScan.nextInt();
				io = fileScan.nextInt();
				processes.add(new Process(arrival, run, cpu, io));
			}
			fileScan.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return processes;
	}
	
	
	
	public void fifo() {
		Queue<Process> readyQueue = new QueueImplementation<Process>();
		ArrayList<Process> ioQueue = new ArrayList<Process>();
		ArrayList<Process> processList = new ArrayList<Process>();
		processList = createProcesses();
		int numProcesses = processList.size();
		// probably need a counter to keep track of which cycle the
		// cpu is on
		int cycle = 0;
		int cpuMiss = 0; 
		int ioCounter = 0;
		Process currentProcess = null;
		// main loop
		int finishedProcesses = 0;
		while (finishedProcesses != numProcesses) {

			System.out.println("Cycle number: " + cycle);

			//START CYCLE 
			

			// this block fills the ready queue from the initial list of processes.
			for (int i = 0; i < processList.size(); i++) {
				// if a process in the processlist has
				// an arrival time == to the current cycle, add it to the queue

				// takes care of setting up the ready queue
				if (processList.get(i).getArrivalTime() == cycle) {
					System.out.println("added process to ready queue");
					// pull the process into the ready queue
					readyQueue.add(processList.remove(i));
					i--; //since the termination condition is decrementing by one, have to re-balance
					// remove the process from the list (may not end up doing this)

				}

			}

		

			//this gets a new processes from the readyqueue
			if (currentProcess == null) {
				currentProcess = readyQueue.remove();
			}
			
			//if here with a null process, there is a cpu miss 
			if (currentProcess == null) {
				cpuMiss++; 
			}
			

			
			/* END OF CYCLE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			 * Increment, and book keep. 
			 * 
			 */
			
			
			
			
			
			
			// at the end of the cpu cycle, decrement the run time and cpuTime
			
			//this block increments the time spent running and the time spent in CPU for current process
			if (currentProcess != null) {
				currentProcess.setTimeSpentRunning(currentProcess.getTimeSpentRunning() + 1);
				currentProcess.setTimeSpentInCPU(currentProcess.getTimeSpentInCPU() + 1);
			}

			
			
			
			
			
			
			
			//deal with IO no matter whether a currentProcess is set or not. 
			
			//this block deals with the IOqueue
			for (int i = 0; i < ioQueue.size(); i++) {
				// increment the time spent in IO for each process
				ioQueue.get(i).setTimeSpentInIO(ioQueue.get(i).getTimeSpentInIO() + 1);
				System.out.println("processes time in IO: " + ioQueue.get(i).getTimeSpentInIO());

				int ioTime = ioQueue.get(i).getTimeSpentInIO();
				if (ioTime == ioQueue.get(i).getIoTime()) {
					// if the process still needs to run and it is finished with io
					// put it into the ready queue
					// also need to reset the time spent in IO
					ioQueue.get(i).setTimeSpentInIO(0);
					if (ioQueue.get(i).getTimeSpentRunning() < ioQueue.get(i).getRunTime()) {

						readyQueue.add(ioQueue.remove(i));

					}
					// if it doesnt need to run anymore, remove it from the queue. Process is
					// complete
					else {
						
						ioQueue.remove(i);
						finishedProcesses++;
					}

				}
			}

			
			if (currentProcess != null) {
				System.out.println("Current process:");
				System.out.println("Cpu time: " + currentProcess.getTimeSpentInCPU());
				System.out.println("run time: " + currentProcess.getTimeSpentRunning());
				System.out.println("IO time: " + currentProcess.getTimeSpentInIO());
				//print the stats about the current process, then deal with book keeping for setting up the next cycle 
				// if true, process is finished or ready to block for IO
				
				
				
				
				// this block checks if the currentProcess is ready for IO or is finished
				
				if(currentProcess.getTimeSpentRunning() == currentProcess.getRunTime())
				{
					finishedProcesses++;
					currentProcess = null; 
				}
				
				
				else if (currentProcess.getTimeSpentInCPU() == currentProcess.getCpuTime()) {
					currentProcess.setTimeSpentInCPU(0);
				
						
						if (currentProcess.getIoTime() > 0) {
							System.out.println("current process added to ioqueue");
							ioQueue.add(currentProcess);
						} else {
							readyQueue.add(currentProcess);
						}
					

					// in either case, the currentProcess is made null
					currentProcess = null;

				}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			} else {
				System.out.println("No current process");
			}
			
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			cycle++;

			// end of cpu cycle
		} // main loop
			// have to add 1 to total cycles because the initial cycle starts from 0
		System.out.println("cycles: " + (cycle));
		System.out.println("misses: " + cpuMiss);
	}
	
	public void roundRobin() {
		Queue<Process> readyQueue = new QueueImplementation<Process>();
		ArrayList<Process> ioQueue = new ArrayList<Process>();
		ArrayList<Process> processList = new ArrayList<Process>();
		processList = createProcesses();
		int numProcesses = processList.size();
		// probably need a counter to keep track of which cycle the
		// cpu is on
		int cycle = 0;
		int cpuMiss = 0; 
		int ioCounter = 0;
		int timeSliceCounter = 0;
		int timeSlice = 1; 
		Process currentProcess = null;
		// main loop
		int finishedProcesses = 0;
		while (finishedProcesses != numProcesses) {

			System.out.println("Cycle number: " + cycle);

			//START CYCLE 
			

			// this block fills the ready queue from the initial list of processes.
			for (int i = 0; i < processList.size(); i++) {
				// if a process in the processlist has
				// an arrival time == to the current cycle, add it to the queue

				// takes care of setting up the ready queue
				if (processList.get(i).getArrivalTime() == cycle) {
					System.out.println("added process to ready queue");
					// pull the process into the ready queue
					readyQueue.add(processList.remove(i));
					i--; //since the termination condition is decrementing by one, have to re-balance
					// remove the process from the list (may not end up doing this)

				}

			}
			
		

			//this gets a new processes from the readyqueue
			if (currentProcess == null) {
				currentProcess = readyQueue.remove();
				
				
			}
			
			//if here with a null process, there is a cpu miss 
			if (currentProcess == null) {
				cpuMiss++; 
			}
			

			
			/* END OF CYCLE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			 * Increment, and book keep. 
			 * 
			 */
			
			
			
			
			
			
			// at the end of the cpu cycle, decrement the run time and cpuTime
			
			//this block increments the time spent running and the time spent in CPU for current process
			if (currentProcess != null) {
				currentProcess.setTimeSpentRunning(currentProcess.getTimeSpentRunning() + 1);
				currentProcess.setTimeSpentInCPU(currentProcess.getTimeSpentInCPU() + 1);
				timeSliceCounter++;
			}

			
			
			
			
			
			
			
			//deal with IO no matter whether a currentProcess is set or not. 
			
			//this block deals with the IOqueue
			for (int i = 0; i < ioQueue.size(); i++) {
				// increment the time spent in IO for each process
				ioQueue.get(i).setTimeSpentInIO(ioQueue.get(i).getTimeSpentInIO() + 1);
				System.out.println("processes time in IO: " + ioQueue.get(i).getTimeSpentInIO());

				int ioTime = ioQueue.get(i).getTimeSpentInIO();
				if (ioTime == ioQueue.get(i).getIoTime()) {
					// if the process still needs to run and it is finished with io
					// put it into the ready queue
					// also need to reset the time spent in IO
					ioQueue.get(i).setTimeSpentInIO(0);
					if (ioQueue.get(i).getTimeSpentRunning() < ioQueue.get(i).getRunTime()) {

						readyQueue.add(ioQueue.remove(i));

					}
					// if it doesnt need to run anymore, remove it from the queue. Process is
					// complete
					else {
						
						ioQueue.remove(i);
						finishedProcesses++;
					}

				}
			}

			
			if (currentProcess != null) {
				System.out.println("Current process:");
				System.out.println("Cpu time: " + currentProcess.getTimeSpentInCPU());
				System.out.println("run time: " + currentProcess.getTimeSpentRunning());
				System.out.println("IO time: " + currentProcess.getTimeSpentInIO());
				//print the stats about the current process, then deal with book keeping for setting up the next cycle 
				// if true, process is finished or ready to block for IO
				
				
				
				
				// this block checks if the currentProcess is ready for IO or is finished
				//it also checks if the time slice has been taken up and if it needs reset and context switched
				
				
				//context switch happens because currentProcess rather:
					//1. run time has been met
					//2. cpu time has been met 
					//3. run out of time slice
				if(currentProcess.getTimeSpentRunning() == currentProcess.getRunTime())
				{
					finishedProcesses++;
					currentProcess = null; 
					timeSliceCounter = 0; 

				}
				
				
				else if (currentProcess.getTimeSpentInCPU() == currentProcess.getCpuTime()) {
					currentProcess.setTimeSpentInCPU(0);
				
						
						if (currentProcess.getIoTime() > 0) {
							System.out.println("current process added to ioqueue");
							ioQueue.add(currentProcess);
						} else {
							readyQueue.add(currentProcess);
						}
					

					// in either case, the currentProcess is made null
					currentProcess = null;
					timeSliceCounter = 0; 

				}

				else if(timeSliceCounter == timeSlice) {
					System.out.println("Used up timeslice");
					
					readyQueue.add(currentProcess);
					currentProcess = null; 
					timeSliceCounter = 0; 

					
				}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			} else {
				System.out.println("No current process");
			}
			
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			cycle++;

			// end of cpu cycle
		} // main loop
			// have to add 1 to total cycles because the initial cycle starts from 0
		System.out.println("cycles: " + (cycle));
		System.out.println("misses: " + cpuMiss);
		
		
		
	}
	
	public void shortestProcessNext() {
		
	}
	
	

}
