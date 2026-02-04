package com.mllfjn.simyys.character.list.sp.kongmian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.EventCharacterDie;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

import java.util.List;
import java.util.Optional;

// √     非召唤物的目标阵亡时,凝聚其灵魂化作未面
//      受到致命伤害时,进入黑光姿态
// √     [释放]累计收集不少于4张未面时可释放
//          引燃敌方目标的一线目
//          进入黑光姿态
//      lv2-黑光姿态下,每张未面为全体友方增加5点速度
//      lv3-每张未面为[skill3Special]追加1段伤害
//      lv4-若直到下回合开始未获得未面,则下回合结束时获得1张未面
//      lv5-释放后获得新回合
// √     未面:通用,印记:最多可获得7张,每张使自身攻击时无视50点防御
//      黑光:通用,印记:移除一线目和自身所有减益,将[skill3]替换成[skill3Special],恢复100%生命,攻击时额外无视200点防御并附加15%吸血效果

class Skill2 extends Skill {
    private static final String SkillName = "表里之相";
    private double max;

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 0, 0, 2);
        // 开局时记录一下队友的总攻击
        belongTo.bp.atBattleStart(() -> {
            List<Character> targets = new CharacterFinder(belongTo)
                    .filterTeammate()
                    .filterShiShen()
                    .getList();
            double sum = 0;
            for (Character target : targets) {
                sum += target.getInitAttack();
            }
            max = sum * 40;
        });

        belongTo.bp.addActionListener(belongTo, event -> {
            // 非召唤物的目标阵亡时,凝聚其灵魂化作未面
            if (event instanceof EventCharacterDie ecd) {
                Character characterDie = ecd.getCharacter();
                if (!characterDie.isSummon()) {
                    return StatusWeiMian.addStack(belongTo);
                }
            }
            return false;
        });


        // lv4-若直到下回合开始时未获得未面,则下回合结束时获得1张未面
        if (level >= 4) {
            belongTo.addStatus(new StatusWeiMianListener(belongTo));
        }
    }

    public double getMax() {
        return max;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public boolean canUse(BattlePane bp) {
        if (!super.canUse(bp)) {
            return false;
        }
        // [释放]累计收集不少于4张未面时可释放
        Optional<StatusWeiMian> oStatus = getBelongTo().getStatus(StatusWeiMian.class);
        return oStatus.filter(statusWeiMian -> statusWeiMian.stack >= 4).isPresent();
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {

        // lv5-释放后获得新回合
        if (getLevel() == 5) {
            getBelongTo().getInteractive().getNewRound(getBelongTo());
        }

        return Optional.empty();
    }

    static class StatusWeiMian extends Status implements AttributeModifier, Displayable {
        private static final String StatusName = "未面";

        private int stack;

        public StatusWeiMian(Character character) {
            super(character, character, StatusType.GENERAL, StatusForm.YIN_JI);
        }

        public static boolean addStack(Character character) {
            StatusWeiMian statusWeiMian = character.getStatus(StatusWeiMian.class).orElseGet(() -> {
                StatusWeiMian status = new StatusWeiMian(character);
                character.addStatus(status);
                return status;
            });

            character.getStatus(StatusWeiMianListener.class)
                    .ifPresent(status -> status.canAdd = false);

            return statusWeiMian.addStack();
        }

        private boolean addStack() {
            if (stack < 7) {
                stack++;
                return stack == 7;
            }
            return true;
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.IGNORE_DEFENCE;
        }

        @Override
        public double getInfluence(Attribute attribute) {
            // 每张使自身攻击时无视50点防御
            return 50 * stack;
        }

        @Override
        public String getDisplayText() {
            return StatusName + stack;
        }
    }

    static class StatusWeiMianListener extends Status implements StatusRunnable {
        private boolean canAdd = true;

        public StatusWeiMianListener(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return canAdd && trigger == Trigger.BEFORE_ROUND;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (canAdd) {
                belongTo.addStatus(new StatusAddAfterRound(belongTo));
            }
            return false;
        }

        static class StatusAddAfterRound extends Status implements StatusRunnable {
            public StatusAddAfterRound(Character character) {
                super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
            }

            @Override
            public boolean runnable(Trigger trigger) {
                return trigger == Trigger.AFTER_ROUND;
            }

            @Override
            public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
                if (StatusWeiMian.addStack(belongTo)) {
                    belongTo.removeStatus(StatusWeiMianListener.class);
                }
                return true;
            }
        }
    }

    static class StatusHeiGuang extends Status {

        // TODO
        // 移除一线目和自身所有减益,将[梦虚空境]替换成[轮回一息],恢复100%生命,攻击时额外无视200点防御并附加15%吸血效果
        public StatusHeiGuang(Character character) {
            super(character, character, StatusType.GENERAL, StatusForm.YIN_JI);
        }
    }
}
