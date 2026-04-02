package comp20050.qssboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

public class Bot {
    public GameState state;
    public Bot(GameState state) {
        this.state = state;
    }

    // bot doesnt make move after player
    // board representation can be off. getNeighbour->addValid
    // account for activate pie button

    public Position makeMove() {
        ArrayList<Position> legalMoves = state.getLegalMoves();

        if (legalMoves.isEmpty()) {
            return null;
        }

        Position bestMove = null;
        int bestValue = Integer.MIN_VALUE;
        int depth = 3; // or 2, increasing depth means it looks ahead more

        for (Position move : legalMoves) {
            GameState child = state.copyState(); // NOTE:
            Bot botPlayer = Bot(simState);
            applyMove(child, move);

            int eval = minmax(child, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false, GameState.getCurrentPlayer());

            if (eval >= bestValue) {
                bestValue = eval;
                bestMove = move;
            }
        }

        return bestMove;
    }

    // QUESTION: changed simState from GameState to QuaxBoard bc. getValidMoves was in that class
    int minmax(GameState simState, int depth, int alpha, int beta, boolean isMax, GameState.Player botPlayer) {
        if (depth == 0 || simState.getLegalMoves().isEmpty()) {
            return evaluate(simState, simState.getCurrentPlayer());
            // ^^ We have compute distance in evaluate, but I don't see that working for the rest of minmax, making me think that its extra work
        }

        ArrayList<Position> legalMoves = getOrderedMoves(simState, botPlayer); // QUESTION: Why are we getting legalMoves both in minmax and makeMove?
        // To answer the above question, it's the structure that requires this, so the fix is to make sure you're not regenerating the same depth level unecessarily
        // So then the question becomes how do we know if we've regenerated the list unecessarily or not?

        if (isMax) {
            int bestValue = Integer.MIN_VALUE;

            for (Position move : legalMoves) {
                GameState child = simState.copyState(); // Again, we copyState in two functions
                // Make sure that it's doing a deep copy and not a shallow copy. So we need to find what kind of copy it's doing
                applyMove(child, move);

                int eval = minmax(child, depth - 1, alpha, beta, false, botPlayer);
                bestValue = Math.max(bestValue, eval);
                alpha = Math.max(alpha, bestValue);

                if (beta <= alpha) break;
            }

            return bestValue;
        }

        // maybe we can check the max inside so we don't do the copy twice if we're in max?
        else {
            int bestValue = Integer.MAX_VALUE;

            for (Position move : legalMoves) {
                GameState child = simState.copyState();
                applyMove(child, move);

                int eval = minmax(child, depth - 1, alpha, beta, true, botPlayer);
                bestValue = Math.min(bestValue, eval);
                beta = Math.min(beta, bestValue);

                if (beta <= alpha) break;
            }

            return bestValue;
        }
    }

}
