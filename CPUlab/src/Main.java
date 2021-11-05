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
	
	
	//fix IO device - only 1 at a time. **** 
	
	// use some type of flags or constants to indicate which algo you are in 
	// flags indicate preemptive / nonpreemtptive, and which DS to use for the ready queue. 
	
	
	
	

	public void go() {

		

		
		//for reading from keyboard: 
		//1. return an arraylist of the values. 
		//2. to make sure that each 
		
		
		
		ArrayList<Integer> data = read();
		
		
		
		//System.out.println(test.size());
		
		//5 different Algos, 
		//fifo() non 1 
		//roundRobin() pre 2 
		//shortestProcessNext() non 3 
		//shortestRemainingTime() pre 4 
		// shortestJobFirst() non - priority 5 
	
		System.out.println("FIFO: ");
	
		processorLoop(1, data);
		
		System.out.println("RR: ");
		processorLoop(2,data);
		System.out.println("SPN: ");
		
		processorLoop(3,data);
		System.out.println("SRT: ");
		processorLoop(4,data);
		System.out.println("SJN: ");
		processorLoop(5,data);
		

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

	

	
	
	
	public void processorLoop(int algoFlag, ArrayList<Integer> data) {
		//create the while loop, based on the algoFlags, different parts of the code will be initialized and/or chosen to run 
		//in conditional statements. 
		//the while loop will look the same as in my current methods, 
		// but add conditionality to decide which DS and conditions to run. 
		
		//this will be conditionally chosen. 
		Queue<Process> readyQueue;
		if(algoFlag == 1 || algoFlag == 2) {
		 readyQueue = new QueueImplementation<Process>();
		}
		else if (algoFlag == 3 ||  algoFlag == 4) {
			readyQueue = new ProcessPriorityQueue();
		}
		else {
			// flag == 5 
			readyQueue = new ShortestJobQueue();
		}
		ArrayList<Process> ioQueue = new ArrayList<Process>();
		ArrayList<Process> processList = new ArrayList<Process>();
		ArrayList<Process> finishedList = new ArrayList<Process>();
		processList = makeProcesses(data);
		
		
		int numProcesses = processList.size();
		// probably need a counter to keep track of which cycle the
		// cpu is on
		int cycle = 0;
		int cpuMiss = 0;
		int timeSliceCounter = 0;
		int timeSlice = 5;
		Process currentProcess = null;
		// main loop
		int finishedProcesses = 0;
		while (finishedProcesses != numProcesses) {

		//	System.out.println("Cycle number: " + cycle);

			// START CYCLE

			
			//this wont be conditional, because all DS have been created to use this.
			fillReadyQueue(processList, readyQueue, cycle);
			
			
			
			//not conditional 
			// this gets a new processes from the readyqueue
			if (currentProcess == null) {
				currentProcess = readyQueue.remove();
			}
			
			//not conditional
			// if here with a null process, there is a cpu miss
			if (currentProcess == null) {
				cpuMiss++;
			}

			/*
			 * END OF CYCLE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! Increment, and book keep.
			 * 
			 */

			// at the end of the cpu cycle, decrement the run time and cpuTime

			// this block increments the time spent running and the time spent in CPU for
			// current process
			//not conditional
			
			if (currentProcess != null) {
				currentProcess.setTimeSpentRunning(currentProcess.getTimeSpentRunning() + 1);
				currentProcess.setTimeSpentInCPU(currentProcess.getTimeSpentInCPU() + 1);
				timeSliceCounter++;
			}
			
			for(int i =0; i<readyQueue.size(); i++) {
				//for each process in the readyQueue, increment the waiting time 
				readyQueue.peek(i).setWaitingTime(readyQueue.peek(i).getWaitingTime() + 1);
				
			}

			// deal with IO no matter whether a currentProcess is set or not.
			//
			
			
			//not conditional
			// this block deals with the IOqueue
			//
			if(!ioQueue.isEmpty()) {
			ioQueue.get(0).setTimeSpentInIO(ioQueue.get(0).getTimeSpentInIO() + 1);
			//increment the total time the process spent in IO (not waiting on IO, just in IO)
			ioQueue.get(0).setTotalIOTime(ioQueue.get(0).getTotalIOTime() + 1);
		//	System.out.println("processes time in IO: " + ioQueue.get(0).getTimeSpentInIO());

			int ioTime = ioQueue.get(0).getTimeSpentInIO();
			if (ioTime == ioQueue.get(0).getIoTime()) {
				// if the process still needs to run and it is finished with io
				// put it into the ready queue
				// also need to reset the time spent in IO
				ioQueue.get(0).setTimeSpentInIO(0);
				if (ioQueue.get(0).getTimeSpentRunning() < ioQueue.get(0).getRunTime()) {

					readyQueue.add(ioQueue.remove(0));

				}
				// if it doesnt need to run anymore, remove it from the queue. Process is
				// complete
				else {

					Process l = ioQueue.remove(0);
					finishedList.add(l);
					//finished processes need to be stored in a separate list, to print stats at the end of the 
					// simulation. 
					
					l.setFinishTick(cycle);
					
					finishedProcesses++;
					
				}

			}
			
			}
			
			
			
		
			//the code inside here will be conditional. 
			//based on the parameter pulled in, will decide if preemptive or non preemptive. 
			if (currentProcess != null) {
			//	System.out.println("Current process:");
			//	System.out.println("Cpu time: " + currentProcess.getTimeSpentInCPU());
			//	System.out.println("run time: " + currentProcess.getTimeSpentRunning());
			//	System.out.println("IO time: " + currentProcess.getTimeSpentInIO());
				// print the stats about the current process, then deal with book keeping for
				// setting up the next cycle
				// if true, process is finished or ready to block for IO

				// this block checks if the currentProcess is ready for IO or is finished
				// it also checks if the time slice has been taken up and if it needs reset and
				// context switched

				// context switch happens because currentProcess rather:
				// 1. run time has been met
				// 2. cpu time has been met
				// 3. run out of time slice
				
				if (currentProcess.getTimeSpentRunning() == currentProcess.getRunTime()) {
				//	System.out.println("Process complete");
					finishedProcesses++;
					finishedList.add(currentProcess);
					currentProcess.setFinishTick(cycle);
					currentProcess = null;
					timeSliceCounter = 0;

				}

				else if (currentProcess.getTimeSpentInCPU() == currentProcess.getCpuTime()) {
					currentProcess.setTimeSpentInCPU(0);

					if (currentProcess.getIoTime() > 0) {
					//	System.out.println("current process added to ioqueue");
						ioQueue.add(currentProcess);
					} else {
						readyQueue.add(currentProcess);
					}

					// in either case, the currentProcess is made null
					currentProcess = null;
					timeSliceCounter = 0;

				}
				
				//if an even algo, then it is preemptive
				else if ((algoFlag % 2 == 0 || algoFlag ==5) && timeSliceCounter == timeSlice) {
				//	System.out.println("Used up timeslice");

					readyQueue.add(currentProcess);
					currentProcess = null;
					timeSliceCounter = 0;

				}

			} else {
			//	System.out.println("No current process");
			}
			

			
			cycle++;

			// end of cpu cycle
		} // main loop
			
		
		
		
		
		//System.out.println("Finished--------------------------------------");
		finishedListReporting(finishedList,cycle,cpuMiss);
		
		
		
	}
	
	
	
	
	
	public void finishedListReporting(ArrayList<Process> finishedList, float numCycles, float numMisses) {
		//using the finishedList, find what we need to find. 
				//Throughput - Processes Complete / time elapsed X 100 
				//CPU utilization - cycles - misses / cycles  
				// I/o utilization - cycles in IO / cycles 
				// turnaround time average - finish tick - start 
				//waiting time - ticks not as the current process or in I/0 
	
		//for each process in the list, print: 
		// Process: 
		// Time tick the process finsihed 
		//Turnaround time 
		// I/0 time 
		// waiting time 
		float cpuUtilization = ((numCycles - numMisses) / numCycles) * 100;
		int ioUtilization = 0;
		int throughput = 0; 
		int turnaroundTime = 0;
		int waitingTime = 0; 
		
		for(int i =0; i < finishedList.size(); i++) {
			System.out.println("Process:");
			System.out.println("Total time spent in IO: " + finishedList.get(i).getTotalIOTime());
			System.out.println("Finished on tick: " + finishedList.get(i).getFinishTick());
			System.out.println("Turnaround Time: " + (finishedList.get(i).getFinishTick() - finishedList.get(i).getArrivalTime()));
			System.out.println("Waiting time: " + finishedList.get(i).getWaitingTime());
			// after printing, add these to the running totals to get the overal avergaes 
			
			//average turnaround time is the turnaround time of each process divided by numProcesses
			turnaroundTime += (finishedList.get(i).getFinishTick() - finishedList.get(i).getArrivalTime());
			waitingTime += finishedList.get(i).getWaitingTime();
			ioUtilization += finishedList.get(i).getTotalIOTime();
		}
	
		
		
	//prints out, for each of the algorithms the 
	System.out.println("CPU utilization: " +cpuUtilization);
	float calcioUtilization = ioUtilization/numCycles;
	System.out.println("IO utilization: " + calcioUtilization * 100);
	System.out.println("Average Throughput: " + ((finishedList.size())/numCycles) * 100 );
	System.out.println("Average waiting time: " +waitingTime/finishedList.size());
	System.out.println("Total cycles: " + numCycles);
	System.out.println("Number of misses: " + numMisses);
	
	System.out.println("Average turnaround Time: " + (turnaroundTime/finishedList.size()));
	
	
	
	
	
	}
	
	
	
	public void fillReadyQueue(ArrayList<Process> processList, Queue<Process> readyQueue, int cycle) {
		for (int i = 0; i < processList.size(); i++) {
			// if a process in the processlist has
			// an arrival time == to the current cycle, add it to the queue

			// takes care of setting up the ready queue
			if (processList.get(i).getArrivalTime() == cycle) {
			//	System.out.println("added process to ready queue");
				// pull the process into the ready queue
				readyQueue.add(processList.remove(i));
				i--; // since the termination condition is decrementing by one, have to re-balance
				// remove the process from the list (may not end up doing this)

			}

		}
		for(int i =0; i < readyQueue.size(); i ++) {
		//	System.out.println(readyQueue.peek(i).getRunTime());
		}
	
	
	
	
	
	}

	
	

	


	
	
	
	
	
	public ArrayList<Integer> read () {
		System.out.println("Input your processes below: ");
		ArrayList<Integer> results = new ArrayList<Integer>();
		Scanner scan = new Scanner(System.in);
		try {
		int num = scan.nextInt();
		
		
	for (int i=0; i < num*4; i++ ) {
			int b = scan.nextInt();
			
			results.add(b);
		}
		//this populates an arrayList with all the ints that the user enters into the CLI
		
		}
		catch (Exception e) {
			System.out.println("Wrong input");
			read();
		}
	
		scan.close();
	return results;
		
	
	
	
	
	}

	public ArrayList<Process> makeProcesses(ArrayList<Integer> data) {
		ArrayList<Process> processes = new ArrayList<Process>();
		//data.remove(0); //the number of Processes
		int arrival;
		int run;
		int cpu;
		int io;
		
		for(int i =0; i < data.size(); i+=4) {
			arrival = data.get(i);
			run = data.get(i+1);
			cpu = data.get(i+2);
			io = data.get(i+3);
			processes.add(new Process(arrival,run,cpu,io));
		}
		
		
		
		return processes;
		
		
	}



}	

		
		
		/* 1.a function that reads the input, puts it into an array list 
		 * 2. Pass this arrayList to each function 
		 * 3. first number is how many processes to make
		 * 
		 * 
		 * 
		 * ArrayList<Process> createProcesses(ArrayList<Integers> data)
		 * Processes
		 * data.get(0) = numProcesses
		 * numProcesses.remove(0) gets rid of the counter. every other number is the next variable
		 * 
		 * 
		 * for (int i = 0; i < numProcesses; i+=4) {
				arrival = numProcesses
				run = numProcesses + 1
				cpu = numProcesses + 2
				io = numProcesses + 3
				processes.add(new Process(arrival, run, cpu, io));
			}
		 *	
		 *  
		 *
		 * 
		 * 
		 * 
		 * 
		 * 
		 * */
	


