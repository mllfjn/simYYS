package com.mllfjn.simyys.character.list.ssr.kongquemingwang;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;
import java.util.Optional;

class Skill3Special extends Skill {
    private static final int[] EXTRA_MULTIPLIER = new int[]{0, 45, 50, 55, 60, 60};

    boolean firstUse = true;

    private final Status ignoreDefense;

    public Skill3Special(Character belongTo, int level) {
        super(belongTo, level, "雀舞斩棘", 3, 0, 3);
        if (level >= 5) {
            ignoreDefense = Status.of("无视防御", belongTo)
                    .attribute(Attribute.IGNORE_DEFENCE, 240);
        } else {
            ignoreDefense = null;
        }
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        int level = getLevel();
        Interactive interactive = belongTo.getInteractive(this);
        List<Character> list = new CharacterFinder(belongTo)
                .filterEnemy()
                .getList();

        interactive.effect(list, 100, true, StatusJiaoXie.getSupplier(2));

        int baseTimes = 4;
        if (firstUse) {
            baseTimes *= 2;
        }
        for (int i = 0; i < baseTimes; i++) {
            interactive.attackTypical(list, 45, AttackType.QUN_TI);
        }

        int extraTimes = 2;
        if (firstUse) {
            extraTimes *= 2;
        }
        beforeExtraAttack();
        for (int i = 0; i < extraTimes; i++) {
            interactive.attackTypical(list, EXTRA_MULTIPLIER[level], AttackType.QUN_TI);
        }
        afterExtraAttack();

        firstUse = false;

        return Optional.empty();
    }

    private void beforeExtraAttack() {
        if (ignoreDefense != null) {
            ignoreDefense.addTo();
        }
    }

    private void afterExtraAttack() {
        if (ignoreDefense != null) {
            getBelongTo().removeStatus(ignoreDefense);
        }
    }
}
