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

public class IterativeDeepeningPVS extends IterativeDeepeningAlphaBetaSearch<State, Action, State.Turn> {

    public IterativeDeepeningPVS(Game<State, Action, State.Turn> game, double utilMin, double utilMax, int timeInSeconds) {
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

        // Step 2: Sort actions based on their evaluation (descending order for maximizing player)
        actionsWithEval.sort(Comparator.comparingDouble(EvaluatedAction::getEvaluation).reversed());

        return actionsWithEval.stream()
                .map(EvaluatedAction::getAction)
                .collect(Collectors.toList());
    }

    @Override
    protected double eval(State state, Turn player) {
        return this.game.getUtility(state, player);
    }

    /* @Override
    public Action makeDecision(State state) {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + timeLimitMillis;

        Action bestAction = null;
        // Orders all the possibile future actions retrieved using min max, uses the getAction that we created
        //the order should ensure that the most promising action is analyzed as the first one 
        List<Action> actions = orderActions(state, game.getActions(state), game.getPlayer(state), 0);

        // Iterativedeepening with setting the time limit
        for (currDepthLimit = 2; System.currentTimeMillis() < endTime; currDepthLimit++) {
            for (Action action : actions) {
                double value = maxValue(game.getResult(state, action), game.getPlayer(state), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 1);

                if (System.currentTimeMillis() >= endTime) {
                    break; // the search stops once the time limit is reached
                }

                if (bestAction == null || value > eval(game.getResult(state, bestAction), game.getPlayer(state))) {
                    bestAction = action;
                }
            }
        }

        System.out.println("Explored nodes: " + getMetrics().get(METRICS_NODES_EXPANDED));
        System.out.println("Max depth reached: " + getMetrics().get(METRICS_MAX_DEPTH));

        return bestAction;
    }

    @Override
    public double maxValue(State state, State.Turn player, double alpha, double beta, int depth) {
        if (game.isTerminal(state) || depth >= currDepthLimit) {
            return eval(state, player);
        }

        double value = Double.NEGATIVE_INFINITY;
        List<Action> actions = orderActions(state, game.getActions(state), player, depth);

        boolean isFirstChild = true;
        for (Action action : actions) {
            if (isFirstChild) {
                // Full window search for the first child
                value = Math.max(value, minValue(game.getResult(state, action), player, alpha, beta, depth + 1));
                isFirstChild = false;
            } else {
                // Null window search for subsequent children
                double score = minValue(game.getResult(state, action), player, alpha, alpha + 1, depth + 1);
                if (score > alpha && score < beta) {
                    // Re-search with the full window if null window search is inconclusive
                    score = minValue(game.getResult(state, action), player, alpha, beta, depth + 1);
                }
                value = Math.max(value, score);
            }

            if (value >= beta) {
                return value; // Beta cutoff
            }
            alpha = Math.max(alpha, value);
        }

        return value;
    }

    @Override
    public double minValue(State state, State.Turn player, double alpha, double beta, int depth) {
        if (game.isTerminal(state) || depth >= currDepthLimit || System.currentTimeMillis() >= timeLimitMillis) {
            return eval(state, player);
        }

        double value = Double.POSITIVE_INFINITY;
        List<Action> actions = orderActions(state, game.getActions(state), player, depth);

        boolean isFirstChild = true;
        for (Action action : actions) {
            if (isFirstChild) {
                // Full window search for the first child
                value = Math.min(value, maxValue(game.getResult(state, action), player, alpha, beta, depth + 1));
                isFirstChild = false;
            } else {
                // Null window search for subsequent children
                double score = maxValue(game.getResult(state, action), player, beta - 1, beta, depth + 1);
                if (score > alpha && score < beta) {
                    // Re-search with the full window if null window search is inconclusive
                    score = maxValue(game.getResult(state, action), player, alpha, beta, depth + 1);
                }
                value = Math.min(value, score);
            }

            if (value <= alpha) {
                return value; // Alpha cutoff
            }
            beta = Math.min(beta, value);
        }

        return value;
    } */
}
