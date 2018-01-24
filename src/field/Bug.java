package field;

import move.MoveType;

import java.awt.*;

public class Bug {
    private Point position;
    private MoveType direction = null;
    private char type;

    public Bug(Point position, char type){
        this.position = position;
        this.type = type;
    }

    public MoveType getDirection() {
        return this.direction;
    }

    public void setDirection(MoveType direction) {
        this.direction = direction;
    }

    public Point getPosition() {
        return this.position;
    }

    public char getType() {
        return type;
    }
}
