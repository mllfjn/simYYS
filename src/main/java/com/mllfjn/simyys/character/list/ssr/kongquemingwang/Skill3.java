package com.mllfjn.simyys.character.list.ssr.kongquemingwang;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.StatusAdder;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusDurationType;
import com.mllfjn.simyys.character.status.Trigger;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;

import java.util.List;
import java.util.Optional;

class Skill3 extends Skill {
    public Skill3(Character belongTo, int level) {
        super(belongTo, level, "迷离之境", 2, 0, 3);
        if (level >= 5) {
            belongTo.bp().addPriorityMove(belongTo, this::useWithoutCost);
        }
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        KongQueMingWang belongTo = (KongQueMingWang) getBelongTo();
        int level = getLevel();

        belongTo.getStatus(StatusJieJie.class).ifPresentOrElse(
                status -> status.duration(2),
                () -> new StatusJieJie(belongTo, level >= 3, level >= 4)
        );
        belongTo.addKongQueLing();

        if (level >= 2) {
            List<Character> list = new CharacterFinder(belongTo)
                    .filterEnemy()
                    .getList();
            belongTo.getInteractive(this).effect(list, 40, true, StatusJiaoXie.getSupplier(1));
        }

        return Optional.empty();
    }

    private class StatusJieJie extends Status {
        StatusAdder<Status> adder;

        StatusJieJie(Character character, boolean protectMinHp, boolean reduceDamage) {
            super("雀羽结界", character);
            duration(StatusDurationType.CHI_XU, 2);
            if (reduceDamage) {
                runOn(Trigger.BEING_ATTACKED, param ->
                        ((ParamAttackInfo) param).getAttackInfo().getTraceableNumber().mul(0.7, "雀羽结界"));
            }
            adder = character.bp().addStatusAdder(c -> {
                if (c.team != character.team) {
                    return Status.of("雀羽结界敌方降属性", character, c)
                            .attribute(Attribute.EFFECT_RESIST_RATE, -30)
                            .runOn(Trigger.WHEN_ATTACK, param -> {
                                AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
                                if (attackInfo.getAttackType() == AttackType.DAN_TI) {
                                    attackInfo.getTraceableNumber().mul(0.7, "雀羽结界");
                                }
                            });
                } else if (protectMinHp && !c.isSummon()) {
                    return new StatusBeingAttackListener(character, c);
                } else {
                    return null;
                }
            });
            beforeDelete(adder::deleteAndRemove);
            addTo();
        }

        private boolean isMinHp(Character character) {
            for (Status status : adder.getList()) {
                if (status instanceof StatusBeingAttackListener) {
                    if (status.belongTo.getHp() < character.getHp()) {
                        return false;
                    }
                }
            }
            return true;
        }

        private class StatusBeingAttackListener extends Status {
            protected StatusBeingAttackListener(Character from, Character belongTo) {
                super("雀羽结界受击监听", from, belongTo);
                runOn(Trigger.BEING_ATTACKED, param -> {
                    if (isMinHp(belongTo)) {
                        from.doInteractive(Skill3.this, interactive ->
                                interactive.effect(((ParamAttackInfo) param).getAttackInfo().getAttacker(),
                                        40, true, StatusJiaoXie.getSupplier(1)
                                )
                        );
                    }
                });
            }
        }
    }
}
