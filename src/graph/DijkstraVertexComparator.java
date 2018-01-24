package graph;

import java.util.Comparator;

public class DijkstraVertexComparator implements Comparator<Vertex> {

    @Override
    public int compare(Vertex v1, Vertex v2){
        return Integer.compare(v1.getDistanceToVertex(), v2.getDistanceToVertex());
    }
}
