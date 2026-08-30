package com.mllfjn.simyys.character.list.sp.luwan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.*;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.IgnoreDebuff;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;

import java.util.Optional;

class Skill2 extends Skill {
    private static final String SkillName = "铭海之主";

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 0, 0, 2);
        if (level >= 5) {
            belongTo.bp.addPriorityMove(belongTo, this::useWithoutCost);
        }
    }

    @Override
    public String getSkillDesc() {
        return """
                √\t进入驭魂形态,持续2回合,提升100%效果抵抗
                √\t其他友方释放妖术技能后,获得1层麓泽
                √\t驭魂形态下,场上有3名或以上非召唤物单位阵亡时,移除自身所有状态和印记
                √\t\t进入归骸形态,提升100点速度,受到的伤害降低50%,免疫减益
                \t\t\t和放逐
                √\t\t造成伤害时附带12%吸血,自身行动回合自动释放[逆魂尽断]
                √\tlv2-驭魂形态下自身受到的单体伤害降低50%
                √\tlv3-驭魂形态下任意非召唤物单位阵亡时,恢复生命上限30%的生命
                √\tlv4-驭魂形态持续3回合
                √\tlv5-先机:释放该技能
                √\t麓泽:增益,印记:上限4层,每层使[断末无铭]的鬼火消耗降低1点,释放[断末无铭]后消耗所有麓泽
                """;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        int level = getLevel();
        int duration = level >= 4 ? 3 : 2;

        belongTo.getStatus(StatusYuHun.class).ifPresentOrElse(
                status -> status.duration(duration),
                () -> belongTo.addStatus(new StatusYuHun(belongTo, duration, level))
        );
        return Optional.empty();
    }

    class StatusYuHun extends Status {
        private static final String StatusName = "驭魂";

        private final BattleActionListener listener;
        private final StatusAdder<?> adder;

        // true:监听单位死亡事件,false:回合结束后变身 能跑就行
        private boolean listenerType = true;

        public StatusYuHun(Character character, int duration, int level) {
            super(StatusName, character);
            type(StatusType.BUFF, StatusForm.ZHUANG_TAI);

            listener = new BattleActionListener(character) {
                @Override
                public boolean onBattleAction(BattleEvent event) {
                    if (!listenerType && event instanceof EventActionDone) {
                        transform();
                        return true;
                    } else if (listenerType && event instanceof EventCharacterDie) {
                        if (level >= 3) {
                            belongTo.doInteractive(interactive ->
                                    interactive.recovery(Skill2.this, belongTo, belongTo.getMaxHp() * 0.3)
                            );
                        }
                        check();
                    }
                    return false;
                }
            };

            adder = belongTo.bp.addStatusAdder(c ->
                    c.team == belongTo.team && c != belongTo
                            ? Status.of(SkillName + "监听技能", character, c)
                            .runOn(Trigger.USED_SKILL, _ -> StatusLuZe.addStack(character))
                            : null
            );

            character.bp.addActionListener(listener);

            duration(StatusDurationType.WEI_CHI, duration);
            beforeDelete(() -> {
                belongTo.bp.removeActionListener(listener);
                adder.deleteAndRemove();
            });
            attribute(Attribute.EFFECT_RESIST_RATE, _ -> 100.0);
            displayNameAndDuration();
            if (level >= 2) {
                runOn(Trigger.BEING_ATTACKED, triggerParam -> {
                    AttackInfo attackInfo = ((ParamAttackInfo) triggerParam).getAttackInfo();
                    if (attackInfo.getAttackType() == AttackType.DAN_TI) {
                        attackInfo.getTraceableNumber().mul(0.5, StatusName);
                    }
                });
            }
        }

        private void transform() {
            belongTo.deleteStatusIf(status -> status.statusType != StatusType.SPECIAL);
            belongTo.addStatus(new StatusGuiHai(belongTo));
            ((LuWan) belongTo).skill2Special = new Skill2Special(belongTo);
        }

        private void check() {
            int count = 0;
            for (Character deadCharacter : belongTo.bp.situation.team0DeadCharacters) {
                if (!deadCharacter.isSummon()) {
                    if (count == 2) {
                        listenerType = false;
                        return;
                    } else {
                        count++;
                    }
                }
            }

            for (Character deadCharacter : belongTo.bp.situation.team1DeadCharacters) {
                if (!deadCharacter.isSummon()) {
                    if (count == 2) {
                        listenerType = false;
                        return;
                    } else {
                        count++;
                    }
                }
            }
        }
    }

    class StatusGuiHai extends Status
            implements AttributeModifier, IgnoreDebuff, StatusRunnable {
        private static final String StatusName = "归骸形态";

        public StatusGuiHai(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.SPEED;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return 100;
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.CAUSE_ATTACK || trigger == Trigger.BEING_ATTACKED;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
            if (trigger == Trigger.CAUSE_ATTACK) {
                double number = attackInfo.getTraceableNumber().getNumber();
                belongTo.doInteractive(interactive ->
                        interactive.recovery(Skill2.this, belongTo, number * 0.12)
                );
            } else {
                attackInfo.getTraceableNumber().mul(0.5, StatusName);
            }
            return false;
        }
    }
}
