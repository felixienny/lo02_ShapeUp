package core;

import java.util.Scanner;
/**
 * Provides a formatted input/output console observing a given Grid.
 *
 */
public class GameConsole implements Observer {
	public Scanner scanner = new Scanner(System.in);
	private GameMaster gameMaster;

    public GameConsole(GameMaster gameMaster){

		this.gameMaster = gameMaster;
		gameMaster.addObserver(this); 

        this.scanner = new Scanner(System.in);
	}
	
	public void update(GameMaster gameMaster, Update update) {
		switch (update) {
			case ALL:
				this.gameMaster.getGridAddress().display();
				break;
			case GRID:
				this.gameMaster.getGridAddress().display();
				break;
			case PLAYER:
				System.out.println("\n\n\nC'est au tour de "+this.gameMaster.getCurrentPlayer().getName()+" !");
				break;
			case TURN:
				System.out.println("\nREMPLISSAGE DU PLATEAU = "+(this.gameMaster.getGridAddress().getNumberOfPlacedCards()*100 / ((this.gameMaster.getGridAddress().getRealHeight()*(this.gameMaster.getGridAddress().getRealWidth())-this.gameMaster.getGridAddress().getNumberOfDeadTiles()))+"%"));
				System.out.println("\nTABLEAU DES SCORES : ");
				for(Player player : this.gameMaster.getPlayers()) System.out.println(player.getName() + " : " + player.getCurrentScore());
				System.out.println("\n\n\n\n\nNous commencons le tour "+this.gameMaster.getCurrentTurn());
				break;
			case MATCH:
				StringBuffer sb = new StringBuffer();
				System.out.println("\nTABLEAU DES SCORES AU BOUT DU MATCH "+this.gameMaster.getCurrentMatch()+" : ");
				for(Player player : this.gameMaster.getPlayers()) {
					sb.delete(0, sb.length());
					sb.append(player.getName());
					sb.append(" : ");
					player.getScores().forEach(score -> sb.append(" | "+score));
					sb.append(" | "+player.getFinalScore());
					System.out.println(sb.toString());
				}
				break;
			default:
				break;
		}
	}
	/**
	 * Starts the interactive prompt to give the constructing parameters to the Grid class.
	 */
    public Grid instantiateGrid() {
        boolean isAdvancedGame;
        int height, width;


		System.out.println("Hauteur de la zone de jeu ?");
		height = this.scanner.nextInt();
		System.out.println("Largeur de la zone de jeu ?");
		width = this.scanner.nextInt();
		
		System.out.println("Veux-tu jouer en mode avancé ? y/n");
		char choix = this.scanner.next().charAt(0);
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
			choiceGrid = this.scanner.nextInt();
		} while (!(choiceGrid>=1 && choiceGrid<=5));

		gridDynamic = null;	gridDiamond = null;	gridCircle = null;	gridTriangle = null; gridPerso = null;

		if (choiceGrid==2) return new Grid(width, height, false, isAdvancedGame, "diamond");
		else if (choiceGrid==3) return new Grid(width, height, false, isAdvancedGame, "circle");
		else if (choiceGrid==4) return new Grid(width, height, false, isAdvancedGame, "triangle");
		else if(choiceGrid==5) {
			String choiceDeadTile;
			StringBuffer deadTiles = new StringBuffer();
			do {
				System.out.println("Saisis les coordonnées où tu souhaites mettre des zones mortes (ex: 1,1 PUIS ENTREE 1,2)");
				System.out.println("Saisis la lettre q pour stopper ta saisie et valider");
				choiceDeadTile =  this.scanner.next().trim();
				if (choiceDeadTile.matches("^[0-9],[0-9]$")) deadTiles.append(choiceDeadTile+";");
			} while (!(choiceDeadTile.matches("^q$")));
			return new Grid(width, height, false, isAdvancedGame, deadTiles.toString());
        }
        else return new Grid(width, height, true, isAdvancedGame);
	}
}
