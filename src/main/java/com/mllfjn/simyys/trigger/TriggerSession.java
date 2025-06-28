package com.mllfjn.simyys.trigger;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.state.Runnable;
import com.mllfjn.simyys.state.State;

import java.util.*;

public class TriggerSession {
    private static final Map<String, Integer> orderBeforeRound = new HashMap<>();
    private static final Map<String, Integer> orderAfterRound = new HashMap<>();
    static {
        // 回合前状态执行顺序
        List<String> beforeRound = List.of();

        // 回合后状态执行顺序
        List<String> afterRound = List.of();

        for (int i = 0; i < beforeRound.size(); i++) {
            orderBeforeRound.put(beforeRound.get(i), i);
        }

        for (int i = 0; i < afterRound.size(); i++) {
            orderAfterRound.put(afterRound.get(i), i);
        }
    }

    public static void trigger(BattlePane bp, Trigger trigger, List<State> states) {
        switch (trigger) {
            case BEFORE_ROUND -> runByOrder(bp, states, trigger, orderBeforeRound);
            case AFTER_ROUND -> runByOrder(bp, states, trigger, orderAfterRound);
            default -> runByDefault(bp, states, trigger);
        }
    }

    private static void runByOrder(BattlePane bp, List<State> states, Trigger trigger, Map<String, Integer> order) {
        List<Runnable> runByOrder = new ArrayList<>();
        List<Runnable> runLater = new ArrayList<>();
        for (State state : states) {
            if (state instanceof Runnable r && r.runnable(trigger)) {
                if (order.containsKey(state.name)) {
                    runByOrder.add(r);
                } else {
                    runLater.add(r);
                }
            }
        }

        runByOrder.sort(Comparator.comparingInt(o -> order.get(((State)o).name)));

        for (Runnable state : runByOrder) {
            state.run(trigger, bp);
        }

        for (Runnable state : runLater) {
            state.run(trigger, bp);
        }
    }

    private static void runByDefault(BattlePane bp, List<State> states, Trigger trigger) {
        // 危险 可能涉及状态删除
        for (State state : states) {
            if (state instanceof Runnable r && r.runnable(trigger)) {
                r.run(trigger, bp);
            }
        }
    }
}
