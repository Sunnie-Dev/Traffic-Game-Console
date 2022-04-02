package traffic;

import java.util.*;
import java.io.*;
// ******** Created By Casey Munga
// Feb 9th 2022

public class TrafficGame {

	public TeamMember moveEmpty;
	public ArrayList<TeamMember> Team1 = new ArrayList<TeamMember>();
	public ArrayList<TeamMember> Team2 = new ArrayList<TeamMember>();
	Scanner in = new Scanner(System.in);
	

	public void PrintGameMenu() {
		
		int choice=-1; 
		
		while(choice != 0){
			String s0="-------------------------------------------- Welcome to TRAFFIC JAM -------------------------------------------";
			String s1="  Game Objective : Get your Team OUT of Traffic and to the OTHER SIDE of town!";
			String s2="     1:	GAME RULES";
			String s3="     2: PLAY GAME ";
			String s4="     0:	QUIT GAME";
			String s5="";
			String s6= " Make a choice:  ";
			System.out.print(s0+"\n"+"\n"+s1+"\n"+"\n"+s2+"\n"+s3+"\n"+s4+"\n"+s5+"\n"+s6);
			 try
			    { 
				 choice = in.nextInt();
			    }
			 catch ( InputMismatchException ex )
			    { 
			      System.out.println("You entered bad data." );
			      System.out.println("Run the program again." );
			    } 
			if(choice ==1)
				PrintRules();
			if( choice == 2) {
				int[] gameDetails = GetGameDetails();	
				LoadBoard(gameDetails,false);
			}	
		
		}
		System.out.println("Thank you for playing ---- GAME OVER -----");
		//close stream
		in.close();
	}
	
	public void PrintRules() {
		
		String s0="************* 		GAME RULES 				************";
		String s1="	1.  Your Objective is to move the RED team or BLUE Team's to your OPPONENT's Position.   ";
		String s2="	2.  You can choose to WALK 1 Space or JUMP 2 spaces.";
		String s3="	3.  You can ONLY go FORWARD  OR JUMP over your OPPONENT";
		String s4="	4.  Going BACKWARDS or JUMPING over your team member is PROHIBITED.";
		String s5="	5.  If you can't solve it 'cause you're a baby, just CHEAT and select SOLVE";
		String s6="	6.       You can RESTART the game to TRY your LUCK.";
		String s8="	Enjoy!!!!!!!";
		System.out.println("\n"+s0+"\n"+"\n"+s1+"\n"+s2+"\n"+s3+"\n"+s4+"\n"+s5+"\n"+s6+"\n"+s8+"\n");
	}
	
	public int[] GetGameDetails(){
		// Keeps the game stats
		int [] gameDetails = {0,0,0};
		
		// Choose Teams
		try {
		System.out.print("\n Which Team goes first? 1. RED    2. BLUE   "); 
		int first = in.nextInt();
		gameDetails[0]=first;
		System.out.print("\n"+" Enter team 1 Players : Max 10 : ");
		int team1Players = in.nextInt();
		gameDetails[1]= team1Players;
		System.out.print("\n"+" Enter team 2 Players : Max 10 : ");
		int team2Players = in.nextInt();
		gameDetails[2]= team2Players;
		// close stream
		}
		 catch ( InputMismatchException ex )
	    { 
	      System.out.println("You entered bad data." );
	      System.out.println("Run the program again." );
	    } 
		return gameDetails;
		
	}
	
