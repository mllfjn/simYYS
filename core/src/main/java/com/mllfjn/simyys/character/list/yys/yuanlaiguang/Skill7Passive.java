package com.mllfjn.simyys.character.list.yys.yuanlaiguang;

import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.battleevent.BattleEvent;
import com.mllfjn.simyys.battleevent.EventActionDone;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.AttackType;

import java.util.List;
import java.util.Optional;

class Skill7Passive extends PassiveSkill {
    static final String SkillName = "剑之垒";

    private int count;
    private BattleActionListener listener;

    public Skill7Passive(Character belongTo, int level, int shuYin) {
        super(belongTo, level, 7);

        Status.of(SkillName + "回合开始前刷新", belongTo)
                .runOn(Trigger.BEFORE_ROUND, _ -> refuel())
                .addTo();
    }

    public void piZhan(Character target) {
        // 鬼兵部附身的目标受到攻击但未受到伤害时，鬼兵部劈斩目标来源
        // 自身回合开始前限5次
        if (count == 5 || listener != null) {
            return;
        }
        Character belongTo = getBelongTo();
        int level = getLevel();
        // 造成130%伤害
        // lv5-劈斩初始伤害提升至150%
        int multiplier = level >= 5 ? 150 : 130;

        // 每次劈斩伤害系数增加10%,可进化2次,进化效果持续1回合.
        // lv2-每次劈斩使下次劈斩伤害系数提升20%
        if (level >= 2) {
            multiplier += count * 20;
        } else {
            multiplier += count * 10;
        }

        // lv4-劈斩净化效果持续2个回合
        int duration = level >= 4 ? 2 : 1;

        StringBuilder sb = new StringBuilder(belongTo.name);
        // lv3-第3次劈斩进化为攻击敌方全体目标
        int mul = multiplier;
        Optional<StatusEvolution> status = belongTo.getStatus(StatusEvolution.class);
        // 需要满足条件:等级大于等于3,存在进化,进化次数大于等于3,才可以攻击全体
        if (level >= 3 && status.isPresent() && status.get().getCount() >= 3) {
            listener = new BattleActionListener(belongTo) {
                @Override
                public boolean onBattleAction(BattleEvent event) {
                    if (event instanceof EventActionDone) {
                        List<Character> targets = new CharacterFinder(belongTo)
                                .filterEnemy()
                                .getList();
                        belongTo.doInteractive(interactive ->
                                interactive.attackTypical(Skill7Passive.this, targets, mul, AttackType.QUN_TI));
                        listener = null;
                        belongTo.bp.log.addSkill(sb.toString());
                        return true;
                    }
                    return false;
                }
            };
        } else {
            sb.append("对").append(target.name);
            listener = new BattleActionListener(belongTo) {
                @Override
                public boolean onBattleAction(BattleEvent event) {
                    if (event instanceof EventActionDone) {
                        belongTo.doInteractive(interactive ->
                                interactive.attackTypical(Skill7Passive.this, target, mul, AttackType.DAN_TI));
                        listener = null;
                        belongTo.bp.log.addSkill(sb.toString());
                        return true;
                    }
                    return false;
                }
            };
        }
        belongTo.bp.addActionListener(listener);

        sb.append("使用了").append(getName());

        StatusEvolution.addEvolution(belongTo, duration);
        count++;
    }

    public void refuel() {
        count = 0;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    static class StatusEvolution extends Status {
        private int count = 0;

        private StatusEvolution(Character character, int duration) {
            super(SkillName + "进化", character);

            duration(StatusDurationType.CHI_XU, duration);
        }

        public static void addEvolution(Character character, int duration) {
            StatusEvolution evolution = character.getStatus(StatusEvolution.class)
                    .orElseGet(() -> {
                        StatusEvolution statusEvolution = new StatusEvolution(character, duration);
                        character.addStatus(statusEvolution);
                        return statusEvolution;
                    });
            evolution.evolution();
        }

        public void evolution() {
            if (count < 3) {
                count++;
            }
        }

        public int getCount() {
            return count;
        }
    }
}
