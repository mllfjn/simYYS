package com.mllfjn.simyys.character.list.sr.qingji;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.AttackInfo;

class StatusFenHuo extends Status implements Displayable {
    private Skill skill;
    private int multiplier;

    private StatusFenHuo(Character from, Character belongTo, Skill skill, int multiplier) {
        super(from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
        this.skill = skill;
        this.multiplier = multiplier;

        duration(StatusDurationType.CHI_XU, 1);
    }

    static void install(Character from, Character belongTo, Skill skill, int multiplier) {
        belongTo.getStatus(StatusFenHuo.class)
                .ifPresentOrElse(
                        status -> status.replace(skill, multiplier),
                        () -> belongTo.addStatus(new StatusFenHuo(from, belongTo, skill, multiplier))
                );
    }

    private void replace(Skill newSkill, int newMultiplier) {
        if (multiplier < newMultiplier) {
            skill = newSkill;
            multiplier = newMultiplier;
        }
    }

    @Override
    public void beforeDelete() {
        from.doInteractive(interactive -> {
            AttackInfo attackInfo = AttackInfo.createJianJieAttack(from, skill, belongTo, from.getAttack());
            attackInfo.setMultiplier(multiplier);
            interactive.attack(attackInfo);
            skill.useDone();
        });
    }

    @Override
    public String getDisplayText() {
        // 这个状态在技能里显示焚火,但是在状态栏显示蛇袭
        return "蛇袭";
    }
}
