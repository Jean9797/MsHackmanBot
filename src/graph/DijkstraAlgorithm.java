package graph;

import field.Bug;
import field.Field;
import field.TickingBomb;
import move.MoveType;


import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class DijkstraAlgorithm {
    public static void compute(Point position, Graph graph){
        Vertex startingVertex = graph.getVertexAtPosition(position);

        PriorityQueue<Vertex> priorityQueue = new PriorityQueue<>(graph.getVertices().size(), new DijkstraVertexComparator());

        //initialization
        graph.getVertices().forEach(((point, vertex) -> {
            vertex.setDistanceToVertex(50000);
            vertex.setParent(null);
            priorityQueue.add(vertex);
        }));

        startingVertex.setDistanceToVertex(0);
        if (priorityQueue.remove(startingVertex))
            priorityQueue.add(startingVertex);

        while (!priorityQueue.isEmpty()){
            Vertex current = priorityQueue.poll();
            for (Vertex neighbour : current.getNeighbours()){
                if (!priorityQueue.contains(neighbour)) continue;
                int newDistance = current.getDistanceToVertex() + getNeighbourWeight(current, neighbour, graph.getField());
                if (neighbour.getDistanceToVertex() > newDistance){
                    neighbour.setDistanceToVertex(newDistance);
                    neighbour.setParent(current);
                    if (priorityQueue.remove(neighbour))
                        priorityQueue.add(neighbour);
                }
            }
        }
    }

    //here we create edge weight. We create it dynamically
    private static int getNeighbourWeight(Vertex current, Vertex neighbour, Field field){
        int weight = 1; //basic weight

        //we check direction of bug
        if (neighbour.getVertexContent().contains(ObjectType.Enemy)){
            MoveType moveType = MoveType.convertPointsToMoveType(current.getPosition(), neighbour.getPosition());
            for (Bug bug : field.getBugs()){
                if (bug.getPosition().equals(neighbour.getPosition())) {
                    if (bug.getDirection() == moveType) {
                        continue;
                    }
                    weight += 80;
                }
            }
        }

        //for each bug that will come on that position in next round
        for (int i = 0; i < getNumberOfDirectedNeighbourBugs(neighbour.getPosition(), field); i++) weight += 80;

        //for each explosion in time we add another weight
        for (int i = 0; i < getNumberOfExplosionsAtVertexAffectedByTickingBomb(current, neighbour, field); i++) weight += 80;

        return weight;
    }

    private static int getNumberOfExplosionsAtVertexAffectedByTickingBomb(Vertex current, Vertex vertex, Field field){
        int numberOfExplosions = 0;
        for (TickingBomb bomb : field.getTickingBombs()){
            if (field.getShortestDistance(current.getPosition(), vertex.getPosition()) != bomb.getTicks()) continue;      //we will not be on the place in eruption time
            if (getPositionsAffectedByBomb(bomb.getPosition(), field).contains(vertex.getPosition())) numberOfExplosions++;
        }
        return numberOfExplosions;
    }

    private static List<Point> getPositionsAffectedByBomb(Point position, Field field){
        List<Point> positions = new ArrayList<>();
        positions.add(position);
        for (MoveType moveType : field.getPositionValidMoveTypes(position)){
            Point current = MoveType.getPointAfterMove(position, moveType);
            while(field.isPointValid(current)){
                positions.add(current);
                current = MoveType.getPointAfterMove(current, moveType);
            }
        }
        return positions;
    }

    private static int getNumberOfDirectedNeighbourBugs(Point position, Field field){
        int numberOfImportantBugs = 0;

        List<Point> neighbourPositions = new ArrayList<>();

        field.getPositionValidMoveTypes(position).forEach(moveType -> {
            neighbourPositions.add(MoveType.getPointAfterMove(position, moveType));
        });

        for (Bug bug : field.getBugs()){
            if (bug.getDirection() != null && neighbourPositions.contains(bug.getPosition())){
                if (position.equals(MoveType.getPointAfterMove(bug.getPosition(), bug.getDirection()))) {
                    numberOfImportantBugs++;        //if bug is going to go on the given position we catch this event
                }
                else if (checkIfBugIsGoingToPosition(bug, position, field)){
                    numberOfImportantBugs++;
                }
            }
        }

        return numberOfImportantBugs;
    }

    private static boolean checkIfBugIsGoingToPosition(Bug bug, Point position, Field field){
        List<MoveType> moveTypes = field.getPositionValidMoveTypes(bug.getPosition());
        if (moveTypes.size() != 2) return false;

        //we throw away direction from which bug came
        MoveType reverseDirection = MoveType.getOppositeMoveType(bug.getDirection());
        if (reverseDirection == moveTypes.get(0)) moveTypes.remove(0);
        else moveTypes.remove(1);

        return MoveType.getPointAfterMove(bug.getPosition(), moveTypes.get(0)).equals(position);
    }
}
