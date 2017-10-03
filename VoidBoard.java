public class VoidBoard{

	private VoidShip[][] board;

	public VoidBoard(){
		
		board = new VoidShip[5][18];
		
		reset();
		
	}
	
	public void print(){
	
		System.out.println("\n    0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17\n");
	
		for(int o = 4; o >= 0; o--){
		
			System.out.print(o + "   ");
		
			for(int s = 0; s < 18; s++){
			
				if(board[o][s] == null){
				
					System.out.print("0  ");
				
				}else{
				
					System.out.print(board[o][s].getIcon() + "  ");
				
				}
			
			}
		
			System.out.print("\n");
		
		}
		
		System.out.print("\n");
		
	}
	
	public void clear(int o, int s){
		
		board[o][s] = null;
		
	}
	
	public void move(int oi, int si, int of, int sf){		
			
			board[of][sf] = board[oi][si];
			board[oi][si] = null;
	
	}
	
	public VoidShip getShip(int o, int s){
	
		return board[o][s];
	
	}
	
	public void reset(){
	
		for(int o = 4; o >= 0; o--){
		
			for(int s = 0; s < 18; s++){
			
				board[o][s] = null;
			
			}
			
		}
	
		board[4][3] = new VoidShip(true, VoidShipType.BOMBER);
		board[4][4] = new VoidShip(true, VoidShipType.FLAG);
		board[4][5] = new VoidShip(true, VoidShipType.BOMBER);
		board[3][3] = new VoidShip(true, VoidShipType.SWEEPER);
		board[3][5] = new VoidShip(true, VoidShipType.SWEEPER);
		board[2][3] = new VoidShip(true, VoidShipType.FIGHTER);
		board[2][4] = new VoidShip(true, VoidShipType.FIGHTER);
		board[2][5] = new VoidShip(true, VoidShipType.FIGHTER);
		
		board[4][12] = new VoidShip(false, VoidShipType.BOMBER);
		board[4][13] = new VoidShip(false, VoidShipType.FLAG);
		board[4][14] = new VoidShip(false, VoidShipType.BOMBER);
		board[3][12] = new VoidShip(false, VoidShipType.SWEEPER);
		board[3][14] = new VoidShip(false, VoidShipType.SWEEPER);
		board[2][12] = new VoidShip(false, VoidShipType.FIGHTER);
		board[2][13] = new VoidShip(false, VoidShipType.FIGHTER);
		board[2][14] = new VoidShip(false, VoidShipType.FIGHTER);
	
	}

}