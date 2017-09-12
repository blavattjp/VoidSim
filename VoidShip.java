public class VoidShip{

	private VoidShipType type;
	private String icon;
	private boolean playerOne;
	
	public VoidShip(boolean isPlayerOne, VoidShipType vst){
	
		type = vst;
		playerOne = isPlayerOne;
		
		switch(type){
			case FLAG:
				if(playerOne){
					icon = "}";
				}else{
					icon = "{";
				}
				break;
			case FIGHTER:
				if(playerOne){
					icon = ">";
				}else{
					icon = "<";
				}
				break;
			case SWEEPER:
				if(playerOne){
					icon = ")";
				}else{
					icon = "(";
				}
				break;
			case BOMBER:
				if(playerOne){
					icon = "]";
				}else{
					icon = "[";
				}
				break;
		}
			
	}
	
	public VoidShipType getType(){
	
		return type;
	
	}
	
	public String getIcon(){
	
		return icon;
	
	}
	
	public boolean isPlayerOne(){
	
		return playerOne;
	
	}
	
}