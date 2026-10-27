package com.mllfjn.simyys.character.list.ssr.kongquemingwang;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.ParamStatus;

class StatusKaiPing extends Status {
    protected StatusKaiPing(Character belongTo, StatusKongQueLing kongQueLing, int duration,
                            boolean immune, boolean increaseAttack
    ) {
        super("开屏", belongTo, belongTo, StatusType.GENERAL, StatusForm.YIN_JI);
        attribute(Attribute.CRIT_RATE, 40);
        attribute(Attribute.EFFECT_HIT_RATE, 75);
        duration(StatusDurationType.CHI_XU, duration);

        if (increaseAttack) {
            attribute(Attribute.ATTACK, belongTo.getInitBaseAttack() * 0.01 * belongTo.getEffectHitRate());
        }

        if (immune) {
            // 只会在回合开始前创建,所以默认开启
            runOn(Trigger.BEING_ATTACKED, param ->
                    ((ParamAttackInfo) param).getAttackInfo().setCancel(true)
            );
            runOn(Trigger.ADDED_STATUS, param -> {
                Status status = ((ParamStatus) param).getStatus();
                if (status.statusType == StatusType.DEBUFF) {
                    status.delete();
                }
            });
            runOn(Trigger.AFTER_ROUND, _ -> {
                disableAction(Trigger.BEING_ATTACKED);
                disableAction(Trigger.ADDED_STATUS);
            });
            runOn(Trigger.BEFORE_ROUND, _ -> {
                enableAction(Trigger.BEING_ATTACKED);
                enableAction(Trigger.ADDED_STATUS);
            });
        }

        beforeDelete(kongQueLing::stopKaiPing);
        addTo();
    }
}
