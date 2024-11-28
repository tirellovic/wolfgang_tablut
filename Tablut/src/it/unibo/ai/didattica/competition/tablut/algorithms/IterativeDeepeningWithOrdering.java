package it.unibo.ai.didattica.competition.tablut.algorithms;

import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
import aima.core.search.adversarial.Game;
import it.unibo.ai.didattica.competition.tablut.domain.State;
import it.unibo.ai.didattica.competition.tablut.domain.State.Turn;
import it.unibo.ai.didattica.competition.tablut.domain.Action;
import it.unibo.ai.didattica.competition.tablut.heuristic.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Extention of the base class IterativeDeepeningAlphaBetaSearch in which the method orderActions effectively 
 * reorders the actions.
 */
public class IterativeDeepeningWithOrdering extends IterativeDeepeningAlphaBetaSearch<State, Action, State.Turn> {

    public IterativeDeepeningWithOrdering(Game<State, Action, State.Turn> game, double utilMin, double utilMax, int timeInSeconds) {
        super(game, utilMin, utilMax, timeInSeconds);
    }

    @Override
    public List<Action> orderActions(State state, List<Action> actions, Turn player, int depth) {
        List<EvaluatedAction> actionsWithEval = new ArrayList<>();

        for (Action action : actions) {
            State resultingState = game.getResult(state.clone(), action);   
            double evaluation = eval(resultingState, player);

            actionsWithEval.add(new EvaluatedAction(action, evaluation));
        }

        actionsWithEval.sort(Comparator.comparingDouble(EvaluatedAction::getEvaluation).reversed());

        return actionsWithEval.stream()
                .map(EvaluatedAction::getAction)
                .collect(Collectors.toList());
    }

    @Override
    protected double eval(State state, Turn player) {
        return this.game.getUtility(state, player);
    }
}
