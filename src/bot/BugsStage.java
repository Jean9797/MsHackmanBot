package bot;

import move.Move;
import move.MoveType;

import java.awt.*;

public class BugsStage {
    private BotState state;
    private MiniMax algorithm;

    public Move getMove(BotState state){
        Node node = new Node(state, true);
        Tree gameTree = new Tree(node);
        this.algorithm = new MiniMax(gameTree);

        int bestScore = algorithm.alphaBeta(node, 6, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        Node bestChild = node.getChildren().get(0);
        for (Node child : node.getChildren()){
            if (child.getScore() == bestScore)
                bestChild = child;
        }

        Point myNewPosition = bestChild.getState().getField().getMyPosition();
        Point myOldPosition = state.getField().getMyPosition();
        return new Move(MoveType.convertPointsToMoveType(myOldPosition, myNewPosition));
    }
}
