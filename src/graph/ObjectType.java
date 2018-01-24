package graph;

public enum ObjectType {
    Snippet,
    Bomb,
    TickingBomb,
    Enemy,
    Player;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
