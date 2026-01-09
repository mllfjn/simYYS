package com.mllfjn.simyys.character.list.yys.boya;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamUseSkill;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackType;

// √     战斗开始和回合结束后,获得3层诛邪箭,自身外友方回合内普攻时,消耗1层诛邪箭进行协战,并提升自身10%行动条
// √     lv2-每箭伤害增加至55%
// √     lv3-每箭伤害增加至60%
// √     lv4-每箭伤害增加至65%
// √     lv5-每箭伤害增加至70%
// √     诛邪箭:增益,印记.至多3层.连续射出3发箭,攻击敌方目标,每箭造成攻击50%的伤害,每箭伤害递增25%(不知道这个递增是什么,按照加25系数写)
// √     术印:每箭造成的基础伤害额外提升40%
class Skill5 extends PassiveSkill {
    public static final String SkillName = "诛邪箭";

    public Skill5(Character belongTo, int level, int shuYin) {
        super(belongTo, level, 5);

        belongTo.addStatus(new StatusZXJ(belongTo, 45 + level * 5 + shuYin * 40));
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

    class StatusZXJ extends Status implements StatusRunnable, Displayable {

        private final int multiplier;

        private int stack = 3;

        public StatusZXJ(Character character, int multiplier) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
            this.multiplier = multiplier;

            character.bp.forEveryone(c -> {
                if (c.team == character.team && c != character) {
                    c.addStatus(new StatusZXJListener(character, c, this));
                }
            });
        }

        public void use(Character target) {
            if (stack == 0) {
                return;
            }
            stack--;
            belongTo.doInteractive(interactive -> {
                for (int i = 0; i < 3; i++) {
                    interactive.attackTypical(Skill5.this, target, multiplier + i * 25, AttackType.DAN_TI);
                }
                interactive.increaseLocation(belongTo, 10);
            });
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ROUND;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            stack = 3;
            return false;
        }

        @Override
        public String getDisplayText() {
            return SkillName + stack;
        }
    }

    static class StatusZXJListener extends Status implements StatusRunnable {
        private final StatusZXJ status;

        public StatusZXJListener(Character from, Character belongTo, StatusZXJ status) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
            this.status = status;
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.USE_PU_GONG && belongTo.isInRound();
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (param instanceof ParamUseSkill pus) {
                status.use(pus.getTarget().orElseThrow());
            }
            return false;
        }
    }
}
