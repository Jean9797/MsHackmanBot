package graph;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Vertex {
    private List<ObjectType> vertexContent;
    private List<Vertex> neighbours;
    private Point coords;

    public Vertex(Point coords){
        this.vertexContent = new ArrayList<>();
        this.neighbours = new ArrayList<>();
        this.coords = coords;
    }

    public List<ObjectType> getVertexContent() {
        return vertexContent;
    }

    public void setVertexContent(List<ObjectType> vertexContent) {
        this.vertexContent = vertexContent;
    }

    public List<Vertex> getNeighbours() {
        return neighbours;
    }

    public void addNeighbour(Vertex neighbour) {
        this.neighbours.add(neighbour);
    }

    public Point getCoords() {
        return coords;
    }
}
