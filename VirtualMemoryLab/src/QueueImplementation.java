import java.util.ArrayList;

public class QueueImplementation<T> implements Queue<T> {
	private ArrayList<T> items = new ArrayList<T>();

	public void add(T item) {
		items.add(item);

	}

	public T remove() {
		if (!items.isEmpty()) {
			T data = items.get(0);
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
}