package bot;

import field.Field;
import graph.DijkstraAlgorithm;
import graph.Graph;
import graph.Vertex;
import move.Move;
import move.MoveType;

import java.awt.*;

public class BugStage {
    public Move getMove(Field field){
        Graph myGraph = new Graph(field);
        myGraph.addPresentObjectsOnTheField();
        DijkstraAlgorithm.compute(field.getMyPosition(), myGraph);
        System.out.println(myGraph.toString());

        Graph opponentGraph = new Graph(field);
        opponentGraph.addPresentObjectsOnTheField();
        DijkstraAlgorithm.compute(field.getOpponentPosition(), opponentGraph);

        int myBestDistance = 500;
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

        if (myBestTarget == null){
            myBestTarget = new Point(0, 7);
        }

        //we go back by the path to out position to figure out what move type chose
        Vertex firstVertex = myGraph.getVertexAtPosition(myBestTarget);
        Vertex secondVertex = firstVertex.getParent();
        while (secondVertex.getParent() != null){
            firstVertex = secondVertex;
            secondVertex = secondVertex.getParent();
        }

        //we have to select tunnel case
        if (secondVertex.getPosition().equals(new Point(0, 7)) && firstVertex.getPosition().equals(new Point(18, 7))){
            return new Move(MoveType.LEFT);
        }
        if (secondVertex.getPosition().equals(new Point(18, 7)) && firstVertex.getPosition().equals(new Point(0, 7))){
            return new Move(MoveType.RIGHT);
        }

        MoveType myBestMoveType = MoveType.convertPointsToMoveType(secondVertex.getPosition(), firstVertex.getPosition());

        return new Move(myBestMoveType);
    }
}
