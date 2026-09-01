package com.mllfjn.simyys.character.list.sp.spjin;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.list.ssr.bujianyue.StatusJieJieEffect;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamUseSkill;
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
        listener = Status.of("会合后唤醒玄象", belongTo)
                .runOn(Trigger.AFTER_ROUND, _ -> wakeUp());
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

        int level = getLevel();
        if (level >= 2) {
            StatusXXSelfBuff.install(belongTo, level >= 3);
            if (level >= 4) {
                StatusXXDefence.install(belongTo);
            }
        }
    }

    class StatusXX extends Status {
        private static final String StatusName = "玄象";

        public StatusXX(Character from, Character belongTo) {
            super(StatusName, from, belongTo);
            displayName();
            runOn(Trigger.USED_SKILL, Trigger.USED_PU_GONG, param -> {
                if (!belongTo.isInRound()) {
                    return;
                }

                ParamUseSkill pus = (ParamUseSkill) param;
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

                    delete();
                }
            });
        }
    }

    static class StatusXXSelfBuff extends Status {
        private StatusXXSelfBuff(Character character, boolean effectResist) {
            super(StatusXX.StatusName + "自身BUFF", character);
            type(StatusType.BUFF, StatusForm.ZHUANG_TAI);
            duration(StatusDurationType.CHI_XU, 2);
            attribute(Attribute.CRIT_RESIST, 100.0);
            if (effectResist) {
                attribute(Attribute.EFFECT_RESIST_RATE, 100.0);
            }
        }

        public static void install(Character character, boolean effectResist) {
            character.addStatusOrChange(StatusXXSelfBuff.class,
                    status -> status.duration(2),
                    () -> new StatusXXSelfBuff(character, effectResist)
            );
        }
    }

    static class StatusXXDefence extends Status {

        public StatusXXDefence(Character from, Character belongTo) {
            super(StatusXX.StatusName + "全队防御", from, belongTo);
            type(StatusType.BUFF, StatusForm.ZHUANG_TAI);
            attribute(Attribute.DEFENCE, 160.0);
            duration(StatusDurationType.CHI_XU, 2);
        }

        public static void install(Character from) {
            List<Character> list = new CharacterFinder(from)
                    .filterTeammate()
                    .getList();

            for (Character to : list) {
                to.addStatusOrChange(StatusXXDefence.class,
                        status -> status.duration(2),
                        () -> new StatusXXDefence(from, to)
                );
            }
        }
    }
}
