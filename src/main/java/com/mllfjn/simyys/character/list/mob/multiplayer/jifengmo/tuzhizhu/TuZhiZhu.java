package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.tuzhizhu;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.list.mob.multiplayer.MultiStageManager;
import com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.CharacterJiFengMoBase;
import com.mllfjn.simyys.character.propertygetter.PropertiesHolder;

public class TuZhiZhu extends CharacterJiFengMoBase {
    public static final String CharacterName = "土蜘蛛";

    Skill6 skill6;


    @Override
    public void init(PropertiesHolder propertiesHolder, BattlePane bp) {
        super.init(propertiesHolder, bp);
    }

    @Override
    protected void addStage(MultiStageManager multiStageManager) {
        // 血量在90 70 50 30 时召唤一波小蜘蛛
        multiStageManager.addStage(() -> {
            skill6.forceSummon();
            setHpWithoutTrigger(getMaxHp() * 0.9);
        });
        multiStageManager.addStage(() -> {
            skill6.forceSummon();
            setHpWithoutTrigger(getMaxHp() * 0.7);
        });
        multiStageManager.addStage(() -> {
            skill6.forceSummon();
            setHpWithoutTrigger(getMaxHp() * 0.5);
        });
        multiStageManager.addStage(() -> {
            skill6.forceSummon();
            setHpWithoutTrigger(getMaxHp() * 0.3);
        });
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new Skill1(this));
        addSkill(new Skill2(this));
        Skill4 skill4 = new Skill4(this);
        addSkill(skill4);
        addSkill(new Skill3(this, skill4));
        Skill7 skill7 = new Skill7(this);
        addSkill(new Skill5(this, skill7));
        skill6 = new Skill6(this, skill7);
        addSkill(skill6);
        addSkill(skill7);
    }

    @Override
    protected String getJiFengMoSpeed() {
        return "200";
    }
}
