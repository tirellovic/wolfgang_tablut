package it.unibo.ai.didattica.competition.tablut.heuristic;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.State.Pawn;
public abstract class BaseHeuristics{

    protected State state;
    protected State.Pawn[][] board;
    protected int[] kingPosition;

    public BaseHeuristics(State state) {
        this.state = state;
        this.board = state.getBoard();
        this.kingPosition = getKingPosition();
    }

    /**
     * Abstract method that computes the heuristic value of a state.
     */
    public abstract double evaluateState();

    /**
     * Returns the actual position of the king
     */
    public int[] getKingPosition() {
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                if (this.board[i][j].equals(Pawn.KING)) {
                    return new int[] {i, j};
                }
            }
        }
        return new int[] {4, 4}; //initial position of the king
    }
}