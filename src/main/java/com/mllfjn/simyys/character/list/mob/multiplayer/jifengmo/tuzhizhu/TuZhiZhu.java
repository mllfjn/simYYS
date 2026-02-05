package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.tuzhizhu;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.list.mob.multiplayer.MultiStageManager;
import com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.CharacterJiFengMoBase;
import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;

public class TuZhiZhu extends CharacterJiFengMoBase {
    public static final String CharacterName = "土蜘蛛";

    @Override
    public void init(PropertiesHolder propertiesHolder, BattlePane bp) {
        super.init(propertiesHolder, bp);
    }

    @Override
    protected void addStage(MultiStageManager multiStageManager) {

    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this));
        addSkill(new Skill2(this));
        Skill4 skill4 = new Skill4(this);
        addSkill(skill4);
        addSkill(new Skill3(this, skill4));
        addSkill(new Skill5(this));
        addSkill(new Skill6(this));
        addSkill(new Skill7(this));
    }

    @Override
    protected String getJiFengMoSpeed() {
        return "200";
    }
}
