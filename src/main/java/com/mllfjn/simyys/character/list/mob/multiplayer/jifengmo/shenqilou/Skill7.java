package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.shenqilou;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.interactive.AttackInfo;

class Skill7 extends PassiveSkill {
    private static final String SkillName = "蜃气升腾";
    private int count = 0;

    public Skill7(Character belongTo) {
        super(belongTo, 0, 7);
        // 蜃气楼每受到3次非暴击伤害，获得[蜃雾笼罩]
        Status.of(SkillName, belongTo)
                .runOn(Trigger.AFTER_ATTACK, triggerParam -> {
                    AttackInfo attackInfo = ((ParamAttackInfo) triggerParam).getAttackInfo();
                    double number = attackInfo.getTraceableNumber().getNumber();
                    if (!attackInfo.isCrit() && number > 0) {
                        count++;
                        if (count == 3) {
                            count = 0;
                            StatusShenWuHuDun.get(belongTo);
                        }
                    }
                }).addTo();
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
