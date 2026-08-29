package com.mllfjn.simyys.character.list.ssr.bujianyue;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill1PuGongBase;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.interactive.Interactive;

import java.util.Iterator;

class Skill1 extends Skill1PuGongBase {
    private static final String SkillName = "重峦";

    public Skill1(Character belongTo, int level) {
        super(belongTo, level);
    }

    @Override
    public void usePrivate(Interactive interactive, Character target) {
        super.usePrivate(interactive, target);
        if (getLevel() >= 5) {
            getBelongTo().replaceStatus(new StatusEffectResist(getBelongTo()));
        }

        boolean findOne = false;
        Iterator<Status> iterator = getBelongTo().getStatuses().iterator();
        while (iterator.hasNext()) {
            Status status = iterator.next();
            if (status instanceof StatusYun sy) {
                sy.consumeStack();
            } else if (status instanceof StatusShan ss) {
                ss.consumeStack(this, interactive);
            }

            if (status instanceof StatusYun || status instanceof StatusShan) {
                iterator.remove();
                if (findOne) {
                    return;
                }
                findOne = true;
            }
        }

    }

    @Override
    public String getName() {
        return SkillName;
    }

    private static class StatusEffectResist extends Status implements AttributeModifier {
        public StatusEffectResist(Character character) {
            super(character, character, StatusType.BUFF, StatusForm.ZHUANG_TAI);
            duration(StatusDurationType.CHI_XU, 2);
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