	public void LoadBoard(int[] gameDetails, Boolean solve) {
		//Create Teams and Fill Game Board

		// Solve =false print game board else don't print
		String team1Color = null;String team2Color = null;
		int count = 0;
		int team1Players = gameDetails[1];
		int team2Players = gameDetails[2];
		ArrayList <TeamMember> GameBoard = new ArrayList <TeamMember>(); // Game Board to put the players
		
		if (gameDetails[0] == 1) {
			team1Color="RED";
			team2Color="BLUE";}
		else {
			team1Color="BLUE";
			team2Color="RED";}
	
		 //Load team1 
		 TeamMember player;
		
		 for (int i=0; i<= team1Players-1; i++) {
			 player=new TeamMember(team1Color.substring(0,1)+i, i , team1Color);
			 Team1.add(player);
			 GameBoard.add(player);
			 count++;
			 }
		
		//Load Empty Space and keep its position
		int EmptyPos=count++;
		moveEmpty= new TeamMember("Empty",EmptyPos,"Empty");
		GameBoard.add(moveEmpty);
		 
		for (int i= team2Players-1; i >= 0; i--) {
			player=new TeamMember(team2Color.substring(0,1)+i, count++ , team2Color);; // so that when they are facing the blue team starts at the nex
			Team2.add(player);
			GameBoard.add(player);// Team 2 players added to the game board
			}
		if (solve)
			SolveGame(GameBoard,team1Players,team1Color,team2Players); // parameters needed to create the cheat sheet
		else	
			PlayGame(GameBoard,team1Color,team2Color);
	}
	
	public void PrintBoard( ArrayList<TeamMember> GameBoard) {
		System.out.println();
		GameBoard.forEach(member->System.out.println("    Player: " +member.Name + "| Team "+member.Color+" | Game board position "+GameBoard.indexOf(member)));
		}
	
	public Boolean ValidateWin(ArrayList<TeamMember> board, String team2Color, int team2Size) {
		// Since team 1 starts first check for team 2 at the top or beginning or left of the board
	   
		for(int i = 0; i <= team2Size-1; i++) {
			if (board.get(i).Color != team2Color) {
				return false; // members are not contiguous
			}
		}
		if (board.get(team2Size).Color == "Empty")  // all members of the team are contiguous and there is a Empty behind them
			return true;   // So they have successfully traversed to the opposite side of the game board
		
		return false;
	}
	
	public Boolean Jump(ArrayList<TeamMember> board, TeamMember currentPlayer, String teamColor) {
		
		int tempPos = 0;
		boolean jump=false;
		
		// jump only if the person in front of you is not on your team and there is an EmptySpace
		if  (currentPlayer.Color=="RED" && currentPlayer.Pos+2 == moveEmpty.Pos && (currentPlayer.Color !=board.get(currentPlayer.Pos+1).Color)){
				tempPos = currentPlayer.Pos; // Start swap
				jump=true;
				currentPlayer.Pos = moveEmpty.Pos; //player moves to the Empty space
				moveEmpty.Pos = tempPos; //Swap done
				board.set(currentPlayer.Pos, currentPlayer); //update game board
				board.set(moveEmpty.Pos, moveEmpty);
				System.out.println("\n "+" "+currentPlayer.Name+" JUMPS to position "+currentPlayer.Pos+". A space open now at pos "+moveEmpty.Pos);}
			
		else
			if (currentPlayer.Color=="BLUE" && currentPlayer.Pos-2 == moveEmpty.Pos && (currentPlayer.Color !=board.get(currentPlayer.Pos-1).Color)){
				tempPos = currentPlayer.Pos; // Start swap
				jump=true;
				currentPlayer.Pos = moveEmpty.Pos; //player moves to the Empty space
				moveEmpty.Pos = tempPos; //Swap done
				board.set(currentPlayer.Pos, currentPlayer); //update game board
				board.set(moveEmpty.Pos, moveEmpty);
				System.out.println("\n "+" "+currentPlayer.Name+" JUMPS to position "+currentPlayer.Pos+". A space open now at pos "+moveEmpty.Pos);
		}
	 return jump;
	}
	
