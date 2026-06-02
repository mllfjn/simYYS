package com.mllfjn.simyys.character.list.ssr.guiqie;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.PassiveSkill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.AttackInfo;
import com.mllfjn.simyys.interactive.AttackType;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.List;

class Skill2ForMob extends PassiveSkill {
    private static final String SkillName = "鬼刃·罗城门(怪物)";

    private boolean isActive;

    public Skill2ForMob(Character belongTo) {
        super(belongTo, 1, 2);
    }

    void attacked(Interactive interactive, Character target) {
        if (isActive) {
            if (target.getHp() < (target.getMaxHp() * 0.85)) {
                interactive.attackTypical(this, target, 125, AttackType.DAN_TI);
                Character belongTo = getBelongTo();
                StatusEffectResist.install(belongTo);
                if (target.getHp() < (target.getMaxHp() * 0.65)) {
                    List<Character> list = new CharacterFinder(belongTo)
                            .filterEnemy()
                            .getList();
                    for (Character character : list) {
                        character.sealPassiveSkill();
                    }
                    interactive.attack(this, list, c -> {
                        AttackInfo attackInfo = AttackInfo
                                .createTypicalAttack(belongTo, this, c, 125, AttackType.QUN_TI);
                        attackInfo.setCanThroughShield(true);
                        attackInfo.setCalYuHun(false);
                        return attackInfo;
                    });
                    for (Character character : list) {
                        character.unsealPassiveSkill();
                    }
                }
                this.useDone();
            }
        }
    }

    @Override
    public void enable() {
        isActive = true;
    }

    @Override
    public void disable() {
        isActive = false;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    static class StatusEffectResist extends Status implements AttributeModifier {
        public StatusEffectResist(Character character) {
            super(character, character, StatusType.BUFF, StatusForm.ZHUANG_TAI);
            setDurationType(StatusDurationType.CHI_XU, 1);
        }

        public static void install(Character character) {
            character.getStatus(StatusEffectResist.class)
                    .ifPresentOrElse(
                            status -> status.setDuration(1),
                            () -> character.addStatus(new StatusEffectResist(character))
                    );
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.EFFECT_RESIST_RATE;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return 50;
        }
    }
}
