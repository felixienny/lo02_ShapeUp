package core;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.swing.JButton;

/**
* This class provides an implementation of the Model View Controller pattern.
* It represents the Controller in the MVC.
*/
public class GameController {
    /**
     * An attribute pointing on the graphical view of the game.
     */
    private GameGraphical graphicalView;
    /**
     * An attribute pointing on the model of the game, the GameMaster object.
     */
    private GameMaster game;

    /**
     * Enters the addresses of the objects necessary for operation of the MVC.
     * @param gameMaster
     * @param graphical
     */
    public GameController(GameMaster gameMaster, GameGraphical graphical) {
        this.game = gameMaster;
        this.graphicalView = graphical;
    }
    /**
     * This private class serves the humans players's turns.
     * It is an implementation of Runnable in order to be a thread and ActionListener in order to process the clicks on the graphical view.
     * It gives also a console typing function.
     */
    private class TurnOfPlayerHuman implements Runnable, ActionListener {
        /**
        * The x value when user wants moving a card, in graphical by clicking on first card and then the second.
        */
        private int xTemp;
        /**
        * The y value when user wants moving a card, in graphical by clicking on first card and then the second.
        */
        private int yTemp;
        /**
        * A boolean to make sure that user don't move a card twice.
        */
        private boolean moveDone;
        /**
        * A boolean to make sure that user don't set a card twice.
        */
        private boolean setDone;
        /**
        * A boolean to know if player wants to finish his turn.
        */
        private boolean turnFinished;
        /**
        * A boolean, in Graphical to know if player wants to move a card or set a card.
        */
        private boolean wantToMove;
        /**
        * This attribute is for moving a card and know if user has clicked on the card to move.
        */
        private boolean oneClickAlreadyDone;
        /**
        * The player's hand of cards, in advanced or classic game it's the same.
        */
        private ArrayList<Card> playerHand;
        /**
        * For advanced game only, when player must choose a card to place.
        */
        private int vCardToUse;

        
        /**
        * The contructor of the class, taking in the player's hand of cards and place action listner on buttons wantToMove and turnFinished of graphical view object.
        */
        private TurnOfPlayerHuman(ArrayList<Card> playerHand) {
            this.setDone = false;
            this.moveDone = false;
            this.turnFinished = false;
            this.wantToMove = false;
            this.oneClickAlreadyDone = false;
            this.playerHand = playerHand;
            this.vCardToUse = -1;
            graphicalView.turnFinished.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    turnFinished = true;
                }
            });
            graphicalView.wantToMove.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    wantToMove = !wantToMove;
                    if (wantToMove) graphicalView.wantToMove.setBackground(java.awt.Color.GREEN);
                    else graphicalView.wantToMove.setBackground(java.awt.Color.LIGHT_GRAY);
                }
            });
        }

        /**
        * Method called to place action listeners on all tiles of the grid and, if the game is in advanced mode, to place listeners on victory cards of the current player.
        */
        public void addListenersOnTiles() {
            if (game.getGridAddress().isAdvancedGame()) {
                for (int i = 0; i < game.getCurrentPlayer().getPlayerHand().size(); i++) {
                    JButton button = (JButton) graphicalView.player.getComponent(i);
                    button.addActionListener(this);
                }
            }
            for (int i = 0; i < graphicalView.table.getComponentCount(); i++) {
                JButton button = (JButton) graphicalView.table.getComponent(i);
                button.addActionListener(this);
            }
        }

        /**
        * Function of the interface ActionListener, the class must implement, used to make action when the differents buttons are clicked in graphical.
        */
        public void actionPerformed(ActionEvent event) {
            if (game.getGridAddress().isAdvancedGame()) {
                for (int i = 0; i < game.getCurrentPlayer().getPlayerHand().size(); i++)
                    if (event.getSource() == graphicalView.player.getComponent(i))
                        vCardToUse = i;
            }

            JButton button = (JButton) event.getSource();
            int x = button.getY() / button.getHeight();
            int y = button.getX() / button.getWidth();
            if (this.wantToMove && !this.moveDone) {
                if (oneClickAlreadyDone) {
                    if (game.getGridAddress().moveTile(xTemp - 1, yTemp - 1, x - 1, y - 1)) {
                        this.moveDone = true;
                        game.notifyObservers(Update.GRID);
                        game.notifyObservers(Update.PLAYER);
                        this.addListenersOnTiles();
                    }
                } else {
                    xTemp = x;
                    yTemp = y;
                    oneClickAlreadyDone = true;
                }
            } else if (!this.setDone) {
                if (game.getGridAddress().isAdvancedGame()) {
                    if (this.vCardToUse != -1 && game.getGridAddress().testSettingTile(x - 1, y - 1)) {
                        game.getGridAddress().setTile(x - 1, y - 1, playerHand.remove(vCardToUse));
                        this.setDone = true;
                        game.notifyObservers(Update.GRID);
                        game.notifyObservers(Update.PLAYER);
                        this.addListenersOnTiles();
                    }
                } else {
                    if (game.getGridAddress().testSettingTile(x - 1, y - 1)) {
                        game.getGridAddress().setTile(x - 1, y - 1, playerHand.remove(1));
                        this.setDone = true;
                        game.notifyObservers(Update.GRID);
                        game.notifyObservers(Update.PLAYER);
                        this.addListenersOnTiles();
                    }
                }
            }
        }
        /**
        * Private class to run a thread for the console typing.
        */
        private class GameConsoleTyping implements Runnable {

            /**
             * Method called to get a String if user has typed something, else in returns a null String.
             * @return String the text typed
             */
            private String getTextTyped() {
                BufferedReader br = new BufferedReader (new InputStreamReader(System.in));
                String resultat = null;
                try {
                    System.out.print(">");
                    resultat = br.readLine();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
                return resultat;	
            }
            
            /**
             * Method called when this second thread is executed.
             * It updates the TurnOfPlayerHuman attributes in order to make changes as on the graphical view.
             * So the player can type text to play.
             */
            public void run() {
                while (!(turnFinished && setDone)) {
                    String text = this.getTextTyped();
                    if (text != null) {
                        if (!setDone) {
                            if (game.getGridAddress().isAdvancedGame()) {
                                if (text.matches("^[0-2]$")) vCardToUse = (int) Integer.valueOf(text);
                                if (vCardToUse != -1 && game.getGridAddress().testSettingTile(text)) {
                                    game.getGridAddress().setTile(text, playerHand.remove(vCardToUse));
                                    setDone = true;
                                    game.notifyObservers(Update.GRID);
                                    game.notifyObservers(Update.PLAYER);
                                    addListenersOnTiles();
                                }
                            }
                            else if (game.getGridAddress().testSettingTile(text)) {
                                game.getGridAddress().setTile(text, playerHand.remove(1));
                                setDone = true;
                                game.notifyObservers(Update.GRID);
                                game.notifyObservers(Update.PLAYER);
                                addListenersOnTiles();
                            }
                        }
                        if (!moveDone && game.getGridAddress().moveTile(text)) {
                            moveDone = true;
                            game.notifyObservers(Update.GRID);
                            addListenersOnTiles();
                        }
                        if (text.matches("^q$")) turnFinished = true;
                    }
                }
            }
        }

        
        /**
         * Method called when thread starts.
         * It runs until the player has finished the turn and at least set a card on the @see core.Grid.
         */
        public void run() {
            this.addListenersOnTiles();
            Thread gameConsoleTyping = new Thread(new GameConsoleTyping(),"GameConsoleThread");
            gameConsoleTyping.start();
            while (!(this.turnFinished && this.setDone)) {
                if (this.setDone && this.moveDone) this.turnFinished = true;
                try { Thread.sleep(10); } catch (InterruptedException e) { e.printStackTrace(); }
            }
            gameConsoleTyping.interrupt();
        }
    }

    /**
     * Used to handle the two potential inputs (Console & Graphical) for a human player. 
     * The main is waiting for this new thread to continue. 
     * @param playerHand Cards of the player to play his turn.
     */
    public void makeTurnOfPlayer(ArrayList<Card> playerHand) {
        Thread turn = new Thread(new TurnOfPlayerHuman(playerHand),"TurnThread");
        turn.start();
        try { turn.join(); } catch (InterruptedException e) { e.printStackTrace(); }
        turn = null;
    }
}