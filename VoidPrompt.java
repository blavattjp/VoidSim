import java.util.Scanner;

public class VoidPrompt{
	
	private Scanner keyboard;
	
	public VoidPrompt(){
		
		keyboard = new Scanner(System.in);
		
	}
	
	public String getString(String prompt){
		
		System.out.println(prompt);
		
		String input = keyboard.nextLine();
		
		return input; 
		
	}
	
}