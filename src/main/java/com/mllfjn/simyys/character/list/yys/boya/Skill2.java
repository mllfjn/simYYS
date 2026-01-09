package com.mllfjn.simyys.character.list.yys.boya;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.ratecontroller.RateController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// √     对指定敌方目标和随机1名敌方目标造成攻击100%的伤害
// √     lv2-lv4-伤害增加10%
// √     lv5-对额外1个目标造成伤害
// √     术印:系数额外提升40,攻击目标增加1(至多增至3)

class Skill2 extends Skill {
    public static final String SkillName = "多重箭";
    private static final int[] baseMultiplier = new int[]{0, 100, 110, 120, 130, 130};

    private final int realMultiplier;
    private final int extraTarget;

    public Skill2(Character belongTo, int level, int shuYin) {
        super(belongTo, level, 0, 0, 2);

        realMultiplier = baseMultiplier[level] + 40 * shuYin;

        extraTarget = Math.min(3, (level == 5 ? 1 : 0) + shuYin);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        Interactive interactive = belongTo.getInteractive();
        Character target = new CharacterFinder(belongTo)
                .filterEnemy()
                .getAutoOrElseRandom();

        if (extraTarget == 0) {
            interactive.attackTypical(this, target, realMultiplier, AttackType.DAN_TI);
        } else {
            List<Character> candidate = new CharacterFinder(belongTo)
                    .filterEnemy()
                    .filter(character -> character != target)
                    .getList();

            List<Character> targets = new ArrayList<>();

            for (int i = 0; i < extraTarget; i++) {
                Character extraTarget = RateController
                        .choose(SkillName + "的额外目标", candidate, Character::getName, belongTo.bp.calc);
                candidate.remove(extraTarget);
                targets.add(extraTarget);
            }

            interactive.attackTypical(this, targets, realMultiplier, AttackType.DAN_TI);
        }

        return Optional.of(target);
    }
}
