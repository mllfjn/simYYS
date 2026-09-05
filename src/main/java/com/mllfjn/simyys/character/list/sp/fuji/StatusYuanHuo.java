package com.mllfjn.simyys.character.list.sp.fuji;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.AttackInfo;

class StatusYuanHuo extends Status {
    private static final String StatusName = "怨火";
    private static final Skill SKILL = Skill.getInstance(StatusName);

    public StatusYuanHuo(Character from, Character belongTo) {
        super(StatusName, from, belongTo);
        type(StatusType.DEBUFF, StatusForm.YIN_JI);
        duration(StatusDurationType.CHI_XU, 1);
        runOn(Trigger.BEFORE_ROUND, _ ->
                from.doInteractive(interactive -> {
                    AttackInfo attackInfo = AttackInfo
                            .createJianJieAttack(from, SKILL, belongTo, from.getAttack());
                    attackInfo.setMultiplier(99);
                    interactive.attack(attackInfo);
                    SKILL.useDone();
                })
        );
    }
}
