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
//    public Position makeMove() {
//        // a bot that randomly chooses tiles
//        Boolean[][] valid_moves = state.game_board.getValidMoves();
//        Random r = new Random();
//        int x;
//        int y;
//        do {
//            x = r.nextInt(Tile.NUM_ROWS);
//            y = r.nextInt(Tile.NUM_COLS);
//        } while (!valid_moves[x][y]);
//
//        Tile tile = state.game_board.getTile(x, y);
//        String botMoveId = (tile.type.toString().equals("RHOMBUS") ? "R" : "O") + "_" + x + "_" + y;
//        Position pos = new Position(botMoveId);
//        return pos;
//    }

    public Position makeMove() {
        ArrayList<Position> legalMoves = state.getLegalMoves();

        if (legalMoves.isEmpty()) {
            return null;
        }

        Position bestMove = null;
        int bestValue = Integer.MIN_VALUE;
        int depth = 3; // or 3, increasing depth means it looks ahead more

        for (Position move : legalMoves) {
            GameState child = state.copyState();
            applyMove(child, move);

            int eval = minmax(child, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);

            if (eval >= bestValue) {
                bestValue = eval;
                bestMove = move;
            }
        }

        return bestMove;
    }

    // TOOK FROM CHAT
    private void applyMove(GameState simState, Position move) {
        move.extractPosition();
        int row = move.getRow();
        int col = move.getCol();

        QuaxBoard.TileType tileType = simState.game_board.getTileType(row, col);

        simState.game_board.makeMove(row, col, simState.current_player, tileType);
        simState.switchPlayerTurn();
    }

    // TOOK FROM CHAT, this is our heuristic function I believe
    private int evaluate(GameState simState) {
        int ownDist = computeDistance(simState, simState.current_player);
        GameState.Player oppPlayer =
                (simState.current_player == GameState.Player.P1) ? GameState.Player.P2 : GameState.Player.P1;
        int oppDist = computeDistance(simState, oppPlayer);

        return oppDist - ownDist;
    }

    // TOOK FROM CHAT, MAY NEED TO MAKE SOME CHANGES
    private int tileTraversalCost(GameState simState, int row, int col,
                                  QuaxBoard.TileOwner own, QuaxBoard.TileOwner opp) {
        QuaxBoard.TileOwner owner = simState.game_board.getTileOwner(row, col);

        // make this more robust to hit different cases
        // QUESTION: does it hit rounding around a player, borders, blocking opponent
        if (owner == opp) return 1_000_000;
        if (owner == own) return 0;
        return 1; // empty tile
    }

    private void evaluateNeighbours(GameState simState, Node curr, int INF, QuaxBoard.TileOwner own, QuaxBoard.TileOwner opp, int[][] dist, PriorityQueue pq) {
        for (int[] n : simState.getNeighbours(curr.row, curr.col)) {
            int nearRow = n[0];
            int nearCol = n[1];

            int nextCost = tileTraversalCost(simState, nearRow, nearCol, own, opp);
            if (nextCost >= INF) continue;

            int newDist = curr.cost + nextCost;
            if (newDist < dist[nearRow][nearCol]) {
                dist[nearRow][nearCol] = newDist;
                pq.add(new Node(nearRow, nearCol, newDist));
            }
        }
    }


    // QUESTION: changed simState from GameState to QuaxBoard bc. getValidMoves was in that class
    int minmax(GameState simState, int depth, int alpha, int beta, boolean isMax) {
        if (depth == 0 ||
                simState.checkWin(simState.game_board.getColor(GameState.Player.P1)) ||
                simState.checkWin(simState.game_board.getColor(GameState.Player.P2)) ||
                simState.getLegalMoves().isEmpty()) {
            return evaluate(simState);
        }

        ArrayList<Position> legalMoves = simState.getLegalMoves();

        if (isMax) {
            int bestValue = Integer.MIN_VALUE;

            for (Position move : legalMoves) {
                GameState child = simState.copyState();
                applyMove(child, move);

                int eval = minmax(child, depth - 1, alpha, beta, false);
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

                // QUESTIONS: copyGameState could become copy board?
                // applyMove could become makeMove in quaxboard

                int eval = minmax(child, depth - 1, alpha, beta, true);
                bestValue = Math.min(bestValue, eval);
                beta = Math.min(beta, bestValue);

                if (beta <= alpha) break;
            }

            return bestValue;
        }
    }

    private int computeDistance(GameState simState, GameState.Player player) {
        QuaxBoard.TileOwner own;
        QuaxBoard.TileOwner opp;

        // you can skip two to consider a legal move

        if (player == GameState.Player.P1) {
            own = QuaxBoard.TileOwner.BLACK;
            opp = QuaxBoard.TileOwner.WHITE;
        } else {
            own = QuaxBoard.TileOwner.WHITE;
            opp = QuaxBoard.TileOwner.BLACK;
        }

        int[][] dist = new int[Tile.NUM_ROWS][Tile.NUM_COLS];
        int INF = 1_000_000;
        for (int row = 0; row < Tile.NUM_ROWS; row++) {
            for (int col = 0; col < Tile.NUM_COLS; col++) {
                dist[row][col] = INF; // QUESTION: can't I just get inf from a package?
            }
        }

        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a.cost));

        if (player == GameState.Player.P1) {
            for (int col = 0; col < Tile.NUM_COLS; col++) {
                int startCost = tileTraversalCost(simState, 0, col, own, opp); // Create helper function
                if (startCost < INF) {
                    dist[0][col] = startCost;
                    pq.add(new Node(0, col, startCost));
                }
            }

            while (!pq.isEmpty()) {
                Node curr = pq.poll();

                if (curr.cost > dist[curr.row][curr.col]) continue;
                evaluateNeighbours(simState, curr, INF, own, opp, dist, pq);
            }

            int best = INF;
            for (int col = 0; col < Tile.NUM_COLS; col++) {
                best = Math.min(best, dist[Tile.NUM_ROWS - 1][col]);
            }
            return best;
        }

        else {
            for (int row = 0; row < Tile.NUM_ROWS; row++) {
                int startCost = tileTraversalCost(simState, row, 0, own, opp);
                if (startCost < INF) {
                    dist[row][0] = startCost;
                    pq.add(new Node(row, 0, startCost));
                }
            }

            while (!pq.isEmpty()) {
                Node curr = pq.poll();

                if (curr.cost > dist[curr.row][curr.col]) continue;;

                evaluateNeighbours(simState, curr, INF, own, opp, dist, pq);
            }

            int best = INF;
            for (int row = 0; row < Tile.NUM_ROWS; row++) {
                best = Math.min(best, dist[row][Tile.NUM_COLS - 1]);
            }

            return best;
        }
    }
}
