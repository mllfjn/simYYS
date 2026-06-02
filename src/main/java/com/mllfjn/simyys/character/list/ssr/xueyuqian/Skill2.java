package com.mllfjn.simyys.character.list.ssr.xueyuqian;

import com.mllfjn.simyys.BattlePane;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.skill.Skill;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.determinant.RetainAfterChangeWave;
import com.mllfjn.simyys.character.status.determinant.RetainAfterDie;
import com.mllfjn.simyys.character.status.triggerParam.ParamKilledCharacter;
import com.mllfjn.simyys.character.status.triggerParam.TriggerParam;

import java.util.Optional;

class Skill2 extends Skill {
    private static final String SkillName = "龙胆·绽";
    private static final double[] InitLDLLHp = new double[]{0, 2.4, 2.8, 3.2, 3.6, 3.6};

    private final StatusWLXR statusWLXR;

    private CharacterLDLL characterLDLL;

    public Skill2(Character belongTo, int level) {
        super(belongTo, level, 0, 0, 2);
        statusWLXR = new StatusWLXR(belongTo);
        if (level >= 5) {
            belongTo.bp.addPriorityMove(belongTo, () -> summon(0, false));
        }
        belongTo.addStatus(new StatusKilledCharacterListener(belongTo));
    }

    StatusWLXR getStatusWLXR() {
        return statusWLXR;
    }

    @Override
    public String getName() {
        return SkillName;
    }

    @Override
    public Optional<Character> usePrivate(BattlePane bp) {
        summon(0, false);
        return Optional.empty();
    }

    private void summon(double location, boolean forceChangeLocation) {
        if (characterLDLL == null || !characterLDLL.alive) {
            characterLDLL = new CharacterLDLL(((XueYuQian) getBelongTo()), InitLDLLHp[getLevel()], location);
            getBelongTo().bp.addCharacter(characterLDLL);
        } else {
            characterLDLL.repeatSummon(location, forceChangeLocation);
        }
    }

    private class StatusKilledCharacterListener extends Status
            implements StatusRunnable, RetainAfterDie, RetainAfterChangeWave {
        public StatusKilledCharacterListener(Character character) {
            super(character, character, StatusType.SPECIAL, StatusForm.SPECIAL);
        }

        @Override
        public boolean runnable(Trigger trigger) {
            return trigger == Trigger.KILLED_CHARACTER;
        }

        @Override
        public boolean run(Trigger trigger, BattlePane bp, TriggerParam param) {
            Character character = ((ParamKilledCharacter) param).getCharacter();
            if (character.team != belongTo.team && !character.isSummon()) {
                Skill2.this.summon(character.getLocation(), true);
            }
            return false;
        }
    }
}
