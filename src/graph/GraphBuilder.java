package graph;

import field.Field;
import move.MoveType;

import java.awt.*;

public class GraphBuilder {
    private Field field;
    private Graph graph;

    public GraphBuilder(Field field){
        this.field = field;
        this.graph = new Graph(this.field);
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
                    this.graph.addVertex(vertex);
                }
            }
        }

        graph.getVertices().forEach(((point, vertex) -> {
            for (MoveType moveType : this.field.getPositionValidMoveTypes(point)){
                Point position = MoveType.getPointAfterMove(vertex.getPosition(), moveType);
                vertex.addNeighbour(graph.getVertexAtPosition(position));
            }
        }));

        return this.graph;
    }

    public void addPossibleBugWeights(){

    }

}
