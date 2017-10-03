public class VoidAiPlayer implements VoidPlayer{

	private VoidOverlord overlord;
	
	public VoidAiPlayer(VoidOverlord o){
		
		overlord = o;
		
	}
	
	public void play(){
		
		//Flagship Negative Test.
		overlord.request(4, 13, 4, 12, 0, 0); //collision!
		overlord.request(4, 13, 3, 13, 0, 0); //elevation change
		
		overlord.request(3, 12, 3, 8, 0, 0); //moving sweeper out of the way for diagonal test.
		overlord.request(4, 12, 3, 11, 0, 0); //moving bomber out of the way for range test.
		
		overlord.request(4, 13, 4, 9, 0, 0); //range!
		overlord.request(4, 13, 3, 12, 0, 0); //diagonal!
		
		//Bomber Negative Test.
		overlord.request(4, 14, 4, 13, 0, 0); //collision!
		overlord.request(4, 14, 4, 1, 0, 0); //range!
		
		
		//Sweeper Negative Test.
		overlord.request(3, 14, 4, 14, 0, 0); //collision!
		overlord.request(3, 14, 3, 9, 0, 0); //collision!
		overlord.request(3, 8, 1, 8, 0, 0); //range!
		
		//Fighter Negative Test.
		overlord.request(2, 12, 4, 14, 0, 0); //collision!
		overlord.request(2, 14, 2, 16, 0, 0); //direction!
		
	}
	
}