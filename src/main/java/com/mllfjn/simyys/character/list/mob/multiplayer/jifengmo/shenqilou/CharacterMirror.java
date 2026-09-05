package com.mllfjn.simyys.character.list.mob.multiplayer.jifengmo.shenqilou;

import com.mllfjn.simyys.character.Character;
import com.mllfjn.simyys.character.list.mob.multiplayer.ClearHpHandler;
import com.mllfjn.simyys.utils.Utils;

import java.io.Serializable;

record CharacterMirror(ShenQiLou shenQiLou, Character existCharacter) implements Serializable {

    public Character getInstance() {
        try {
            Character newCharacter = existCharacter.getClass().getDeclaredConstructor().newInstance();
            newCharacter.reset(shenQiLou.bp);
            newCharacter.name = existCharacter.name;
            newCharacter.team = shenQiLou.team;
            newCharacter.setMaxHp(99999999, true);
            newCharacter.setInitDefense(704);
            newCharacter.setInitSpeed(existCharacter.getInitSpeed());
            newCharacter.setInitBaseAttack(existCharacter.getInitBaseAttack());
            newCharacter.setInitAdditionAttack(existCharacter.getInitAdditionAttack());
            newCharacter.setInitCritRate(existCharacter.getInitCritRate());
            newCharacter.setInitCritPower(existCharacter.getInitCritPower());

            // 镜像身上会跳出一个封印,好像是把被动封了
            newCharacter.sealPassiveSkill();
            newCharacter.fillSkills();

            // 有可能有1,2,3火,有的会普攻有的放技能,看不懂 先这么写不管了
            newCharacter.setMob(1, 1);
            newCharacter.setLockSkill(1);

            newCharacter.getCharacterIcon().setEventHandlerContainer(new ClearHpHandler(newCharacter));

            return newCharacter;
        } catch (Exception e) {
            Utils.throwException("创造镜像失败", e);
        }
        return null;
    }
}
