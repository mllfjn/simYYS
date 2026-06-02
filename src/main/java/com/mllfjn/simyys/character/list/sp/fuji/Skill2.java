package com.mllfjn.simyys.character.list.sp.fuji;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterSummonBase;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.instance.StatusUnselectable;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;

import java.util.Optional;

class Skill2 extends Skill {
    private static final String SkillName = "蚀骨";

    private boolean canUse = true;

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 0, 0, 2);
    }

    @Override
    public String getSkillDesc() {
        return """
                √\t选择一名非召唤物敌方,施加怨,并召唤出蛇灵
                \t\t每回目限释放1次,唯一效果
                \t受到致命伤害时,扑向带有怨的目标,使其之后受到伤害时失去等量的生命上限(最多降低至20%),之后和蛇灵阵亡
                √\tlv2-蛇灵攻击后提升清姬30%行动条
                √\tlv3-蛇灵被召唤后立即攻击1次
                √\tlv4-蛇灵在场时,清姬受到的伤害降低40%
                √\tlv5-带有怨的目标回合结束后,蛇灵将额外攻击1次
                √\t怨:通用,印记.相对于初始生命上限每损失1%生命值,总攻击降低1%,受到的伤害增加1%
                √\t怨火:行动前受到施加者攻击99%间接伤害
                √\t蛇灵:不可被攻击,继承清姬全部属性,被召唤时移除我方召唤物位置上的召唤物
                √\t\t行动时对带有怨的目标造成攻击200%伤害,并附加[怨火]持续1回合
                \t\t带有怨的目标阵亡时,蛇灵返回,清姬移除自身所有控制效果
                \t\t\t和放逐.
                \t\t并吸取目标40%全体属性(吸取的生命和攻击不超过清姬的对应属性)
                """;
    }

    @Override
    public boolean canUse(BattlePane bp) {
        return canUse && super.canUse(bp);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        canUse = false;
        Character belongTo = getBelongTo();
        // 目标为红标或者攻击最高的敌方单位
        Character target = new CharacterFinder(belongTo)
                .filterEnemy()
                .filterSummon(false)
                .getPriorAuto(Attribute.ATTACK, CharacterFinder.Criteria.MAX);

        // 施加怨
        target.addStatus(new StatusYuan(belongTo, target));
        // 召唤蛇灵
        CharacterSheLing sheLing = new CharacterSheLing(belongTo, target, getLevel());
        bp.addCharacter(sheLing);
        // 隐去状态栏(top)
        sheLing.doIfCharacterIconExist(characterIcon ->
                characterIcon.setVisualEffectTop(node -> node.setVisible(false))
        );

        if (getLevel() >= 3) {
            sheLing.attack();
        }


        return Optional.of(target);
    }

    static class StatusYuan extends Status implements Displayable, AttributeModifier, StatusRunnable {
        private static final String StatusName = "怨";

        private boolean isCounting = false;

        public StatusYuan(Character from, Character belongTo) {
            super(from, belongTo, StatusType.GENERAL, StatusForm.YIN_JI);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return !isCounting && attribute == Attribute.ATTACK;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            double hpPercentCeil = Math.ceil(Attribute.HP_PERCENT.getGetter().apply(belongTo));
            if (hpPercentCeil == 100) {
                return 0;
            }
            isCounting = true;
            double attack = belongTo.getAttack();
            isCounting = false;

            return -attack * (100 - hpPercentCeil) / 100;
        }

        @Override
        public String getDisplayText() {
            return StatusName;
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.BEING_ATTACKED;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            double hpPercentCeil = Math.ceil(Attribute.HP_PERCENT.getGetter().apply(belongTo));
            if (hpPercentCeil <= 100) {
                ((ParamAttackInfo) param).getAttackInfo().getTraceableNumber()
                        .mul((1 + 0.01 * (100 - hpPercentCeil)), StatusName);
            }
            return false;
        }
    }

    static class CharacterSheLing extends CharacterSummonBase {
        private static final String CharacterName = "蛇灵";

        private final Skill skill;

        private final Status StatusLv4ReduceDamage;

        public CharacterSheLing(Character owner, Character target, int level) {
            super(owner.bp, "蛇灵", owner.team);

            // 移除我方召唤物位置上的召唤物
            Character summon = new CharacterFinder(owner)
                    .filterTeammate()
                    .filterSummon(true)
                    .getFirst();
            if (summon != null) {
                summon.die();
            }

            addStatus(new StatusUnselectable(this, this));

            setInitBaseAttack(owner.getInitBaseAttack());
            setInitAdditionAttack(owner.getInitAdditionAttack());
            setInitCritRate(owner.getInitCritRate());
            setInitCritPower(owner.getInitCritPower());
//            setInitSpeed(owner.getInitSpeed());
            setInitSpeed(owner.getSpeed());
            forceSetMaxHp(999, true);

            final boolean isLv2 = level >= 2;

            skill = new Skill(this, -1, 0, 0, 1) {
                @Override
                public String getName() {
                    return CharacterName;
                }

                @Override
                public Optional<Character> usePrivate(BattlePane bp) {
                    getBelongTo().doInteractive(interactive -> {
                        interactive.attackTypical(this, target, 200, AttackType.DAN_TI);
                        target.addStatus(new StatusYuanHuo(CharacterSheLing.this, target));
                        if (isLv2) {
                            interactive.increaseLocation(owner, 30);
                        }
                    });
                    return Optional.of(target);
                }
            };

            if (level >= 4) {
                StatusLv4ReduceDamage = new StatusLv4ReduceDamage(this, owner);
                owner.addStatus(StatusLv4ReduceDamage);
            } else {
                StatusLv4ReduceDamage = null;
            }

            if (level >= 5) {
                target.addStatus(new StatusLv5AfterRoundListener(this, target));
            }
        }

        @Override
        public void round() {
            attack();
        }

        @Override
        public boolean isUncontrollable() {
            return true;
        }

        private void attack() {
            skill.useWithoutCost();
        }

        @Override
        public void beforeDie(AttackInfo attackInfo, double excessDamage) {
            if (StatusLv4ReduceDamage != null) {
                StatusLv4ReduceDamage.delete();
            }
        }

        static class StatusLv4ReduceDamage extends Status implements StatusRunnable {
            public StatusLv4ReduceDamage(Character from, Character belongTo) {
                super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
            }

            @Override
            public boolean runnable(Trigger trigger) {
                return trigger == Trigger.BEING_ATTACKED;
            }

            @Override
            public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
                ((ParamAttackInfo) param).getAttackInfo().getTraceableNumber().mul(0.6, CharacterName);
                return false;
            }
        }

        class StatusLv5AfterRoundListener extends Status implements StatusRunnable {

            public StatusLv5AfterRoundListener(Character from, Character belongTo) {
                super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
            }

            @Override
            public boolean runnable(Trigger trigger) {
                return trigger == Trigger.AFTER_ROUND;
            }

            @Override
            public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
                CharacterSheLing.this.attack();
                return false;
            }
        }
    }
}
