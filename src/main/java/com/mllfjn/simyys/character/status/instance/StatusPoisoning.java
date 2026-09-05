package com.mllfjn.simyys.character.status.instance;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.StatusSupplier;

public class StatusPoisoning extends Status {
    private static final String StatusName = "中毒";

    private int stack;

    private StatusPoisoning(Character from, Character belongTo, int stack, int duration) {
        super(StatusName, from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
        this.stack = stack;

        duration(StatusDurationType.CHI_XU, duration);
        attribute(Attribute.SPEED, _ -> -0.1 * belongTo.getInitSpeed());
        attribute(Attribute.DEFENCE, param -> {
            if (param == null || param.attackType() != AttackType.JIAN_JIE) {
                return 0.0;
            } else {
                return -10.0 * stack;
            }
        });
        display(() -> StatusName + stack + "-" + getDuration());
    }

    public static void add(Character from, Character belongTo, int stack, int duration) {
        for (Status belongToStatus : belongTo.getStatuses()) {
            if (belongToStatus instanceof StatusPoisoning bsp
                    && bsp.getDuration() == duration
            ) {
                bsp.stack += stack;
                return;
            }
        }
        belongTo.addStatus(new StatusPoisoning(from, belongTo, stack, duration));
    }

    public static StatusSupplier getSupplier(int stack, int duration) {
        return new StatusSupplier(StatusName, StatusPoisoning.class,
                (from, to) -> add(from, to, stack, duration)
        );
    }
}
