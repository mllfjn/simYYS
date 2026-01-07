package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.shenqilou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.instance.StatusStun;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;
import java.util.Optional;

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
    public Optional<Character> usePrivate(BattlePane bp) {
        Interactive interactive = getBelongTo().getInteractive();
        List<Character> targets = new CharacterFinder(getBelongTo())
                .filterEnemy()
                .getList();
        // 对群体造成攻击400%的伤害
        interactive.attackTypical(this, targets, 400, AttackType.QUN_TI);
        // 有50%概率眩晕目标1回合
        interactive.effect(this, targets, 50, false, StatusStun.getSupplier(1));
        return Optional.empty();
    }

    @Override
    public boolean canUse(BattlePane bp) {
        // 蜃气楼仅在回合开始仍被[蜃气笼罩]，且不处于[蜃气勘破]（TODO ？这是什么）
        //  状态时，才能用此技能
        return getBelongTo().isHaveStatus(StatusShenWuHuDun.class) && super.canUse(bp);
    }
}
