
public class Process {
	private int arrivalTime;
	private int runTime;
	private int cpuTime; 
	private int ioTime; 
	
	
	//Keep track of the processes state based on cycles 
	//start at 0, increment per cycle, if = to the above variables, then tie for io or end of process 
	private int timeSpentRunning =0;
	private int timeSpentInIO=0;
	private int timeSpentInCPU=0; 
	
	
	private int finishTick;
	//arrival tick to finish tick 
	private int turnaroundTime;
	//total ticks spent int IO
	private int totalIOTime;
	
	//time spent waiting in the readyqueue
	private int waitingTime;


	public Process(int arrivalTime, int runTime, int cpuTime, int ioTime) {
		this.setArrivalTime(arrivalTime);
		this.setRunTime(runTime); 
		this.setCpuTime(cpuTime); 
		this.setIoTime(ioTime); 
	}


	public int getArrivalTime() {
		return arrivalTime;
	}


	public void setArrivalTime(int arrivalTime) {
		this.arrivalTime = arrivalTime;
	}


	public int getRunTime() {
		return runTime;
	}


	public void setRunTime(int runTime) {
		this.runTime = runTime;
	}


	public int getCpuTime() {
		return cpuTime;
	}


	public void setCpuTime(int cpuTime) {
		this.cpuTime = cpuTime;
	}


	public int getIoTime() {
		return ioTime;
	}


	public void setIoTime(int ioTime) {
		this.ioTime = ioTime;
	}


	public int getTimeSpentRunning() {
		return timeSpentRunning;
	}


	public void setTimeSpentRunning(int timeSpentRunning) {
		this.timeSpentRunning = timeSpentRunning;
	}


	public int getTimeSpentInIO() {
		return timeSpentInIO;
	}


	public void setTimeSpentInIO(int timeSpentInIO) {
		this.timeSpentInIO = timeSpentInIO;
	}


	public int getTimeSpentInCPU() {
		return timeSpentInCPU;
	}


	public void setTimeSpentInCPU(int timeSpentInCPU) {
		this.timeSpentInCPU = timeSpentInCPU;
	}


	public int getFinishTick() {
		return finishTick;
	}


	public void setFinishTick(int finishTick) {
		this.finishTick = finishTick;
	}


	public int getTurnaroundTime() {
		return turnaroundTime;
	}


	public void setTurnaroundTime(int turnaroundTime) {
		this.turnaroundTime = turnaroundTime;
	}


	public int getWaitingTime() {
		return waitingTime;
	}


	public void setWaitingTime(int waitingTime) {
		this.waitingTime = waitingTime;
	}


	public int getTotalIOTime() {
		return totalIOTime;
	}


	public void setTotalIOTime(int totalIOTime) {
		this.totalIOTime = totalIOTime;
	}






}
