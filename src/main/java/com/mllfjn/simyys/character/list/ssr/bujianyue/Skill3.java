package com.mllfjn.simyys.character.list.ssr.bujianyue;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.StatusAdder;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageWhenAttack;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.ParamUseSkill;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;

import java.util.Optional;

class Skill3 extends Skill {
    private static final String SkillName = "水宿山行";

    private final Skill2 skill2;
    private StatusJieJieContainer status;

    public Skill3(Character belongTo, int level, Skill2 skill2) {
        super(belongTo, level, 3, 0, 3);
        this.skill2 = skill2;
        belongTo.bp.addPriorityMove(belongTo, this::useWithoutCost);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        if (status != null) {
            status.refresh();
        } else {
            status = new StatusJieJieContainer(getBelongTo(), getLevel());
            getBelongTo().addStatus(status);
        }
        return Optional.empty();
    }

    class StatusJieJieContainer extends Status {
        private final StatusAdder<StatusJieJieEffect> adder;

        private final boolean reduceCritDamage;
        private final boolean increaseNonCritDamage;
        private final boolean increaseAttack;

        public StatusJieJieContainer(Character character, int level) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
            setDurationType(StatusDurationType.WEI_CHI, 3);
            adder = character.bp.addStatusAdder(c ->
                    c.team == character.team
                            ? new StatusJieJieEffect(character, c)
                            : null
            );
            reduceCritDamage = level >= 2;
            increaseNonCritDamage = level >= 3;
            increaseAttack = level >= 4;
        }

        void refresh() {
            setDuration(3);
            for (StatusJieJieEffect statusJieJieEffect : adder.getList()) {
                statusJieJieEffect.lastSkill = null;
            }
        }

        @Override
        public void beforeDelete() {
            adder.deleteAndRemove();
            Skill3.this.status = null;
        }

        private class StatusJieJieEffect extends Status implements AttributeModifier, StatusRunnable, InfluenceDamageWhenAttack {
            private static final String StatusName = "山行结界";

            private Skill lastSkill;
            private boolean getShanSeAfterRound;

            public StatusJieJieEffect(Character from, Character belongTo) {
                super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
            }

            @Override
            public boolean isAffectAttribute(Attribute attribute) {
                return StatusJieJieContainer.this.increaseAttack && attribute == Attribute.ATTACK;
            }

            @Override
            public double getInfluence(Attribute attribute, StatusModifyParam param) {
                return Math.min(from.getInitDefense() * 0.5, belongTo.getInitAttack() * 0.2);
            }

            @Override
            public boolean runnable(Trigger trigger) {
                return getShanSeAfterRound
                        || (trigger == Trigger.WILL_USE_PU_GONG || trigger == Trigger.WILL_USE_SKILL)
                        || (StatusJieJieContainer.this.reduceCritDamage && trigger == Trigger.BEFORE_ATTACK);
            }

            @Override
            public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
                if (trigger == Trigger.BEFORE_ATTACK) {
                    AttackInfo attackInfo = ((ParamAttackInfo) param).getAttackInfo();
                    if (attackInfo.isCrit()) {
                        attackInfo.getTraceableNumber().mul(0.75, StatusName);
                    }
                } else if (getShanSeAfterRound && trigger == Trigger.AFTER_ROUND) {
                    belongTo.addStatus(new StatusShanSe(from, belongTo));
                    Skill3.this.skill2.getShanSe();
                    getShanSeAfterRound = false;
                } else {
                    Skill skill = ((ParamUseSkill) param).getSkill();
                    if (lastSkill == null || lastSkill == skill) {
                        belongTo.addStatus(new StatusYunYi(from, belongTo));
                        Skill3.this.skill2.getYunYi();
                    } else {
                        getShanSeAfterRound = true;
                    }
                    lastSkill = skill;
                }
                return false;
            }

            @Override
            public void doInfluenceWhenAttack(AttackInfo attackInfo) {
                if (StatusJieJieContainer.this.increaseNonCritDamage && !attackInfo.isCrit()) {
                    attackInfo.getTraceableNumber().mul(1.25, StatusName);
                }
            }

            static class StatusYunYi extends Status implements AttributeModifier {
                public StatusYunYi(Character from, Character belongTo) {
                    super(from, belongTo, StatusType.BUFF, StatusForm.ZHUANG_TAI);
                    setDurationType(StatusDurationType.CHI_XU, 0);
                }

                @Override
                public boolean isAffectAttribute(Attribute attribute) {
                    return attribute == Attribute.ATTACK;
                }

                @Override
                public double getInfluence(Attribute attribute, StatusModifyParam param) {
                    return Math.min(from.getInitDefense() * 2, belongTo.getInitAttack() * 0.4);
                }
            }

            static class StatusShanSe extends StatusShield implements Displayable, AttributeModifier {
                public StatusShanSe(Character from, Character belongTo) {
                    super(from, belongTo, from.getInitDefense() * 3.5);
                    setDurationType(StatusDurationType.CHI_XU, 1);
                }

                @Override
                public boolean isAffectAttribute(Attribute attribute) {
                    return attribute == Attribute.EFFECT_RESIST_RATE;
                }

                @Override
                public double getInfluence(Attribute attribute, StatusModifyParam param) {
                    return 30;
                }

                @Override
                public String getDisplayText() {
                    return "山色";
                }
            }
        }
    }
}
