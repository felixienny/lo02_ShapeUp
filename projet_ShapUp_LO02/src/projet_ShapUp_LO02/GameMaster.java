package projet_ShapUp_LO02;
import java.util.ArrayList;
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
    public List<Player> player = new ArrayList<Player> ();
    
	private int nPlayerH;
	private int nPlayerCPU;
	
	public void play()
	{
		
	}
	
    private void instantiateGameMembers()
    {
    	for(int i=0;i<nPlayerH;i++)
    	{
    		System.out.println("Nom du joueur humain n°"+String.valueOf(i)+" ?");
    		String playerName=ShapUp.scanner.next();
    		player.add(new PlayerH(playerName));
    	}
    	
    	for(int i=0;i<nPlayerCPU;i++)
    		player.add(new PlayerCPU(String.valueOf(i+1)));
    	
    }

    
    
}
