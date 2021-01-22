package core;

/**
 * Defines a class that can accept an object of type Tile. Used in the {@link core.Visitor} class.
 */
public interface Visitor {
    public void visit(Tile currentTile);
}
