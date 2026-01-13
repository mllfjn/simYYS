package com.mllfjn.simyys.character.list.ssr.shenwuyue;

import com.mllfjn.simyys.battleevent.EventBattleStart;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.Displayable;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;

// √     战斗开始时,神无月获得美梦必成
//           每回目仅限1次(好像是指触发之后就没了)
// √     每次创造幻境,自身获得1层梦神
// √     lv2-梦神伤害提升增至13%
// √     lv3-梦神伤害提升增至15%
// √     lv4-梦神伤害提升增至17%
// √     lv5-先机:获得1层梦神
// √     梦神：增益,印记.上限5层,使幻境中的友方获得11%增伤

class Skill2 extends PassiveSkill {
    public static final String SkillName = "织梦曲";
    private static final int[] perStacks = new int[]{0, 11, 13, 15, 17, 17};

    public Skill2(ShenWuYue belongTo, int level) {
        super(belongTo, level, 2);

        StatusMengShen statusMengShen = new StatusMengShen(belongTo, perStacks[level]);
        belongTo.addStatus(statusMengShen);
        belongTo.setStatusMengShen(statusMengShen);

        belongTo.bp.addActionListener(belongTo, (event) -> {
            if (event instanceof EventBattleStart) {
                belongTo.addStatus(new StatusMeiMengBiCheng(belongTo, belongTo));
                if (level == 5) {
                    belongTo.addStack();
                }
                return true;
            }
            return false;
        });
    }

    @Override
    public void enable() {

    }

    @Override
    protected void disable() {

    }

    @Override
    public String getName() {
        return SkillName;
    }

    static class StatusMengShen extends Status implements Displayable {
        private static final String StatusName = "梦神";

        private final int perStack;

        private int stack;

        private StatusMengShen(Character character, int perStack) {
            super(character, character, StatusType.BUFF, StatusForm.YIN_JI);
            this.perStack = perStack;
        }

        public void addStack(int addStack) {
            stack = Math.min(addStack + stack, 5);
        }

        public int getIncrease() {
            return perStack * stack;
        }

        @Override
        public String getDisplayText() {
            return StatusName + stack;
        }
    }
}
