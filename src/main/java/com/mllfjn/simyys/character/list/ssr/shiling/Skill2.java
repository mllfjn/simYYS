package com.mllfjn.simyys.character.list.ssr.shiling;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageWhenAttack;
import com.mllfjn.simyys.character.status.triggerParam.ParamUseSkill;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;

import java.util.List;

// √     全体友方回合外造成的伤害增加30%
// √     每当友方目标进入饱食时,攻击提升30%,最多叠加5次
// √     lv2-回合外造成的伤害增加至40%
// √     lv3-初始暴击伤害最高的友方额外获得30%协战概率
// √     lv4-回合外造成的伤害增加至50%
// √     lv5-初始暴击伤害最高的友方回合内若选择敌方单体目标,食灵使用普攻进行助战

class Skill2 extends PassiveSkill {
    private static final String SkillName = "热浪";

    private final double multiplier;

    private BattleActionListener listener = null;
    private Character maxCritPower;

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 2);
        this.multiplier = level >= 4 ? 1.5 : level >= 2 ? 1.4 : 1.3;
        if (level >= 3) {
            belongTo.bp.atBattleStart(() -> {
                maxCritPower = new CharacterFinder(belongTo)
                        .filterTeammate()
                        .filterSelf()
                        .get(Attribute.CRIT_POWER, CharacterFinder.Criteria.MAX);

                if (level >= 5) {
                    maxCritPower.addStatus(new StatusShiLingZhuZhan(belongTo, maxCritPower));
                }

                enable();
            });
        }
    }

    @Override
    public void enable() {
        // 如果有3级以上，那么需要等到战斗开始时判断最高爆伤之后再执行
        if (maxCritPower == null && getLevel() >= 3) {
            return;
        }

        Character belongTo = getBelongTo();
        listener = belongTo.bp.forEveryone(belongTo, character -> {
            if (character.team == belongTo.team) {
                character.addStatus(new StatusOutRoundInfluence(belongTo, character, multiplier));
            }
        });

        if (getLevel() >= 3) {
            maxCritPower.addStatus(new StatusXieZhanFromShiLing(belongTo, maxCritPower));
        }
    }

    @Override
    protected void disable() {
        if (listener != null) {
            List<Character> targets = new CharacterFinder(getBelongTo())
                    .filterTeammate()
                    .getList();

            for (Character target : targets) {
                target.removeStatus(StatusOutRoundInfluence.class);
                target.removeStatus(StatusXieZhanFromShiLing.class);
            }

            getBelongTo().bp.removeActionListener(getBelongTo(), listener);
        }
    }

    @Override
    public String getName() {
        return SkillName;
    }

    static class StatusBaoShiAttack extends Status implements AttributeModifier {
        private int stack = 0;

        private StatusBaoShiAttack(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        public static void addStack(Character character) {
            character.getStatus(StatusBaoShiAttack.class)
                    .orElseGet(() -> new StatusBaoShiAttack(character))
                    .addStack();
        }

        private void addStack() {
            if (stack < 5) {
                stack++;
            }
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.ATTACK;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return belongTo.getInitBaseAttack() * 0.3 * stack;
        }
    }

    static class StatusOutRoundInfluence extends Status implements InfluenceDamageWhenAttack {
        private final double multiplier;

        public StatusOutRoundInfluence(Character from, Character belongTo, double multiplier) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
            this.multiplier = multiplier;
        }

        @Override
        public void doInfluenceWhenAttack(AttackInfo attackInfo) {
            if (!belongTo.isInRound()) {
                attackInfo.getTraceableNumber().mul(multiplier, "食灵回合外增伤");
            }
        }
    }

    static class StatusXieZhanFromShiLing extends Status implements AttributeModifier {
        public StatusXieZhanFromShiLing(Character from, Character belongTo) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.XIE_ZHAN;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return 30;
        }
    }

    static class StatusShiLingZhuZhan extends Status implements StatusRunnable, Displayable {

        public StatusShiLingZhuZhan(Character from, Character belongTo) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return belongTo.isInRound() && (trigger == Trigger.USE_PU_GONG || trigger == Trigger.USED_SKILL);
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (param instanceof ParamUseSkill pus) {
                pus.getTarget().ifPresent(target -> from.xieZhan(pus.getSkill(), target));
            }
            return false;
        }

        @Override
        public String getDisplayText() {
            return "协战";
        }
    }
}
