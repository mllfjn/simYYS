package com.mllfjn.simyys.character.list.mob.jifengmo;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.EventBattleStart;
import com.mllfjn.simyys.battleevent.EventUsePuGong;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.StatusRunnable;
import com.mllfjn.simyys.character.status.triggerParam.ParamAfterAttack;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;

import java.util.List;
import java.util.Optional;

public class CiTiao1QiaoJin {
    public static final String CiTiaoName = "巧劲";

    public static void install(Character character) {
        character.addStatus(new StatusQJListener(character));

        // 己方（指玩家）攻击力最高的单位普攻时，额外获得一次行动，回合结束后增加自身35%行动条
        character.bp.addActionListener(character, event -> {
            if (event instanceof EventBattleStart) {
                Character maxAttack = new CharacterFinder(character)
                        .setTargetTeam(CharacterFinder.TargetTeam.ENEMY)
                        .get(Attribute.ATTACK, CharacterFinder.Criteria.MAX);
                maxAttack.bp.addActionListener(maxAttack, e -> {
                    if (e instanceof EventUsePuGong eup) {
                        if (eup.getAttacker() == maxAttack) {
                            Optional<StatusQJNewRoundMark> optional = maxAttack.getStatus(StatusQJNewRoundMark.class);
                            if (optional.isEmpty()) {
                                maxAttack.addStatus(new StatusQJNewRoundMark(character, maxAttack));
                                maxAttack.doInteractive(interactive -> interactive.getNewRound(maxAttack));
                            }
                        }
                    }
                    return false;
                });

                return true;
            }
            return false;
        });
    }

    static class StatusQJNewRoundMark extends Status implements StatusRunnable {

        public StatusQJNewRoundMark(Character from, Character belongTo) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ROUND;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            belongTo.doInteractive(interactive -> interactive.increaseLocation(belongTo, 35));
            return true;
        }
    }

    // 巧劲要用状态类生效在BOSS身上，因为只有BOSS本体直接受到伤害才会触发这个效果，打在盾上不生效
    static class StatusQJListener extends Status implements StatusRunnable {
        public StatusQJListener(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ATTACK;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            // 己方（指玩家，在这里是对面的人）普攻时，会造成40%溅射伤害
            if (param instanceof ParamAfterAttack pa) {
                AttackInfo attackInfo = pa.attackInfo;
                Character attacker = attackInfo.getAttacker();
                Skill skill = attackInfo.getSkill();
                if (skill.getSkillID() == 1
                        && attacker.team != belongTo.team
                        && attackInfo.getTraceableNumber().getNumber() > 0) {
                    double number = attackInfo.getTraceableNumber().getNumber();
                    List<Character> targets = new CharacterFinder(belongTo)
                            .setTargetTeam(CharacterFinder.TargetTeam.TEAMMATE)
                            .filterSelf()
                            .getList();

                    if (attacker.getStatus(StatusAddStackAfterRound.class).isEmpty()) {
                        attacker.addStatus(new StatusAddStackAfterRound(attacker));
                    }

                    attacker.doInteractive(interactive -> {
                        for (Character target : targets) {
                            interactive.attack(AttackInfo.createRealAttack(attacker
                                    , Skill.getInstance(CiTiao1QiaoJin.CiTiaoName)
                                    , target, (c1, c2) -> number * 0.4), AttackType.ZHEN_SHI);
                        }
                    });
                }
            }
            return false;
        }
    }

    static class StatusQJJianShang extends Status implements Displayable, AttributeModifier {
        private int stack = 0;

        private StatusQJJianShang(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        public static void addStack(Character character) {
            StatusQJJianShang status = character.getStatus(StatusQJJianShang.class)
                    .orElseGet(() -> {
                        StatusQJJianShang newStatus = new StatusQJJianShang(character);
                        character.addStatus(newStatus);
                        return newStatus;
                    });

            // 上限30层
            if (status.stack < 30) {
                status.stack++;
            }
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.JIAN_SHANG;
        }

        @Override
        public double getInfluence(Attribute attribute) {
            // 全队受到的伤害降低5%
            return stack * 5;
        }

        @Override
        public String getText() {
            return CiTiao1QiaoJin.CiTiaoName + stack;
        }
    }

    static class StatusAddStackAfterRound extends Status implements StatusRunnable {

        public StatusAddStackAfterRound(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ROUND;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            List<Character> targets = new CharacterFinder(belongTo)
                    .setTargetTeam(CharacterFinder.TargetTeam.TEAMMATE)
                    .getList();
            for (Character character : targets) {
                StatusQJJianShang.addStack(character);
            }
            return true;
        }
    }
}
