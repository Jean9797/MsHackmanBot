package graph;

import field.Field;
import move.MoveType;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Graph {
    private Field field;
    private HashMap<Point, Vertex> vertices;

    public Graph(Field field) {
        this.vertices = new LinkedHashMap<>();
        this.field = field;
        buildBasicGraph();
    }

    public void addVertex(Vertex v){
        this.vertices.put(v.getPosition(), v);
    }

    public HashMap<Point, Vertex> getVertices() {
        return this.vertices;
    }

    public Vertex getVertexAtPosition(Point position) {
        return this.vertices.get(position);
    }

    public Field getField() {
        return field;
    }

    public void clearGraphToBasic() {
        this.vertices.forEach(((point, vertex) -> {
            vertex.clearVertexToBasic();
        }));
    }

    private Graph buildBasicGraph(){
        int height = this.field.getHeight();
        int width = this.field.getWidth();
        for (int j = 0; j < height; j++){
            for (int i = 0; i < width; i++){
                Point currentPosition = new Point(i, j);
                if (this.field.isPointValid(currentPosition)){
                    Vertex vertex = new Vertex(currentPosition);
                    vertex.setWeight(1);    //normal distance between vertices
                    addVertex(vertex);
                }
            }
        }

        this.vertices.forEach(((point, vertex) -> {
            for (MoveType moveType : this.field.getPositionValidMoveTypes(point)){
                Point position = MoveType.getPointAfterMove(vertex.getPosition(), moveType);
                vertex.addNeighbour(getVertexAtPosition(position));
            }
        }));

        //we also have to add tunnel
        Vertex oneSide = getVertexAtPosition(new Point(0, 7));
        Vertex anotherSide = getVertexAtPosition(new Point(18, 7));
        oneSide.addNeighbour(anotherSide);
        anotherSide.addNeighbour(oneSide);

        return this;
    }

    public void addPresentObjectsOnTheField(){
        clearGraphToBasic();
        for (Point position : field.getEnemyPositions()){
            this.vertices.get(position).addVertexContent(ObjectType.Enemy);
        }

        for (Point position : field.getTickingBombPositions()){
            this.vertices.get(position).addVertexContent(ObjectType.TickingBomb);
        }

        for (Point position : field.getBombPositions()){
            this.vertices.get(position).addVertexContent(ObjectType.Bomb);
        }

        for (Point position : field.getSnippetPositions()){
            this.vertices.get(position).addVertexContent(ObjectType.Snippet);
        }

        this.vertices.get(field.getOpponentPosition()).addVertexContent(ObjectType.Player);
    }

    public void addWeightToVertex(Point position, int weight) {
        this.vertices.get(position).addWeight(weight);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        vertices.forEach(((point, vertex) -> {
            builder.append(vertex.toString() + "\n");
        }));

        return builder.toString();
    }

}