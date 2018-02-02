package bot;

import field.Field;
import graph.DijkstraAlgorithm;
import graph.Graph;
import graph.ObjectType;
import graph.Vertex;
import move.Move;
import move.MoveType;
import player.Player;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class BugStage {

    public Move getMove(BotState state){
        Field field = state.getField();
        Graph myGraph = new Graph(field);
        myGraph.addPresentObjectsOnTheField();
        DijkstraAlgorithm.compute(field.getMyPosition(), myGraph);

        Graph opponentGraph = new Graph(field);
        opponentGraph.addPresentObjectsOnTheField();
        DijkstraAlgorithm.compute(field.getOpponentPosition(), opponentGraph);

        int myBestDistance = 50000;
        Point myBestTarget = null;

        for (Point snippetPosition : field.getSnippetPositions()){
            int myCurrentDistance = myGraph.getVertexAtPosition(snippetPosition).getDistanceToVertex();
            int opponentCurrentDistance = opponentGraph.getVertexAtPosition(snippetPosition).getDistanceToVertex();
            if (myCurrentDistance < myBestDistance && myCurrentDistance < opponentCurrentDistance){
                myBestDistance = myCurrentDistance;
                myBestTarget = snippetPosition;
            }
        }

        for (Point bombPosition : field.getBombPositions()){
            int myCurrentDistance = myGraph.getVertexAtPosition(bombPosition).getDistanceToVertex();
            int opponentCurrentDistance = opponentGraph.getVertexAtPosition(bombPosition).getDistanceToVertex();
            if (myCurrentDistance < myBestDistance && myCurrentDistance < opponentCurrentDistance){
                myBestDistance = myCurrentDistance;
                myBestTarget = bombPosition;
            }
        }

        //if we have't good snippet or bomb we choose closest one
        if (myBestTarget == null){
            for (Point snippetPosition : field.getSnippetPositions()){
                int myCurrentDistance = myGraph.getVertexAtPosition(snippetPosition).getDistanceToVertex();
                if (myCurrentDistance < myBestDistance){
                    myBestDistance = myCurrentDistance;
                    myBestTarget = snippetPosition;
                }
            }

            for (Point bombPosition : field.getBombPositions()){
                int myCurrentDistance = myGraph.getVertexAtPosition(bombPosition).getDistanceToVertex();
                if (myCurrentDistance < myBestDistance){
                    myBestDistance = myCurrentDistance;
                    myBestTarget = bombPosition;
                }
            }
        }

        if (myBestTarget == null){
            myBestTarget = new Point(0, 7);
        }

        if (field.getMyPosition().equals(myBestTarget)){
            return specialStagnateSituation(field);
        }

        //we go back by the path to out position to figure out what move type chose
        Vertex firstVertex = myGraph.getVertexAtPosition(myBestTarget);
        Vertex secondVertex = firstVertex.getParent();
        while (secondVertex.getParent() != null){
            firstVertex = secondVertex;
            secondVertex = secondVertex.getParent();
        }

        MoveType myBestMoveType = MoveType.convertPointsToMoveType(secondVertex.getPosition(), firstVertex.getPosition());

        if (checkIfDropBomb(state, myGraph)) return new Move(myBestMoveType, 2);

        return new Move(myBestMoveType);
    }

    private Move specialStagnateSituation(Field field){
        Point p1 = closestBugPosition(field, field.getMyPosition());
        if (p1 != null) {
            if (field.getShortestDistance(p1, field.getMyPosition()) == 1) {
                return new Move(MoveType.LEFT);
            }
        }
        return new Move();
    }

    private Point closestBugPosition(Field field, Point position){
        Point closestBugPosition = null;
        int minimalDistance = 500;
        for (Point bugPosition : field.getEnemyPositions()){
            int currentDistance = field.getShortestDistance(bugPosition, position);
            if (currentDistance < minimalDistance){
                minimalDistance = currentDistance;
                closestBugPosition = bugPosition;
            }
        }
        return closestBugPosition;
    }

    private boolean checkIfDropBomb(BotState state, Graph graph){
        Field field = state.getField();
        Player me = state.getPlayers().get(state.getMyName());
        if (me.getBombs() < 1) return false;

        Vertex myVertex = graph.getVertexAtPosition(field.getMyPosition());
        List<MoveType> moveTypes = field.getValidMoveTypes();

        if (!checkIfThereIsSomeoneInRange(field.getMyPosition(), field, moveTypes)) return false;

        return checkIfIsHide(myVertex, moveTypes, field, graph);
    }

    private boolean checkIfIsHide(Vertex vertex, List<MoveType> moveTypes, Field field, Graph graph){
        for (MoveType moveType : moveTypes){
            Point neighbour = MoveType.getPointAfterMove(vertex.getPosition(), moveType);
            if (!checkIfVertexIsClear(vertex.getVertexContent())) return false;
            for (MoveType neighbourMoveType : field.getPositionValidMoveTypes(neighbour)){
                if (neighbourMoveType != moveType && neighbourMoveType != MoveType.getOppositeMoveType(moveType)){
                    Vertex vertexNextToNeighbour = graph.getVertexAtPosition(MoveType.getPointAfterMove(neighbour, neighbourMoveType));
                    if (checkIfVertexIsClear(vertexNextToNeighbour.getVertexContent())) return true;
                }
            }
        }
        return false;
    }

    private boolean checkIfVertexIsClear(List<ObjectType> content){
        if (content.contains(ObjectType.Enemy) || content.contains(ObjectType.TickingBomb)) return false;
        return true;
    }

    private boolean checkIfThereIsSomeoneInRange(Point position, Field field, List<MoveType> moveTypes){
        List<Point> range = new ArrayList<>();
        range.add(position);
        for (MoveType moveType : moveTypes){
            Point point = MoveType.getPointAfterMove(position, moveType);
            while (field.isPointValid(point)){
                range.add(point);
                point = MoveType.getPointAfterMove(point, moveType);
            }
        }

        if (range.contains(field.getOpponentPosition())) return true;

        int i = 0;
        for (Point enemyPosition : field.getEnemyPositions()){
            if (range.contains(enemyPosition)) i++;
        }

        return i > 1;
    }
}
