package com.mllfjn.simyys.character.list.ssr.xueyuqian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterSummonBase;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;

import java.util.Optional;

class CharacterLDLL extends CharacterSummonBase {
    private final XueYuQian xueYuQian;

    private int repeatCount = 0;

    private StatusImmuneAttack status;

    public CharacterLDLL(XueYuQian xueYuQian, double initHpMultiplier, double location) {
        super(xueYuQian.bp, "龙胆蓝璃", xueYuQian.team);
        this.xueYuQian = xueYuQian;
        xueYuQian.isLDLLExist = true;

        setMaxHp(xueYuQian.getInitAttack() * initHpMultiplier, true);
        setInitCritRate(xueYuQian.getInitCritRate());
        setInitCritPower(xueYuQian.getInitCritPower());
        setInitDefense(xueYuQian.getInitDefense());
        setInitSpeed(xueYuQian.getInitSpeed() * 0.95);
        forceSetLocation(location);

        status = new StatusImmuneAttack(xueYuQian);
        xueYuQian.addStatus(status);
    }

    void repeatSummon(double location, boolean forceChangeLocation) {
        if (repeatCount < 3) {
            repeatCount++;
            setMaxHp(getMaxHp() + 0.39 * xueYuQian.getInitAttack(), true);
        }
        if (forceChangeLocation) {
            forceSetLocation(location);
        }
    }

    @Override
    protected void dieHandle() {
        xueYuQian.isLDLLExist = false;
        if (status != null) {
            xueYuQian.removeStatus(status);
        }
    }

    @Override
    public boolean isUncontrollable() {
        return true;
    }

    @Override
    public void round() {
        tryUseSkill(1);
    }

    @Override
    public <T extends Status> boolean addStatus(T newStatus) {
        if (newStatus.statusType == StatusType.DEBUFF || newStatus instanceof CrowdControl) {
            return false;
        } else {
            return super.addStatus(newStatus);
        }
    }

    @Override
    protected void addOwnSkills() {
        addSkill(new SkillLR(this));
    }

    private static class SkillLR extends Skill {
        private static final String SkillName = "璃刃";

        public SkillLR(Character belongTo) {
            super(belongTo, 0, 0, 0, 1);
        }

        @Override
        public String getName() {
            return SkillName;
        }

        @Override
        public Optional<Character> usePrivate(BattlePane bp) {
            Character belongTo = getBelongTo();
            Character target = new CharacterFinder(belongTo)
                    .filterEnemy()
                    .get(Attribute.HP, CharacterFinder.Criteria.MIN);
            AttackInfo attackInfo = new AttackInfo(belongTo, this, target, AttackType.DAN_TI, belongTo.getMaxHp());
            attackInfo.setMultiplier(50);
            attackInfo.setFluctuationLimit(0);
            belongTo.getInteractive().attack(attackInfo);
            return Optional.of(target);
        }
    }

    private class StatusImmuneAttack extends Status implements StatusRunnable {
        public StatusImmuneAttack(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public void beforeDelete() {
            CharacterLDLL.this.status = null;
            CharacterLDLL.this.die();
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.BEING_ATTACKED;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            ((ParamAttackInfo) param).getAttackInfo().setCancel(true);
            return false;
        }
    }
}
