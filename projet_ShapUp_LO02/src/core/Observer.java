package core;
/**
 * Implements by the observers of the model in the MVC pattern.
 * This unique method can notify of any change in the game and is handled by the GameMaster.
 */
public interface Observer {
    public void update(GameMaster gameMaster, Update arg);
}
