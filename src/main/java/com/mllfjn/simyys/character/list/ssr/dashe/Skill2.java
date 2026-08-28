package com.mllfjn.simyys.character.list.ssr.dashe;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.battleevent.BattleActionListener;
import com.mllfjn.simyys.battleevent.BattleEvent;
import com.mllfjn.simyys.battleevent.EventCharacterDie;
import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.CharacterFinder;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.InfluenceDamageWhenAttack;
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;
import com.mllfjn.simyys.interactive.AttackInfo;

import java.util.List;
import java.util.Optional;

class Skill2 extends Skill {
    private static final String SkillName = "不洁之力";

    public Skill2(DaShe belongTo, int level, Skill3 skill3) {
        super(belongTo, level, 0, 1, 2);
        belongTo.bp().addActionListener(new BattleActionListener(belongTo) {
            @Override
            public boolean onBattleAction(BattleEvent event) {
                if (event instanceof EventCharacterDie ecd && ecd.getCharacter().team == belongTo.team) {
                    List<Character> list = new CharacterFinder(belongTo)
                            .filterTeammate()
                            .filterShiShen()
                            .filterSelf()
                            .getList();
                    if (list.size() == 1) {
                        if (list.get(0).isYYS()) {
                            convert(list.get(0));
                            return !skill3.needListener();
                        }
                    } else if (skill3.needListener() && list.isEmpty()) {
                        skill3.convert();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    void convert(Character target) {
        getBelongTo().removeSkill(this);
        getBelongTo().addSkill(new Skill2Special(getBelongTo(), target, this), true);
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        Character target = new CharacterFinder(getBelongTo())
                .filterTeammate()
                .filterSelf()
                .filterYYS(false)
                .filterSummon(false)
                .getAutoOrElseRandom();

        target.addStatus(new StatusBJZL(getBelongTo(), target));

        if (getLevel() >= 5) {
            getBelongTo().getInteractive().getNewRound(target);
        }

        return Optional.of(target);
    }

    void convertSheMo(Character target) {
        CharacterSheMo.convert(((DaShe) getBelongTo()), target, this);
    }

    boolean increaseSheMoAttribute() {
        return getLevel() >= 2;
    }

    boolean increaseEffectResist() {
        return getLevel() >= 3;
    }

    boolean increaseSpeed() {
        return getLevel() >= 4;
    }

    private class StatusBJZL extends Status
            implements AttributeModifier, InfluenceDamageWhenAttack, Displayable, StatusRunnable {
        public StatusBJZL(Character from, Character belongTo) {
            super(from, belongTo, StatusType.BUFF, StatusForm.YIN_JI);
            setDurationType(StatusDurationType.WEI_CHI, 2);
        }

        @Override
        public void beforeDelete() {
            Skill2.this.convertSheMo(belongTo);
        }

        @Override
        public boolean isAffectAttribute(Attribute attribute) {
            return attribute == Attribute.SPEED;
        }

        @Override
        public double getInfluence(Attribute attribute, StatusModifyParam param) {
            return 30;
        }

        @Override
        public void doInfluenceWhenAttack(AttackInfo attackInfo) {
            attackInfo.getTraceableNumber().mul(1.3, SkillName);
        }

        @Override
        public String getDisplayText() {
            return SkillName + getDuration();
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
