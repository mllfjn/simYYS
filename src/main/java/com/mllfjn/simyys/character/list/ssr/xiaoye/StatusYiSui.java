package com.mllfjn.simyys.character.list.ssr.xiaoye;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;

class StatusYiSui extends Status {
    private StatusYiSui(XiaoYe from, Character belongTo, int duration) {
        super("易碎", from, belongTo, StatusType.DEBUFF, StatusForm.YIN_JI);
        attribute(Attribute.DEFENCE, -300);
        if (!belongTo.isMob()) {
            runOn(Trigger.AFTER_ATTACK, param -> {
                double damage = ((ParamAttackInfo) param).getAttackInfo().getTraceableNumber().getNumber();
                if (damage > (from.getInitAttack() * 3)) {
                    delete();
                }
            });
        }
        if (duration > 0) {
            duration(StatusDurationType.CHI_XU, duration);
        }
        from.statusYiSui = this;
        beforeDelete(() -> from.statusYiSui = null);
        addTo();
    }

    static void add(XiaoYe from, Character belongTo, int duration) {
        if (from.statusYiSui != null && from.statusYiSui.belongTo == belongTo) {
            from.statusYiSui.delete();
        }

        if (from.statusYiSui == null) {
            new StatusYiSui(from, belongTo, duration);
        }
    }
}
