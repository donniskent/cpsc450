
public class Chairs {
	private Queue<Student> line;
	
	public Chairs() {
		line = new QueueImplementation<Student>(); 
		
		
	}

	
	public Queue<Student> getLine() {
		return line;
	}

	
	public synchronized boolean sit(Student student) {
		
		if(getLine().size() < 3) {
			
			
			
			
			
			
			line.add(student);
			return true;
		}
		return false; 
		
	}
	
	//
	public synchronized Student leave() {
		Student removedStudent = line.remove();
		
		return removedStudent; 
		
	}
	public int size()
	{
		return getLine().size();
	}




}
