package it.unibo.ai.didattica.competition.tablut.heuristic;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import it.unibo.ai.didattica.competition.tablut.domain.GameAshtonTablut;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.State.Pawn;

public class WhiteHeuristics extends BaseHeuristics{
    
    // total sum of the weights is 75
    private static final double WEIGHT_WHITE_PAWN_POSITION = 10.0;
    private static final double WEIGHT_WHITE_PAWN = 18.0;
    private static final double WEIGHT_BLACK_PAWN_EATEN = 9.0;
    private static final double WEIGHT_KING_ESCAPE = 15.0;
    private static final double WEIGHT_KING_PROTECTION = 23.0;

    private static final Logger heuristicLogger = Logger.getLogger(WhiteHeuristics.class.getName());
    static {
        try {
            FileHandler fileHandler = new FileHandler("logs/white_heuristics.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            heuristicLogger.addHandler(fileHandler);
            heuristicLogger.setUseParentHandlers(false);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore nella configurazione del logger per BlackHeuristics", e);
        }
    }
        
    public WhiteHeuristics(State state) {
        super(state);
    }

    @Override
    public double evaluateState() {
        double utility = 0.0;

        // Evaluation of white pawns in strategic positions
        double fracWhitePawn = state.getNumberOf(Pawn.WHITE) / GameAshtonTablut.INITIAL_NUM_WHITE;
        utility += WEIGHT_WHITE_PAWN * fracWhitePawn;

        utility += WEIGHT_WHITE_PAWN_POSITION * evaluateWhitePawnPosition();

        int numBlackPawns = state.getNumberOf(Pawn.BLACK);
        utility += WEIGHT_BLACK_PAWN_EATEN * (GameAshtonTablut.INITIAL_NUM_BLACK - numBlackPawns) / GameAshtonTablut.INITIAL_NUM_BLACK;

        // Evaluation of the King's escape routes
        utility += WEIGHT_KING_ESCAPE * (countKingEscapeWays() / GameAshtonTablut.MAX_ESCAPES_NUM);

        // Evaluation of King's protection
        utility += WEIGHT_KING_PROTECTION * evaluateKingProtection();

        // Terminal states
        if (state.getTurn().equals(State.Turn.WHITEWIN)) {
            utility += 100.0; 
        } else if (state.getTurn().equals(State.Turn.BLACKWIN)) {
            utility -= 100.0;
        }
        heuristicLogger.info(
                ", Utility: " + utility);
        return utility;
    }

    private double evaluateWhitePawnPosition() {
        int[][] strategicPositions = { {2, 3}, {3, 5}, {5, 3}, {6, 5} };
        int count = 0;

        for (int[] pos : strategicPositions) {
            if (board[pos[0]][pos[1]].equals(Pawn.WHITE)) {
                count++;
            }
        }

        return (double) count / strategicPositions.length;
    }

    private int countKingEscapeWays() {
        int row = kingPosition[0];
        int col = kingPosition[1];
        int escapeWays = 0;

        for (int i = row + 1; i < board.length && board[i][col].equals(Pawn.EMPTY); i++) {
            escapeWays++;
        }
        for (int i = row - 1; i >= 0 && board[i][col].equals(Pawn.EMPTY); i--) {
            escapeWays++;
        }
        for (int i = col + 1; i < board[0].length && board[row][i].equals(Pawn.EMPTY); i++) {
            escapeWays++;
        }
        for (int i = col - 1; i >= 0 && board[row][i].equals(Pawn.EMPTY); i--) {
            escapeWays++;
        }

        return escapeWays;
    }

    private double evaluateKingProtection() {

        int[][] directions = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} };
        double protectionScore = 0.0;
        double threatScore = 0.0;

        for (int[] dir : directions) {
            int newRow = kingPosition[0] + dir[0];
            int newCol = kingPosition[1] + dir[1];
            if (newRow >= 0 && newRow < board.length && newCol >= 0 && newCol < board[0].length) {
                if (board[newRow][newCol].equals(Pawn.WHITE)) {
                    protectionScore += 1.0;
                } else if (board[newRow][newCol].equals(Pawn.BLACK)) {
                    threatScore += 1.0; 
                }
            }
        }

        return (protectionScore - threatScore) / 4.0; 
    }   
}

