package core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import core.Player.StrategyType;

/**
 * Handles the interactions between the {@link core.Player} and the {@link core.Grid}. Manages the creation of the players, the game chronology and gives access to the 
 * most important informations about the current game.
 * 
 */
public class GameMaster {
	private int currentTurn;
	private int currentMatch;
	private int nPlayerH;
	private int nPlayerCPU;
	private int numberOfMatch;
    private List<Player> players = new ArrayList<>();
    private ArrayList<Observer> observers;
    private Deck deck;
	private Player currentPlayer;
	private Grid grid;

	public GameConsole console;
	public GameGraphical graphical;

	/**
	 * Prompts the player for the type of {@link core.Strategy} ({@link core.StrategyCPU} or {@link core.StrategyHuman}) and number of players (number of human and cpu) as well as 
	 * the number of game in the tournament.
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

	/**
	 * Called when the game is all set-up. Doesn't exit until the end of the tournament. Handles the choice of the play area for each game, the choregraphy
	 * of the different object to conduct a game (give card, determine when the game is finished). 
	 */
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

	/**
	 * Called when a player has to play. Handles the different ways of asking the player to move.
	 * @param he The player that has to play. Human or CPU.
	 */
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
	/**
	 * Gives the first cards to each player at the beginning of a game. 1 for a basic game, 3 for advanced.
	 */
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
	/**
	 * Instantiate the players and gives them their strategy. Asks the names for each human player. The CPU players are given number names.
	 * @param gc A reference to the GameController
	 */
	public void instantiatePlayers(GameController gc) {
    	for(int i=1;i<=nPlayerH;i++) {
    		System.out.println("Nom du joueur humain n°"+String.valueOf(i)+" ?");
    		String playerName=ShapUp.scanner.next();
			players.add(new Player(playerName, StrategyType.HUMAN, gc));
    	}
    	
    	for(int i=1;i<=nPlayerCPU;i++) players.add(new Player("Bot"+String.valueOf(i), StrategyType.CPU, null));    	
	}
	/**
	 * Triggers the generation of the {@link core.Deck} and calls the function to ask in the console the size and the shape of the {@link core.Grid}.
	 */
    private void instantiatePlayArea() {
		this.grid = console.instantiateGrid();
		this.deck = new Deck();
	}	
    /**
     * Adds an {@link core.Observer} in a {@link java.util.ArrayList} to notify when there is a change to be notified of.
     * @param {@link core.Observer} The object keep in the {@link java.util.ArrayList}.
     */
	public void addObserver(Observer observer) { observers.add(observer); }
	/**
	 * Notifies the observers kept in the observers {@link java.util.ArrayList}.
	 * @param update An argument to pass along the notification.
	 */
	public void notifyObservers(Update update) { observers.forEach(observer -> observer.update(this, update)); }

	/**
	 * Getter of the {@link core.Grid}.
	 * @return The reference to the current grid.
	 */
	public Grid getGridAddress() { return this.grid; }
	/**
	 * Getter of the reference of the {@link core.Player} that has to play now.
	 * @return the player that has to play.
	 */
	public Player getCurrentPlayer() { return this.currentPlayer; }
	/**
	 * Getter of the list of players in a tournament.
	 * @return the {@link java.util.ArrayList} of players.
	 */
	public List<Player> getPlayers() { return this.players; }
	/**
	 * Getter of the number of the game in the tournament. Starts from 1 !
	 * @return
	 */
	public int getCurrentMatch() { return this.currentMatch; }
	/**
	 * Getter of the index of the player that has to play in the list of players.
	 * @return
	 */
	public int getCurrentTurn() { return this.currentTurn; }
	/**
	 * Getter of the number of human players.
	 * @return
	 */
	public int getNumberOfHumanPlayers() { return this.nPlayerH; }
	/**
	 * Getter of the number of CPU players.
	 * @return
	 */
	public int getNumberOfCPUPlayers() { return this.nPlayerCPU; }
	/**
	 * Getter of the total number of game in a tournament.
	 * @return
	 */
	public int getNumberOfMatchs() { return this.numberOfMatch; }
	

}