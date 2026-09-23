package com.mllfjn.simyys.character.list.sp.luwan;

import com.mllfjn.simyys.character.CharacterShiShenBase;

import java.util.Optional;

public class LuWan extends CharacterShiShenBase {
    public static final String CharacterName = "麓铭大岳丸";

    Skill2Special skill2Special;

    @Override
    protected String getDefaultSkillLevel() {
        return "555";
    }

    @Override
    protected boolean canAwakening() {
        return false;
    }

    @Override
    protected String getDefaultBaseAttack() {
        return "3350";
    }

    @Override
    public boolean isUncontrollable() {
        if (skill2Special != null) {
            return true;
        } else {
            return super.isUncontrollable();
        }
    }

    @Override
    public void round() {
        if (skill2Special != null) {
            skill2Special.use(bp);
        } else {
            super.round();
        }
    }

    @Override
    protected boolean useSkillAuto() {
        Optional<Skill2.StatusYuHun> oStatus = getStatus(Skill2.StatusYuHun.class);
        if (oStatus.isEmpty()) {
            return tryUseSkill(2);
        } else {
            return tryUseSkill(3);
        }
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        addSkill(new Skill2(this, skill2Level));
        addSkill(new Skill3(this, skill3Level));
    }
}
