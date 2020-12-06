package core;

import java.util.ArrayList;

class StrategyHuman extends Strategy {

	public void makeBestMove(ArrayList<Card> victoryCards) {
		System.out.print("Tes cartes sont : ");
		victoryCards.forEach(card -> System.out.print(" "+card));
		System.out.println("");

		this.actualGrid.display();
		
		boolean choice1done=false;
		boolean turnFinished=false;
		
		String choice;

		do {
			if (!choice1done) {
				System.out.println("\t1:0,1 - Poser une de tes cartes victoires sur le jeu sur le jeu");
			}
			else {
				this.actualGrid.display();
				System.out.println("\t1,2:2,1 - Déplacer une carte déjà sur le jeu");
				System.out.println("\t3 - Finir ton tour");
			}
			
			choice = ShapUp.scanner.next().trim();

			if (choice.matches("^3$")) turnFinished = true;

			else if (choice.matches("^[0-2]:(-1|[0-9]),(-1|[0-9])$")) {
				if (this.actualGrid.testSettingTile(choice.split(":")[1])) {
					this.actualGrid.setTile(choice.split(":")[1], victoryCards.remove((int) Integer.valueOf(choice.split(":")[0])));
					choice1done = true;
				}
			}

			else if (choice.matches("^[0-9],[0-9]:(-1|[0-9]),(-1|[0-9])$")) {
				if (this.actualGrid.moveTile(choice)) {
					this.actualGrid.moveTile(choice);
					turnFinished = true;
				}
			}

		} while(!(choice1done && turnFinished));
	}

	public void makeBestMove(Card victoryCard, Card cardToPlace) {
		System.out.println("Ta carte victoire est "+victoryCard+".");
		
		this.actualGrid.display();
		System.out.print("\nTa carte piochée est : ");
		System.out.println(cardToPlace.toString());
		System.out.println();
		
		boolean choice1done=false;
		boolean choice2done=false;
		boolean turnFinished=false;
		
		String choice;

		do {
			this.actualGrid.display();

			if (!choice1done) {
				System.out.println("\t0,1 - Poser ta carte sur le jeu");
				System.out.println("\t1,2:2,1 - Déplacer une carte déjà sur le jeu");
			}
			else if (choice1done && !choice2done) {
				System.out.println("\t1,2:2,1 - Déplacer une carte déjà sur le jeu");
				System.out.println("\tq - Finir ton tour");
			}
			else {
				turnFinished=true;
			}
			
			choice = ShapUp.scanner.next().trim();

			if (choice.matches("^q$") && choice1done) turnFinished = true;

			else if (choice.matches("^(-1|[0-9]),(-1|[0-9])$") && !choice1done) {
				if (this.actualGrid.testSettingTile(choice)) {
					this.actualGrid.setTile(choice, cardToPlace);
					choice1done = true;
				}
			}

			else if (choice.matches("^[0-9],[0-9]:(-1|[0-9]),(-1|[0-9])$") && !choice2done) {
				if (this.actualGrid.moveTile(choice)) {
					this.actualGrid.moveTile(choice);
					choice2done = true;
				}
			}
		} while(!(choice1done && turnFinished));
	}
}