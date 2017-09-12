import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class VoidBoard{

	private VoidShip[][] board;
	private FileWriter logWriter;

	public VoidBoard(String logFileName){
		
		
		try{	
			
			File logFile = new File(logFileName);
			logFile.createNewFile();
		
		}catch (IOException e){
		
			System.out.println("Error: Something went wrong while attempting to create a logfile.");
		
		}
		
		try{
		
			logWriter = new FileWriter(logFileName);
		
		}catch (IOException e){
		
			System.out.println("Error: Something went wrong while attempting to create a writer for the logfile.");
		
		}
		
		board = new VoidShip[5][18];
		
		log("new board");
		
		reset();
		
	}
	
	public void print(){
	
		System.out.println("    0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17\n");
	
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
		
		log("print");
		
	}
	
	public boolean move(int oi, int si, int of, int sf, int oa, int sa){

		if(true){ //TODO: check move legality		
			
			board[of][sf] = board[oi][si];
			board[oi][si] = null;
			
			logMove(oi, si, of, sf, oa, sa);
			
			return true;
		
		}else{
		
			log("REJECTED: " + oi + "," + si + " to " + of + "," + sf + " hit " + oa + "," + sa);
			
			return false;
		
		}
		
		
	
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
		
		log("board reset");
	
	}
	
	private void logMove(int oi, int si, int of, int sf, int oa, int sa){
	
		String iCoords = ("(" + oi + "," + si + ")");
		String fCoords = ("(" + of + "," + sf + ")");
		String aCoords = ("(" + oa + "," + sa + ")");
		
		log(iCoords + " to " + fCoords + " hit " + aCoords);
	
	}
	
	private void log(String logEntry){
	
		try{
		
			logWriter.write(logEntry + "\n");
		
		}catch (IOException e){
		
			System.out.println("Error: IOException thrown while writing to the logfile.");
		
		}
	
	}
	
	public void closeLog(){
	
		try{
		
			logWriter.close();
		
		}catch (IOException e){
		
			System.out.println("Error: IOException thrown while closing the logfile.");
		
		}
	
	}

}