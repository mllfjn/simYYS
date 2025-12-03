package com.mllfjn.simyys.character.mob.shenqilou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;

class Skill4 extends Skill {
    public static final String SkillName = "强力——钳鳌重击";

    public Skill4(Character belongTo) {
        super(belongTo, 0, 0, 0, 4);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public void usePrivate(BattlePane bp) {
        // 对群体造成攻击400%的伤害

        // 有50%概率眩晕目标1回合
    }

    @Override
    public boolean canUse(BattlePane bp) {
        // 蜃气楼仅在回合开始仍被[蜃气笼罩]，且不处于[蜃气勘破]状态时时，才能用此技能
        // TODO
        return super.canUse(bp);
    }
}
