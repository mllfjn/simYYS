package com.mllfjn.simyys.character.list.sp.spjin;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.list.ssr.bujianyue.StatusJieJieEffect;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamUseSkill;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackType;

import java.util.List;
import java.util.Optional;

// √     战斗开始时与每次回合结束后,唤醒琵琶玄象
// √     lv2-玄象被唤醒时,提升自身100%暴击抵抗,持续2回合
// √     lv3-玄象被唤醒时,提升自身100%效果抵抗,持续2回合
// √     lv4-玄象被唤醒时,提升友方全体160点防御,持续2回合
// √     lv5-玄象每次攻击后,提升[skill3]12%伤害系数(至多24%)
//           TODO (跨回目不消失)
// √     玄象:唤醒后将在初始攻击最高的非召唤物友方回合内协助攻击3次,每次造成攻击62%伤害,攻击后沉眠.若未指定目标,优先攻击生命比例最低的敌方目标

class Skill2 extends PassiveSkill {
    private static final String SkillName = "与世同奏";

    private final Status listener;
    private final Skill skill;

    private boolean useAtStart = true;

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 2);
        this.skill = Skill.getInstance(StatusXX.StatusName);
        listener = new StatusXXListener(belongTo, this);
    }

    @Override
    public void enable() {
        if (useAtStart) {
            getBelongTo().bp.addPriorityMove(getBelongTo(), () -> {
                wakeUp();
                useAtStart = false;
            });
        }
        getBelongTo().addStatus(listener);
    }

    @Override
    public void disable() {
        getBelongTo().removeStatus(listener);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    public void wakeUp() {
        Character belongTo = getBelongTo();
        Character target = new CharacterFinder(belongTo)
                .filterTeammate()
                .filterSummon(false)
                .get(Attribute.INIT_ATTACK, CharacterFinder.Criteria.MAX);

        if (!target.isHaveStatus(StatusXX.class)) {
            target.addStatus(new StatusXX(belongTo, target));
        }

        switch (Math.min(getLevel(), 4)) {
            case 4:
                StatusXXDefence.install(belongTo);
            case 3:
                StatusXXEffectResist.install(belongTo);
            case 2:
                StatusXXCritResist.install(belongTo);
        }
    }

    static class StatusXXListener extends Status implements StatusRunnable {
        private final Skill2 skill2;

        public StatusXXListener(Character character, Skill2 skill2) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
            this.skill2 = skill2;
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ROUND;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            skill2.wakeUp();
            return false;
        }
    }

    class StatusXX extends Status implements StatusRunnable, Displayable {
        private static final String StatusName = "玄象";

        public StatusXX(Character from, Character belongTo) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public String getDisplayText() {
            return StatusName;
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return (trigger == Trigger.USED_SKILL || trigger == Trigger.USED_PU_GONG)
                    && belongTo.isInRound();
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (param instanceof ParamUseSkill pus) {
                Character target;
                Optional<Character> oTarget = pus.getTarget();
                if (oTarget.isPresent() && oTarget.get().team != belongTo.team && oTarget.get().alive) {
                    target = oTarget.get();
                } else {
                    target = new CharacterFinder(belongTo)
                            .filterEnemy()
                            .get(Attribute.HP_PERCENT, CharacterFinder.Criteria.MIN);
                }
                if (target != null) {
                    // TODO 这段为与不见岳奇怪的交互设立,如果后续找到解决方法需要修改
                    Status status;
                    Optional<StatusJieJieEffect> oStatusJJ = from.getStatus(StatusJieJieEffect.class);
                    if (oStatusJJ.isPresent()) {
                        StatusJieJieEffect statusJJ = oStatusJJ.get();
                        status = statusJJ.getYunYi(statusJJ.from, from);
                        from.addStatus(status);
                    } else {
                        status = null;
                    }


                    // lv5-玄象每次攻击后,提升[skill3]12%伤害系数(至多24%)
                    Skill3 skill3 = ((Skill3) from.getSkill(3).orElse(null));
                    boolean increasing = skill3 != null && skill3.isIncreasing(target);

                    from.doInteractive(interactive -> {
                        double multiplier = 62;
                        for (int i = 0; i < 3; i++) {
                            interactive.attackTypical(Skill2.this.skill, target, multiplier, AttackType.DAN_TI);
                            if (increasing) {
                                multiplier *= 1.23;
                            }
                        }
                        Skill2.this.skill.useDone();
                    });

                    if (skill3 != null && getLevel() >= 5) {
                        skill3.increaseMultiplier();
                    }

                    // TODO 这段为与不见岳奇怪的交互设立,如果后续找到解决方法需要修改
                    if (status != null) {
                        from.removeStatus(status);
                    }


                    return true;
                }
            }
            return false;
        }
    }

    static class StatusXXCritResist extends Status implements AttributeModifier {

        private StatusXXCritResist(Character character) {
            super(character, character, StatusType.BUFF, StatusForm.ZHUANG_TAI);
            duration(StatusDurationType.CHI_XU, 2);
        }

        public static void install(Character character) {
            character.getStatus(StatusXXCritResist.class).orElseGet(() -> {
                StatusXXCritResist status = new StatusXXCritResist(character);
                character.addStatus(status);
                return status;
            }).duration(2);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.CRIT_RESIST;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return 100;
        }
    }

    static class StatusXXEffectResist extends Status implements AttributeModifier {

        private StatusXXEffectResist(Character character) {
            super(character, character, StatusType.BUFF, StatusForm.ZHUANG_TAI);
        }

        public static void install(Character character) {
            character.getStatus(StatusXXEffectResist.class).orElseGet(() -> {
                StatusXXEffectResist status = new StatusXXEffectResist(character);
                character.addStatus(status);
                return status;
            }).duration(2);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.EFFECT_RESIST_RATE;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return 100;
        }
    }

    static class StatusXXDefence extends Status implements AttributeModifier {

        public StatusXXDefence(Character from, Character belongTo) {
            super(from, belongTo, StatusType.BUFF, StatusForm.ZHUANG_TAI);
        }

        public static void install(Character from) {
            List<Character> list = new CharacterFinder(from)
                    .filterTeammate()
                    .getList();

            for (Character to : list) {
                to.getStatus(StatusXXDefence.class).orElseGet(() -> {
                    StatusXXDefence status = new StatusXXDefence(from, to);
                    to.addStatus(status);
                    return status;
                }).duration(2);
            }
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.DEFENCE;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return 160;
        }
    }
}
