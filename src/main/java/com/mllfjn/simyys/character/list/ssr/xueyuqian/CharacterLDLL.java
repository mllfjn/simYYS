package com.mllfjn.simyys.character.list.ssr.xueyuqian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterSummonBase;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.CrowdControl;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;

import java.util.Optional;

class CharacterLDLL extends CharacterSummonBase {
    private final XueYuQian xueYuQian;

    private int repeatCount = 0;

    public CharacterLDLL(XueYuQian xueYuQian, double initHpMultiplier) {
        super(xueYuQian.bp, "龙胆蓝璃", xueYuQian.team);
        this.xueYuQian = xueYuQian;
        xueYuQian.isLDLLExist = true;

        setMaxHp(xueYuQian.getInitAttack() * initHpMultiplier, true);
        setInitCritRate(xueYuQian.getInitCritRate());
        setInitCritPower(xueYuQian.getInitCritPower());
        setInitDefense(xueYuQian.getInitDefense());
        setInitSpeed(xueYuQian.getInitSpeed() * 0.95);

        fillSkills();
    }

    void repeatSummon() {
        if (repeatCount < 3) {
            repeatCount++;
            setMaxHp(getMaxHp() + 0.39 * xueYuQian.getInitAttack(), true);
        }
    }

    @Override
    protected void dieHandle() {
        xueYuQian.isLDLLExist = false;
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
    public <T extends Status> Optional<T> addStatus(T newStatus) {
        if (newStatus.statusType == StatusType.DEBUFF || newStatus instanceof CrowdControl) {
            return Optional.empty();
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
            belongTo.getInteractive().attack(attackInfo);
            return Optional.of(target);
        }
    }
}
