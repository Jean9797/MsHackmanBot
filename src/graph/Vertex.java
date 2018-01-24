package graph;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Vertex {
    private List<ObjectType> vertexContent;
    private List<Vertex> neighbours;
    private Point position;
    private int weight;     //we interpret weight of the edge as weight of second vertex
    private int distanceToVertex;
    private Vertex parent;

    public Vertex(Point position){
        this.vertexContent = new ArrayList<>();
        this.neighbours = new ArrayList<>();
        this.position = position;
    }

    public List<ObjectType> getVertexContent() {
        return vertexContent;
    }

    public void addVertexContent(ObjectType content) {
        this.vertexContent.add(content);
    }

    public List<Vertex> getNeighbours() {
        return neighbours;
    }

    public void addNeighbour(Vertex neighbour) {
        this.neighbours.add(neighbour);
    }

    public Point getPosition() {
        return position;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void addWeight(int weight) {
        this.weight += weight;
    }

    public void clearVertexToBasic() {
        this.vertexContent.clear();
        this.weight = 1;
    }

    public int getDistanceToVertex() {
        return distanceToVertex;
    }

    public void setDistanceToVertex(int distanceToVertex) {
        this.distanceToVertex = distanceToVertex;
    }

    public Vertex getParent() {
        return parent;
    }

    public void setParent(Vertex parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vertex)) return false;
        Vertex vertex = (Vertex) o;
        return position.equals(vertex.position);
    }

    @Override
    public int hashCode() {

        return Objects.hash(position);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(getWeight() + " " + getDistanceToVertex() + " ");

        builder.append(position.toString() + " ");

        if (parent != null)
            builder.append(parent.getPosition().toString() + " ");

        for (ObjectType type : vertexContent){
            builder.append(type.toString() + " ");
        }
        return builder.toString();
    }
}
