package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.huangkulou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.instance.StatusStun;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.ratecontroller.RateController;

import java.util.List;
import java.util.Optional;

class Skill4 extends Skill {
    private static final String SkillName = "花海突刺";

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
        interactive.effect(this, targets, 20, 0, true, StatusStun.getSupplier(1));
        // 当场上有部下时,邀战所有部下
        List<Character> bx = new CharacterFinder(belongTo)
                .filterTeammate()
                .filter(c -> c instanceof Skill5.CharacterBX)
                .getList();
        if (!bx.isEmpty()) {
            for (Character character : bx) {
                character.xieZhan(this, RateController
                        .choose(character.name + "攻击目标", targets, Character::getName, bp.calc)
                );
            }
        }
        return Optional.empty();
    }
}
