package core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import core.Player.StrategyType;
/**
 * Handles the interactions between the players and the Grid. Manages the creation of the players, the game chronology and gives access to the 
 * most important informations about the current game.
 * 
 */
public class GameMaster {
//attributes
	//private
	private Player currentPlayer;
	private int currentTurn;
	private int currentMatch;

	private int nPlayerH;
	private int nPlayerCPU;
	private int numberOfMatch;
    private List<Player> players = new ArrayList<>();
    private Deck deck;
	private Grid grid;

	private ArrayList<Observer> observers;
	public GameConsole console;
	public GameGraphical graphical;

	/**
	 * Prompts the player for the types and number of players for the tournament.
	 */
	public GameMaster() {
		this.currentTurn = 0;
		this.observers = new ArrayList<>();
		this.console = new GameConsole(this);
		this.graphical = new GameGraphical(this);
		System.out.println("Combien de joueur humain ?");
		nPlayerH = ShapUp.scanner.nextInt();
		System.out.println("Combien de joueur ordi ?");
		nPlayerCPU = ShapUp.scanner.nextInt();
		System.out.println("Combien de matchs ?");
		numberOfMatch = ShapUp.scanner.nextInt();
	}

	public void play() {
		for (this.currentMatch = 1; this.currentMatch <= numberOfMatch; this.currentMatch++) {
			notifyObservers(Update.MATCH);

			instantiatePlayArea();
			notifyObservers(Update.GRID);
			this.startGame();

			Iterator<Player> he2 = players.iterator();
			this.currentTurn = 1;
			this.notifyObservers(Update.TURN);
			while (!this.grid.isFull()) {
				this.playOneTurn(he2);
				if (he2.hasNext() == false) {
					this.currentTurn++;
					this.notifyObservers(Update.TURN);
					he2 = players.iterator();
				}
			}
			this.notifyObservers(Update.ALL);
			for (Player he3 : players)
				he3.gameEnds();
		}

		notifyObservers(Update.MATCH);
	}

	private void playOneTurn(Iterator<Player> he) {
		this.currentPlayer = he.next();
		notifyObservers(Update.GRID);

		if (this.grid.isAdvancedGame()) {
			notifyObservers(Update.PLAYER);
			currentPlayer.askMove();
			currentPlayer.giveCard(this.deck.pickNextCard());
		} 
		else {
			currentPlayer.giveCard(this.deck.pickNextCard());
			notifyObservers(Update.PLAYER);
			currentPlayer.askMove();
		}
	}
	
	private void startGame() {
		for(Player player : this.players) {
			player.gameStarts(this.grid);
			player.giveCard(this.deck.pickNextCard());		
			if(this.grid.isAdvancedGame()) {
				player.giveCard(this.deck.pickNextCard());
				player.giveCard(this.deck.pickNextCard());
			}		
		}
	}
	
	public void instantiatePlayers(GameController gc) {
    	for(int i=1;i<=nPlayerH;i++) {
    		System.out.println("Nom du joueur humain n°"+String.valueOf(i)+" ?");
    		String playerName=ShapUp.scanner.next();
			players.add(new Player(playerName, StrategyType.HUMAN, gc));
    	}
    	
    	for(int i=1;i<=nPlayerCPU;i++) players.add(new Player("Bot"+String.valueOf(i), StrategyType.CPU, null));    	
	}
	
    private void instantiatePlayArea() {
		this.grid = console.instantiateGrid();
		this.deck = new Deck();
	}	

	public void addObserver(Observer observer) { observers.add(observer); }
	public void notifyObservers(Update update) { observers.forEach(observer -> observer.update(this, update)); }


	public Grid getGridAddress() { return this.grid; }
	public Player getCurrentPlayer() { return this.currentPlayer; }
	public List<Player> getPlayers() { return this.players; }

	public int getCurrentMatch() { return this.currentMatch; }
	public int getCurrentTurn() { return this.currentTurn; }

	public int getNumberOfHumanPlayers() { return this.nPlayerH; }
	public int getNumberOfCPUPlayers() { return this.nPlayerCPU; }
	public int getNumberOfMatchs() { return this.numberOfMatch; }
	

}