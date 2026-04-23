package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.dizhennian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterSummonBase;
import com.mllfjn.simyys.character.list.mob.multiplayer.ClearHpHandler;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageWhenAttack;
import com.mllfjn.simyys.character.status.triggerParam.ParamAfterAttack;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.ratecontroller.RateController;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.List;
import java.util.Optional;

public class HouZi extends CharacterSummonBase {
    private static final String CharacterName = "猴子";

    private final DiZhenNian owner;

    private final ClearHpHandler clearHpHandler = new ClearHpHandler(this);

    public HouZi(DiZhenNian owner) {
        super(owner.bp, CharacterName, owner.team);
        this.owner = owner;

        this.forceSetMaxHp(9999999999L, true);
        setMob(1, 1);

        this.setInitDefense(352);
        this.setInitSpeed(100);

        fillSkills();

        bp.forEveryone(this, character -> {
            if (character.team != team) {
                character.addStatus(new StatusIncreaseActionListener(this, character));
            }
        });

        addStatus(new StatusDamageRecord(this));
    }

    @Override
    protected EventHandler<MouseEvent> getEventHandler() {
        return clearHpHandler.getEventHandler();
    }

    @Override
    public void afterRound() {
        super.afterRound();
        if (RateController.otherWhether(CharacterName, "多动", bp.calc, 50)) {
            doInteractive(interactive -> interactive.getNewRound(this));
        }
    }

    @Override
    protected void dieHandle() {
        List<Character> targets = new CharacterFinder(this)
                .filterEnemy()
                .getList();

        // 每百万伤害获得1%的独立增伤
        double bonus = 1;
        Optional<StatusDamageRecord> oStatus = getStatus(StatusDamageRecord.class);
        if (oStatus.isPresent()) {
            bonus += ((int) (oStatus.get().getDamage() / 1000000)) * 0.01;
        }

        // 移除拉条踢一次的效果，添加增伤
        for (Character target : targets) {
            target.removeStatus(StatusIncreaseActionListener.class);
            if (bonus != 1) {
                target.addStatus(new StatusHouZiBonus(this, target, bonus));
            }
        }

        owner.houZiDie();
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new HouZiSkill(this));
    }

    static class HouZiSkill extends Skill1PuGongBase {
        private static final String SkillName = "猴子飞踢";

        public HouZiSkill(Character belongTo) {
            super(belongTo, 0);
        }

        @Override
        public String getName() {
            return SkillName;
        }

        @Override
        public Character getTarget() {
            return new CharacterFinder(getBelongTo())
                    .filterEnemy()
                    .getAutoOrElseRandom();
        }

        @Override
        public void usePrivate(Interactive interactive, Character target) {
            interactive.attack(AttackInfo.createRealAttack(getBelongTo(), this, target,
                    (c1, c2) -> 5000.0)
            );
            interactive.increaseLocation(getBelongTo(), 10);
        }
    }

    static class StatusIncreaseActionListener extends Status implements StatusRunnable {

        public StatusIncreaseActionListener(Character from, Character belongTo) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.INCREASE_LOCATION;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            from.doInteractive(interactive ->
                    from.getPuGong().ifPresent(skill1 -> {
                        skill1.usePrivate(interactive, belongTo);
                        skill1.log(belongTo);
                    })
            );
            return false;
        }
    }

    static class StatusDamageRecord extends Status implements StatusRunnable {
        private double damage;

        public StatusDamageRecord(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        public double getDamage() {
            return damage;
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.AFTER_ATTACK;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (param instanceof ParamAfterAttack paa) {
                double number = paa.attackInfo.getTraceableNumber().getNumber();
                damage += number;
                ((HouZi) belongTo).owner.getInfoDisplay().addDamage(number);
            }
            return false;
        }
    }

    static class StatusHouZiBonus extends Status implements InfluenceDamageWhenAttack {
        private static final String StatusName = "猴子伤害加成";

        private final double bonus;

        public StatusHouZiBonus(Character from, Character belongTo, double bonus) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
            this.bonus = bonus;
        }

        @Override
        public void doInfluenceWhenAttack(AttackInfo attackInfo) {
            attackInfo.getTraceableNumber().mul(bonus, StatusName);
        }
    }
}
