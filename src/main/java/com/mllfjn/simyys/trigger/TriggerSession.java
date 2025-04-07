package com.mllfjn.simyys.trigger;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.state.State;

import java.util.*;

public class TriggerSession {
    private static final Map<String, Integer> orderBeforeRound = new HashMap<>();
    private static final Map<String, Integer> orderAfterRound = new HashMap<>();
    static {
        // 回合前状态执行顺序
        List<String> beforeRound = Arrays.asList(

        );

        // 回合后状态执行顺序
        List<String> afterRound = Arrays.asList(

        );

        for (int i = 0; i < beforeRound.size(); i++) {
            orderBeforeRound.put(beforeRound.get(i), i);
        }

        for (int i = 0; i < afterRound.size(); i++) {
            orderAfterRound.put(afterRound.get(i), i);
        }
    }

    public static void trigger(BattlePane battlePane, Trigger trigger, List<State> states) {
        switch (trigger) {
            case BEFOREROUND -> runByOrder(battlePane, states, trigger, orderBeforeRound);
            case AFTERROUND -> runByOrder(battlePane, states, trigger, orderAfterRound);
            default -> runByDefault(battlePane, states, trigger);
        }
    }

    private static void runByOrder(BattlePane battlePane, List<State> states, Trigger trigger, Map<String, Integer> order) {
        List<State> runByOrder = new ArrayList<>();
        List<State> runLater = new ArrayList<>();
        for (State state : states) {
            if (state.runnable(trigger)) {
                if (order.containsKey(state.name)) {
                    runByOrder.add(state);
                } else {
                    runLater.add(state);
                }
            }
        }

        runByOrder.sort(Comparator.comparingInt(o -> order.get(o.name)));

        for (State state : runByOrder) {
            state.run(trigger, battlePane);
        }

        for (State state : runLater) {
            state.run(trigger, battlePane);
        }
    }

    private static void runByDefault(BattlePane battlePane, List<State> states, Trigger trigger) {
        for (State state : states) {
            if (state.runnable(trigger)) {
                state.run(trigger, battlePane);
            }
        }
    }
}
