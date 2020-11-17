package projet_ShapUp_LO02;
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
		
		instantiateGameMembers();
	}
	
	public PlayArea playArea;
    public Tournoi tournoi;
    public List<Player> players = new ArrayList<Player> ();
    
	private int nPlayerH;
	private int nPlayerCPU;
	
	public void play()
	{
		for(Player he : players) he.victoryCard=this.playArea.deck.pickNextCard();
		Iterator<Player> he = players.iterator();
		while(!this.playArea.grid.isFull())
		{
			Card tempPickedCard = this.playArea.deck.pickNextCard();
			Player currentPlayer = he.next();
		
			currentPlayer.setPlayingGridAdress(this.playArea.grid);//XXX playingGridAdress is accessible ?!
			currentPlayer.askMove(tempPickedCard);
			
			if(he.hasNext()==false) he = players.iterator();
		}
		
	}
	
    private void instantiateGameMembers()
    {
    	for(int i=1;i<=nPlayerH;i++)
    	{
    		System.out.println("Nom du joueur humain n°"+String.valueOf(i)+" ?");
    		String playerName=ShapUp.scanner.next();
    		players.add(new PlayerH(playerName));
    	}
    	
    	for(int i=1;i<=nPlayerCPU;i++)
    		players.add(new PlayerCPU(String.valueOf(i)));
    	
    	playArea = new PlayArea(nPlayerH+nPlayerCPU);
    }

    
    
}
