package core;
/**
 * Defines a class that can keep a reference to a GameMaster object in order to be notified of any change in the game handled by the GameMaster.
 *
 */
public interface Observer {
    public void update(GameMaster gameMaster, Update arg);
}
