public class Void{

	public static void main(String[] args){
		
		System.out.println("\nWelcome to Void!\n");
	
		VoidBoard board = new VoidBoard("gameLog.txt");
		board.print();
		
		board.move(4, 5, 3, 6, 0, 0);
		board.move(2, 12, 4, 10, 0, 0);
		board.move(4, 4, 0, 17, 0, 0);
		
		board.print();
		
		System.out.print("\n");
		
		board.closeLog();
		
	}

}