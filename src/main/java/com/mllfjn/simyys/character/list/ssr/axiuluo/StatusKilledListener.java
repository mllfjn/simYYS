package com.mllfjn.simyys.character.list.ssr.axiuluo;

import com.mllfjn.simyys.character.Attribute;
import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.status.*;
import com.mllfjn.simyys.character.status.triggerParam.ParamKilledCharacter;

class StatusKilledListener extends Status {
    private final SkillWuJianShaLu skillWuJianShaLu;

    public StatusKilledListener(Character character, Skill2 skill2) {
        super(AXiuLuo.CharacterName + "击杀监听", character);
        skillWuJianShaLu = new SkillWuJianShaLu(character, skill2);

        retainAfterDie();
        retainAfterChangeWave();

        runOn(Trigger.KILLED_CHARACTER, param -> {
            Character target = ((ParamKilledCharacter) param).getCharacter();
            target.replaceStatus(new StatusZhuMie(belongTo, target));
            skillWuJianShaLu.replace();
        });
    }

    private static class StatusZhuMie extends Status {
        private static final String StatusName = "诛灭";

        public StatusZhuMie(Character from, Character belongTo) {
            super(StatusName, from, belongTo, StatusType.GENERAL, StatusForm.YIN_JI);
            displayName();
            retainAfterDie();
            attribute(Attribute.ATTACK, _ -> -0.5 * belongTo.getInitAttack());
        }
    }
}
