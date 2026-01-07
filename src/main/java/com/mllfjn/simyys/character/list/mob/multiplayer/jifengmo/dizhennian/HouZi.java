package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.dizhennian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.list.mob.multiplayer.DisplayDamageRecord;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageWhenAttack;
import com.mllfjn.simyys.character.status.triggerParam.ParamAfterAttack;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;
import com.mllfjn.simyys.interactive.InteractiveInfo;

import java.util.List;
import java.util.Optional;

public class HouZi extends Character {
    public static final String CharacterName = "猴子";

    private final Character owner;

    public HouZi(BattlePane bp, Character owner) {
        this.owner = owner;
        this.name = CharacterName;
        this.bp = bp;
        this.team = owner.team;
        this.setMaxHp(99999999, true);
        setMob(1, 1);

        this.setInitDefense(352);
        this.setInitSpeed(100);

        addSkills();

        bp.forEveryone(character -> {
            if (character.team != team) {
                character.addStatus(new StatusIncreaseActionListener(this, character));
            }
        });

        addStatus(new StatusDamageRecord(this));
    }

    public Character getOwner() {
        return owner;
    }

    @Override
    public void afterRound() {
        super.afterRound();

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
    }

    @Override
    protected String getDefaultBaseAttack() {
        return CharacterName;
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new HouZiSkill(this));
    }

    static class HouZiSkill extends Skill1PuGongBase {
        public static final String SkillName = "猴子飞踢";

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
            InteractiveInfo info = InteractiveInfo.createRealAttack(getBelongTo(), this, target
                    , (c1, c2) -> 5000.0);

            interactive.attack(info, AttackType.ZHEN_SHI);
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
                    from.getPuGong().ifPresent(skill1 -> skill1.usePrivate(interactive, belongTo)));
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
                double number = paa.interactiveInfo.getTraceableNumber().getNumber();
                damage += number;
                ((DiZhenNian) ((HouZi) belongTo).owner).display.addDamage(number);
            }
            return false;
        }
    }

    static class StatusHouZiBonus extends Status implements InfluenceDamageWhenAttack {
        public static final String StatusName = "猴子伤害加成";

        private final double bonus;

        public StatusHouZiBonus(Character from, Character belongTo, double bonus) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
            this.bonus = bonus;
        }

        @Override
        public void doInfluenceWhenAttack(AttackType attackType, InteractiveInfo interactiveInfo) {
            interactiveInfo.getTraceableNumber().mul(bonus, StatusName);
        }
    }
}
