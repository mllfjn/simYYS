package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.shenqilou;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.CharacterFactory;

import java.io.Serializable;

record CharacterMirror(ShenQiLou shenQiLou, Character existCharacter) implements Serializable {

    public Character getInstance() {
        Character newCharacter = CharacterFactory.getCharacter(existCharacter.name).orElseThrow();
        newCharacter.reset(shenQiLou.bp);
        newCharacter.name = existCharacter.name;
        newCharacter.team = shenQiLou.team;
        newCharacter.setMaxHp(99999999, true);
        newCharacter.setMob(3, 3);
        newCharacter.setInitDefense(704);
        newCharacter.setInitSpeed(existCharacter.getInitSpeed());
        newCharacter.setInitBaseAttack(existCharacter.getInitBaseAttack());
        newCharacter.setInitAdditionAttack(existCharacter.getInitAdditionAttack());
        newCharacter.setInitCritRate(existCharacter.getInitCritRate());
        newCharacter.setInitCritPower(existCharacter.getInitCritPower());
        newCharacter.fillSkills();
        return newCharacter;
    }
}
