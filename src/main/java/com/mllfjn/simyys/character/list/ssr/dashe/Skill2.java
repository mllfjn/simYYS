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
import com.mllfjn.simyys.character.status.triggerParam.ParamAttackInfo;

import java.util.Optional;

class Skill2 extends Skill {
    private static final String SkillName = "不洁之力";

    public Skill2(DaShe belongTo, int level, Skill3 skill3) {
        super(belongTo, level, 0, 1, 2);
        belongTo.bp().addActionListener(new BattleActionListener(belongTo) {
            @Override
            public boolean onBattleAction(BattleEvent event) {
                if (event instanceof EventCharacterDie ecd && ecd.getCharacter().team == belongTo.team) {
                    Character target = new CharacterFinder(belongTo)
                            .filterTeammate()
                            .filterYYS(true)
                            .getFirst();
                    if (target != null) {
                        convert(target);
                        return !skill3.needListener();
                    } else if (skill3.needListener()) {
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

    private class StatusBJZL extends Status {
        public StatusBJZL(Character from, Character belongTo) {
            super("不洁之力", from, belongTo, StatusType.BUFF, StatusForm.YIN_JI);
            duration(StatusDurationType.WEI_CHI, 2);
            displayNameAndDuration();
            beforeDelete(() -> Skill2.this.convertSheMo(belongTo));
            attribute(Attribute.SPEED, 30.0);
            runOn(Trigger.WHEN_ATTACK, param ->
                    ((ParamAttackInfo) param).getAttackInfo().getTraceableNumber().mul(1.3, SkillName)
            );
            runOn(Trigger.BEING_ATTACKED, param ->
                    ((ParamAttackInfo) param).getAttackInfo().setCancel(true)
            );
        }
    }
}
