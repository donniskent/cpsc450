import java.util.ArrayList;

public class ProcessPriorityQueue implements Queue<Process>{
	private ArrayList<Process> items = new ArrayList<Process>();

	public void add(Process item) {
		//0 is the head of the queue
		//items.add(item);
		
		//sort the queue 
		
		
		
		
		
		int remainingTime = item.getRunTime() - item.getTimeSpentRunning();
		
		if(items.size() == 0) {
			items.add(item);
		//System.out.println("Success");
		}
		
		
		else {
			for(int i =0; i< items.size(); i++) {
			int remainSpot = items.get(i).getRunTime() - items.get(i).getTimeSpentRunning();
			if(remainingTime < remainSpot) {
				items.add(i, item);
			//	System.out.println("Success");
				break;
			} 
			
			else if(i == items.size() -1 ) {
				items.add(item);
				break;
			}
			
			
			}
			
		
		}
			
		
		
		
		
		
		
		
		
	
	
	}
// implement the rest of queue, test.
	
	
	
	
	
	
	
	
	public Process remove() {
		if (!items.isEmpty()) {
			Process data = items.get(0);
			items.remove(0);
			return data;
		}

		return null;
	}

	public boolean isEmpty() {
		if (items.size() == 0) {
			return true;
		}
		return false;
	}

	
	
	public int size() {
		return items.size(); 
		
		
	}
	public Process peek(int index) {
		if (!items.isEmpty()) {
			 Process data = items.get(index);
			
			return data;
		}

		return null;
	}










}
	
	

