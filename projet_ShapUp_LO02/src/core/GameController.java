package core;

public class GameController {
    private Graphical graphicalView;
    private Console consoleView;
    private GameMaster game;

    public GameController(GameMaster gameMaster, Console console, Graphical graphical) {
        this.game = gameMaster;
        this.consoleView = console;
        this.graphicalView = graphical;
    }

    public void askMove(Player player) {

    }
}
