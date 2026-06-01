package com.mllfjn.simyys.character.list.ssr.xueyuqian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;
import java.util.Optional;

class Skill3 extends Skill {
    private static final String SkillName = "千雪·斩";

    private final StatusWLXR statusWLXR;

    public Skill3(Character belongTo, int level, StatusWLXR statusWLXR) {
        super(belongTo, level, 3, 0, 3);
        this.statusWLXR = statusWLXR;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        int level = getLevel();
        Interactive interactive = belongTo.getInteractive();
        // lv2-刀光伤害系数
        final int daoGuangMultiplier = level >= 2 ? 27 : 18;
        // lv3-雪爆伤害系数
        final int xueBaoMultiplier = level >= 3 ? 108 : 88;
        // lv4-指定敌方伤害提升
        final boolean increaseTarget = level >= 4;
        // lv5-击败目标转火
        final boolean canSwitchTarget = level >= 5;

        Character skillTarget = new CharacterFinder(belongTo)
                .filterEnemy()
                .getPriorAuto(Attribute.HP, CharacterFinder.Criteria.MAX);
        Character target = skillTarget;

        for (int i = 0; i < 9; i++) {
            if (!target.alive) {
                if (!canSwitchTarget) {
                    break;
                } else {
                    target = new CharacterFinder(belongTo)
                            .filterEnemy()
                            .getPriorAuto(Attribute.HP, CharacterFinder.Criteria.MIN);
                }
            }

            if (statusWLXR.evolve()) {
                List<Character> list = new CharacterFinder(belongTo)
                        .filterEnemy()
                        .getList();
                Character thisTarget = target;
                interactive.attack(this, list, c -> {
                    AttackInfo attackInfo = AttackInfo
                            .createTypicalAttack(belongTo, this, c, xueBaoMultiplier, AttackType.QUN_TI);
                    if (increaseTarget && c == thisTarget) {
                        attackInfo.getTraceableNumber().mul(1.5, "雪爆指定目标");
                    }
                    return attackInfo;
                });
            } else {
                interactive.attackTypical(this, target, daoGuangMultiplier, AttackType.DAN_TI);
                statusWLXR.addStack();
            }
        }


        return Optional.of(skillTarget);
    }
}
