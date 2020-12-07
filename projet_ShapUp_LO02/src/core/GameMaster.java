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
	
	//setter
	
	//getter
	
	//job specific
	public void play()
	{
		for(int matchNumber=0;matchNumber<numberOfMatch;matchNumber++)//match loop
		{
			instantiatePlayArea();
			this.startGame();
		
			Iterator<Player> he2 = players.iterator();
			while(!this.grid.isFull())//game loop
			{
				this.playOneTurn(he2);
				this.grid.display();
				for(Player she : players) System.out.print(she.getCurrentScore()+" ");
				if(he2.hasNext()==false) he2 = players.iterator();
			}
			
			for(Player he3 : players) he3.gameEnds();//end
		}
	}
	private void playOneTurn(Iterator<Player> he)
	{
		Player currentPlayer = he.next();
<<<<<<< HEAD
=======
		System.out.println("\n\n\nC'est au tour de "+currentPlayer.getName()+" !");
>>>>>>> felixienny
		
		if(this.grid.isAdvancedGame())
		{
			currentPlayer.askMove();			
			currentPlayer.giveCard(this.deck.pickNextCard());
		}
		else
		{
			currentPlayer.giveCard(this.deck.pickNextCard());
			currentPlayer.askMove();			
		}

	}
	private void startGame()
	{
		if(this.grid.isAdvancedGame())
		{
			for(Player he : players)
			{
				he.gameStarts(this.grid);
				he.giveCard(this.deck.pickNextCard());
				he.giveCard(this.deck.pickNextCard());
				he.giveCard(this.deck.pickNextCard());
			}
		}
		else
		{
			for(Player he : players)
			{
				he.gameStarts(this.grid);
				he.giveCard(this.deck.pickNextCard());				
			}
		}
	}
    private void instantiatePlayers()
    {
<<<<<<< HEAD
    	for(int i=1;i<=nPlayerH;i++)
    	{
    		System.out.println("Nom du joueur humain n°"+String.valueOf(i)+" ?");
=======
    	for(int i=1;i<=nPlayerH;i++) {
    		System.out.println("Nom du joueur humain nÂ°"+String.valueOf(i)+" ?");
>>>>>>> felixienny
    		String playerName=ShapUp.scanner.next();
    		players.add(new Player(playerName, "Human"));
    	}
    	
<<<<<<< HEAD
    	for(int i=1;i<=nPlayerCPU;i++) players.add(new Player(String.valueOf(i), "Bot"));    	
=======
    	for(int i=1;i<=nPlayerCPU;i++) players.add(new Player("Bot"+String.valueOf(i), "Bot"));    	
>>>>>>> felixienny
    }
    private void instantiatePlayArea()
    {
    	System.out.println("Hauteur de la zone de jeu ?");
		int height = ShapUp.scanner.nextInt();
		System.out.println("Largeur de la zone de jeu ?");
		int width = ShapUp.scanner.nextInt();
		
		grid = new Grid(width, height);
		deck = new Deck(width*height+players.size());
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