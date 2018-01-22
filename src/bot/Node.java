package bot;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private BotState state;
    private boolean isMaxPlayer;
    private int score;
    private List<Node> children;

    public Node(BotState state, boolean isMaxPlayer){
        this.state = state;
        this.isMaxPlayer = isMaxPlayer;
        this.children = new ArrayList<>();
    }

    public void setScore(int score){
        this.score = score;
    }

    public void addChild(Node child){
        this.children.add(child);
    }

    public BotState getState(){
        return this.state;
    }

    public boolean getIsMaxPlayer(){
        return this.isMaxPlayer;
    }

    public int getScore(){
        return this.score;
    }

    public List<Node> getChildren(){
        return this.children;
    }
}
