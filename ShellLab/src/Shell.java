import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Shell {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Shell shell = new Shell();
		shell.go();

	}

	public void go() {
		shell_loop();

	}

	public void shell_loop() {
		ProcessBuilder pb = new ProcessBuilder();
		// print working directory

		ArrayList<String> previous_calls = new ArrayList<String>();
		Scanner user_input = new Scanner(System.in);
		while (true) {
			//prints the current directory
			pb.command("cmd.exe", "/c", "cd");
			try {
				Process process = pb.start();
				Scanner input = new Scanner(process.getInputStream());
				while (input.hasNextLine())
					System.out.print(input.nextLine());
				System.out.print("> ");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			String current_command = user_input.nextLine();
			//switch only deals with internal commands
			switch (current_command) {
			case ("!"):
				if (previous_calls.isEmpty()) {
					System.out.println("No previous calls");
				} else {
					current_command = previous_calls.get(0);
				}
			break;
			case ("history"):
				
					if (previous_calls.isEmpty())
						System.out.println("Empty History");

					for (int i = 0; i < previous_calls.size(); i++) {
						System.out.println((i + 1) + "." + previous_calls.get(i));
					}

					current_command = "";
				
			
			default:
				//!n 
				if (current_command.startsWith("!")) {
					try {
						int number = Integer.parseInt(current_command.substring(1));
						current_command = previous_calls.get(number - 1);

					} catch (Exception e) {
						System.out.println("Invalid history command.");
						current_command = " ";
					}
				}
			}	
			
				//deals with external commands, excluding cmd and powershell
				if (!current_command.equals("cmd") && !current_command.equals("powershell")) {
					pb.command("cmd.exe", "/c", current_command);
					try {
						Process p = pb.start();
						Scanner input = new Scanner(p.getInputStream());

						while (input.hasNextLine())
							System.out.println(input.nextLine());
						Scanner scanner = new Scanner(p.getErrorStream());
						
						while (scanner.hasNextLine())
						{
						System.out.println(scanner.nextLine());
						
						} 

					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			

			// end loop
			if (current_command.equals("exit")) {
				break;
			}

			// deals with the arraylist and the previous 10 calls
			// dont add to list if the command is blank or is a previous call
			// the array list ultimately functions like a modified queue
			
			if (!current_command.isBlank() && !current_command.equals("!")) {
				// have to delete from list if it is of length 10
				if (previous_calls.size() > 9) {
					// removes the end of the list
					previous_calls.remove(previous_calls.size() - 1);
					// adds to the beginning of the list
					previous_calls.add(0, current_command);
				} else {
					//if size isnt an issue, just add to the beginning of the list 
					previous_calls.add(0, current_command);
				}
			}
		}
		System.out.println("exiting shell");

		user_input.close();
	}
}
