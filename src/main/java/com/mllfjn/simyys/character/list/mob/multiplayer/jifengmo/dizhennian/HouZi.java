package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.dizhennian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.StatusAdder;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterSummonBase;
import com.mllfjn.simyys.character.list.mob.multiplayer.ClearHpHandler;
import com.mllfjn.simyys.character.list.mob.multiplayer.StatusRecordDamage;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageWhenAttack;
import com.mllfjn.simyys.character.status.triggerParam.ParamLocationChange;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.ratecontroller.RateController;

import java.util.List;
import java.util.Optional;

public class HouZi extends CharacterSummonBase {
    private static final String CharacterName = "猴子";

    private final DiZhenNian owner;

    private final StatusAdder<?> adder;

    public HouZi(DiZhenNian owner) {
        super(owner.bp, CharacterName, owner.team);
        this.owner = owner;

        this.forceSetMaxHp(999999999, true);
        setMob(1, 1);

        this.setInitDefense(352);
        this.setInitSpeed(100);

        adder = bp.addStatusAdder(c ->
                c.team != team
                        ? new StatusIncreaseActionListener(this, c)
                        : null
        );

        addStatus(new StatusDamageRecord(this));
        getCharacterIcon().setEventHandlerContainer(new ClearHpHandler(this));
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
            bonus += ((int) (oStatus.get().getHouZiDamage() / 1000000)) * 0.01;
        }

        // 移除拉条踢一次的效果，添加增伤
        adder.deleteAndRemove();
        for (Character target : targets) {
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
            interactive.attack(AttackInfo.createRealAttack(getBelongTo(), this, target, 5000));
            interactive.increaseLocation(getBelongTo(), 10);
        }
    }

    static class StatusIncreaseActionListener extends Status implements StatusRunnable {

        public StatusIncreaseActionListener(Character from, Character belongTo) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.LOCATION_CHANGE;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            if (((ParamLocationChange) param).isFromIncrease) {
                from.doInteractive(interactive ->
                        from.getPuGong().ifPresent(skill1 -> {
                            skill1.usePrivate(interactive, belongTo);
                            skill1.log(belongTo);
                        })
                );
            }
            return false;
        }
    }

    static class StatusDamageRecord extends StatusRecordDamage {
        private double houZiDamage;

        public StatusDamageRecord(Character character) {
            super(character);
        }

        public double getHouZiDamage() {
            return houZiDamage;
        }

        @Override
        protected void addDamage(double damage) {
            houZiDamage += damage;
            ((HouZi) belongTo).owner.getInfoDisplay().addDamage(damage);
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
