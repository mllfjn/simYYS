package com.mllfjn.simyys.character.list.ssr.yunwaijing;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.Status;
import com.mllfjn.simyys.character.status.StatusDurationType;

import java.util.Optional;

abstract class Skill2 extends Skill {
    protected Skill2(Character belongTo, String name) {
        super(belongTo, 1, name, 0, 0, 2);
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        getBelongTo().setHp(getBelongTo().getMaxHp() - getBelongTo().getHp());
        Status.of("翻转加速", getBelongTo())
                .attribute(Attribute.SPEED, 100)
                .duration(StatusDurationType.CHI_XU, 1)
                .addTo();
        setCost(getCost() + 2);
        doSpecial();
        return Optional.empty();
    }

    abstract void doSpecial();

    static class Skill2Yin extends Skill2 {
        protected Skill2Yin(Character belongTo) {
            super(belongTo, "斗转");
        }

        @Override
        void doSpecial() {
            YunWaiJing yunWaiJing = (YunWaiJing) getBelongTo();
            if (yunWaiJing.awakening) {
                getBelongTo().addStatus(new StatusJingDun(getBelongTo(), getBelongTo()));
            }
            yunWaiJing.changeTo(YunWaiJing.Type.Yang);
        }
    }

    static class Skill2Yang extends Skill2 {
        protected Skill2Yang(Character belongTo) {
            super(belongTo, "昭回");
        }

        @Override
        void doSpecial() {
            YunWaiJing yunWaiJing = (YunWaiJing) getBelongTo();
            yunWaiJing.changeTo(YunWaiJing.Type.Yin);
        }
    }
}
