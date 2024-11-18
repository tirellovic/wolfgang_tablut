package it.unibo.ai.didattica.competition.tablut.heuristic;
import it.unibo.ai.didattica.competition.tablut.domain.State;
public abstract class BaseHeuristics{

    protected State state;

    public BaseHeuristics(State state) {
        this.state = state;
    }

    /**
     * Metodo astratto per calcolare il valore euristico di uno stato.
     * @return il valore euristico dello stato.
     */
    public abstract double evaluateState();

    /**
     * Controlla se il re si trova nella posizione iniziale (trono).
     * @param state lo stato corrente.
     * @return true se il re è sul trono, altrimenti false.
     */
    public boolean checkKingPosition(State state) {
        return state.getPawn(4, 4).equalsPawn("K");
    }
}