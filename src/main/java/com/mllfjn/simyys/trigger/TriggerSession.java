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

    public static void trigger(BattlePane bp, Trigger trigger, List<State> states) {
        switch (trigger) {
            case BEFOREROUND -> runByOrder(bp, states, trigger, orderBeforeRound);
            case AFTERROUND -> runByOrder(bp, states, trigger, orderAfterRound);
            default -> runByDefault(bp, states, trigger);
        }
    }

    private static void runByOrder(BattlePane bp, List<State> states, Trigger trigger, Map<String, Integer> order) {
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
            state.run(trigger, bp);
        }

        for (State state : runLater) {
            state.run(trigger, bp);
        }
    }

    private static void runByDefault(BattlePane bp, List<State> states, Trigger trigger) {
        for (State state : states) {
            if (state.runnable(trigger)) {
                state.run(trigger, bp);
            }
        }
    }
}
