/*
 * Copyright 2017 riddles.io (developers@riddles.io)
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 *     For the full copyright and license information, please view the LICENSE
 *     file that was distributed with this source code.
 */

package bot;

import java.awt.*;
import java.util.HashMap;
import java.util.function.Predicate;

import field.Field;
import move.MoveType;
import player.Player;

/**
 * bot.BotState
 *
 * [description]
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class BotState {
    private int MAX_TIMEBANK;
    private int TIME_PER_MOVE;
    private int MAX_ROUNDS;

    private int roundNumber;
    private int timebank;
    private String myName;
    private String opponentName;
    private HashMap<String, Player> players;

    private Field field;


    BotState() {
        this.field = new Field();
        this.players = new HashMap<>();
    }

    private BotState(Field field){
        this.field = field;
        this.players = new HashMap<>();
    }

    public void setTimebank(int value) {
        this.timebank = value;
    }

    public void setMaxTimebank(int value) {
        this.MAX_TIMEBANK = value;
    }

    public void setTimePerMove(int value) {
        this.TIME_PER_MOVE = value;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public void setMaxRounds(int value) {
        this.MAX_ROUNDS = value;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public int getTimebank() {
        return this.timebank;
    }

    public int getRoundNumber() {
        return this.roundNumber;
    }

    public HashMap<String, Player> getPlayers() {
        return this.players;
    }

    public Field getField() {
        return this.field;
    }

    public String getMyName() {
        return this.myName;
    }

    public int getMaxTimebank() {
        return this.MAX_TIMEBANK;
    }

    public int getTimePerMove() {
        return this.TIME_PER_MOVE;
    }

    public int getMaxRound() {
        return this.MAX_ROUNDS;
    }

    public BotState clone(BotState stateToClone){
        BotState newState = new BotState(stateToClone.field.clone(stateToClone.field));     //cloning field
        newState.MAX_TIMEBANK = stateToClone.MAX_TIMEBANK;
        newState.timebank = stateToClone.timebank;
        newState.TIME_PER_MOVE = stateToClone.TIME_PER_MOVE;
        newState.MAX_ROUNDS = stateToClone.MAX_ROUNDS;
        newState.roundNumber = stateToClone.roundNumber;
        newState.myName = stateToClone.myName;
        stateToClone.players.forEach((k, v) -> newState.players.put(k, v.clone(v)));    //cloning players
        return newState;
    }

    public BotState modifyStateWhenOnlyMyBotMoves(MoveType moveType){
        Point myOldPosition = this.field.getMyPosition();
        Point myNewPosition = MoveType.getPointAfterMove(myOldPosition, moveType);
        if (this.field.isPointValid(myNewPosition)){
            String myNewCell = this.field.getField()[myNewPosition.x][myNewPosition.y];
            int[] content = processCell(myNewCell);
            Player me = this.players.get(this.myName);
            Predicate<Point> pointPredicate = p -> p.x == myNewPosition.x && p.y == myNewPosition.y;
            processContent(content, me, pointPredicate);
        }
        return this;
    }

    public BotState modifyStateWhenOnlyOpponentBotMoves(MoveType moveType){
        Point opponentOldPosition = this.field.getOpponentPosition();
        Point opponentNewPosition = MoveType.getPointAfterMove(opponentOldPosition, moveType);
        if (this.field.isPointValid(opponentNewPosition)){
            String opponentNewCell = this.field.getField()[opponentNewPosition.x][opponentNewPosition.y];
            int[] content = processCell(opponentNewCell);
            Player opponent = this.players.get(this.opponentName);
            Predicate<Point> pointPredicate = p -> p.x == opponentNewPosition.x && p.y == opponentNewPosition.y;
            processContent(content, opponent, pointPredicate);
        }
        return this;
    }

    private void processContent(int[] content, Player player, Predicate<Point> pointPredicate){
        if (content[0] > 0){                //we modify numer of snippets
            player.setSnippets(player.getSnippets() + content[0]);
            this.field.getSnippetPositions().removeIf(pointPredicate);
        }
        if (content[1] > 0){
            player.setSnippets(player.getSnippets() - 4 * content[1]);
            this.field.getEnemyPositions().removeIf(pointPredicate);
        }
        if (content[2] > 0){
            player.setBombs(player.getBombs() + content[2]);
            this.field.getBombPositions().removeIf(pointPredicate);
        }
    }

    private int[] processCell(String cell){
        int[] content = new int[4];     //content[0] -> snippets, content[1] -> bugs, content[2] -> bombs, content[3] -> tickingBombs
        for (String cellPart : cell.split(";")) {
            switch (cellPart.charAt(0)) {
                case 'P':
                    break;
                case 'e':
                    // TODO: store spawn points
                    break;
                case 'E':
                    content[1]++;
                    break;
                case 'B':
                    if (cell.length() <= 1) {
                        content[2]++;
                    } else {
                        content[3]++;
                    }
                    break;
                case 'C':
                    content[0]++;
                    break;
            }
        }
        return content;
    }

    public BotState modifyStateWhebBugsMove(){

        return this;
    }
}
