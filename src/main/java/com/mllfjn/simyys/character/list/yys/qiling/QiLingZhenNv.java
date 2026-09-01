package com.mllfjn.simyys.character.list.yys.qiling;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.interactive.AttackInfo;

import java.io.Serializable;
import java.util.List;

public class QiLingZhenNv implements Serializable {
    static final String QiLingName = "针女";
    private final Skill skill = Skill.getInstance("针女");

    static void install(Character character) {
        new QiLingZhenNv().installBase(character);
    }

    private void installBase(Character character) {
        character.bp().addPriorityMove(character, () -> {
            Character target = new CharacterFinder(character)
                    .filterTeammate()
                    .get(Attribute.ATTACK, CharacterFinder.Criteria.MAX);
            target.addStatus(new StatusZhenNvListener(character, target));
        });
        character.addStatus(new StatusYYSAttackListener(character));
    }

    private class StatusZhenNvListener extends Status {
        private int count;

        public StatusZhenNvListener(Character from, Character belongTo) {
            super(QiLingName + "攻击监听", from, belongTo);
            runOn(Trigger.CAUSE_ATTACK, param -> {
                AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
                if (count == 5) {
                    count = 0;
                    from.doInteractive(interactive -> {
                        interactive.attack(AttackInfo.createRealAttack(from, QiLingZhenNv.this.skill,
                                attackInfo.getTarget(), Math.min(from.getAttack() * 0.5, from.getInitAttack())
                        ));
                        skill.useDone();
                    });
                    QiLingFactory.yuHunEffect(from, QiLingName);
                } else {
                    if (attackInfo.isCrit()) {
                        count++;
                    }
                }
            });
        }
    }

    private class StatusYYSAttackListener extends Status {
        public StatusYYSAttackListener(Character character) {
            super(QiLingName + "攻击监听", character, character);
            retainAfterDie();
            retainAfterChangeWave();
            runOn(Trigger.CAUSE_ATTACK, param -> {
                AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
                if (attackInfo.getSkill() != QiLingZhenNv.this.skill) {
                    disableAction(Trigger.CAUSE_ATTACK);
                    belongTo.bp().addOutRoundSkill(QiLingZhenNv.this.skill, () -> {
                        List<Character> list = new CharacterFinder(belongTo)
                                .filterEnemy()
                                .getList();
                        belongTo.doInteractive(interactive -> {
                            for (int i = 0; i < 3; i++) {
                                interactive.attack(QiLingZhenNv.this.skill, list, c ->
                                        AttackInfo.createRealAttack(belongTo, QiLingZhenNv.this.skill, c,
                                                belongTo.getAttack() * 2
                                        )
                                );
                            }
                        });
                        enableAction(Trigger.CAUSE_ATTACK);
                        QiLingFactory.yuHunEffect(belongTo, QiLingName);
                    });
                }
            });
        }
    }
}
