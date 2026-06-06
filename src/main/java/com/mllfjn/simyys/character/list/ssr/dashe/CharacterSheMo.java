package com.mllfjn.simyys.character.list.ssr.dashe;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterSummonBase;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.character.status.AttributeModifier;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusForm;
import com.mllfjn.simyys.character.status.StatusType;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

class CharacterSheMo extends CharacterSummonBase {
    private final DaShe daShe;

    SkillDuYe skillDuYe;

    private CharacterSheMo(DaShe daShe, Character target) {
        super(daShe.getBp(), "蛇魔", daShe.team);
        this.daShe = daShe;

        setInitBaseAttack(target.getInitBaseAttack() + daShe.getInitBaseAttack() * 0.2);
        setInitAdditionAttack(target.getInitAdditionAttack() + daShe.getInitAdditionAttack() * 0.2);
        setMaxHp(target.getMaxHp() + daShe.getMaxHp() * 0.2, true);
        setInitDefense(target.getInitDefense() + daShe.getInitDefense() * 0.2);
        setInitSpeed(target.getInitSpeed() + daShe.getInitSpeed() * 0.2);
        setInitCritRate(target.getInitCritRate() + daShe.getInitCritRate() * 0.2);
        setInitCritPower(target.getInitCritPower() + daShe.getInitCritPower() * 0.2);
        setInitEffectResistRate(target.getInitEffectResistRate() + daShe.getInitEffectResistRate() * 0.2);
    }

    static void convert(DaShe daShe, Character target, Skill2 skill2) {
        if (daShe != target) {
            target.die();
        }
        CharacterSheMo characterSheMo = new CharacterSheMo(daShe, target);
        daShe.addSheMo(characterSheMo);
        if (skill2.increaseSheMoAttribute()) {
            characterSheMo.addStatus(new StatusIncreaseAttribute(daShe, characterSheMo, skill2));
            if (daShe.getSheMoCount() == 1) {
                daShe.addStatus(new StatusIncreaseAttribute(daShe, daShe, skill2));
            }
        }
        daShe.getBp().addCharacter(characterSheMo);
    }

    @Override
    protected void dieHandle() {
        daShe.removeSheMo(this);
    }

    @Override
    public void afterRound() {
        super.afterRound();
        daShe.doInteractive(interactive -> interactive.increaseLocation(daShe, 10));
    }

    @Override
    public boolean isUncontrollable() {
        return true;
    }

    @Override
    protected void addOwnSkills() {
        skillDuYe = new SkillDuYe(this);
        addSkill(skillDuYe);
    }

    class SkillDuYe extends Skill1PuGongBase {
        public SkillDuYe(Character belongTo) {
            super(belongTo, -1);
        }

        @Override
        public void usePrivate(Interactive interactive, Character target) {
            attack(interactive, target);
        }

        void attack(Interactive interactive, Character target) {
            interactive.attackTypical(this, target, 100, AttackType.DAN_TI);
            daShe.addMo(target);
        }

        @Override
        public String getName() {
            return "毒液";
        }
    }

    private static class StatusIncreaseAttribute extends Status implements AttributeModifier {
        private final Skill2 skill2;

        public StatusIncreaseAttribute(Character from, Character belongTo, Skill2 skill2) {
            super(from, belongTo, StatusType.SPECIAL, StatusForm.SPECIAL);
            this.skill2 = skill2;
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.DEFENCE
                    || (attribute == Attribute.EFFECT_RESIST_RATE && skill2.increaseEffectResist())
                    || (attribute == Attribute.SPEED && skill2.increaseSpeed());
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            DaShe daShe = (DaShe) from;
            if (attribute == Attribute.DEFENCE) {
                return belongTo.getInitDefense() * 0.3 * daShe.getSheMoCount();
            }
            if (attribute == Attribute.EFFECT_RESIST_RATE) {
                return 20 * daShe.getSheMoCount();
            }
            if (attribute == Attribute.SPEED) {
                return 10 * daShe.getSheMoCount();
            }

            return 0;
        }
    }
}
