package it.unibo.ai.didattica.competition.tablut.heuristic;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.State.Pawn;

public class BlackHeuristics extends BaseHeuristics{



    private static final double WEIGHT_BLACK_PAWN = 10.0;
    private static final double WEIGHT_WHITE_PAWN_EATEN = 15.0;
    private static final double WEIGHT_KING_PROXIMITY = 25.0;
    private static final double PENALTY_KING_ESCAPE = 50.0;

    private State.Pawn[][] board;
    private int[] kingPosition;
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

        // Valutazione delle pedine nere rimaste
        int numBlackPawns = state.getNumberOf(Pawn.BLACK);
        utility += WEIGHT_BLACK_PAWN * numBlackPawns / countBlackPawns(state);

        // Valutazione delle pedine bianche mangiate
        int numWhitePawns = state.getNumberOf(Pawn.WHITE);
        utility += WEIGHT_WHITE_PAWN_EATEN * (countWhitePawns(state) - numWhitePawns) / countWhitePawns(state);

        // Valutazione della vicinanza al re
        utility += WEIGHT_KING_PROXIMITY * evaluateKingProximity();

        // Penalizzazione per vie di fuga aperte del re
        if (kingHasOpenWays()) {
            utility -= PENALTY_KING_ESCAPE;
        }

        // Stati terminali
        if (state.getTurn().equals(State.Turn.WHITEWIN)) {
            utility -= 100.0; // Penalità severa per la vittoria del bianco
        } else if (state.getTurn().equals(State.Turn.BLACKWIN)) {
            utility += 100.0; // Premio per la vittoria del nero
        }

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

        return proximityScore / 4.0; // Normalizzare il punteggio
    }

    private boolean kingHasOpenWays() {
        int row = kingPosition[0];
        int col = kingPosition[1];

        // Controllo le righe e colonne aperte
        for (int i = row + 1; i < board.length; i++) {
            if (!board[i][col].equals(Pawn.EMPTY)) return false;
        }
        for (int i = row - 1; i >= 0; i--) {
            if (!board[i][col].equals(Pawn.EMPTY)) return false;
        }
        for (int i = col + 1; i < board[0].length; i++) {
            if (!board[row][i].equals(Pawn.EMPTY)) return false;
        }
        for (int i = col - 1; i >= 0; i--) {
            if (!board[row][i].equals(Pawn.EMPTY)) return false;
        }

        return true;
    }

    private int[] getKingPosition() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].equals(Pawn.KING)) {
                    return new int[] {i, j};
                }
            }
        }
        return new int[] {4, 4}; // Posizione iniziale del re
    }
    // Calcola il numero di pedine bianche
public int countWhitePawns(State state) {
    int count = 0;
    for (int i = 0; i < state.getBoard().length; i++) {
        for (int j = 0; j < state.getBoard()[i].length; j++) {
            if (state.getPawn(i, j).equalsPawn(State.Pawn.WHITE.toString()) ||
                state.getPawn(i, j).equalsPawn(State.Pawn.KING.toString())) {
                count++;
            }
        }
    }
    return count;
}

// Calcola il numero di pedine nere
public int countBlackPawns(State state) {
    int count = 0;
    for (int i = 0; i < state.getBoard().length; i++) {
        for (int j = 0; j < state.getBoard()[i].length; j++) {
            if (state.getPawn(i, j).equalsPawn(State.Pawn.BLACK.toString())) {
                count++;
            }
        }
    }
    return count;
}

}

