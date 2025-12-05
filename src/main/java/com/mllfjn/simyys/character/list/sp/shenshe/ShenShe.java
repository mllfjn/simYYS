package com.mllfjn.simyys.character.list.sp.shenshe;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.CharacterShiShenBase;

import java.util.List;

public class ShenShe extends CharacterShiShenBase {
    public static final String CharacterName = "神堕八岐大蛇";

    // 共被6把神剑·天羽羽斩镇压，最后一把是篡位，所以计数是5
    private int zhenyaRest = 5;
    public ShenShe() {}

    public boolean isZhenYa() {
        return zhenyaRest > 0;
    }

    public void poChuZhenYa() {
        if (zhenyaRest > 0) {
            zhenyaRest--;
        }
    }

    @Override
    protected boolean useSkillAuto() {
        return tryUseSkill(2) || tryUseSkill(3);
    }

    @Override
    public void dieHandle() {
        // 阵亡时将封存的攻击全部归还
        List<Character> list = new CharacterFinder(this)
                .setTargetTeam(CharacterFinder.TargetTeam.TEAMMATE)
                .filterShiShen()
                .getList();
        for (Character teammate : list) {
            teammate.removeStatus(StatusStoreAttack.class);
        }
    }

    @Override
    protected String getDefaultBaseAttack() {
        return "4153";
    }

    @Override
    protected String getDefaultSkillLevel() {
        return "555";
    }

    @Override
    protected boolean canAwakening() {
        return false;
    }

    @Override
    public void addOwnSkills() {
        addSkill(new Skill1(this, skill1Level));
        addSkill(new Skill2(this, skill2Level));
        addSkill(new Skill3(this, skill3Level));
    }
}
