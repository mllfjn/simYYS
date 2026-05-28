package com.mllfjn.simyys.character.list.ssr.geye;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;
import java.util.PriorityQueue;

class Skill3 extends Skill {
    static final String SkillName = "归源术";

    private int initTeammateCount;

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, 0, 0, 3);
        belongTo.bp.atBattleStart(
                () -> initTeammateCount = new CharacterFinder(belongTo, true)
                        .filterTeammate()
                        .getCount());
    }

    @Override
    public String getSkillDesc() {
        return """
                √\t选择友方式神,移除其所有减益,使其进入幻化效果
                √\t与之合体为大妖姿态并获得新回合(该技能固定幻化初始攻击最高的至多3个单位,如果需要指定幻化或者取消,使用技能5)
                √\tlv2-每幻化1名式神,提升大妖姿态20%生命
                √\tlv3-每幻化1名式神,提升大妖姿态20%攻击
                √\tlv4-每幻化1名式神,提升大妖姿态20%防御
                √\tlv5-每幻化1名式神,提升大妖姿态20%爆伤
                """;
    }

    int getInitTeammateCount() {
        return initTeammateCount;
    }

    @Override
    public boolean canUse(BattlePane bp) {
        return getBelongTo().isHaveStatus(StatusJiuWei.class)
                && !getBelongTo().isHaveStatus(StatusDaYao.class)
                && super.canUse(bp);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        GeYe belongTo = ((GeYe) getBelongTo());

        // 大妖姿态
        StatusDaYao statusDaYao = new StatusDaYao(belongTo, initTeammateCount, getLevel());
        belongTo.addStatus(statusDaYao);

        // 幻化友方
        int maxTarget = belongTo.getStatus(StatusJiuWei.class).orElseThrow().getStack();
        PriorityQueue<Character> priorityQueue = new CharacterFinder(belongTo)
                .filterTeammate()
                .filterSelf()
                .filterYYS(false)
                .getPriorityQueue(Attribute.INIT_ATTACK, CharacterFinder.Criteria.MAX);
        for (int i = 0; i < maxTarget; i++) {
            Character next = priorityQueue.poll();
            if (next == null) {
                break;
            }
            statusDaYao.addHuanHua(next);
        }

        statusDaYao.changeDone();

        // 获得新回合
        belongTo.getInteractive().getNewRound(belongTo);

        return Optional.empty();
    }
}
