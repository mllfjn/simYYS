package com.mllfjn.simyys.character.sp.laotou;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.status.Runnable;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.trigger.Trigger;

import java.util.List;

class Skill2 {
    public static final String SkillName = "洪福降临";


    public Skill2(LaoTou laoTou, int level) {
        laoTou.addStatus(new StatusAfterRound(laoTou, level));
        // lv5-战斗开始后,自身首次受到伤害时开始打盹
        if (level >= 5) {
            laoTou.addStatus(new StatusFirstAttackListener(laoTou));
        }
    }

    static class StatusAfterRound extends Status implements Runnable {
        private final int level;

        public StatusAfterRound(LaoTou laoTou, int level) {
            super(laoTou, laoTou, StatusType.SPECIAL, StatusForm.SPECIAL);
            this.level = level;
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ROUND;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp) {
            // 自身回合结束时，击退全体敌方目标10%行动条
            // lv2-击退行动条效果提升至15%
            belongTo.doInteractive(interactive -> {
                List<Character> enemy = CharacterFinder.findEnemy(belongTo, belongTo.bp.situation.characters);
                for (Character character : enemy) {
                    interactive.decreaseLocation(character, level >= 2 ? 15 : 10);
                }
            });

            // 若回合中释放过委以重任,则开始打盹
            if (belongTo.isHaveStatus(StatusUse3Flag.class)) {
                belongTo.addStatus(new StatusDaDun((LaoTou) belongTo));
                // lv4-打盹额外获得1点鬼火
                if (level >= 4) {
                    belongTo.bp.gainGuiHuo(belongTo, 1);
                }
            }

            // lv3-回合结束时额外获得2点鬼火
            if (level >= 3) {
                belongTo.bp.gainGuiHuo(belongTo, 2);
            }

            return false;
        }
    }

    static class StatusFirstAttackListener extends Status implements Runnable {
        private final LaoTou laoTou;

        public StatusFirstAttackListener(LaoTou laoTou) {
            super(laoTou, laoTou, StatusType.SPECIAL, StatusForm.SPECIAL);
            this.laoTou = laoTou;
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ATTACK;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp) {
            laoTou.addStatus(new StatusDaDun(laoTou));
            return true;
        }
    }
}


