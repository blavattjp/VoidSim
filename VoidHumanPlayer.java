public class VoidHumanPlayer implements VoidPlayer{

	private VoidOverlord overlord;
	
	public VoidHumanPlayer(VoidOverlord o){
		
		overlord = o;
		
	}
	
	public void play(){
		
		//Bomber Posative Test.
		overlord.request(4, 5, 3, 6, 0, 0); //diagonal.
		overlord.request(3, 6, 3, 7, 0, 0); //orbital.
		overlord.request(3, 7, 2, 7, 4, 13); //elevational + victory.
		
		//Flagship Posative Test.
		overlord.request(4, 4, 4, 5, 4, 6); //orbital.
		
		
		//Sweeper Posative Test.
		overlord.request(3, 5, 3, 10, 0, 0); //orbital.
		overlord.request(3, 3, 3, 16, 0, 0); //orbital.
		overlord.request(3, 16, 4, 16, 0, 0); //elevational.
		overlord.request(3, 10, 2, 10, 0, 0); //elevational.
		overlord.request(2, 10, 1, 10, 0, 0); //elevational.
		
		
		//Fighter Posative Test.
		overlord.request(2, 5, 0, 7, 0, 8); //diagonal.
		overlord.request(0, 7, 4, 11, 4, 12); //diagonal.
		overlord.request(4, 11, 0, 7, 0, 8); //diagonal.
	}
	
}