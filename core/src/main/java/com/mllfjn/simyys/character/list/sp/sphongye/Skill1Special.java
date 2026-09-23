package com.mllfjn.simyys.character.list.sp.sphongye;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.Comparator;
import java.util.Optional;

class Skill1Special extends Skill1PuGongBase {
    private static final String SkillName = "叶刃";
    private static final int[] multiplier = new int[]{0, 75, 85, 85, 100, 100};
    private static final int[] bounceTimes = new int[]{0, 1, 1, 2, 2, 3};

    private final StatusLinYin statusLinYin;

    public Skill1Special(Character belongTo, int level, StatusLinYin statusLinYin) {
        super(belongTo, level);
        this.statusLinYin = statusLinYin;
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        Character belongTo = getBelongTo();
        int level = getLevel();
        interactive.attackTypical(this, target, multiplier[level], AttackType.DAN_TI);

        Character lastTarget = target;
        Comparator<Character> comparator = Comparator
                .comparingInt(c -> c.getStatus(StatusYeJin.class)
                        .map(StatusYeJin::getStack)
                        .orElse(0)
                );
        for (int i = 0; i < bounceTimes[level]; i++) {
            final Character temp = lastTarget;
            Optional<Character> oTarget = new CharacterFinder(belongTo)
                    .filterEnemy()
                    .filter(character -> character != temp)
                    .max(comparator);
            if (oTarget.isEmpty()) {
                return;
            }

            Character next = oTarget.get();
            interactive.attackTypical(this, next, multiplier[level], AttackType.DAN_TI);
            lastTarget = next;
        }

        // 回合内释放减少1层林隐
        if (belongTo.isInRound()) {
            statusLinYin.reduceStack();
        }
    }

    @Override
    public String getName() {
        return SkillName;
    }
}
