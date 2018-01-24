package field;

import java.awt.*;

public class TickingBomb {
    private Point position;
    private int ticks;

    public TickingBomb(Point position, int ticks){
        this.position = position;
        this.ticks = ticks;
    }

    public Point getPosition() {
        return this.position;
    }

    public int getTicks() {
        return this.ticks;
    }
}
