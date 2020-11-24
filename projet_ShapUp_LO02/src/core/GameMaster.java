package core;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameMaster {
//methods
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
	
	//setter
	
	//getter
	
	//job specific
	public void play()
	{
		for(int matchNumber=0;matchNumber<numberOfMatch;matchNumber++)//match loop
		{
			instantiatePlayArea();
			for(Player he : players) he.gameStarts(this.grid, this.deck.pickNextCard());//setup
		
			Iterator<Player> he2 = players.iterator();
			while(!this.grid.isFull())//game loop
			{
				Card tempPickedCard = this.deck.pickNextCard();
				Player currentPlayer = he2.next();
				//ShapUp.scanner.nextLine();//wait between turns ?
				currentPlayer.askMove(tempPickedCard);
				System.out.println(currentPlayer.getName()+"\n");
				for(Player she : players) System.out.print(she.getCurrentScore()+" ");
				System.out.println("\n");
				this.grid.display();
				
				if(he2.hasNext()==false) he2 = players.iterator();
			}
			
			for(Player he3 : players) he3.gameEnds();//end
		}
	}
    private void instantiatePlayers()
    {
    	for(int i=1;i<=nPlayerH;i++)
    	{
    		System.out.println("Nom du joueur humain n°"+String.valueOf(i)+" ?");
    		String playerName=ShapUp.scanner.next();
    		players.add(new PlayerH(playerName));
    	}
    	
    	for(int i=1;i<=nPlayerCPU;i++)
    		players.add(new PlayerCPU(String.valueOf(i)));
    	
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
	
    //public
    public List<Player> players = new ArrayList<Player> ();
    public Deck deck;
    public Grid grid;

}
