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

class Skill2 extends Skill implements YingFenShenCopy {
    static final String SkillName = "多重箭";
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
        BoYa boYa = (BoYa) getBelongTo();
        Character target = new CharacterFinder(boYa)
                .filterEnemy()
                .getAutoOrElseRandom();

        use(boYa, target, boYa.getInteractive(), realMultiplier, false);
        boYa.getYinFenShen().ifPresent(yfs -> yfs.usedSkill(this, target));

        return Optional.of(target);
    }

    private void use(Character skillUser, Character target, Interactive interactive, int multiplier, boolean doLog) {

        if (extraTarget == 0) {
            interactive.attackTypical(this, target, realMultiplier, AttackType.DAN_TI);
        } else {
            List<Character> candidate = new CharacterFinder(skillUser)
                    .filterEnemy()
                    .getList();

            candidate.remove(target);

            List<Character> targets = new ArrayList<>();
            targets.add(target);

            for (int i = 0; i < extraTarget && !candidate.isEmpty(); i++) {
                Character extraTarget;
                if (candidate.size() > 1) {
                    extraTarget = RateController
                            .choose(SkillName + "的额外目标", candidate, Character::getName, skillUser.bp.calc);
                } else {
                    extraTarget = candidate.get(0);
                }
                candidate.remove(extraTarget);
                targets.add(extraTarget);
            }

            interactive.attackTypical(this, targets, multiplier, AttackType.DAN_TI);

            if (doLog) {
                log(skillUser, target);
            }
        }
    }

    @Override
    public void copy(Character skillUser, Character target, Interactive interactive, int extraMultiplier) {
        use(skillUser, target, interactive, realMultiplier + extraMultiplier, true);
    }
}
