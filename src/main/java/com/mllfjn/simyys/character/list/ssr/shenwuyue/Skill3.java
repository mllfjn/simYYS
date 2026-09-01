package com.mllfjn.simyys.character.list.ssr.shenwuyue;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.StatusAdder;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;

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
    private static final String SkillName = "美梦赐予";

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
            if (!target.isHaveStatus(StatusMeiMengJiangCheng.class)) {
                target.addStatus(new StatusMeiMengJiangCheng(belongTo, target));
            }
        }

        return Optional.of(target);
    }

    static class StatusHuanJingListener extends Status {
        private StatusHuanJingListener(Character character, int duration, boolean gainGuiHuo
                , Skill2.StatusMengShen statusMengShen) {
            super(SkillName + "幻境容器", character);
            duration(StatusDurationType.CHI_XU, duration);

            StatusAdder<?> adder = belongTo.bp.addStatusAdder(c ->
                    c.team == belongTo.team
                            ? new StatusHuanJing(belongTo, c, gainGuiHuo, statusMengShen)
                            : null
            );
            beforeDelete(adder::deleteAndRemove);
        }

        public static void add(Character character, int duration, boolean gainGuiHuo
                , Skill2.StatusMengShen statusMengShen) {
            character.getStatus(StatusHuanJingListener.class).ifPresentOrElse(
                    status -> status.duration(duration),
                    () -> character.addStatus(
                            new StatusHuanJingListener(character, duration, gainGuiHuo, statusMengShen)
                    )
            );
        }
    }

    static class StatusHuanJing extends Status {
        public StatusHuanJing(Character from, Character belongTo, boolean gainGuiHuo
                , Skill2.StatusMengShen statusMengShen) {
            super(SkillName + "幻境增伤", from, belongTo);

            attribute(Attribute.ZENG_SHANG, _ -> statusMengShen.getIncrease());
            if (gainGuiHuo && (belongTo == from || belongTo == ((ShenWuYue) from).getRuMengCarrier())) {
                runOn(Trigger.BEFORE_ROUND, _ -> belongTo.bp.gainGuiHuo(belongTo, 1));
            }
        }
    }

    static class StatusMeiMengJiangCheng extends Status {
        private static final String StatusName = "美梦将成";

        public StatusMeiMengJiangCheng(Character from, Character belongTo) {
            super(StatusName, from, belongTo);
            displayName();
            runOn(Trigger.BEFORE_ROUND, _ -> {
                belongTo.addStatus(new StatusMeiMengBiCheng(from, belongTo));
                delete();
            });
        }
    }
}
