package com.mllfjn.simyys.character.list.ssr.geye;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;

import java.util.Optional;
import java.util.PriorityQueue;

class Skill3 extends Skill {
    private static final String SkillName = "归源术";

    private int initTeammateCount;

    public Skill3(Character belongTo, int level) {
        super(belongTo, level, 0, 0, 3);
        belongTo.bp.atBattleStart(
                () -> initTeammateCount = new CharacterFinder(belongTo, true)
                        .filterTeammate()
                        .getList().size());
    }

    @Override
    public String getSkillDesc() {
        return """
                √\t选择友方式神,移除其所有减益,使其进入幻化效果
                √\t与之合体为大妖姿态并获得新回合
                \t或选择友方式神解除幻化效果(未完成)
                """;
    }

    @Override
    public boolean canUse(BattlePane bp) {
        return getBelongTo().isHaveStatus(StatusJiuWei.class) && super.canUse(bp);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        GeYe belongTo = ((GeYe) getBelongTo());

        // 幻化友方
        int maxTarget = belongTo.getStatus(StatusJiuWei.class).orElseThrow().getStack();
        int actualTarget = 0;
        PriorityQueue<Character> priorityQueue = new CharacterFinder(belongTo)
                .filterTeammate()
                .filterSelf()
                .filterYYS(false)
                .getPriorityQueue(Attribute.ATTACK, CharacterFinder.Criteria.MAX);
        for (int i = 0; i < maxTarget; i++) {
            Character next = priorityQueue.poll();
            if (next == null) {
                break;
            }
            actualTarget++;
            next.removeAllDeBuff();
            next.addStatus(new StatusHuanHua(belongTo, next));
        }

        // 大妖姿态
        StatusDaYao.install(belongTo, actualTarget, initTeammateCount);
        belongTo.getInteractive().getNewRound(belongTo);

        return Optional.empty();
    }
}
