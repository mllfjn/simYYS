package com.mllfjn.simyys.character.list.ssr.dishitian;

import com.mllfjn.simyys.character.CharacterShiShenBase;
import com.mllfjn.simyys.character.status.*;

public class DiShiTian extends CharacterShiShenBase {
    public static final String CharacterName = "帝释天";

    int enemyCount;

    private StatusJinLian jinLian;

    @Override
    protected boolean useSkillAuto() {
        if (jinLian == null && tryUseSkill(2)) {
            return true;
        }
        return tryUseSkill(3);
    }

    void addJinLian(StatusJinLian newJinLian) {
        if (jinLian != null) {
            jinLian.delete();
        }
        jinLian = newJinLian;
    }

    void removeJinLian() {
        jinLian = null;
    }

    StatusJinLian getJinLian() {
        return jinLian;
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
        return "3109";
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        addSkill(new Skill2(this, skill2Level));
        addSkill(new Skill3(this, skill3Level));
    }
}
