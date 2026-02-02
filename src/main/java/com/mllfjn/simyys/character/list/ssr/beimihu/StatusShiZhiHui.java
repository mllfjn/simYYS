package com.mllfjn.simyys.character.list.ssr.beimihu;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Displayable;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;

import java.util.Optional;

public class StatusShiZhiHui extends Status implements Displayable {
    public static final String StatusName = "时之辉";

    private final int level;

    private int stack;

    private StatusShiZhiHui(Character from, Character belongTo, int level) {
        super(from, belongTo, StatusType.BUFF, StatusForm.YIN_JI);
        this.level = level;
    }

    public static void get(Character from, Character belongTo, int stack, int level) {
        Optional<StatusShiZhiHui> oStatus = belongTo.getStatus(StatusShiZhiHui.class);
        oStatus.ifPresent(statusShiZhiHui ->
                statusShiZhiHui.stack = Math.min(statusShiZhiHui.stack + stack, 2));

        if (oStatus.isEmpty()) {
            StatusShiZhiHui statusShiZhiHui = new StatusShiZhiHui(from, belongTo, level);
            statusShiZhiHui.stack = stack;

            ((BeiMiHu) from).setShiZhiHuiCarrier(statusShiZhiHui);
            belongTo.addStatus(statusShiZhiHui);
        }
    }

    public void useSkill3() {
        consumeStack();
        StatusShiZhiXi.enter(from, belongTo, level);
    }

    public void transform() {
        consumeStack();
        belongTo.addStatus(new StatusShiZhiXi(from, belongTo, level));
    }

    private void consumeStack() {
        stack--;
        if (stack == 0) {
            belongTo.removeStatus(this);
            ((BeiMiHu) from).setShiZhiHuiCarrier(null);
        }
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String getDisplayText() {
        return StatusName + stack;
    }
}