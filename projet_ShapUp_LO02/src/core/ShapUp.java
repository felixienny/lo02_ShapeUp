package core;

import java.util.Scanner;

public class ShapUp {
	public static Scanner scanner = new Scanner(System.in);
    public static void main(String args[]) {
        
    	char playing;
    	
    	System.out.println("Bienvenue dans le jeu Shap'Up !");
		
    	do {
    		System.out.println("Lancer une partie ? y/n");
    		playing = scanner.next().charAt(0);
    		
    		if(playing=='y') {
    			GameMaster currentGame = new GameMaster();
    			currentGame.play();
    			currentGame=null;
    		}
    	}while(playing!='n');
    	
    	scanner.close();
    }
}
