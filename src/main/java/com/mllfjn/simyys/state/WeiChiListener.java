package com.mllfjn.simyys.state;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.trigger.Trigger;

public class WeiChiListener extends State implements Runnable{
    public static final String privateName = "维持";
    private final State content;
    public WeiChiListener(Character belongTo, Character comeFrom, State content) {
        super(belongTo, comeFrom, StateType.SPECIAL, StateForm.SPECIAL);
        this.content = content;
    }

    @Override
    public void setName() {
        name = privateName;
    }

    @Override
    public boolean runnable(Trigger trigger) {
        return trigger == Trigger.AFTER_ROUND;
    }

    @Override
    public void run(Trigger trigger, BattlePane bp) {
        content.pastRound();
        if (content.getRestRound() == 0) {
            delete();
        }
    }
}
