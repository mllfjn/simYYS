package com.mllfjn.simyys.character.list.ssr.yunwaijing;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.triggerParam.ParamStatus;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.interactive.StatusSupplier;

import java.util.List;
import java.util.Optional;

class Skill3Yin extends Skill {
    private static final int[] MULTIPLIER = new int[]{0, 131, 135, 139, 145, 145};

    public Skill3Yin(Character belongTo, int level) {
        super(belongTo, level, "云岸净空", 3, 0, 3);
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        Interactive interactive = belongTo.getInteractive(this);
        int level = getLevel();

        List<Character> enemy = new CharacterFinder(belongTo)
                .filterEnemy()
                .getList();

        interactive.attackTypical(enemy, MULTIPLIER[level], AttackType.QUN_TI);
        interactive.effect(enemy, 100, true, StatusJingNu.getSupplier());

        Character target = new CharacterFinder(belongTo)
                .filterEnemy()
                .getPriorAuto(Attribute.HP, CharacterFinder.Criteria.MAX);

        int heiLian = 1;
        if (level >= 5) {
            heiLian += belongTo.getLostHpPercentage() / 30;
        }
        for (int i = 0; i < heiLian; i++) {
            interactive.attackTypical(target, MULTIPLIER[level], AttackType.DAN_TI);
        }

        return Optional.of(target);
    }

    private static class StatusJingNu extends Status {
        protected StatusJingNu(Character from, Character belongTo) {
            super("镜怒", from, belongTo, StatusType.DEBUFF, StatusForm.ZHUANG_TAI);
            displayName();
            runOn(Trigger.ADDED_STATUS, param -> {
                Status status = ((ParamStatus) param).getStatus();
                if (status.statusType == StatusType.BUFF && status.statusForm == StatusForm.ZHUANG_TAI) {
                    status.delete();
                    delete();
                }
            });
        }

        private static StatusSupplier getSupplier() {
            return new StatusSupplier("镜怒", StatusJingNu.class,
                    (from, to) -> to.addStatus(new StatusJingNu(from, to)));
        }
    }
}