	public boolean Walk(ArrayList<TeamMember> board, TeamMember currentPlayer) {

		Boolean walk = true; // Player can only WALK ONCE per Turn
		//TeamMember currentPlayer = new TeamMember();
		int tempPos = 0; 
		if (currentPlayer.Name == "Empty") {
			System.out.println (" "+"Invalid Walk - Cannot walk from Empty Space");
		}else 
		if ((currentPlayer.Color == "RED" && currentPlayer.Pos+1 == moveEmpty.Pos) || (currentPlayer.Color=="BLUE" && currentPlayer.Pos-1 == moveEmpty.Pos )){ // TRY Walk
			 tempPos = currentPlayer.Pos; // Start swap
			 currentPlayer.Pos = moveEmpty.Pos; //player moves to the Empty space
			 moveEmpty.Pos = tempPos; //Swap done
			 walk = false; // player used up his walk for the turn	
			 board.set(currentPlayer.Pos, currentPlayer); //update game board
			 board.set(moveEmpty.Pos, moveEmpty);
			 System.out.println("\n"+" "+currentPlayer.Name+" walks to position "+currentPlayer.Pos+". A Space open now at pos "+moveEmpty.Pos);
			
		 }

		return walk; 
	}
	
	public void SolveGame(ArrayList<TeamMember> GameBoard,int team1Size, String team1Color, int team2Size) {
		// Automatic solution of game cause it was too hard for the players
		int team1Turn=0;
		int team2Turn=0; 		//Turns per team used to create log
		Boolean gameFinished= false; 
		Boolean walk = true;
		String team2Color=null;
		int teamCount=0;
		System.out.println ( " Traffic Jam Solution - Log- Casey Munga");
		System.out.println ("             BOARD IS SET ");
		PrintBoard(GameBoard);
		
		if(team1Color =="RED")
			team2Color ="BLUE";
		else
			team2Color="RED";
		
		//decide which team moves first and increment turns - for log file
				while (gameFinished== false) {
					if(team1Color == "RED" || team1Color =="BLUE") { 
						teamCount=Team1.size();
						System.out.println();
						team1Turn++;
						System.out.println(" "+team1Color +" Team Turn " +team1Turn);
						//Try WALK FIRST
						for(int i = team1Size-1; i>=0; i--) {
							if (walk) {
								walk=Walk(GameBoard,Team1.get(i));
								PrintBoard(GameBoard);}
							else {
							if (Jump(GameBoard,Team1.get(i),team1Color))
							PrintBoard(GameBoard);}
						}
						System.out.println();
						team2Turn++;
						
					if(team2Color == "RED" || team2Color =="BLUE") { // RedTeam moves first
					System.out.println(" ");
					System.out.println(" "+team2Color +" Team Turn " +team2Turn);
					walk=true;
						//Try JUMP FIRST
						for(int i = 0; i<= team2Size-1; i++) {
							if (Jump(GameBoard,Team2.get(i),Team2.get(i).Color))
								PrintBoard(GameBoard);
							else
							if (walk) {
								System.out.println();
								walk=Walk(GameBoard,Team2.get(i));
								PrintBoard(GameBoard);
								}
							
						}
					}
				}
					gameFinished = ValidateWin(GameBoard,Team2.get(0).Color,team2Size); // Check to see if there is a win after the turn is completed
			}
				
				System.out.println();
				System.out.println("        ***** The Game is SOLVED. *********");
				PrintBoard(GameBoard);
	
	}

	
		
	
	
	

