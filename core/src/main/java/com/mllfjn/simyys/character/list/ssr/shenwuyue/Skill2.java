package com.mllfjn.simyys.character.list.ssr.shenwuyue;

import com.mllfjn.simyys.battleevent.StatusAdder;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;

// 26.1.14更新
// √     神无月和入梦携带者获得40%减伤，其他友方获得20%减伤
// √     战斗开始时,获得美梦必成
// √     每次创造幻境,自身获得1层梦神
// √     lv2-梦神伤害提升增至14%
// √     lv3-梦神伤害提升增至16%
// √     lv4-梦神伤害提升增至18%
// √     lv5-先机:获得1层梦神
// √     梦神：增益,印记.上限5层,使幻境中的友方获得12%增伤

class Skill2 extends PassiveSkill {
    private static final String SkillName = "织梦曲";
    private static final int[] perStacks = new int[]{0, 12, 14, 16, 18, 18};

    public Skill2(ShenWuYue belongTo, int level) {
        super(belongTo, level, 2);

        StatusMengShen statusMengShen = new StatusMengShen(belongTo, perStacks[level]);
        belongTo.addStatus(statusMengShen);
        belongTo.setStatusMengShen(statusMengShen);

        belongTo.bp.addPriorityMove(belongTo, () -> {
            belongTo.addStatus(new StatusMeiMengBiCheng(belongTo, belongTo));
            belongTo.addStatus(new JianShangContainer(belongTo));
            if (level == 5) {
                belongTo.addStack();
            }
        });
    }

    @Override
    public String getName() {
        return SkillName;
    }

    static class StatusMengShen extends Status {
        private static final String StatusName = "梦神";

        private final int perStack;

        private int stack;

        private StatusMengShen(Character character, int perStack) {
            super(StatusName, character, character, StatusType.BUFF, StatusForm.YIN_JI);
            this.perStack = perStack;
            display(() -> StatusName + stack);
        }

        public void addStack(int addStack) {
            stack = Math.min(addStack + stack, 5);
        }

        public double getIncrease() {
            return perStack * stack;
        }
    }

    static class JianShangContainer extends Status {
        public JianShangContainer(Character character) {
            super(SkillName + "减伤监听", character);
            StatusAdder<?> adder = character.bp.addStatusAdder(c -> c.team == belongTo.team
                    ? Status.of(SkillName + "减伤", from, belongTo)
                    .attribute(Attribute.JIAN_SHANG, from == belongTo ? 40 : 20)
                    : null);
            beforeDelete(adder::deleteAndRemove);
        }
    }
}
