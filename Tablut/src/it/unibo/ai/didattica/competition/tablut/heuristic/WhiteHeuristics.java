package it.unibo.ai.didattica.competition.tablut.heuristic;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import it.unibo.ai.didattica.competition.tablut.domain.GameAshtonTablut;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.State.Pawn;

public class WhiteHeuristics extends BaseHeuristics{
    

    private static final double WEIGHT_WHITE_PAWN_POSITION = 8.0;
    private static final double WEIGHT_BLACK_PAWN_EATEN = 10.0;
    private static final double WEIGHT_KING_ESCAPE = 21.0;
    private static final double WEIGHT_KING_PROTECTION = 36.0;

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

        // Valutazione delle pedine bianche in posizioni strategiche
        //utility += WEIGHT_WHITE_PAWN_POSITION * evaluateWhitePawnPosition();
        utility += WEIGHT_WHITE_PAWN_POSITION * state.getNumberOf(Pawn.WHITE);

        // Valutazione delle pedine nere 
        int numBlackPawns = state.getNumberOf(Pawn.BLACK);
        utility += WEIGHT_BLACK_PAWN_EATEN * (GameAshtonTablut.INITIAL_NUM_BLACK - numBlackPawns) / GameAshtonTablut.INITIAL_NUM_BLACK;

        // Valutazione delle vie di fuga del re
        utility += WEIGHT_KING_ESCAPE * (countKingEscapeWays() / GameAshtonTablut.MAX_ESCAPES_NUM);

        // Valutazione della protezione del re
        utility += WEIGHT_KING_PROTECTION * evaluateKingProtection();

        // Stati terminali
        if (state.getTurn().equals(State.Turn.WHITEWIN)) {
            utility += 100.0; // Premio per la vittoria del bianco
        } else if (state.getTurn().equals(State.Turn.BLACKWIN)) {
            utility -= 100.0; // Penalità per la vittoria del nero
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

    // the king is also pro
    private double evaluateKingProtection() {
        int[][] directions = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} };
        double protectionScore = 0.0;

        for (int[] dir : directions) {
            int newRow = kingPosition[0] + dir[0];
            int newCol = kingPosition[1] + dir[1];
            if (newRow >= 0 && newRow < board.length && newCol >= 0 && newCol < board[0].length) {
                if (board[newRow][newCol].equals(Pawn.WHITE)) {
                    protectionScore += 0.5; // Ogni pedina bianca vicina aggiunge al punteggio
                }
            }
        }

        return protectionScore / 2.0; // Normalizzazione
    }

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

