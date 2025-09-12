package com.mllfjn.simyys.character.mob.shenqilou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import javafx.collections.ObservableList;

import java.util.Map;

public class ShenQiLou extends Character {
    public static final String privateName = "蜃气楼";
    public ShenQiLou() {}

    @Override
    public void addSkill(ObservableList<Skill> skills) {
        skills.add(new Skill4TODO(this));
    }

    @Override
    protected boolean useSkillAuto(BattlePane bp) {
        return getSkill(4).tryUse(bp);
    }
}
