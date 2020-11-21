package projet_ShapUp_LO02.src.projet_ShapUp_LO02;
import java.util.Scanner;

public class PlayerH extends Player {
	public PlayerH(String name) {super(name);}
	
	public void askMove(Card card) {
		// this.display()
		Scanner scannerChoix = new Scanner(System.in);
		int choix;
		do {
			System.out.println("Que veux-tu faire :");
			System.out.println("1 - Jouer la carte que tu as pioché");
			System.out.println("2 - Déplacer une carte déjà sur le jeu");
			choix = scannerChoix.nextInt();
		} while (!(choix==1 || choix==2));
		if (choix==1) {
			int x;
			int y;
			do {
				System.out.println("Où veux-tu la jouer ?");
				String choixPlace = scannerChoix.next().trim();
				
				while (!choixPlace.matches("^[0-9],[0-9]$")) {
					System.out.println("Mauvaise saisie, où veux-tu la jouer ?");
					System.out.println("Exemple : 1,7");
					choixPlace = scannerChoix.next().trim();
				}
				x = Integer.valueOf(choixPlace.split(",")[0]);
				y = Integer.valueOf(choixPlace.split(",")[1]);
			} while (this.playingGridAdress.isFreeToPlaceACardOn(x, y));
			this.playingGridAdress.setTile(x, y, card);
		}
		else if (choix==2) {
			// this.display();
			int xSrc;
			int ySrc;
			int xDest;
			int yDest;
			do { 
				System.out.println("Quelle carte veux-tu déplacer ?");
				String choixMove = scannerChoix.next().trim();
				
				while (!choixMove.matches("^[0-9],[0-9]:[0-9],[0-9]$")) {
					System.out.println("Mauvaise saisie, quel carte veux-tu déplacer ?");
					System.out.println("Exemple : 1,3:6,7");
					choixMove = scannerChoix.next().trim();
				}
				xSrc = Integer.valueOf(choixMove.split(":")[0].split(",")[0]);
				ySrc = Integer.valueOf(choixMove.split(":")[0].split(",")[1]);
				xDest = Integer.valueOf(choixMove.split(":")[1].split(",")[1]);
				yDest = Integer.valueOf(choixMove.split(":")[1].split(",")[1]);
			} while(!this.playingGridAdress.isFreeToPlaceACardOn(xSrc, ySrc) && this.playingGridAdress.isFreeToPlaceACardOn(xDest, yDest));
			this.playingGridAdress.setTile(xDest, yDest, this.playingGridAdress.getTile(xSrc, ySrc));
			// TODO Remove the card placed before
		}
		scannerChoix.close();
	}
	
	public void display() {
		int width = this.playingGridAdress.getWidth();
		int height = this.playingGridAdress.getHeight();
		String ANSI_BLACK  = "\u001B[30m";
		String ANSI_RESET  = "\u001B[0m";
		String ANSI_BG_RED    = "\u001B[41m";
		String ANSI_BG_GREEN  = "\u001B[42m";
		String ANSI_BG_BLUE   = "\u001B[44m";
		for(int h=0; h<=height; h++) {
			for(int w=0; w<=width; w++) {
				System.out.print(" ");
				if(!this.playingGridAdress.isFreeToPlaceACardOn(w, h)) {
					Card card = this.playingGridAdress.getTile(w, h);

					if(card.getHollow()) {
						System.out.print(ANSI_BLACK);
					}
					else {						
						System.out.print(ANSI_RESET);
					}

					switch (card.getColor()) {
					case RED:
						System.out.print(ANSI_BG_RED);
						break;
					case GREEN:
						System.out.print(ANSI_BG_GREEN);
						break;
					case BLUE:
						System.out.print(ANSI_BG_BLUE);
						break;
					default:
						break;
					}

					switch (card.getShape()) {
					case CIRCLE:
						System.out.print(" â€¢ ");
						break;
					case TRIANGLE:
						System.out.print(" â–² ");
						break;
					case SQUARE:
						System.out.print(" â–  ");
						break;
					default:
						break;
					}
					
				}
				else{
					System.out.print("   ");
				}

				System.out.print(ANSI_RESET + " ");
			}		  
			System.out.println("\n");
		}
		System.out.println(ANSI_RESET);
	}
	
	// public static void main(String args[]) {
	// 	int width = 20; //this.playingGridAdress.getWidth();
	// 	int height = 5;
	// 	Deck d = new Deck(178);
	// 	String ANSI_BLACK  = "\u001B[30m";
	// 	String ANSI_RESET  = "\u001B[0m";
	// 	String ANSI_BG_RED    = "\u001B[41m";
	// 	String ANSI_BG_GREEN  = "\u001B[42m";
	// 	String ANSI_BG_BLUE   = "\u001B[44m";
	// 	for(int h=0; h<=height; h++) {
	// 		for(int w=0; w<=width; w++) {
	// 			System.out.print(" ");
	// 			// if(!this.playingGridAdress.isFreeToPlaceACardOn(w, h)) {
	// 				Card card = d.pickNextCard();
	// 				if(card.getHollow()) {
	// 					System.out.print(ANSI_BLACK);
	// 				}
	// 				else {						
	// 					System.out.print(ANSI_RESET);
	// 				}

	// 				switch (card.getColor()) {
	// 				case RED:
	// 					System.out.print(ANSI_BG_RED);
	// 					break;
	// 				case GREEN:
	// 					System.out.print(ANSI_BG_GREEN);
	// 					break;
	// 				case BLUE:
	// 					System.out.print(ANSI_BG_BLUE);
	// 					break;
	// 				default:
	// 					break;
	// 				}

	// 				switch (card.getShape()) {
	// 				case CIRCLE:
	// 					System.out.print(" â€¢ ");
	// 					break;
	// 				case TRIANGLE:
	// 					System.out.print(" â–² ");
	// 					break;
	// 				case SQUARE:
	// 					System.out.print(" â–  ");
	// 					break;
	// 				default:
	// 					break;
	// 				}
					
	// 			// }
	// 			// else{
	// 			// 	System.out.print("   ");
	// 			// }

	// 			System.out.print(ANSI_RESET + " ");
	// 		}		  
	// 		System.out.println("\n");
	// 	}
	// 	System.out.println(ANSI_RESET);
	// }
	// }
}