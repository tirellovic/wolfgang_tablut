package it.unibo.ai.didattica.competition.tablut.heuristic;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.State.Pawn;

public class WhiteHeuristics extends BaseHeuristics{
    

    private static final double WEIGHT_WHITE_PAWN_POSITION = 10.0;
    private static final double WEIGHT_BLACK_PAWN_EATEN = 20.0;
    private static final double WEIGHT_KING_ESCAPE = 30.0;
    private static final double WEIGHT_KING_PROTECTION = 15.0;

    private State.Pawn[][] board;
    private int[] kingPosition;

    public WhiteHeuristics(State state) {
        super(state);
    }

    @Override
    public double evaluateState() {
        this.board = state.getBoard();
        this.kingPosition = getKingPosition();

        double utility = 0.0;

        // Valutazione delle pedine bianche in posizioni strategiche
        utility += WEIGHT_WHITE_PAWN_POSITION * evaluateWhitePawnPosition();

        // Valutazione delle pedine nere mangiate
        //FIXME: isn't it always == 0?
        int numBlackPawns = state.getNumberOf(Pawn.BLACK);
        utility += WEIGHT_BLACK_PAWN_EATEN * (countBlackPawns(state) - numBlackPawns) / countBlackPawns(state);

        // Valutazione delle vie di fuga del re
        utility += WEIGHT_KING_ESCAPE * countKingEscapeWays();

        // Valutazione della protezione del re
        utility += WEIGHT_KING_PROTECTION * evaluateKingProtection();

        // Stati terminali
        if (state.getTurn().equals(State.Turn.WHITEWIN)) {
            utility += 100.0; // Premio per la vittoria del bianco
        } else if (state.getTurn().equals(State.Turn.BLACKWIN)) {
            utility -= 100.0; // Penalità per la vittoria del nero
        }

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

