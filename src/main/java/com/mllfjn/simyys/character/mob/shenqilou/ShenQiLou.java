package com.mllfjn.simyys.character.mob.shenqilou;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Map;

public class ShenQiLou extends Character {
    public static final String privateName = "蜃气楼";
    public ShenQiLou() {
    }

    @Override
    public void initSelf(int[] skillLevels) {
        Map<Integer, Skill> map = getSkills();
        map.put(4, new Skill4TODO(this));
    }

    @Override
    public int[] getUseSkillOrder() {
        return new int[0];
    }
}
