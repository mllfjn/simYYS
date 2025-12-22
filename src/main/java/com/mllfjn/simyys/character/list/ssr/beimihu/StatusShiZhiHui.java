package com.mllfjn.simyys.character.list.ssr.beimihu;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.Displayable;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;

import java.util.Optional;

public class StatusShiZhiHui extends Status implements Displayable {
    public static final String StatusName = "时之辉";

    private int stack;

    private StatusShiZhiHui(Character from, Character belongTo) {
        super(from, belongTo, StatusType.BUFF, StatusForm.YIN_JI);
    }

    public static void get(Character from, Character belongTo, int stack) {
        Optional<StatusShiZhiHui> oStatus = belongTo.getStatus(StatusShiZhiHui.class);
        oStatus.ifPresent(statusShiZhiHui ->
                statusShiZhiHui.stack = Math.min(statusShiZhiHui.stack + stack, 2));

        if (oStatus.isEmpty()) {
            ((BeiMiHu) from).setShiZhiHuiCarrier(belongTo);

            StatusShiZhiHui statusShiZhiHui = new StatusShiZhiHui(from, belongTo);
            statusShiZhiHui.stack = stack;
            belongTo.addStatus(statusShiZhiHui);
        }
    }

    public void transform() {
        stack--;
        if (stack == 0) {
            belongTo.removeStatus(this);
        }
        belongTo.addStatus(new StatusShiZhiXi(from, belongTo));
    }

    @Override
    public String getText() {
        return StatusName + stack;
    }
}