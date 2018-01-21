package bot;

import move.Move;
import move.MoveType;

import java.awt.*;

public class InitialStage {
    private BotState state;

    public Move getMove(BotState state){
        this.state = state;
        MoveType bestMoveType = MoveType.PASS;
        if (!state.getField().getSnippetPositions().isEmpty()){
            int distance = 500;
            for (Point snippetPosition : state.getField().getSnippetPositions()){
                int myDistance = 500;
                int opponentDistance = 500;
                MoveType myCurrentBestMoveType = MoveType.PASS;
                for (MoveType moveType : state.getField().getValidMoveTypes()){
                    int myNewDistance = distanceFromPlayerPosition(moveType, state.getField().getMyPosition(), snippetPosition);
                    if (myDistance > myNewDistance){
                        myDistance = myNewDistance;
                        myCurrentBestMoveType = moveType;
                    }
                }
                for (MoveType moveType : state.getField().getPositionValidMoveTypes(state.getField().getOpponentPosition())){
                    int opponentNewDistance = distanceFromPlayerPosition(moveType, state.getField().getOpponentPosition(), snippetPosition);
                    if (opponentDistance > opponentNewDistance){
                        opponentDistance = opponentNewDistance;
                    }
                }
                if (myDistance < opponentDistance && distance > myDistance){
                    distance = myDistance;
                    bestMoveType = myCurrentBestMoveType;
                }
            }

        }
        if (bestMoveType == MoveType.PASS){
            if (state.getField().getMyPosition().x > state.getField().getWidth() / 2){
                bestMoveType = bestMoveTypeWhenFieldIsEmpty(new Point(14, 7));
            }
            else {
                bestMoveType = bestMoveTypeWhenFieldIsEmpty(new Point(4, 7));
            }
        }
        return new Move(bestMoveType);
    }

    private MoveType bestMoveTypeWhenFieldIsEmpty(Point point){
        int distance = 500;
        MoveType bestMoveType = MoveType.PASS;
        for (MoveType moveType : state.getField().getValidMoveTypes()){
            int newDistance = distanceFromPlayerPosition(moveType, state.getField().getMyPosition(), point);
            if (distance > newDistance){
                distance = newDistance;
                bestMoveType = moveType;
            }
        }
        return bestMoveType;
    }

    private int distanceFromPlayerPosition(MoveType moveType, Point playerPosition, Point destination){
        return state.getField().getShortestDistance(getMovePoint(playerPosition, moveType), destination);
    }

    private Point getMovePoint(Point point, MoveType moveType){
        switch(moveType){
            case UP: return new Point(point.x + 1, point.y);
            case DOWN: return new Point(point.x - 1, point.y);
            case LEFT: return new Point(point.x, point.y - 1);
            case RIGHT: return new Point(point.x, point.y + 1);
            default: return point;
        }
    }

}
