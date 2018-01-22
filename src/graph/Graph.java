package graph;

import java.awt.*;
import java.util.HashMap;

public class Graph {
    private HashMap<Point, Vertex> vertices;

    public Graph(){
        this.vertices = new HashMap<>();
    }

    public void addVertex(Vertex v){
        this.vertices.put(v.getCoords(), v);
    }

    public HashMap<Point, Vertex> getVertices(){
        return this.vertices;
    }

}