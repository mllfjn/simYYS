package com.mllfjn.simyys.character.list.sp.luwan;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;

import java.util.Optional;

class StatusLuShi extends Status {
    private static final String StatusName = "麓蚀";

    private boolean isCounting = false;

    private StatusLuShi(Character from, Character belongTo) {
        super(StatusName, from, belongTo);
        type(StatusType.DEBUFF, StatusForm.YIN_JI);
        attribute(Attribute.ATTACK, _ -> {
            if (isCounting) {
                return 0.0;
            }

            isCounting = true;
            double attack = belongTo.getAttack();
            isCounting = false;
            return -0.15 * attack;
        });
        displayName();
        runOn(Trigger.AFTER_ROUND, _ -> {
            belongTo.addStatus(new StatusLuYa(from, belongTo));
            delete();
        });
    }

    static void addStatus(Character from, Character belongTo) {
        Optional<StatusLuShi> oStatus = belongTo.getStatus(StatusLuShi.class);
        if (oStatus.isEmpty()) {
            belongTo.addStatus(new StatusLuShi(from, belongTo));
        }
    }

    static class StatusLuYa extends Status implements CrowdControl {
        private static final String StatusName = "麓压";

        private boolean isCounting = false;

        public StatusLuYa(Character from, Character belongTo) {
            super(StatusName, from, belongTo);
            type(StatusType.DEBUFF, StatusForm.YIN_JI);
            attribute(Attribute.SPEED, _ -> {
                if (isCounting) {
                    return 0.0;
                }

                isCounting = true;
                double speed = belongTo.getSpeed();
                isCounting = false;
                return -0.3 * speed;
            });
            displayName();
            runOn(Trigger.BEFORE_ROUND, _ -> delete());
        }
    }
}
