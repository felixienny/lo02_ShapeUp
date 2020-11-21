package projet_ShapUp_LO02;

public class PlayerH extends Player {
	public PlayerH(String name)
	{
		super(name);
	}
	
	public void askMove(Card Card) {
		
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
						System.out.print(" • ");
						break;
					case TRIANGLE:
						System.out.print(" ▲ ");
						break;
					case SQUARE:
						System.out.print(" ■ ");
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
	// 					System.out.print(" • ");
	// 					break;
	// 				case TRIANGLE:
	// 					System.out.print(" ▲ ");
	// 					break;
	// 				case SQUARE:
	// 					System.out.print(" ■ ");
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