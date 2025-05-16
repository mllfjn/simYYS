package com.mllfjn.simyys.state;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.trigger.Trigger;

public interface Runnable {
    boolean runnable(Trigger trigger);
    void run(Trigger trigger, BattlePane bp);
}
