package com.mllfjn.simyys.trigger;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.state.Runnable;
import com.mllfjn.simyys.state.State;

import java.util.*;
/*

public class TriggerSession {
    // 如果以后状态列表很大，改用map
    private static final List<Class<? extends State>> orderBeforeRound = List.of(

    );
    private static final List<Class<? extends State>> orderAfterRound = List.of(

    );

    public static void trigger(BattlePane bp, Trigger trigger, List<State> states) {
        switch (trigger) {
            case BEFORE_ROUND -> runByOrder(bp, states, trigger, orderBeforeRound);
            case AFTER_ROUND -> runByOrder(bp, states, trigger, orderAfterRound);
            default -> runByDefault(bp, states, trigger);
        }
    }

    private static void runByOrder(BattlePane bp, List<State> states, Trigger trigger, List<Class<? extends State>> order) {
        // 按照order中顺序排列，其他的状态排在后面
        states.sort(Comparator.comparingInt(o -> {
            int index = order.indexOf(o.getClass());
            return index == -1 ? Integer.MAX_VALUE : index;
        }));
        runByDefault(bp, states, trigger);
    }

    private static void runByDefault(BattlePane bp, List<State> states, Trigger trigger) {
        */
/*//*
/ removeIf中如果添加元素会抛异常
        states.removeIf(state -> {
            if (state instanceof Runnable r && r.runnable(trigger)) {
                return r.run(trigger, bp);
            }
            return false;
        });*//*


        List<State> copy = new ArrayList<>(states);
        for (State state : copy) {
            if (state instanceof Runnable r && r.runnable(trigger)) {
                if (r.run(trigger, bp)) {
                    states.remove(state);
                }
            }
        }
    }
}
*/
