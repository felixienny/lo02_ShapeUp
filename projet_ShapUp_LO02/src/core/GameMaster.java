package core;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameMaster {
	public GameMaster()
	{
		System.out.println("Combien de joueur humain ?");
		nPlayerH = ShapUp.scanner.nextInt();
		System.out.println("Combien de joueur ordi ?");
		nPlayerCPU = ShapUp.scanner.nextInt();
		System.out.println("Combien de matchs ?");
		numberOfMatch = ShapUp.scanner.nextInt();
		
		instantiatePlayers();
	}

//methods
	//job specific

	public void play() {
		for(int matchNumber=0;matchNumber<numberOfMatch;matchNumber++) {//match loop
			instantiatePlayArea();
			this.startGame();
		
			Iterator<Player> he2 = players.iterator();
			while(!this.grid.isFull())//game loop
			{
				this.playOneTurn(he2);
				this.grid.display();
				System.out.println("\nREMPLISSAGE DU PLATEAU = "+(this.grid.getNumberOfPlacedCards()*100/(this.grid.getRealHeight()*this.grid.getRealWidth()-this.grid.getNumberOfDeadTiles()))+"%");
				System.out.println("\nTABLEAU DES SCORES : ");

				for(Player player : players) System.out.println(player.getName() + " : " + player.getCurrentScore());
				
				System.out.println("\n\n");
				if(he2.hasNext()==false) he2 = players.iterator();
			}
			
			for(Player he3 : players) he3.gameEnds();//end
		}
	}
	
	private void playOneTurn(Iterator<Player> he) {
		Player currentPlayer = he.next();
		System.out.println("\n\n\nC'est au tour de "+currentPlayer.getName()+" !");
		
		if(this.grid.isAdvancedGame()) {
			currentPlayer.askMove();			
			currentPlayer.giveCard(this.deck.pickNextCard());
		}
		else {
			currentPlayer.giveCard(this.deck.pickNextCard());
			currentPlayer.askMove();			
		}

	}
	
	private void startGame() {
		if(this.grid.isAdvancedGame()) {
			for(Player he : players) {
				he.gameStarts(this.grid);
				he.giveCard(this.deck.pickNextCard());
				he.giveCard(this.deck.pickNextCard());
				he.giveCard(this.deck.pickNextCard());
			}
		}
		else {
			for(Player he : players) {
				he.gameStarts(this.grid);
				he.giveCard(this.deck.pickNextCard());				
			}
		}
	}
	
	private void instantiatePlayers() {
    	for(int i=1;i<=nPlayerH;i++) {
    		System.out.println("Nom du joueur humain n°"+String.valueOf(i)+" ?");
    		String playerName=ShapUp.scanner.next();
    		players.add(new Player(playerName, "Human"));
    	}
    	
    	for(int i=1;i<=nPlayerCPU;i++) players.add(new Player("Bot"+String.valueOf(i), "Bot"));    	
	}
	
    private void instantiatePlayArea() {
		System.out.println("Hauteur de la zone de jeu ?");
		int height = ShapUp.scanner.nextInt();
		System.out.println("Largeur de la zone de jeu ?");
		int width = ShapUp.scanner.nextInt();
		
		System.out.println("Veux-tu jouer en mode avancé ? y/n");
		char choix = ShapUp.scanner.next().charAt(0);
		boolean isAdvancedGame;
		if (choix == 'y') isAdvancedGame = true;
		else isAdvancedGame = false;

		Grid gridDynamic = new Grid(width, height, true, isAdvancedGame);
		Grid gridDiamond = new Grid(width, height, false, isAdvancedGame, "diamond");
		Grid gridCircle = new Grid(width, height, false, isAdvancedGame, "circle");
		Grid gridTriangle = new Grid(width, height, false, isAdvancedGame, "triangle");
		Grid gridPerso = new Grid(width, height, false, isAdvancedGame, "");

		int choiceGrid;
		do {
			System.out.println("Quelle Grid choisis tu ?");
			System.out.println("1 - Une grid allouée dynamiquement :"); gridDynamic.display();
			System.out.println("2 - Une grid en forme de diamand avec des zones mortes :"); gridDiamond.display();
			System.out.println("3 - Une grid en forme de cercle avec des zones mortes :"); gridCircle.display();
			System.out.println("4 - Une grid en forme de triangle avec des zones mortes :"); gridTriangle.display();
			System.out.println("5 - Une grid avec des zones mortes que tu définies toi même :"); gridPerso.display();
			choiceGrid = ShapUp.scanner.nextInt();
		} while (!(choiceGrid>=1 && choiceGrid<=5));

		gridDynamic = null;	gridDiamond = null;	gridCircle = null;	gridTriangle = null; gridPerso = null;

		if (choiceGrid==1) this.grid = new Grid(width, height, true, isAdvancedGame);
		else if (choiceGrid==2) this.grid = new Grid(width, height, false, isAdvancedGame, "diamond");
		else if (choiceGrid==3) this.grid = new Grid(width, height, false, isAdvancedGame, "circle");
		else if (choiceGrid==4) this.grid = new Grid(width, height, false, isAdvancedGame, "triangle");
		else if(choiceGrid==5) {
			String choiceDeadTile;
			StringBuffer deadTiles = new StringBuffer();
			do {
				System.out.println("Saisis les coordonnées où tu souhaites mettre des zones mortes (ex: 1,1 PUIS ENTREE 1,2)");
				System.out.println("Saisis la lettre q pour stopper ta saisie et valider");
				choiceDeadTile =  ShapUp.scanner.next().trim();
				if (choiceDeadTile.matches("^[0-9],[0-9]$")) deadTiles.append(choiceDeadTile+";");
			} while (!(choiceDeadTile.matches("^q$")));
			this.grid = new Grid(width, height, false, isAdvancedGame, deadTiles.toString());
		}

		if(isAdvancedGame) this.deck = new Deck(height*width+players.size()*3);
		else this.deck = new Deck(width*height+players.size());

    }
	
    
//attributes
	//private

	private int nPlayerH;
	private int nPlayerCPU;
	private int numberOfMatch;
    private List<Player> players = new ArrayList<Player> ();
    private Deck deck;
    private Grid grid;
}