package it.unibo.ai.didattica.competition.tablut.heuristic;
import it.unibo.ai.didattica.competition.tablut.domain.Action;

/**
 * Helper class to associate actions with their evaluations
 */ 
public class EvaluatedAction {
    private final Action action;
    private final double evaluation;

    public EvaluatedAction(Action action, double evaluation) {
        this.action = action;
        this.evaluation = evaluation;
    }

    public Action getAction() {
        return action;
    }

    public double getEvaluation() {
        return evaluation;
    }
}
