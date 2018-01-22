package bot;

import move.Move;

public class BugsStage {
    private BotState state;
    private MiniMax algorithm;

    public Move getMove(BotState state){
        Node node = new Node(state, true);
        Tree gameTree = new Tree(node);
        this.algorithm = new MiniMax(gameTree);

        return new Move();
    }


}
