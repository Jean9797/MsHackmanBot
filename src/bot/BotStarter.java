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

import java.util.ArrayList;
import java.util.Random;

import move.Move;
import move.MoveType;
import player.CharacterType;
import player.Player;

/**
 * bot.BotStarter
 *
 * Magic happens here. You should edit this file, or more specifically
 * the doMove() method to make your bot do more than random moves.
 *
 * @author Jim van Eeden - jim@riddles.io
 */
public class BotStarter {

    private Random random;
    private InitialStage initialStage;
    private BugStage bugStage;

    private BotStarter() {
        this.random = new Random();
        this.initialStage = new InitialStage();
        this.bugStage = new BugStage();
    }

    /**
     * Return a random character to play as
     * @return A random character
     */
    public CharacterType getCharacter() {
        CharacterType[] characters = CharacterType.values();
        return characters[this.random.nextInt(characters.length)];
    }

    /**
     * Does a move action. Edit this to make your bot smarter.
     * @param state The current state of the game
     * @return A Move object
     */
    public Move doMove(BotState state) {
        ArrayList<MoveType> validMoveTypes = state.getField().getValidMoveTypes();

        if (validMoveTypes.size() <= 0) {
            return new Move(); // No valid moves, pass
        }

        return this.bugStage.getMove(state.getField());

        /*if(state.getField().getEnemyPositions().isEmpty()){
            return this.initialStage.getMove(state);
        }
        else {
            return this.bugStage.getMove(state.getField());
        }*/
    }

    public static void main(String[] args) {
        BotParser parser = new BotParser(new BotStarter());
        parser.run();
    }
}
