package com.mllfjn.simyys.character.list.ssr.shiling;

import com.mllfjn.simyys.battleevent.StatusAdder;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.ParamUseSkill;

// √     全体友方回合外造成的伤害增加30%
// √     每当友方目标进入饱食时,攻击提升30%,最多叠加5次
// √     lv2-回合外造成的伤害增加至40%
// √     lv3-初始暴击伤害最高的友方额外获得30%协战概率
// √     lv4-回合外造成的伤害增加至50%
// √     lv5-初始暴击伤害最高的友方回合内若选择敌方单体目标,食灵使用普攻进行助战

class Skill2 extends PassiveSkill {
    private static final String SkillName = "热浪";

    private final double multiplier;

    private StatusAdder<?> adder;
    private Character maxCritPower;

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 2);
        this.multiplier = level >= 4 ? 1.5 : level >= 2 ? 1.4 : 1.3;
        if (level >= 3) {
            belongTo.bp.addPriorityMove(belongTo, () -> {
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
        adder = belongTo.bp.addStatusAdder(c ->
                c.team == belongTo.team
                        ? new StatusOutRoundInfluence(belongTo, c, multiplier)
                        : null
        );

        if (getLevel() >= 3) {
            maxCritPower.addStatus(new StatusXieZhanFromShiLing(belongTo, maxCritPower));
        }
    }

    @Override
    public void disable() {
        if (adder != null) {
            adder.deleteAndRemove();
        }
    }

    @Override
    public String getName() {
        return SkillName;
    }

    static class StatusBaoShiAttack extends Status {
        private int stack = 0;

        private StatusBaoShiAttack(Character character) {
            super("饱食加攻击", character);
            attribute(Attribute.ATTACK, _ -> 0.3 * stack * belongTo.getInitBaseAttack());
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
    }

    static class StatusOutRoundInfluence extends Status {
        public StatusOutRoundInfluence(Character from, Character belongTo, double multiplier) {
            super("食灵回合外增伤", from, belongTo);
            runOn(Trigger.WHEN_ATTACK, param -> {
                if (!belongTo.isInRound()) {
                    ((ParamAttackInfo) param).getAttackInfo().getTraceableNumber().mul(multiplier, getName());
                }
            });
        }
    }

    static class StatusXieZhanFromShiLing extends Status {
        public StatusXieZhanFromShiLing(Character from, Character belongTo) {
            super("协战", from, belongTo);
            attribute(Attribute.XIE_ZHAN, 30);
        }
    }

    static class StatusShiLingZhuZhan extends Status {

        public StatusShiLingZhuZhan(Character from, Character belongTo) {
            super("单体目标助战", from, belongTo);
            displayName();
            runOn(Trigger.USED_PU_GONG, Trigger.USED_SKILL, param -> {
                if (belongTo.isInRound()) {
                    ParamUseSkill pus = (ParamUseSkill) param;
                    pus.getTarget().ifPresent(target -> {
                        if (target.team != belongTo.team) {
                            from.xieZhan(pus.getSkill(), target);
                        }
                    });
                }
            });
        }
    }
}