	public void PlayGame(ArrayList<TeamMember> GameBoard,String team1Color,String team2Color){		
	
		int round = 0;
		int destination = -1;
		int from = -1;
		int action = 0;
		String currentColor;
		Boolean turnCompleted = false;
		Boolean gameCompleted = false;
		Boolean walk = true; // can walk
	    TeamMember currentPlayer = new TeamMember();
	    int team1Player = Team1.size();
	    int team2Player = Team2.size();
		
		// Print the Game Board
	
		System.out.println("                  STARTING BOARD ");
		System.out.println(" ----------------------------------------------------");
		System.out.println(" If you choose another team player your turn will end");
		PrintBoard(GameBoard);
		
		//Set the first player	
		currentColor = team1Color; // Gets Team1's Color who should play first
		while(gameCompleted == false){
			//START GAME
			round++;
		
			System.out.println("\n"+"**********    ROUND : "+round+"  **********");
			for (int turn = 1; turn <= 2; turn++){ // Each Team plays in one turn
				if (gameCompleted == true)
					break;
				else {	
					walk=true;
					System.out.println("  TURN : "+turn +" "+currentColor+" TEAM PLAYS");
					while(turnCompleted == false){	
						System.out.println("What do you want to do: 	1. WALK		2. JUMP		3. TURN DONE	4. CHEAT-ITS TOO HARD	5. RESET GAME	0. QUIT ");
						try {
						action=in.nextInt();
						}
						 catch ( InputMismatchException ex )
					    { 
					      System.out.println("You entered bad data." );
					      System.out.println("Run the program again." );
					    } 
						if (action == 0)
						{ gameCompleted =true;
						   break;
						}
						// GAME FINISHED
							switch (action){
							case 3: turnCompleted = true; //turn done
							break;
							
							case 4:
									Team1.removeAll(Team1);
									Team2.removeAll(Team2);
									if (team1Color =="RED") {
										int[] gameDetails= {1,team1Player,team2Player};
										LoadBoard(gameDetails,true);
										}
									else {int[] gameDetails= {2,team1Player,team2Player}; //solve game
									LoadBoard(gameDetails,true);
									}
							break;
							
							case 5: 
								Team1.removeAll(Team1);
								Team2.removeAll(Team2);
								if (team1Color =="RED") {
										int[] gameDetails= {1,team1Player,team2Player};
										LoadBoard(gameDetails,false);
										}
									else {int[] gameDetails= {2,team1Player,team2Player}; // reset game
									LoadBoard(gameDetails,false);
									}
							break;
							default:
							}
					if (turnCompleted == false && (action <= 3 && action != 0) ){
						System.out.println ("\n"+"Please Move a "+ currentColor+ " Player"+"\n");
						System.out.print(" Enter the  START position : ");
						from=in.nextInt();
						System.out.print(" Enter the DESTINATION position :");
						destination=in.nextInt();
					
						currentPlayer= GameBoard.get(from);
						
						if (currentPlayer.Color == currentColor ) {
							switch (action) {
							case 1: if (walk) { // can only walk once per turn
										walk=Walk(GameBoard,currentPlayer);
										PrintBoard(GameBoard);
											}
									else System.out.println( " Your Team has walked already. Only Once per turn.");
									
							break;
							
							case 2: if (Jump(GameBoard,currentPlayer,currentPlayer.Color)) //Jump
										PrintBoard(GameBoard);
									else System.out.println("Invalid Jump");
							break;
							
							case 3: turnCompleted = true; //turn done
							break;
			
							default:gameCompleted = true; // user wants out
							}
						}
					}
				}//while turn completed
					// Team 2 Turn to Play
				if(currentColor == "RED") 
					currentColor="BLUE";
				else currentColor = "RED";
					turnCompleted=false;
			}
		}// for
		if(gameCompleted == false) { // Validate the board after each round to see if anyone wins
			if (ValidateWin(GameBoard, team2Color,Team2.size())) {
				String s1= " WWWW    WWWW    IIIIII   NNNN N   NNNN";
				String s2= "  ww     ww       III      NN   N   NN ";
				String s3= "  WW  VV WW		  III      NN    N  NN ";
				String s4="   W   V   W      IIIIII   NNNN     NNNN";
				String s5="";
				String s6="WHOOO-HOO Genius Rank - You got your team to their destination safely. ";
				System.out.println("\n"+"\n"+s1+"\n"+s2+"\n"+s3+"\n"+s4+"\n"+s5+"\n"+s6+"\n");
				gameCompleted = true;
				}
			}	
		}// while game completed
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		 
		TrafficGame Game = new TrafficGame(); // start a new game
		Game.PrintGameMenu();
		}

	
}
