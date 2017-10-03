public class VoidOverlord{
	
	private VoidBoard board = new VoidBoard();
	private VoidLogWriter log = new VoidLogWriter();
	private VoidPrompt prompt = new VoidPrompt();
	private boolean isPlayerOneTurn = true;
	
	private String name;
	private VoidPlayer playerOne;
	private VoidPlayer playerTwo;
	
	public VoidOverlord(){
		
		name = prompt.getString("Overlord Name:");
	
		log.write("NEW OVERLORD: " + name + ".");
		
	}
	
	public VoidOverlord(String n){
		
		name = n;
		
		log.write("NEW OVERLORD: " + name + ".");
		
	}
	
	public void run(){
		
		playerOne = new VoidHumanPlayer(this);
		playerTwo = new VoidAiPlayer(this);
		
		announce("Hello. I am " + name + ". Welcome to VOID.");
		
		printBoard();
		
		playerOne.play();
		
		printBoard();
		
		isPlayerOneTurn = false;
		
		playerTwo.play();
		
		victory();
		
		log.close();
		
	}
	
	public boolean request(int oi, int si, int of, int sf, int oa, int sa){
		
		log.write("REQUEST: (" + oi + "," + si + ") to (" + of + "," + sf + ") hit (" + oa + "," + sa + ").");
		
		boolean okayRequest = false;
		
		boolean orbitsInBounds = !(oi < 0 || of < 0 || oa < 0 || oi > 4 || of > 4 || oa > 4);
		boolean sectorsInBounds = !(si < 0 || sf < 0 || sa < 0 || si > 17 || sf > 17 || sa > 17);
		
		VoidShip requestShip = board.getShip(oi, si);
		
		if(orbitsInBounds && sectorsInBounds){
			
			if(requestShip != null){
				
				if((isPlayerOneTurn && requestShip.isPlayerOne()) || (!isPlayerOneTurn && !requestShip.isPlayerOne())){
					
					VoidShipType shipType = board.getShip(oi, si).getType();

					switch(shipType){
						case FLAG:
							okayRequest = checkFlag(oi, si, of, sf, oa, sa);
							break;
						case BOMBER:
							okayRequest = checkBomber(oi, si, of, sf, oa, sa);
							break;
						case SWEEPER:
							okayRequest = checkSweeper(oi, si, of, sf, oa, sa);
							break;
						case FIGHTER:
							okayRequest = checkFighter(oi, si, of, sf, oa, sa);
							break;
					}
					
				}else{
					
					log.write("REQUEST REJECTED: That ship does not belong to the current player.");
					announce("That ship does not belong to you.");
					
				}
				
			}else{
				
				log.write("REQUEST REJECTED: No ship at initial location.");
				announce("No ship at initial location.");
				
			}
			
		}else{
			
			log.write("REQUEST REJECTED: Parameter out of bounds.");
			announce("Parameter out of bounds.");
			
		}
		
		if(okayRequest){
			
			log.write("REQUEST APPROVED AND EXECUTED.");
			
		}
		
		return okayRequest;
		
	}
	
	private boolean checkFlag(int oi, int si, int of, int sf, int oa, int sa){
		
		boolean okayCheckFlag = false;
		
		boolean sameOrbit = (oi == of);
		boolean oneSpace = (((si + 1)%18 == sf) || ((sf + 1)%18 == si));
		boolean noBlock = (board.getShip(of, sf) == null);
		
		if(!sameOrbit){
			
			log.write("REQUEST REJECTED: The Flagship must not voluntarilly change elevation.");
			announce("The Flagship must not voluntarilly change elevation.");
			
		}else if(!oneSpace){
			
			log.write("REQUEST REJECTED: The Flagship must not move more than one sector.");
			announce("The Flagship must not move more than one sector.");
			
		}else if(!noBlock){
			
			log.write("REQUEST REJECTED: The Flagship must not move into an occupied sector.");
			announce("The Flagship must not move into an occupied sector.");
			
		}else{
			
			okayCheckFlag = true;
			board.move(oi, si, of, sf);
			
		}
		
		boolean attackInRange = (distanceBetween(of, sf, oa, sa) < 3);
		boolean attackUnblocked = isClearBetween(of, sf, oa, sa);
		boolean attackGood = attackInRange && attackUnblocked && !friendlyFire(of, sf, oa, sa);
		
		if(attackGood){
			
			flagshipAttack(oa, sa);
			
		}else{
			
			okayCheckFlag = false;
			board.move(of, sf, oi, si);
			
			log.write("REQUEST REJCTED: The Flagship must attack an enemy along an unobstructed and direct path of at most three sectors.");
			announce("The Flagship must attack an enemy along an unobstructed and direct path of at most three sectors.");
			
		}
		
		return okayCheckFlag;
		
	}
	
	private boolean checkBomber(int oi, int si, int of, int sf, int oa, int sa){
		
		boolean okayCheckBomber = false;

		boolean oneSpace = (((si + 1)%18 == sf) || ((sf + 1)%18 == si) || (si == sf)) 
							&& (((oi + 1) == sf) || ((of + 1) == oi) || oi == of) 
							&& !((oi == of) && (si == sf));
		boolean noBlock = (board.getShip(of, sf) == null);
		
		if(!oneSpace){
			
			log.write("REQUEST REJCTED: The Bomber must not move more than one sector.");
			announce("The Bomber must not move more than one sector.");
			
		}else if(!noBlock){
			
			log.write("REQUEST REJECTED: The Bomber must not move into an occupied sector.");
			announce("The Bomber must not move into an occupied sector.");
			
		}else{
			
			okayCheckBomber = true;
			board.move(oi, si, of, sf);
			
			
		}
		
		boolean attackGood = (oa == (of - 1)) && (sa == sf);
		
		if(attackGood){
			
			bomberAttack(oa, sa);
			
		}
		
		return okayCheckBomber;
		
	}
	
	private boolean checkSweeper(int oi, int si, int of, int sf, int oa, int sa){
		
		boolean okayCheckSweeper = false;
		
		boolean oneSpace = ((of == (oi + 1)%18 && sf == si) || (of == (oi + 17)%18 && sf == si));
		boolean orbitall = (oi == of);
		boolean noBlockClockwise = isClearThrough(oi, si, of, sf);
		boolean noBlockCounterClockwise = isClearUpTo(of, sf, oi, si);
		
		boolean noBlock = (!orbitall && oneSpace && noBlockClockwise) || (orbitall && (noBlockClockwise || noBlockCounterClockwise));
		
		if(!(oneSpace || orbitall)){
			
			log.write("REQUEST REJECTED: The Sweeper must not move more than one orbit or seventeen sectors.");
			announce("The Sweeper must not move more than one orbit or seventeen sectors.");
			
		}else if(!noBlock){
			
			log.write("REQUEST REJCTED: The Sweeper must not move into or through an occupied sector.");
			announce("The Sweeper must not move into or through an occupied sector.");
			
		}else{
			
			okayCheckSweeper = true;
			board.move(oi, si, of, sf);
			
		}
		
		
		boolean attackGood = false;
		
		if(noBlockClockwise){
			
			attackGood = ((sf > si) && (sa <= sf)) || ((sf < si) && (sa >= sf));
			
		}else if(noBlockCounterClockwise){
			
			attackGood = ((sf > si) && (sa >= sf)) || ((sf < si) && (sa <= sf));
			
		}
		
		if(attackGood){
			
			sweeperAttack(oa, sa, sf);
			
		}else{
			
			if(oa == (of + 1)){
				
				okayCheckSweeper = false;
				board.move(of, sf, oi, si);
			
				log.write("REQUEST REJCTED: The Sweeper must attack all enemies emediatly above its path begining with (oTarget, sTarget).");
				announce("The Sweeper must attack all enemies emediatly above its path begining with (oTarget, sTarget).");
				
			}
	
		}
		
		return okayCheckSweeper;
		
	}
	
	private boolean checkFighter(int oi, int si, int of, int sf, int oa, int sa){
		
		boolean okayCheckFighter = false;
		
		boolean diagonal = ((of - oi) == (sf - si)) || ((of - oi) == (si - sf)) && (oi != of);
		boolean noBlock = isClearThrough(oi, si, of, sf);
		
		if(!diagonal){
			
			log.write("REQUEST REJECTED: The Fighter must move diagonally.");
			announce("The Fighter must move diagonally.");
			
		}else if(!noBlock){
			
			log.write("REQUEST REJECTED: The Fighter must not move into or through an occupied sector.");
			announce("The Fighter must not move into or through an occupied sector.");
			
		}else{
			
			okayCheckFighter = true;
			board.move(oi, si, of, sf);
			
		}
		
		boolean attackInRange = distanceBetween(of, sf, oa, sa) < 2;
		boolean attackMomentus = true;
		
		boolean attackGood = attackInRange && attackMomentus;
		
		if(attackGood){
			
			knockDown(oa, sa);
			
		}else{
			
			okayCheckFighter = false;
			board.move(of, sf, oi, si);
			
			log.write("REQUEST REJCTED: The Fighter must attack one enemy within two sectors and within its final orbit and in the direction it has traveled.");
			announce("The Fighter must attack one enemy within two sectors and within its final orbit and in the direction it has traveled.");
			
		}
		
		return okayCheckFighter;
		
	}
	
	private void flagshipAttack(int oTarget, int sTarget){
		
		if(board.getShip(oTarget, sTarget) != null){
			
			if(oTarget > 2){
			
				knockDown((oTarget - 3), sTarget);
				board.move(oTarget, sTarget, (oTarget - 3), sTarget);
			
			}else{
			
				board.clear(oTarget, sTarget);
			
			}
			
		}
		
	}
	
	private void bomberAttack(int oTarget, int sTarget){
		
		int oTemp = oTarget;
		
		while(oTemp >= 0){
			
			knockDown(oTemp, sTarget);
			oTemp --;
			
		}
		
	}
	
	private void sweeperAttack(int oTarget, int sInitial, int sFinal){
		
		if(oTarget > 0){
			
			int sTarget = sInitial;
			boolean done = false;
			
			while(!done){
			
				if(!friendlyFire((oTarget - 1), sFinal, oTarget, sTarget) && board.getShip(oTarget, sTarget) != null){
				
					if(oTarget > 1){
					
						knockDown((oTarget - 2), sTarget);
						board.move(oTarget, sTarget, (oTarget - 2), sTarget);
						
					}else{
					
						board.clear(oTarget, sTarget);
					
					}
				
				}
			
				done = (sTarget == sFinal);
				sTarget = (sTarget + 1)%18;
			
			}
		}
	}
	
	private void knockDown(int oTarget, int sTarget){
		
		if(oTarget > 0){
			
			if(board.getShip(oTarget, sTarget) != null){
				
				knockDown((oTarget - 1), sTarget);
				board.move(oTarget, sTarget, (oTarget - 1), sTarget);
				
			}
			
		}else if(oTarget == 0){
			
			board.clear(oTarget, sTarget);
			
		}
			
	}
	
	private boolean friendlyFire(int os, int ss, int oa, int sa){
		
		VoidShip shooter = board.getShip(os, ss);
		VoidShip shot = board.getShip(oa, sa);
		
		if(shooter != null && shot != null){
			
			return shooter.isPlayerOne() == shot.isPlayerOne();
			
		}else{
			
			return false;
			
		}
		
	}
	
	//returns true if there are no ships between(oI, sI) and (oF, sF) or at (oI, sI).
	private boolean isClearUpTo(int oI, int sI, int oF, int sF){
		
		boolean clear = isClearBetween(oI, sI, oF, sF) && (board.getShip(oI, sI) == null);
		//if(clear){log.write("isClearUpTo");}
		return clear;
		
	}
	
	//returns true if there are no ships between (oI, sI) and (oF, sF) or at (oF, sF).
	private boolean isClearThrough(int oI, int sI, int oF, int sF){
		
		boolean clear = isClearBetween(oI, sI, oF, sF) && (board.getShip(oF, sF) == null);
		//if(clear){log.write("isClearThrough");}
		return clear;
		
	}
	
	//returns true if there are no ships between (oI, sI) and (oF, sF).
	private boolean isClearBetween(int oI, int sI, int oF, int sF){
		
		boolean isDiagonal = (((oF - oI) == (sF - sI)) || ((oF - oI) == (sI - sF))) && (oF != oI);
		boolean isVertical = (oF != oI) && (sF == sI);
		boolean isOrbital = (oF == oI) && (sF != sI);
		
		boolean clear = isDiagonal || isOrbital || isVertical;
		
		if(isDiagonal){
			
			int otest = oF - oI;
			int stest = sF - sI;
		
			int oinc = 0;
			int sinc = 0;
		
			if(otest > 0){oinc = 1;}else if(otest < 0){oinc = -1;}
			if(stest > 0){sinc = 1;}else if(otest < 0){sinc = 17;}
		
			int o = oI + oinc;
			int s = (sI + sinc)%18;
		
			while(o != oF){
			
				//log.write("checking: " + o + "," + s);
				clear = clear && (board.getShip(o, s) == null);
				o = o + oinc;
				s = (s + sinc)%18;
			
			}
			
		}else if(isOrbital){
			
			boolean c = true;
			boolean cc = true;
			
			for(int s = (sI + 1)%18; s != sF; s = (s + 1)%18){
				
				//log.write("checking: " + oF + "," + s);
				clear = clear && (board.getShip(oF, s) == null);
				
			}
			
		}else if(isVertical){
			
			int otest = oF - oI;
			
			int oinc = 0;
			
			if(otest > 0){oinc = 1;}else if(otest < 0){oinc = 17;}
			
			for(int o = (oI + oinc)%18; o != oF; o = (o + oinc)%18){
				
				//log.write("checking: " + o + "," + sF);
				clear = clear && (board.getShip(o, sF) == null);
				
			}
			
		}
		
		//if(clear){log.write("isClearBetween");}
		
		return clear;
		
	}
	
	private int distanceBetween(int oI, int sI, int oF, int sF){
		
		boolean isDiagonal = (((oF - oI) == (sF - sI)) || ((oF - oI) == (sI - sF))) && (oF != oI);
		boolean isVertical = (oF != oI) && (sF == sI);
		boolean isOrbital = (oF == oI) && (sF != sI);
		
		int distance = 0;
		
		if(isDiagonal){
			
			int otest = oF - oI;
			int stest = sF - sI;
		
			int oinc = 0;
			int sinc = 0;
		
			if(otest > 0){oinc = 1;}else if(otest < 0){oinc = -1;}
			if(stest > 0){sinc = 1;}else if(otest < 0){sinc = 17;}
		
			int o = oI + oinc;
			int s = (sI + sinc)%18;
		
			while(o != oF){
			
				distance ++;
				o = o + oinc;
				s = (s + sinc)%18;
			
			}
			
		}else if(isOrbital){
			
			boolean c = true;
			boolean cc = true;
			
			for(int s = (sI + 1)%18; s != sF; s = (s + 1)%18){
				
				distance ++;
				
			}
			
		}else if(isVertical){
			
			int otest = oF - oI;
			
			int oinc = 0;
			
			if(otest > 0){oinc = 1;}else if(otest < 0){oinc = 17;}
			
			for(int o = (oI + oinc)%18; o != oF; o = (o + oinc)%18){
				
				distance ++;
				
			}
			
		}
		
		return distance;
		
	}
	
	private void announce(String message){
		
		if((isPlayerOneTurn && playerOne instanceof VoidHumanPlayer) 
		|| (!isPlayerOneTurn && playerTwo instanceof VoidHumanPlayer)){
			
			System.out.println(message);
			log.write("MESSAGE TO HUMAN PLAYER: " + message);
			
		}
		
	}
	
	private void printBoard(){
		
		if((isPlayerOneTurn && playerOne instanceof VoidHumanPlayer) 
		|| (!isPlayerOneTurn && playerTwo instanceof VoidHumanPlayer)){
			
			board.print();
			log.write("BOARD PRINT.");
			
		}
		
	}
	
	private boolean victory(){
		
		boolean playerOneFlag = false;
		boolean playerTwoFlag = false;
		
		for(int o = 0; o < 5; o++){
			for(int s = 0; s < 18; s++){
			
				VoidShip temp = board.getShip(o, s);
				
				if(temp != null){
					
					if(temp.getType() == VoidShipType.FLAG){
						
						playerOneFlag = playerOneFlag || temp.isPlayerOne();
						playerTwoFlag = playerTwoFlag || !temp.isPlayerOne();
						
					}
					
				}
		
			}
		}

		if(!playerOneFlag){
			
			log.write("VICTORY: Player Two.");
			announce("Player Two wins!");
			return true;
			
		}else if(!playerTwoFlag){
			
			log.write("VICTORY: Player One.");
			announce("Player One wins!");
			return true;
			
		}else{
			
			return false;
			
		}	
		
	}
	
}