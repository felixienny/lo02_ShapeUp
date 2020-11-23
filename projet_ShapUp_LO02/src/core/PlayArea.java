package core;
public class PlayArea {
	public PlayArea(int nPlayers)
	{
		System.out.println("Hauteur de la zone de jeu ?");
		int height = ShapUp.scanner.nextInt();
		System.out.println("Largeur de la zone de jeu ?");
		int width = ShapUp.scanner.nextInt();
		
		grid = new Grid(width, height);
		deck = new Deck(width*height+nPlayers);
	}
	
    public Deck deck;
    public Grid grid;

}
