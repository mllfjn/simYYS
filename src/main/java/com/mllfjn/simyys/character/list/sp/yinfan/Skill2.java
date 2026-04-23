package com.mllfjn.simyys.character.list.sp.yinfan;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.guihuo.SubstituteProvider;

import java.util.List;
import java.util.Optional;

// √     创造存在1回合的幻境并获得3点愿力.
// √     幻境中自身效果抵抗增加80%
// √     除自身外的友方式神释放技能时,获得愿佑,维持1回合(实际是持续1回合,回合内的话持续2回合)
// √     lv2-幻境中御魂产生的鬼火优先转化为等量愿力
// √     lv3-幻境中自身回合开始时获得3点愿力
// √     lv4-幻境效果增加为2回合
// √     lv5-先机:释放[skill2] (好像是无消耗的)
// √     愿力:通用,印记:最多储存8点.当友方目标释放技能鬼火不足时,可使用等量愿力代替
// √     愿佑:增益,印记:获得因幡辉夜姬暴击伤害和防御属性的30%,最多获得不超过120%暴击伤害和200防御

class Skill2 extends Skill {
    private static final String SkillName = "愿满夜";

    private final int skill3Level;

    public Skill2(Character belongTo, int level, int skill3Level) {
        super(belongTo, level, 2, 0, 2);
        this.skill3Level = skill3Level;

        belongTo.bp.atBattleStart(() -> useWithoutCost());
    }

    public void reduceCost() {
        setCost(1);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character belongTo = getBelongTo();
        int level = getLevel();

        StatusHuanJing.create(belongTo, level, skill3Level);
        StatusYuanLi.addYuanLi(belongTo, 3, level, skill3Level);

        setCost(2);

        return Optional.empty();
    }

    static class StatusHuanJing extends Status implements StatusRunnable, AttributeModifier {
        private final boolean getYuanLiBeforeRound;

        private final int skill2Level;
        private final int skill3Level;

        public StatusHuanJing(Character character, int duration, int skill2Level, int skill3Level) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
            this.skill2Level = skill2Level;
            this.skill3Level = skill3Level;
            this.getYuanLiBeforeRound = skill2Level >= 3;
            setDurationType(StatusDurationType.WEI_CHI, duration);

            character.bp.forEveryone(character, c -> {
                if (c.team == character.team && c != character && !c.isYYS()) {
                    c.addStatus(new StatusUseSkillListener(character, c));
                }
            });
        }

        public static void create(Character character, int skill2Level, int skill3Level) {
            int duration = skill2Level >= 4 ? 2 : 1;
            character.getStatus(StatusHuanJing.class).ifPresentOrElse(
                    status -> status.setDuration(duration)
                    , () -> character.addStatus(new StatusHuanJing(character, duration, skill2Level, skill3Level))
            );
        }

        @Override
        public void beforeDelete() {
            List<Character> list = new CharacterFinder(belongTo)
                    .filterTeammate()
                    .filterShiShen()
                    .filterSelf()
                    .getList();

            for (Character character : list) {
                character.removeStatus(StatusUseSkillListener.class);
            }
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.BEFORE_ROUND && getYuanLiBeforeRound;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            StatusYuanLi.addYuanLi(belongTo, 3, skill2Level, skill3Level);
            return false;
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.EFFECT_RESIST_RATE;
        }

        @Override
        public double getInfluence(Attribute attribute) {
            return 80;
        }

        static class StatusUseSkillListener extends Status implements StatusRunnable {

            public StatusUseSkillListener(Character from, Character belongTo) {
                super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
            }

            @Override
            public boolean runnable(Trigger trigger) {
                return trigger == Trigger.WILL_USE_SKILL;
            }

            @Override
            public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
                StatusYuanYou.install(from, belongTo);
                return false;
            }
        }
    }

    static class StatusYuanLi extends Status implements Displayable, SubstituteProvider {
        private static final String StatusName = "愿力";

        private final int skill2Level;
        private final int skill3Level;

        private int stack;

        public StatusYuanLi(Character character, int skill2Level, int skill3Level) {
            super(character, character, StatusType.GENERAL, StatusForm.YIN_JI);
            this.skill2Level = skill2Level;
            this.skill3Level = skill3Level;
            character.bp.setSubstituteProvider(character.team, this);
        }

        public static void addYuanLi(Character character, int num, int skill2Level, int skill3Level) {
            StatusYuanLi status = character.getStatus(StatusYuanLi.class).orElseGet(() -> {
                StatusYuanLi newStatus = new StatusYuanLi(character, skill2Level, skill3Level);
                character.addStatus(newStatus);
                return newStatus;
            });

            status.stack = Math.min(8, status.stack + num);
        }

        @Override
        public String getDisplayText() {
            return StatusName + stack;
        }

        @Override
        public int getGuiHuo(int num, boolean isFromYuHun) {
            if (skill2Level >= 2 && isFromYuHun && stack < 8) {
                int spaceLeft = 8 - stack;
                if (num <= spaceLeft) {
                    stack += num;
                    return 0;
                } else {
                    stack = 8;
                    return num - spaceLeft;
                }
            } else {
                return num;
            }
        }

        @Override
        public boolean canUse(int num) {
            return stack >= num;
        }

        @Override
        public void use(int num) {
            stack -= num;
            if (skill3Level >= 2) {
                StatusYuanLiCritPower.addStack(belongTo, num);
                if (skill3Level >= 3) {
                    StatusYuanLiShield.addShield(belongTo);
                }
            }
        }

        @Override
        public String getSubstituteProviderName() {
            return StatusName;
        }

        static class StatusYuanLiCritPower extends Status implements AttributeModifier {
            private int stack = 0;

            public StatusYuanLiCritPower(Character character) {
                super(character, character, StatusType.BUFF, StatusForm.YIN_JI);
            }

            public static void addStack(Character character, int num) {
                StatusYuanLiCritPower status = character.getStatus(StatusYuanLiCritPower.class).orElseGet(() -> {
                    StatusYuanLiCritPower newStatus = new StatusYuanLiCritPower(character);
                    character.addStatus(newStatus);
                    return newStatus;
                });

                status.stack = Math.min(24, status.stack + num);
            }

            @Override
            public boolean isAffectAttribute(Attribute attribute) {
                return attribute == Attribute.CRIT_POWER;
            }

            @Override
            public double getInfluence(Attribute attribute) {
                return stack * 5;
            }
        }

        static class StatusYuanLiShield extends StatusShield {

            public StatusYuanLiShield(Character character, double shield) {
                super(character, character, shield);
                setDurationType(StatusDurationType.CHI_XU, 2);
            }

            public static void addShield(Character character) {
                int count = 0;
                for (Status status : character.getStatuses()) {
                    if (status instanceof StatusYuanLiShield) {
                        count++;
                        if (count == 8) {
                            return;
                        }
                    }
                }

                character.addStatus(new StatusYuanLiShield(character, character.getMaxHp() * 0.12));
            }
        }
    }
}
