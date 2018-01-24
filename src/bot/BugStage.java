package bot;

import field.Field;
import graph.DijkstraAlgorithm;
import graph.Graph;
import graph.Vertex;
import move.Move;
import move.MoveType;

import java.awt.*;

public class BugStage {
    private int myBestDistance = 50000;
    private Point myBestTarget = null;

    public Move getMove(Field field){
        Graph myGraph = new Graph(field);
        myGraph.addPresentObjectsOnTheField();
        DijkstraAlgorithm.compute(field.getMyPosition(), myGraph);

        Graph opponentGraph = new Graph(field);
        opponentGraph.addPresentObjectsOnTheField();
        DijkstraAlgorithm.compute(field.getOpponentPosition(), opponentGraph);

        myBestDistance = 50000;
        myBestTarget = null;

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

        //if we have't good snippet of bomb we choose closest one
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

        return new Move(myBestMoveType);
    }

    private Move specialStagnateSituation(Field field){
        Point p1 = closestBugPosition(field, field.getMyPosition());
        if (field.getShortestDistance(p1, field.getMyPosition()) == 1){
            return new Move(MoveType.LEFT);
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
}
