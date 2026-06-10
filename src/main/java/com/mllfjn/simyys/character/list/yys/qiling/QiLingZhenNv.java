package com.mllfjn.simyys.character.list.yys.qiling;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.RetainAfterChangeWave;
import com.mllfjn.simyys.character.status.determinant.RetainAfterDie;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
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
        character.getBp().addPriorityMove(character, () -> {
            Character target = new CharacterFinder(character)
                    .filterTeammate()
                    .get(Attribute.ATTACK, CharacterFinder.Criteria.MAX);
            target.addStatus(new StatusZhenNvListener(character, target));
        });
        character.addStatus(new StatusYYSAttackListener(character));
    }

    private class StatusZhenNvListener extends Status implements StatusRunnable {
        private int count;

        public StatusZhenNvListener(Character from, Character belongTo) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.CAUSE_ATTACK;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
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
            return false;
        }
    }

    private class StatusYYSAttackListener extends Status
            implements StatusRunnable, RetainAfterChangeWave, RetainAfterDie {
        private boolean added;

        public StatusYYSAttackListener(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.CAUSE_ATTACK && !added;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
            if (attackInfo.getSkill() != QiLingZhenNv.this.skill) {
                added = true;
                belongTo.getBp().addOutRoundSkill(QiLingZhenNv.this.skill, () -> {
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
                    added = false;
                    QiLingFactory.yuHunEffect(belongTo, QiLingName);
                });
            }
            return false;
        }
    }
}
