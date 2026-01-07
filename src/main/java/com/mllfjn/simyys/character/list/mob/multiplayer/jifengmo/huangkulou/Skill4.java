package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.huangkulou;

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
    public static final String SkillName = "花海突刺";

    public Skill4(Character belongTo) {
        super(belongTo, 0, 2, 4, 4);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        Interactive interactive = belongTo.getInteractive();
        List<Character> targets = new CharacterFinder(belongTo)
                .filterEnemy()
                .getList();
        // 对全体玩家造成1段伤害
        interactive.attackTypical(this, targets, 150, AttackType.QUN_TI);
        // 并附带20%的概率眩晕玩家
        interactive.effect(this, targets, 20, true, StatusStun.getSupplier(1));
        // TODO 当场上有部下时,邀战所有部下
        return Optional.empty();
    }
}
