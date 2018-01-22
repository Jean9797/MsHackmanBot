package bot;

import move.MoveType;
import player.Player;

import java.awt.*;

public class MiniMax {
    private Tree gameTree;

    public MiniMax(Tree gameTree){
        this.gameTree = gameTree;
    }

    public int alphaBeta(Node node, int depth, int alpha, int beta, boolean isMaxPlayer){
        if (depth == 0 || node.getState().getRoundNumber() == node.getState().getMaxRound()){
            return heuristicValueOfState(node.getState());
        }
        if (isMaxPlayer){
            int score = Integer.MIN_VALUE;
            node = buildChildrenWhenOnlyMyBotMoves(node);
            for (Node child : node.getChildren()){
                score = Integer.max(score, alphaBeta(child, depth - 1, alpha, beta, false));
                alpha = Integer.max(alpha, score);
                if (beta <= alpha){
                    break;  //we don't have to search anymore
                }
            }
            node.setScore(score);
            return score;
        }
        else {
            int score = Integer.MAX_VALUE;
            node = buildChildrenWhenOpponentAndBugsMoves(node);
            for (Node child : node.getChildren()){
                score = Integer.min(score, alphaBeta(child, depth - 1, alpha, beta, true));
                beta = Integer.min(beta, score);
                if (beta <= alpha){
                    break;  //we don't have to search anymore, becouse minimizer will always choose worse state of game than we have had already.
                }
            }
            node.setScore(score);
            return score;
        }
    }

    private Node buildChildrenWhenOnlyMyBotMoves(Node node){
        for (MoveType moveType : node.getState().getField().getValidMoveTypes()){
            BotState newState = node.getState().clone(node.getState());
            newState = newState.modifyStateWhenOnlyMyBotMoves(moveType);
            Node child = new Node(newState, false);
            node.addChild(child);
        }
        return node;
    }

    private Node buildChildrenWhenOpponentAndBugsMoves(Node node){
        for (MoveType moveType : node.getState().getField().getValidMoveTypes()){
            BotState newState = node.getState().clone(node.getState());
            newState = newState.modifyStateWhenOnlyOpponentBotMoves(moveType);
            newState = newState.modifyStateWhebBugsMove();
            Node child = new Node(newState, true);
            node.addChild(child);
        }
        return node;
    }

    private int heuristicValueOfState(BotState state){
        int score = 0;
        Player me = state.getPlayers().get(state.getMyName());
        if (me.getSnippets() < 0){  //if i die, i don't like this.
            return Integer.MIN_VALUE;
        }
        for (int i = 0; i < me.getSnippets(); i++){
            score += 100;
        }
        for (int i = 0; i < me.getBombs(); i++){
            score += 30;
        }

        int closestSnippetDistance = Integer.MAX_VALUE;
        for (Point snippetPosition : state.getField().getSnippetPositions()){
            int myDistanceToClosestSnippet = state.getField().getShortestDistance(state.getField().getMyPosition(), snippetPosition);
            int opponentDistanceToThatSnippet = state.getField().getShortestDistance(state.getField().getOpponentPosition(), snippetPosition);
            if (myDistanceToClosestSnippet < closestSnippetDistance && myDistanceToClosestSnippet < opponentDistanceToThatSnippet){
                closestSnippetDistance = myDistanceToClosestSnippet;
            }
        }
        if (closestSnippetDistance != Integer.MAX_VALUE){
            score += 100 - closestSnippetDistance * 2;
        }

        int closestBombDistance = Integer.MAX_VALUE;
        for (Point bombPosition : state.getField().getBombPositions()){
            int myDistanceToClosestBomb = state.getField().getShortestDistance(state.getField().getMyPosition(), bombPosition);
            int opponentDistanceToThatBomb = state.getField().getShortestDistance(state.getField().getOpponentPosition(), bombPosition);
            if (myDistanceToClosestBomb < closestBombDistance && myDistanceToClosestBomb < opponentDistanceToThatBomb){
                closestBombDistance = myDistanceToClosestBomb;
            }
        }
        if (closestBombDistance != Integer.MAX_VALUE){
            score += 30 - closestBombDistance;
        }

        return score;
    }
}
