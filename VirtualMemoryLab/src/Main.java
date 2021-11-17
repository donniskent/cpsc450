import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Main main = new Main();
		main.go();

	
	}

	public void go() {
		ArrayList<String> holder = new ArrayList<String>();
		holder = read();
		ArrayList<String> LRU = new ArrayList<String>();
		ArrayList<String> FIFO = new ArrayList<String>();
		for(int i = 0; i <holder.size(); i++) {
			LRU.add(holder.get(i));
			FIFO.add(holder.get(i));
		}
		
		
		
		
		vmemory(1,FIFO);
		vmemory(2,LRU);
	}

	public void vmemory(int algo, ArrayList<String> virtualAddresses) {
		
		ArrayList<String> TLB = new ArrayList<String>();
		ArrayList<Integer> RAM = new ArrayList<Integer>();

		// TLB is a small PT where the indices only indicate
		// FIFO location
		// Needs to have first two chars represent page # and second 2 to represent
		// frame #
		Boolean[] pageTable = new Boolean[256];

		// pageTable values can be 1 or 0

		// pageTable starts empty
		for (int i = 0; i < pageTable.length; i++) {
			pageTable[i] = false;
		}

		// pageTable has physical addresses
		// indices represent the page number,
		// values represent physical address.

		// each entry in the pageTable:
		// column 0 is the page# column 1 is the frame#

		int TLBHitCounter = 0;
		int pageFaultCounter = 0;

		while (virtualAddresses.size() > 0) {
			// 1. Get address
			String currentPageHex = virtualAddresses.remove(0);

			// 2. Get page # from Address
			String currentPageNumHex = currentPageHex.substring(0, 2);
			int decimal = Integer.parseInt(currentPageNumHex, 16);
			System.out.println(decimal);
			// 3. See if page is in TLB
			if (TLB.contains(currentPageNumHex)) {
				//TLB Hit. Update RAM queue so that the most recently used element is on the end of the queue. 
				System.out.println("TLB Hit");
				
				//only runs for LRU. On a pageTable hit, move the most recently used element
				// to the end. This makes the least recently used element move to element 0
				if(algo==2)
				LRU(RAM, decimal);
				
				
				TLBHitCounter++;

			} 
			
			//TLB miss, see if going to be a page fault. 
			else {
				
				System.out.println("Not in TLB");

				//Check page table to see if in RAM 
				if (pageTable[decimal] == false) {
					//pageFault at this point 
					pageFaultCounter++;
					
					
					// check if spot in RAM is open
					if (RAM.size() < 5) {
						// room in RAM, set to true
						pageTable[decimal] = true;
						// add to the end of RAM
						RAM.add(decimal);
						
						System.out.println("Page Fault, Room in RAM");

					} 
					
					//no room in RAM, have to replace a page 
					else {
						System.out.println("Page Fault, No room in RAM");
						
						//regardless of algo, treat the arraylist as a queue and remove the 0th
						//arrayList element. 
						pageTable[queueRemove(RAM, decimal)] = false;
						
					}

				} else {
					System.out.println("In page table.");
					// if LRU, have to deal with queue

					//only runs for LRU. On a pageTable hit, move the most recently used element
					// to the end. This makes the least recently used element move to element 0
					if(algo ==2)
					LRU(RAM, decimal);

				}

				// regardless of whether in or not in PT,
				// update the TLB with the newest page
				// TLB update
				if (TLB.size() == 16)
					TLB.remove(0);

				TLB.add(currentPageNumHex);

			}

			System.out.println(currentPageNumHex);

		}
		
		
		
		System.out.println("---------");
		for(int i =0; i < TLB.size();i++) {
			System.out.println(TLB.get(i));
		}
		System.out.println(TLB.size());
	
		for(int i =0; i < RAM.size(); i++) {
			System.out.println(RAM.get(i));
		}
		System.out.println("RAM: " + RAM.size());
		System.out.println("Faults: " + pageFaultCounter);
		System.out.println("Hits: " + TLBHitCounter);
	}
	
	
	

	public ArrayList<String> read() {
		System.out.println("Input your Addresses below: ");
		ArrayList<String> results = new ArrayList<String>();
		Scanner scan = new Scanner(System.in);
		try {
			int num = scan.nextInt();

			for (int i = 0; i < num; i++) {
				String b = scan.next();

				results.add(b);
			}
			

		} catch (Exception e) {
			System.out.println("Wrong input");
			
		}

		scan.close();
		return results;

	}

	public int queueRemove(ArrayList<Integer> RAM, int pageNum) {
		int hold = RAM.remove(0);
		RAM.add(pageNum);
		return hold;
	}

	public void LRU(ArrayList<Integer> RAM, int pageNum) {
		int index = RAM.indexOf(pageNum);
		int hold = RAM.remove(index);
		RAM.add(hold);
	}

}

// algorithm
// 1. Get address
// 2. Get page # from Address
// 3. See if page in TLB
// if so, hit
// 4. else, check if Page in RAM
// Go to page table, if page # not in page table, then not in RAM
// If in page table, use to check if in RAM.
// If not in RAM, Page fault, use replacement algorithm to put it into
// update TLB
