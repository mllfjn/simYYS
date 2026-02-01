package com.mllfjn.simyys.character.list.ssr.shenwuyue;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

import java.util.List;
import java.util.Optional;

// √     创造存在1回合的幻境(好像是"持续")
// √     使非召唤物友方目标获得2层入梦,维持2回合
// √     lv2-幻境中自身和入梦携带者回合前,获得1点鬼火
// √     lv3-幻境效果增加为2回合
// √     lv4-消耗鬼火减少2点
// √     lv5-使目标下一回合内获得美梦必成
// √     入梦:增益,印记.至多2层,友方场上至多存在1个此效果.携带者施放妖术消耗鬼火减少3点并消耗1层
// √     美梦必成:自身初始4件套御魂效果必定触发

class Skill3 extends Skill {
    public static final String SkillName = "美梦赐予";

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, level >= 4 ? 0 : 2, 0, 3);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        ShenWuYue belongTo = ((ShenWuYue) getBelongTo());
        int level = getLevel();
        Character target = new CharacterFinder(belongTo)
                .filterTeammate()
                .filterSummon(false)
                .filterSelf()
                .getPriorAuto(Attribute.ATTACK, CharacterFinder.Criteria.MAX);

        target.addStatus(new StatusRuMeng(belongTo, target));

        Skill2.StatusMengShen statusMengShen = belongTo.addStack();

        StatusHuanJingListener.add(belongTo, level >= 3 ? 2 : 1, level >= 2, statusMengShen);


        if (level >= 5) {
            target.addStatus(new StatusMeiMengJiangCheng(belongTo, target));
        }

        return Optional.of(target);
    }

    static class StatusHuanJingListener extends Status {
        private BattleActionListener listener;

        private StatusHuanJingListener(Character character, int duration) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
            setDurationType(StatusDurationType.CHI_XU, duration);
        }

        public static void add(Character character, int duration, boolean gainGuiHuo
                , Skill2.StatusMengShen statusMengShen) {
            int realDuration = character.isInRound() ? duration + 1 : duration;
            character.getStatus(StatusHuanJingListener.class).ifPresentOrElse(
                    status -> status.setDuration(realDuration),
                    () -> {
                        StatusHuanJingListener status =
                                new StatusHuanJingListener(character, realDuration);
                        character.addStatus(status);
                        status.addListener(gainGuiHuo, statusMengShen);
                    }
            );
        }

        private void addListener(boolean gainGuiHuo, Skill2.StatusMengShen statusMengShen) {
            listener = belongTo.bp.forEveryone(belongTo, c -> {
                if (c.team == belongTo.team) {
                    c.addStatus(new StatusHuanJing(belongTo, c, gainGuiHuo, statusMengShen));
                }
            });
        }

        @Override
        public void beforeDelete() {
            List<Character> list = new CharacterFinder(belongTo, true)
                    .filterTeammate()
                    .getList();

            for (Character character : list) {
                // TODO 将来把statuses写成safeRemoved的话改掉这个
                if (character == belongTo) {
                    character.getStatus(StatusHuanJing.class)
                            .ifPresent(status ->
                                    status.setDurationType(StatusDurationType.CHI_XU, 1)
                            );
                } else {
                    character.removeStatus(StatusHuanJing.class);
                }
            }

            belongTo.bp.removeActionTrigger(belongTo, listener);
        }
    }

    static class StatusHuanJing extends Status implements StatusRunnable, AttributeModifier {
        private final boolean gainGuiHuo;

        private final Skill2.StatusMengShen statusMengShen;

        public StatusHuanJing(Character from, Character belongTo, boolean gainGuiHuo
                , Skill2.StatusMengShen statusMengShen) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
            this.gainGuiHuo = gainGuiHuo;
            this.statusMengShen = statusMengShen;
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.ZENG_SHANG;
        }

        @Override
        public double getInfluence(Attribute attribute) {
            return statusMengShen.getIncrease();
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return gainGuiHuo
                    && trigger == Trigger.BEFORE_ROUND
                    && (belongTo == from || belongTo == ((ShenWuYue) from).getRuMengCarrier());
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            belongTo.bp.gainGuiHuo(belongTo, 1);
            return false;
        }
    }

    static class StatusMeiMengJiangCheng extends Status implements StatusRunnable, Displayable {
        public static final String StatusName = "美梦将成";

        public StatusMeiMengJiangCheng(Character from, Character belongTo) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public String getDisplayText() {
            return StatusName;
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.BEFORE_ROUND;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            belongTo.addStatus(new StatusMeiMengBiCheng(from, belongTo));
            return true;
        }
    }
}
