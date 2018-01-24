/*
 * Copyright 2017 riddles.io (developers@riddles.io)
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 *     For the full copyright and license information, please view the LICENSE
 *     file that was distributed with this source code.
 */

package move;

import java.awt.*;

/**
 * move.MoveType
 *
 * All move types
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public enum MoveType {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    PASS;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

    public static Point getPointAfterMove(Point point, MoveType moveType){
        switch(moveType){
            case UP: return new Point(point.x, point.y - 1);
            case DOWN: return new Point(point.x, point.y + 1);
            case LEFT: return new Point(point.x - 1, point.y);
            case RIGHT: return new Point(point.x + 1, point.y);
            default: return point;
        }
    }

    public static MoveType convertPointsToMoveType(Point p1, Point p2){
        MoveType[] moveTypes = {MoveType.UP, MoveType.DOWN, MoveType.LEFT, MoveType.RIGHT};
        for (MoveType type : moveTypes){
            if (p2.equals(getPointAfterMove(p1, type))) return type;
        }

        throw new IllegalArgumentException("Given points are not close." + p1.toString() + p2.toString());
    }

    public static MoveType getOppositeMoveType(MoveType moveType){
        switch (moveType){
            case UP: return MoveType.DOWN;
            case DOWN: return MoveType.UP;
            case LEFT: return MoveType.RIGHT;
            case RIGHT: return MoveType.LEFT;
            default: return moveType;
        }
    }
}
