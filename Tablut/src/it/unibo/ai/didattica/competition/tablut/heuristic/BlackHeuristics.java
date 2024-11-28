package it.unibo.ai.didattica.competition.tablut.heuristic;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import it.unibo.ai.didattica.competition.tablut.domain.GameAshtonTablut;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.State.Pawn;

public class BlackHeuristics extends BaseHeuristics{

    // total sum of the weights is 75
    private static final double WEIGHT_BLACK_PAWN = 10.0;
    private static final double WEIGHT_WHITE_PAWN_EATEN = 40.0;
    private static final double WEIGHT_KING_PROXIMITY = 25.0;

    private static final Logger heuristicLogger = Logger.getLogger(BlackHeuristics.class.getName());
    static {
        try {
            FileHandler fileHandler = new FileHandler("logs/black_heuristics.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            heuristicLogger.addHandler(fileHandler);
            heuristicLogger.setUseParentHandlers(false);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore nella configurazione del logger per BlackHeuristics", e);
        }
    }
    public BlackHeuristics(State state) {
        super(state);
    }

    @Override
    public double evaluateState() {
        this.board = state.getBoard();
        this.kingPosition = getKingPosition();

        double utility = 0.0;

        // evaluation of black pawn remaining
        double fracBlackPawn = state.getNumberOf(Pawn.BLACK) / GameAshtonTablut.INITIAL_NUM_BLACK;
        utility += fracBlackPawn * WEIGHT_BLACK_PAWN;

        // Evaluation of eaten white pawns
        int numWhitePawns = state.getNumberOf(Pawn.WHITE);
        utility += WEIGHT_WHITE_PAWN_EATEN * (GameAshtonTablut.INITIAL_NUM_WHITE - numWhitePawns) / GameAshtonTablut.INITIAL_NUM_WHITE;

        utility += WEIGHT_KING_PROXIMITY * evaluateKingProximity();

        return utility;
    }

    private double evaluateKingProximity() {
        int[][] directions = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} };
        double proximityScore = 0.0;

        for (int[] dir : directions) {
            int newRow = kingPosition[0] + dir[0];
            int newCol = kingPosition[1] + dir[1];
            if (newRow >= 0 && newRow < board.length && newCol >= 0 && newCol < board[0].length) {
                if (board[newRow][newCol].equals(Pawn.BLACK)) {
                    proximityScore += 1.0;
                }
            }
        }

        return proximityScore / 4.0;
    }
}

