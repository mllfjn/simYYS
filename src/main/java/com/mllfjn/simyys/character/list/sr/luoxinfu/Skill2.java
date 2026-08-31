package com.mllfjn.simyys.character.list.sr.luoxinfu;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamUseSkill;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.StatusSupplier;

class Skill2 extends PassiveSkill {
    private static final String SkillName = "蜘蛛印记";

    Skill2(Character belongTo) {
        super(belongTo, 1, 2);
    }

    void madeAttack(Character target) {
        if (isActive()) {
            getBelongTo().doInteractive(interactive ->
                    interactive.effect(this, target, 60, true,
                            new StatusSupplier(StatusZhuYin.StatusName, StatusZhuYin.class,
                                    (_, to) ->
                                            to.getStatus(StatusZhuYin.class)
                                                    .ifPresentOrElse(
                                                            status -> status.duration(2),
                                                            () -> to.addStatus(new StatusZhuYin(getBelongTo(), to))
                                                    )
                            )
                    )
            );
        }
    }

    @Override
    public String getName() {
        return SkillName;
    }

    private class StatusZhuYin extends Status {
        private static final String StatusName = "蛛印";

        private StatusZhuYin(Character from, Character belongTo) {
            super(StatusName, from, belongTo);
            type(StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
            duration(StatusDurationType.CHI_XU, 2);
            runOn(Trigger.USED_SKILL, param -> {
                ParamUseSkill pus = (ParamUseSkill) param;
                if (pus.getCost() > 0) {
                    from.doInteractive(interactive ->
                            interactive.attack(AttackInfo
                                    .createJianJieAttack(from, Skill2.this, belongTo, from.getAttack())
                            )
                    );
                    Status.of(StatusName + "减速", from, belongTo)
                            .type(StatusType.DEBUFF, StatusForm.ZHUANG_TAI)
                            .duration(StatusDurationType.CHI_XU, 1)
                            .attribute(Attribute.SPEED, -20.0)
                            .addTo();
                }
            });
        }
    }
}
