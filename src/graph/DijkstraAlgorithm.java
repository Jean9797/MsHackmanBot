package graph;

import java.awt.*;
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
                if (neighbour.getDistanceToVertex() > current.getDistanceToVertex() + neighbour.getWeight()){
                    neighbour.setDistanceToVertex(current.getDistanceToVertex() + neighbour.getWeight());
                    neighbour.setParent(current);
                    if (priorityQueue.remove(neighbour))
                        priorityQueue.add(neighbour);
                }
            }
        }
    }
}
