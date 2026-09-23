package com.mllfjn.simyys.character.list.ssr.geye;

import com.mllfjn.simyys.character.CharacterShiShenBase;

import java.util.Optional;

public class GeYe extends CharacterShiShenBase {
    public static final String CharacterName = "葛叶";

    @Override
    protected boolean useSkillAuto() {
        // 如果不在大妖姿态,考虑要不要变身,否则直接放4
        if (!isHaveStatus(StatusDaYao.class)) {
            Optional<StatusJiuWei> oStatusJiuWei = getStatus(StatusJiuWei.class);
            // 如果有3层九尾之力,变身
            if (oStatusJiuWei.isPresent()) {
                if (oStatusJiuWei.get().getStack() == 3) {
                    if (tryUseSkill(3)) {
                        return true;
                    }
                }
            }
        }
        return tryUseSkill(4);
    }

    @Override
    protected String getDefaultSkillLevel() {
        return "555";
    }

    @Override
    protected boolean canAwakening() {
        return true;
    }

    @Override
    protected String getDefaultBaseAttack() {
        return "3966";
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        addSkill(new Skill2(this, skill2Level));
        Skill3 skill3 = new Skill3(this, skill2Level);
        addSkill(skill3);
        addSkill(new Skill4(this, skill3Level));
        addSkill(new Skill3Special_or_Skill5(this, skill2Level, skill3));
    }
}
